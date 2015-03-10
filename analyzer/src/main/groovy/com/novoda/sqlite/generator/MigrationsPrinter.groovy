package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Database
import groovy.text.GStringTemplateEngine

public class MigrationsPrinter {

    String packageName
    private final File targetDir

    MigrationsPrinter(File targetDir) {
        this.targetDir = targetDir
    }

    public void print() throws IOException {
        File targetFile = new File(makeFileDir(), "Migrations.java")
        targetFile.text = emitClass()
    }

    private String emitClass(def printers) {
            new GStringTemplateEngine().createTemplate(new File('Migrations.java'))
                    .make(packageName: packageName)
                    .toString()
    }

    private File makeFileDir() {
        String packageAsDir = packageName.replaceAll(~/\./, "/")
        def fileDir = new File(targetDir, packageAsDir)
        fileDir.mkdirs()
        return fileDir
    }

}
