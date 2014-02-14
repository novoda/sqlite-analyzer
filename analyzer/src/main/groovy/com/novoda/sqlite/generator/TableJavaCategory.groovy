package com.novoda.sqlite.generator
import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.Table

class TableJavaCategory {

    private static orgColumns = Table.metaClass.getMetaMethod('getColumns', [] as Class[])

    static Column[] getColumns(Table table) {
        orgColumns.invoke(table).grep({it.name != '_id'})
    }
}

