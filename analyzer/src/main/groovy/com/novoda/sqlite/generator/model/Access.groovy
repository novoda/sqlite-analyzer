package com.novoda.sqlite.generator.model
import com.novoda.sqlite.model.Database
import com.novoda.sqlite.model.Table
import groovy.transform.CompileStatic

@CompileStatic
class Access {
    Collection<DataSet> tables

    static Access from(Database database) {
        [tables: tablesToDataSets(database.tables)] as Access
    }

    private static Collection<DataSet> tablesToDataSets(Collection<Table> tables) {
        tables.collect { Table table -> DataSet.fromTable(table) }
    }
}
