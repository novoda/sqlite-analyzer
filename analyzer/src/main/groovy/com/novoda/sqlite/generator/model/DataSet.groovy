package com.novoda.sqlite.generator.model

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.Table
import groovy.transform.Immutable

@Immutable
class DataSet {
    String sqlName
    String name
    Collection<Field> fields

    static DataSet fromTable(Table table) {
        return [sqlName: table.name, name: table.camelizedName, fields: columnsToFields(table.columns)]
    }

    private static Collection<Field> columnsToFields(Collection<Column> columns) {
        return columns.collect { Column column -> Field.fromColumn(column) }
    }
}
