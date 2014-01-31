package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

class GetTableFromCursorGenerator {

    private static final String TEMPLATE = '''\
public static $returnType fromCursor(android.database.Cursor cursor) {
<% columns.each { column -> %>\
    $column.type $column.name = $column.access(cursor);
<% } %>\
    return new $returnType(\
<% columns.eachWithIndex { column, index -> out << "$column.name"; if (index < columns.size -1) out << ", "} %>\
);
}
'''
    private final Column column
    private final Table table

    GetTableFromCursorGenerator(Table table) {
        this.table = table
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([returnType: table.camelizedName,
                columns: createColumns()])
                .toString()
    }

    private createColumns() {
        use(ColumnJavaCategory) {
            def columns = []
            table.columns.each {
                Column column -> columns << ['type': column.dataType, 'name': column.camelizedSmallName, 'access': column.getterPrefix + column.camelizedName]
            }
            return columns
        }
    }
}
