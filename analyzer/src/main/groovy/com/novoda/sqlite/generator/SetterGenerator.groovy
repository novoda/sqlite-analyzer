package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.text.GStringTemplateEngine

public class SetterGenerator {

    private static final String TEMPLATE = '''\
public static void set$methodName($inputType value, android.content.ContentValues values) {
    values.put("$rowName", value);
}
'''
    private final Column column

    public SetterGenerator(Column column) {
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
        switch (column.getAffinity()) {
            case DataAffinity.INTEGER:
                if (column.isNullable()) {
                    return "Integer";
                }
                return "int";
            case DataAffinity.NONE:
                return "byte[]";
            case DataAffinity.TEXT:
                return "String";
            case DataAffinity.NUMERIC:
                if (column.isBoolean())
                    return column.isNullable() ? "Boolean" : "boolean"
                return "String"
            case DataAffinity.REAL:
                if (column.isNullable()) {
                    return "Double";
                }
                return "double";
            default:
                throw new RuntimeException("unknown affinity: " + column.getAffinity().name());
        }
    }

}