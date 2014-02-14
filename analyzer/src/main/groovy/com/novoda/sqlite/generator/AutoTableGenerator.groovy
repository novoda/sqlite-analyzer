package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Table
import groovy.text.GStringTemplateEngine

public class AutoTableGenerator {

    private static final String TEMPLATE = '''\
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class $className {
<% generators.each { generator ->  out << generator.print() } %>
}
'''
    private final Table table

    public AutoTableGenerator(Table table) {
        this.table = table
    }

    String print() {
        def generators = []
        generators << new GetAutoTableCreateGenerator(table)
        table.columns.each { column ->
            generators << new AutoFieldGenerator(column)
        }
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([generators: generators, className: table.camelizedName])
                .toString()
    }
}
