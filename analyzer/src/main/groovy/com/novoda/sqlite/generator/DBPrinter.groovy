package com.novoda.sqlite.generator

import groovy.text.GStringTemplateEngine

public class DBPrinter {

    private static final String TEMPLATE = '''\
package $packageName;

public final class DB {
<% printers.each { printer -> out << printer.print() } %>
}
'''
    String packageName
    String targetDir

    def printers

    public void print() throws IOException {
        File targetFile = new File(targetDir, "DB.java")
        targetFile.text = emitClass()
    }

    private String emitClass() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make(printers: printers, packageName: packageName)
                .toString()
    }
}