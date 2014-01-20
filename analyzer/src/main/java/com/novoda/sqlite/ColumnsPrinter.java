package com.novoda.sqlite;

import com.novoda.sqlite.model.Column;
import com.novoda.sqlite.model.Database;
import com.novoda.sqlite.model.Table;
import com.squareup.javawriter.JavaWriter;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.EnumSet;

public final class ColumnsPrinter implements Printer {
	private final Database database;

	public ColumnsPrinter(Database database) {
		this.database = database;
	}

	/**
	 * Prints a class containing enum classes for the column names of each
	 * table.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	@Override
	public void print(JavaWriter writer) throws IOException {
		emitClass(writer);

	}

	private void emitClass(JavaWriter javaWriter) throws IOException {
		javaWriter.beginType("Columns", "class", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL));
		for (Table table : database.getTables()) {
			emitTable(table, javaWriter);
		}
		javaWriter.endType();
	}

	private void emitTable(Table table, JavaWriter javaWriter) throws IOException {
		javaWriter.beginType(StringUtil.camelize(table.getName()), "class", EnumSet.of(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL));
		for (Column column : table.getColumns()) {
            javaWriter.emitField("String", StringUtil.camelize(column.getName()), EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), javaWriter.stringLiteral(column.getName()));
		}
		javaWriter.endType();
	}
}
