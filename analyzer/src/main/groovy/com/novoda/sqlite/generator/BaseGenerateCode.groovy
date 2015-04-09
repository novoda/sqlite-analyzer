package com.novoda.sqlite.generator
import com.novoda.sqlite.Analyzer
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
class BaseGenerateCode extends DefaultTask {

    @OutputDirectory
    File outputDir

    @Input
    String packageName

    Closure classGeneration

    Closure dbConnection

    @TaskAction
    void generate() {
        Database database = analyzeDb()
        classGeneration(database, outputDir)
    }

    private Database analyzeDb() {
        Connection connection = dbConnection()
        def database = new Analyzer(connection).analyze()
        connection.close()
        database
    }

}

