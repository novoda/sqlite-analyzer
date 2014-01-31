package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity

class ColumnJavaCategory {

    static String getDataType(Column column) {
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

    static String getGetterPrefix(Column column) {
        column.boolean ? "is" : "get"
    }

    static String getVarName(Column column) {
        column.camelizedSmallName
    }

    static String getMethodName(Column column) {
        column.camelizedName
    }
}

