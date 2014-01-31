package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity

class ColumnAndroidCategory {

    static String getCursorAccessor(Column column) {
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

