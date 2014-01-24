package com.novoda.sqlite.generator
import com.novoda.sqlite.FileConnector
import org.gradle.api.tasks.InputFile

import java.sql.Connection

class GenerateCodeFromFile extends BaseGenerateCode {

    @InputFile
    File databaseFile

    protected Connection createConnection() {
        new FileConnector(databaseFile).connect()
    }

}

