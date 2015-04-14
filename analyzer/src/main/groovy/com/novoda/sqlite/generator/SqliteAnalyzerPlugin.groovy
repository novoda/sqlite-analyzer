package com.novoda.sqlite.generator

import org.gradle.api.Plugin
import org.gradle.api.Project

class SqliteAnalyzerPlugin implements Plugin<Project> {
    void apply(Project project) {
        ensurePluginDependencies(project)
        def extension = project.extensions.create('sqliteAccess', SqliteAnalyzerExtension, project)
        project.afterEvaluate {
            project.android.applicationVariants.all { variant ->
                File sourceFolder = project.file("${project.buildDir}/generated/source/sqlite/${variant.dirName}")
                def javaGenerationTask = project.tasks.create(name: "generate${variant.name.capitalize()}SqliteAccess", type: GenerateDBAccess) {
                    dbConnection extension.dbConnector
                    outputDir sourceFolder
                    packageName extension.packageName
                    classGeneration extension.generator
                }
                variant.registerJavaGeneratingTask(javaGenerationTask, sourceFolder)
            }
        }

    }

    private static void ensurePluginDependencies(Project project) {
        def hasAndroid = project.plugins.hasPlugin('android') || project.plugins.hasPlugin('android-library')
        if (!hasAndroid) {
            throw new RuntimeException("The 'android' or 'android-library' plugin is required.")
        }
    }
}
