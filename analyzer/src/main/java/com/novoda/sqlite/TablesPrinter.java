package com.novoda.sqlite;

import com.novoda.sqlite.model.Database;
import com.novoda.sqlite.model.Table;
import com.squareup.javawriter.JavaWriter;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.*;

public final class TablesPrinter implements Printer {
    private final Database database;

    public TablesPrinter(Database database) {
        this.database = database;
    }

    /**
     * Prints an enum containing the names of each table.
     *
     * @param writer
     * @throws IOException
     */
    @Override
    public void print(JavaWriter writer) throws IOException {
        emitClass(writer);
        for (Table table : database.getTables()) {
            new TablePrinter(table).print(writer);
        }
    }

    private void emitClass(JavaWriter javaWriter) throws IOException {
        javaWriter.beginType("Tables", "class",
                EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL));
        for (Table table : lexSortedTables()) {
            javaWriter.emitField("String", StringUtil.camelize(table.getName()), EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), javaWriter.stringLiteral(table.getName()));
        }
        javaWriter.endType();
    }

    private List<Table> lexSortedTables() {
        ArrayList<Table> copy = new ArrayList<Table>(database.getTables());
        Collections.sort(copy, new Comparator<Table>() {
            @Override
            public int compare(Table left, Table right) {
                return left.getName().compareTo(right.getName());
            }
        });
        return copy;
    }
}
