package com.novoda.sqlite.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SQLFileTest {
    private final static String STATEMENT_CREATE_TABLE = "CREATE TABLE banana(_id INTEGER);";
    private final static String STATEMENT_ALTER_TABLE = "ALTER TABLE banana ADD COLUMN colour TEXT;";
    private final static String NEW_LINE = "\n";
    private final static String SPACE = " ";
    private final static String LINE_COMMENT = "-- this is a line comment";
    private final static String[] EXPECTED_STATEMENTS = new String[]{STATEMENT_CREATE_TABLE, STATEMENT_ALTER_TABLE};
    private static final String BLOCK_COMMENT = "/* this is a \n block comment \n spanning over \n multiple lines */";

    @Test
    public void givenSQLFile_whenParseAndGetStatements_thenGetCorrectStatements() throws IOException {
        SQLFile file = givenSQLFileParsedFromString(STATEMENT_CREATE_TABLE + NEW_LINE + STATEMENT_ALTER_TABLE);

        String[] actual = getStatementsFromFile(file);

        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithSpaces_whenParseAndGetStatements_thenGetCorrectTrimmedStatements() throws IOException {
        SQLFile file = givenSQLFileParsedFromString(SPACE + STATEMENT_CREATE_TABLE + SPACE + NEW_LINE + SPACE + STATEMENT_ALTER_TABLE + SPACE);

        String[] actual = getStatementsFromFile(file);

        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithLineComments_whenParseAndGetStatements_thenIgnoreComments() throws IOException {
        SQLFile file = givenSQLFileParsedFromString(
                STATEMENT_CREATE_TABLE + SPACE + LINE_COMMENT + NEW_LINE + STATEMENT_ALTER_TABLE + SPACE + LINE_COMMENT);

        String[] actual = getStatementsFromFile(file);

        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithBlockComments_whenParseAndGetStatements_thenIgnoreComments() throws IOException {
        SQLFile file = givenSQLFileParsedFromString(
                STATEMENT_CREATE_TABLE + NEW_LINE + BLOCK_COMMENT + NEW_LINE + STATEMENT_ALTER_TABLE);

        String[] actual = getStatementsFromFile(file);

        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    private SQLFile givenSQLFileParsedFromString(String sql) throws IOException {
        SQLFile file = new SQLFile();
        file.parse(new StringReader(sql));
        return file;
    }

    private String[] getStatementsFromFile(SQLFile file) {
        List<String> statements = file.getStatements();
        return statements.toArray(new String[statements.size()]);
    }
}
