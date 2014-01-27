package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.text.GStringTemplateEngine

class GetterGenerator {

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

    GetterGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([returnType: getReturnType(),
                       methodName: column.camelizedName,
                       rowName: column.name,
                       cursorFunction: getCursorFunction(),
                       nullable: column.nullable,
                       isBool: column.boolean,
                       prefix: prefix()])
                .toString()
    }

    private String prefix() {
        column.boolean ? "is" : "get"
    }

    private String getReturnType() {
        switch (column.getAffinity()) {
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

    private String getCursorFunction() {
        switch (column.affinity) {
            case DataAffinity.INTEGER:
                return "Int";
            case DataAffinity.NONE:
                return "Blob";
            case DataAffinity.TEXT:
                return "String";
            case DataAffinity.NUMERIC:
                if (column.boolean)
                    return "Int"
                return "String"
            case DataAffinity.REAL:
                return "Double";
            default:
                throw new RuntimeException("unknown affinity: " + column.affinity.name());
        }
    }

}