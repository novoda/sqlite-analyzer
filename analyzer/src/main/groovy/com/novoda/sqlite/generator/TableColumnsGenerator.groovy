package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class TableColumnsGenerator {

    private static final String TEMPLATE = '''\
public static final class $classname {
<% columns.each { column ->  %>\
    public static final String ${column.camelizedName} = "${column.varName}";
<% } %>\
}
'''
    private final Table table

    public TableColumnsGenerator(Table table) {
        this.table = table
    }

    String print() {
        use(ColumnJavaCategory) {
            new GStringTemplateEngine().createTemplate(TEMPLATE)
                    .make([columns: table.columns, classname: table.camelizedName])
                    .toString()
        }
    }

}

