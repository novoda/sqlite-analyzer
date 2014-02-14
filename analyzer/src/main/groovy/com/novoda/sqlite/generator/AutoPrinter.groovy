package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Database
import groovy.text.GStringTemplateEngine

public class AutoPrinter {

    private static final String TEMPLATE = '''\
package $packageName;

<% printers.each { printer -> out << printer.print() } %>
'''
    String packageName = 'com.novoda.test.auto'
    private final Database database
    private final File targetDir

    AutoPrinter(Database database, File targetDir) {
        this.targetDir = targetDir
        this.database = database
    }

    public void print() throws IOException {
        database.tables.each { table ->
            File targetFile = new File(makeFileDir(), "${table.camelizedName}.java")
            targetFile.text = emitClass(new AutoTableGenerator(table))
        }
    }

    private String emitClass(def printers) {
        use(TableJavaCategory, ColumnJavaCategory, ColumnAndroidCategory) {
            new GStringTemplateEngine().createTemplate(TEMPLATE)
                    .make(printers: printers, packageName: packageName)
                    .toString()
        }
    }

    private File makeFileDir() {
        String packageAsDir = packageName.replaceAll(~/\./, "/")
        def fileDir = new File(targetDir, packageAsDir)
        fileDir.mkdirs()
        return fileDir
    }

}
