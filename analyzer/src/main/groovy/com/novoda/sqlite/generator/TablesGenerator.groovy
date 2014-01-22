package com.novoda.sqlite.generator
import com.novoda.sqlite.model.Database
import groovy.text.GStringTemplateEngine

public class TablesGenerator {

    private static final String TEMPLATE = '''\
public static final class Tables {
<% tables.each { table -> %>\
    public static final String ${table.camelizedName} = "${table.name}";
<% } %>\
}
'''
    private final Database database

    public TablesGenerator(Database database) {
        this.database = database
    }

    String print() {
        new GStringTemplateEngine().createTemplate(TEMPLATE)
                .make([tables: database.getTables()])
                .toString()
    }

}

