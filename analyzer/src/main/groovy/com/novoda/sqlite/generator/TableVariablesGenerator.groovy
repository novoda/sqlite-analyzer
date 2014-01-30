package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class TableVariablesGenerator {

    private static final String TEMPLATE =
            '''\
<% columns.each { column -> %>\
    private final $column.dataType $column.name;
<% } %>
'''

    private final Table table

    TableVariablesGenerator(Table table) { this.table = table }

    String print() {
        new GStringTemplateEngine()
                .createTemplate(TEMPLATE)
                .make([columns: createColumns()])
                .toString()
    }

    private createColumns() {
        def columns = []
        table.columns.each {
            column -> columns << ['dataType': getDataType(column), 'name': column.camelizedSmallName]
        }
        return columns
    }

    private String getDataType(Column column) {
        switch (column.affinity) {
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