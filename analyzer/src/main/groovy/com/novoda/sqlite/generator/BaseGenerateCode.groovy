package com.novoda.sqlite.generator

import com.novoda.sqlite.Analyzer
import com.novoda.sqlite.model.Database
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

import java.sql.Connection

/**
 * This task runs the SqliteAnalyzer to generate code to describe the database tables and columns.
 *
 * We use input and output directory annotations to ensure the task is run on any changes in the migrations directory
 * or when the output is removed.
 */
abstract class BaseGenerateCode extends DefaultTask {

    @OutputDirectory
    File outputDir

    String packageName = "com.novoda.database"

    boolean generateAuto = false
    boolean generateOpenHelper = true

    @TaskAction
    void generate() {
        Database database = analyzeDb()
        generateCode(database)
    }

    private Database analyzeDb() {
        Connection connection = createConnection()
        def database = new Analyzer(connection).analyze()
        connection.close()
        database
    }

    protected abstract Connection createConnection()

    private void generateCode(Database database) {
        def dBPrinter = new DBPrinter()
        dBPrinter.packageName = packageName
        dBPrinter.targetDir = makeFileDir().absolutePath
        dBPrinter.printers = [new ColumnsGenerator(database), new TableNamesGenerator(database)]
        database.getTables().each { table ->
            dBPrinter.printers << new TableGenerator(table)
        }
        dBPrinter.print()
        if (generateAuto)
            new AutoPrinter(database, outputDir).print()
        if (generateOpenHelper) {
            def openHelperPrinter = new OpenHelperPrinter(database, outputDir)
            openHelperPrinter.packageName = packageName
            openHelperPrinter.print()
        }
    }

    private File makeFileDir() {
        String packageAsDir = packageName.replaceAll(~/\./, "/")
        def fileDir = new File(outputDir, packageAsDir)
        fileDir.mkdirs()
        return fileDir
    }
}

