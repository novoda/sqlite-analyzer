package com.novoda.sqlite;

import com.novoda.sqlite.model.Column;
import com.novoda.sqlite.model.DataAffinity;
import com.novoda.sqlite.model.Table;
import com.squareup.javawriter.JavaWriter;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;

public final class TablePrinter implements Printer {
    private final Table table;

    public TablePrinter(Table table) {
        this.table = table;
    }

    @Override
    public void print(JavaWriter writer) throws IOException {
        emitClass(writer);
    }

    private void emitClass(JavaWriter javaWriter) throws IOException {
        javaWriter.beginType(StringUtil.camelize(table.getName()), "class",
                EnumSet.of(Modifier.PUBLIC, Modifier.STATIC));
        for (Column column : table.getColumns()) {
            emitGetter(column, javaWriter);
        }
        javaWriter.endType();
    }

    private void emitGetter(Column column, JavaWriter javaWriter) throws IOException {
        if (column.isNullable()) {
            emitNullableGetter(column, javaWriter);
        } else {
            emitNotNullableGetter(column, javaWriter);
        }
    }

    private void emitNotNullableGetter(Column column, JavaWriter javaWriter) throws IOException {
        beginGetter(column, javaWriter);
        javaWriter.emitStatement("return cursor.get" + getCursorFunction(column.getAffinity()) + "(cursor.getColumnIndexOrThrow(\"" + column.getName() + "\"))");
        javaWriter.endMethod();
    }

    private JavaWriter beginGetter(Column column, JavaWriter javaWriter) throws IOException {
        return javaWriter.beginMethod(getReturnType(column), "get" + StringUtil.camelize(column.getName()), EnumSet.of(Modifier.PUBLIC, Modifier.STATIC), Arrays.asList(new String[]{"android.database.Cursor", "cursor"}), Collections.<String>emptyList());
    }

    private void emitNullableGetter(Column column, JavaWriter javaWriter) throws IOException {
        beginGetter(column, javaWriter);
        javaWriter.emitStatement("int index = cursor.getColumnIndexOrThrow(\"" + column.getName() + "\")");
        javaWriter.beginControlFlow("if (cursor.isNull(index))");
        javaWriter.emitStatement("return null");
        javaWriter.endControlFlow();
        javaWriter.emitStatement("return cursor.get" + getCursorFunction(column.getAffinity()) + "(index)");
        javaWriter.endMethod();
    }

    private String getReturnType(Column column) {
        switch (column.getAffinity()) {
            case INTEGER:
                if (column.isNullable()) {
                    return "Integer";
                }
                return "int";
            case NONE:
                return "byte[]";
            case TEXT:
                return "String";
            case NUMERIC:
            case REAL:
                if (column.isNullable()) {
                    return "Double";
                }
                return "double";
            default:
                throw new RuntimeException("unknown affinity: " + column.getAffinity().name());
        }
    }

    private String getCursorFunction(DataAffinity affinity) {
        switch (affinity) {
            case INTEGER:
                return "Int";
            case NONE:
                return "Blob";
            case TEXT:
                return "String";
            case NUMERIC:
            case REAL:
                return "Double";
            default:
                throw new RuntimeException("unknown affinity: " + affinity.name());
        }
    }
}
