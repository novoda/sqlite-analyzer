package com.novoda.sqlite.generator

import com.novoda.sqlite.generator.model.Access
import com.novoda.sqlite.model.Database
import org.gradle.api.Project

class SqliteAnalyzerExtension {
    String migrationsDir
    String packageName
    String databaseFile
    String className = "DB"
    Closure printer = fullAccessPrinter()

    SqliteAnalyzerExtension(Project project) {
    }

    Access access(Database database) {
        return Access.from(database)
    }

    static void printClass(def template, def templateData, String className, String packageName, File baseDir) {
        ClassEmitter printer = [template    : template,
                                templateData: templateData,
                                baseDir     : baseDir,
                                packageName : packageName,
                                className   : className]
        printer.print()
    }

    void printStaticAccess(Database database, File baseDir) {
        printClass(Templates.ONLY_STATIC_ACCESS, access(database), className, packageName, baseDir)
    }

    void printFullAccess(Database database, File baseDir) {
        printClass(Templates.FULL_ACCESS, access(database), className, packageName, baseDir)
    }

    Closure staticAccessPrinter() {
        return this.&printStaticAccess
    }

    Closure fullAccessPrinter() {
        return this.&printFullAccess
    }
}
