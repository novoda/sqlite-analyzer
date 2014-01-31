package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Database
import groovy.text.GStringTemplateEngine

class ColumnsGenerator {

    private static final String TEMPLATE = '''\
public static final class Columns {
<% tableColumnsGenerators.each { generator ->  out << generator.print() } %>\
}
'''
    private final Database database

    ColumnsGenerator(Database database) {
        this.database = database
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([tableColumnsGenerators: createTableColumnsGenerators()])
                .toString()
    }

    private createTableColumnsGenerators() {
        def generators = []
        database.tables.each { table ->
            generators << new TableColumnsGenerator(table)
        }
        generators
    }
}

