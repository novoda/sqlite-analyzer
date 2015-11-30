package com.novoda.sqlite.generator.model

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.Table
import groovy.transform.CompileStatic

import static com.novoda.sqlite.generator.JavaMapper.javaAccessor

@CompileStatic
class DataSet {
    String sqlName
    String name
    Collection<Field> fields

    static DataSet fromTable(Table table) {
        [sqlName: table.name, name: javaAccessor(table.name), fields: columnsToFields(table.columns)] as DataSet
    }

    private static Collection<Field> columnsToFields(Collection<Column> columns) {
        return columns.collect { Column column -> Field.fromColumn(column) }
    }
}
