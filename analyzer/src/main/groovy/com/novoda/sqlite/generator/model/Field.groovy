package com.novoda.sqlite.generator.model

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.transform.Immutable

@Immutable
final class Field {
    String name
    String accessor
    String sqlName
    String getMethod
    String setMethod
    String type
    boolean optional
    String cursorType

    static Field fromColumn(Column column) {
        return [name      : column.camelizedSmallName,
                accessor  : column.camelizedName,
                sqlName   : column.name,
                getMethod : getGetterPrefix(column) + column.camelizedName,
                setMethod : 'set' + column.camelizedName,
                type      : getDataType(column),
                optional  : column.nullable,
                cursorType: getCursorAccessor(column)]
    }

    private static String getDataType(Column column) {
        switch (column.affinity) {
            case DataAffinity.INTEGER:
                return column.nullable ? "Integer" : "int"
            case DataAffinity.NONE:
                return "byte[]"
            case DataAffinity.TEXT:
                return "String"
            case DataAffinity.NUMERIC:
                if (column.boolean)
                    return column.nullable ? "Boolean" : "boolean"
                return "String"
            case DataAffinity.REAL:
                return column.nullable ? "Double" : "double"
            default:
                throw new RuntimeException("unknown affinity: " + column.affinity.name())
        }
    }

    private static String getCursorAccessor(Column column) {
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


    private static String getGetterPrefix(Column column) {
        column.boolean ? "is" : "get"
    }
}
