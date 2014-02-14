package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

class GetAutoTableCreateGenerator {

    private static final String TEMPLATE = '''\
public static $returnType create(\
<% columns.eachWithIndex { column, index -> out << "$column.type $column.name"; if (index < columns.size -1) out << ", "} %>) {
    return new AutoValue_$returnType(\
<% columns.eachWithIndex { column, index -> out << "$column.name"; if (index < columns.size -1) out << ", "} %>\
);
}
'''
    private final Column column
    private final Table table

    GetAutoTableCreateGenerator(Table table) {
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
            Column column -> columns << ['type': column.dataType, 'name': column.camelizedSmallName, 'access': column.getterPrefix + column.camelizedName]
        }
        return columns
    }
}
