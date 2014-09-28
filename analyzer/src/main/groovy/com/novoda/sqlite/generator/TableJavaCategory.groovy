package com.novoda.sqlite.generator
import com.novoda.sqlite.model.Column
import com.novoda.sqlite.model.Table

class TableJavaCategory {

    static Column[] getNoIdColumns(Table table) {
        table.columns.grep({it.name != '_id'})
    }
}

