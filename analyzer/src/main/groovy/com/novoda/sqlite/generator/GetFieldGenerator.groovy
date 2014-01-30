package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.DataAffinity
import groovy.text.GStringTemplateEngine

class GetFieldGenerator {

    private static final String TEMPLATE = '''\
public $returnType $prefix$methodName() {
  return $variableName;
}
'''
    private final Column column

    GetFieldGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([returnType: getReturnType(),
                       methodName: column.camelizedName,
                       variableName: column.camelizedSmallName,
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
}
