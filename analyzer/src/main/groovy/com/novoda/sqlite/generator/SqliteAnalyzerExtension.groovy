package com.novoda.sqlite.generator

import org.gradle.api.Project

class SqliteAnalyzerExtension {
    String migrationsDir
    String packageName
    String databaseFile
    boolean generateAuto
    boolean onlyStatic

    SqliteAnalyzerExtension(Project project) {
    }
}
