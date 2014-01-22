package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.text.GStringTemplateEngine

public class GetterGenerator {

    private static final String TEMPLATE = '''\
public static $returnType $prefix$methodName(android.database.Cursor cursor) {
  int index = cursor.getColumnIndexOrThrow("$rowName");\
<% if(nullable) {%>
  if (cursor.isNull(index)) {
    return null;
  }\
<% } %>
  return cursor.get$cursorFunction(index)<% if (isBool) out << " > 0" %>;
}
'''
    private final Column column

    public GetterGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([returnType: getReturnType(),
                       methodName: camelizedName(),
                       rowName: column.name,
                       cursorFunction: getCursorFunction(),
                       nullable: column.nullable,
                       isBool: column.isBoolean(),
                       prefix: prefix()])
                .toString()
    }

    private String prefix() {
        column.isBoolean() ? "is" : "get"
    }

    private String camelizedName() {
        String name = column.name
        name = name.startsWith('_') ? name.substring(1) : name
        "$name".capitalize()
    }

    private String getReturnType() {
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

    private String getCursorFunction() {
        def affinity = column.getAffinity()
        switch (affinity) {
            case DataAffinity.INTEGER:
                return "Int";
            case DataAffinity.NONE:
                return "Blob";
            case DataAffinity.TEXT:
                return "String";
            case DataAffinity.NUMERIC:
                if (column.isBoolean())
                    return "Int"
                return "String"
            case DataAffinity.REAL:
                return "Double";
            default:
                throw new RuntimeException("unknown affinity: " + affinity.name());
        }
    }

}