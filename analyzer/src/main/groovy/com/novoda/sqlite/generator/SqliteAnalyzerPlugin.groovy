package com.novoda.sqlite.generator

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class SqliteAnalyzerPlugin implements Plugin<Project> {
    void apply(Project project) {
        ensurePluginDependencies(project)
        def extension = project.extensions.create('sqliteAccess', SqliteAnalyzerExtension, project)
        project.afterEvaluate {
            if (extension.migrationsDir) {
                project.android.applicationVariants.all { variant ->
                    File sourceFolder = project.file("${project.buildDir}/source/sqlite/${variant.dirName}")
                    def javaGenerationTask = project.tasks.create(name: "generate${variant.name.capitalize()}SqliteAccessFromMigrations", type: GenerateCodeFromMigrations) {
                        migrationsDir project.file(extension.migrationsDir)
                        outputDir sourceFolder
                        packageName extension.packageName
                    }
                    variant.registerJavaGeneratingTask(javaGenerationTask, sourceFolder)
                }
            }
            if (extension.databaseFile) {
                project.android.applicationVariants.all { variant ->
                    File sourceFolder = project.file("${project.buildDir}/source/sqlite/${variant.dirName}")
                    def javaGenerationTask = project.tasks.create(name: "generate${variant.name.capitalize()}SqliteAccessFromFile", type: GenerateCodeFromFile) {
                        databaseFile project.file(extension.databaseFile)
                        outputDir sourceFolder
                        packageName extension.packageName
                    }
                    variant.registerJavaGeneratingTask(javaGenerationTask, sourceFolder)
                }
            }
        }
    }

    private void ensurePluginDependencies(Project project) {
        def hasAndroid = project.plugins.hasPlugin('android') || project.plugins.hasPlugin('android-library')
        if (!hasAndroid) {
            throw new ProjectConfigurationException("The 'android' or 'android-library' plugin is required.")
        }
    }
}
