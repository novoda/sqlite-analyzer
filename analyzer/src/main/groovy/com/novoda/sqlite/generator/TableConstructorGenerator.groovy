package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class TableConstructorGenerator {

    private static final String TEMPLATE =
            '''\
public $className(\
<% columns.eachWithIndex {
  column, index -> out << "$column.dataType $column.name"
  if (index < columns.size-1)
      out << ", "
} %>) {
<% columns.each { column -> %>\
   this.$column.name = $column.name;
<% } %>\
}
'''

    private final Table table

    TableConstructorGenerator(Table table) { this.table = table }

    String print() {
        new GStringTemplateEngine()
                .createTemplate(TEMPLATE)
                .make([className: table.camelizedName, columns: createColumns()])
                .toString()
    }

    private createColumns() {
        def columns = []
        use(ColumnJavaCategory) {
            table.columns.each {
                column -> columns << ['dataType': column.dataType, 'name': column.camelizedSmallName]
            }
        }
        return columns
    }
}
