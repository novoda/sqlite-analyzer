package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.text.GStringTemplateEngine

class SetterGenerator {

    private static final String TEMPLATE = '''\
public static void set$methodName($inputType value, android.content.ContentValues values) {
    values.put("$rowName", value);
}
'''
    private final Column column

    SetterGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([methodName: column.camelizedName,
                       inputType: getDataType(),
                       rowName: column.name])
                .toString()
    }

    private String getDataType() {
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