package com.novoda.sqlite.generator.model

import com.novoda.sqlite.generator.JavaMapper
import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.transform.CompileStatic

@CompileStatic
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
        [name      : JavaMapper.javaName(column.name),
         accessor  : JavaMapper.javaAccessor(column.name),
         sqlName   : column.name,
         getMethod : getGetterPrefix(column) + JavaMapper.javaAccessor(column.name),
         setMethod : 'set' + JavaMapper.javaAccessor(column.name),
         type      : getDataType(column),
         optional  : column.nullable,
         cursorType: getCursorAccessor(column)] as Field
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
