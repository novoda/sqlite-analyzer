package com.novoda.sqlite;

import com.novoda.sqlite.model.Database;
import com.novoda.sqlite.model.Table;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnalyzerTestOnSampleMigrations {

    private Analyzer analyzer;
    private Database database;

    @Before
    public void setUp() throws Exception {
        String migrationsDir = MigrationsInDir.class.getResource("/sample_migrations").getFile();
        MigrationsInDir migrations = new MigrationsInDir(new File(migrationsDir));
        Connector connector = new MigrationsConnector(migrations);
        analyzer = new Analyzer(connector.connect());
        database = analyzer.analyze();
    }

    /*
    * see file: sample_migrations/13_program_twitter_tags.sql
     */
    @Test
    public void testTableDependencies() throws Exception {
        Table table = database.findTableByName("programs_with_twitter_tags");
        assertDependentTables(table, "programs", "broadcasts", "clusters");
    }

    private void assertDependentTables(Table table, String... dependentNames) {
        List<String> names = Arrays.asList(dependentNames);
        for (String name : names) {
            Assert.assertTrue(table.getName() + " should depend on " + name, database.findTableByName(name).getDependents().contains(table));
        }
    }
}
