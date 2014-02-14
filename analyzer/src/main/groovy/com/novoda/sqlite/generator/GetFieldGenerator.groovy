package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
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
                .make([returnType: column.dataType,
                methodName: column.camelizedName,
                variableName: column.camelizedSmallName,
                prefix: column.getterPrefix])
                .toString()
    }
}
