package com.novoda.sqlite.generator
import com.novoda.sqlite.MigrationsConnector
import com.novoda.sqlite.MigrationsInDir
import org.gradle.api.tasks.InputDirectory

import java.sql.Connection

/**
 * This task runs the SqliteAnalyzer to generate code to describe the database tables and columns.
 *
 * We use input and output directory annotations to ensure the task is run on any changes in the migrations directory
 * or when the output is removed.
 */
class GenerateCodeFromMigrations extends BaseGenerateCode {

    @InputDirectory
    File migrationsDir

    protected Connection createConnection() {
        def arteMigrations = new MigrationsInDir(migrationsDir)
        new MigrationsConnector(arteMigrations).connect()
    }

}

