package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class TableVariablesGenerator {

    private static final String TEMPLATE =
            '''\
<% columns.each { column -> %>\
    private final $column.dataType $column.camelizedSmallName;
<% } %>
'''

    private final Table table

    TableVariablesGenerator(Table table) { this.table = table }

    String print() {
        new GStringTemplateEngine()
                .createTemplate(TEMPLATE)
                .make([columns: table.columns])
                .toString()
    }
}
