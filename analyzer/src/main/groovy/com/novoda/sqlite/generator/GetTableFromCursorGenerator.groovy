package com.novoda.sqlite.generator
import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
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
        def columns = []
        table.columns.each {
            column -> columns << ['type': getReturnType(column), 'name': column.camelizedSmallName, 'access': prefix(column)+column.camelizedName]
        }
        return columns
    }

    private String prefix(Column column) {
        column.boolean ? "is" : "get"
    }

    private String getReturnType(Column column) {
        switch (column.getAffinity()) {
            case DataAffinity.INTEGER:
                if (column.nullable) {
                    return "Integer";
                }
                return "int";
            case DataAffinity.NONE:
                return "byte[]";
            case DataAffinity.TEXT:
                return "String";
            case DataAffinity.NUMERIC:
                if (column.boolean)
                    return column.nullable ? "Boolean" : "boolean"
                return "String"
            case DataAffinity.REAL:
                if (column.nullable) {
                    return "Double";
                }
                return "double";
            default:
                throw new RuntimeException("unknown affinity: " + column.affinity.name());
        }
    }
}