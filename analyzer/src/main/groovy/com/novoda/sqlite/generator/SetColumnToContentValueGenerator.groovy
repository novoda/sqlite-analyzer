package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import groovy.text.GStringTemplateEngine

class SetColumnToContentValueGenerator {

    private static final String TEMPLATE = '''\
public static void set$methodName($inputType value, android.content.ContentValues values) {
    values.put("$rowName", value);
}
'''
    private final Column column

    SetColumnToContentValueGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([methodName: column.camelizedName,
                inputType: column.dataType,
                rowName: column.name])
                .toString()
    }
}
