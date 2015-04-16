package com.novoda.sqlite.generator

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectConfigurationException

class SqliteAnalyzerPlugin implements Plugin<Project> {
    void apply(Project project) {
        ensurePluginDependencies(project)
        def extension = project.extensions.create('sqliteAccess', SqliteAnalyzerExtension, project)
        project.afterEvaluate {
            project.android[variants(project)].all { variant ->
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
        if (!isApplication(project) && !isLibrary(project)) {
            throw new ProjectConfigurationException("The 'android' or 'android-library' plugin is required.", null)
        }
    }

    private static String variants(Project project) {
        if (isApplication(project)) {
            return 'applicationVariants'
        } else if (isLibrary(project)) {
            return 'libraryVariants'
        }
        throw new ProjectConfigurationException("The 'android' or 'android-library' plugin is required.", null)
    }

    private static boolean isLibrary(Project project) {
        project.plugins.hasPlugin('com.android.library') || project.plugins.hasPlugin('android-library')
    }

    private static boolean isApplication(Project project) {
        project.plugins.hasPlugin('com.android.application') || project.plugins.hasPlugin('android')
    }

}
