package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

class GetContentValuesFromTableGenerator {

    private static final String TEMPLATE = '''\
public android.content.ContentValues toContentValues() {
    android.content.ContentValues values = new android.content.ContentValues();
<% columns.each { column -> %>\
    values.put("$column.name", ${column.getterPrefix+column.camelizedName}());
<% } %>\
    return values;
}
'''
    private Table table

    GetContentValuesFromTableGenerator(Table table) {
        this.table = table
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([columns: table.columns])
                .toString()
    }

}
