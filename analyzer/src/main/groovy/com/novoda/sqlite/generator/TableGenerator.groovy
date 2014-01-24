package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class TableGenerator {

    private static final String TEMPLATE = '''\
public static class $className {
<% generators.each { generator ->  out << generator.print() } %>
}
'''
    private final Table table

    public TableGenerator(Table table) {
        this.table = table
    }

    String print() {
        def generators = []
        table.columns.each { column ->
            generators << new GetterGenerator(column)
            generators << new SetterGenerator(column)
        }
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([generators: generators, className: table.camelizedName])
                .toString()
    }

}

