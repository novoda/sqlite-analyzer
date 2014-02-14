package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Column
import groovy.text.GStringTemplateEngine

class AutoFieldGenerator {

    private static final String TEMPLATE = '''
<% if(nullable) { %>\
@javax.annotation.Nullable
<% } %>\
public abstract $returnType $methodName();
'''
    private final Column column

    AutoFieldGenerator(Column column) {
        this.column = column
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([
                nullable: column.nullable,
                returnType: column.dataType,
                methodName: column.camelizedSmallName])
                .toString()
    }
}
