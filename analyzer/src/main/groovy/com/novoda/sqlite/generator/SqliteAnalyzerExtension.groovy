package com.novoda.sqlite.generator

import com.novoda.sqlite.generator.model.Access
import com.novoda.sqlite.model.Database
import org.gradle.api.Project

class SqliteAnalyzerExtension {
    String migrationsDir
    String packageName
    String databaseFile
    String className = "DB"
    Closure generator = fullAccessGenerator()

    SqliteAnalyzerExtension(Project project) {
    }

    Access access(Database database) {
        return Access.from(database)
    }

    static void generateClass(def template, def templateData, String className, String packageName, File baseDir) {
        ClassEmitter emitter = [template    : template,
                                templateData: templateData,
                                baseDir     : baseDir,
                                packageName : packageName,
                                className   : className]
        emitter.print()
    }

    void generateStaticAccess(Database database, File baseDir) {
        generateClass(Templates.ONLY_STATIC_ACCESS, access(database), className, packageName, baseDir)
    }

    void generateFullAccess(Database database, File baseDir) {
        generateClass(Templates.FULL_ACCESS, access(database), className, packageName, baseDir)
    }

    Closure staticAccessGenerator() {
        return this.&generateStaticAccess
    }

    Closure fullAccessGenerator() {
        return this.&generateFullAccess
    }
}
