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
    private final boolean onlyStatic

    public TableGenerator(Table table, boolean onlyStatic) {
        this.table = table
        this.onlyStatic = onlyStatic
    }

    String print() {
        def generators = []
        if (!onlyStatic) {
            generators << new GetTableFromCursorGenerator(table)
            generators << new GetContentValuesFromTableGenerator(table)
            generators << new TableVariablesGenerator(table)
            generators << new TableConstructorGenerator(table)
        }
        table.columns.each { column ->
            if (!onlyStatic) {
                generators << new GetFieldGenerator(column)
            }
            generators << new GetColumnFromCursorGenerator(column)
            generators << new SetColumnToContentValueGenerator(column)
        }
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([generators: generators, className: table.camelizedName])
                .toString()
    }
}
