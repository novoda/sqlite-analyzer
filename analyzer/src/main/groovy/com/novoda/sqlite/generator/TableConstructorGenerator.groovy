package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class TableConstructorGenerator {

    private static final String TEMPLATE =
            '''\
public $className(\
<% columns.eachWithIndex {
  column, index -> out << "$column.dataType $column.varName"
  if (index < columns.size()-1)
      out << ", "
} %>) {
<% columns.each { column -> %>\
   this.$column.varName = $column.varName;
<% } %>\
}
'''

    private final Table table

    TableConstructorGenerator(Table table) { this.table = table }

    String print() {
        new GStringTemplateEngine()
                .createTemplate(TEMPLATE)
                .make([className: table.camelizedName, columns: table.columns])
                .toString()
    }
}
