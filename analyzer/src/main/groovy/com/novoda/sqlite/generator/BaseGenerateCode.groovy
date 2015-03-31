package com.novoda.sqlite.generator

import com.novoda.sqlite.Analyzer
import com.novoda.sqlite.generator.model.Access
import com.novoda.sqlite.model.Database
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
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

    @Input
    String packageName = "com.novoda.database"

    @Input
    boolean generateAuto = false

    @Input
    boolean onlyStatic = false

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
        NewDBPrinter printer = [access: Access.from(database),
                                baseDir: outputDir,
                                packageName: packageName,
                                className: "DB"]
        printer.print()
        if (generateAuto)
            new AutoPrinter(database, outputDir).print()
    }
}

