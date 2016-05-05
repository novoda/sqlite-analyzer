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
    private final static String[] EXPECTED_STATEMENTS = new String[]{STATEMENT_CREATE_TABLE, STATEMENT_ALTER_TABLE};
    private final static String LINE_COMMENT = "-- this is a line comment";
    private static final String BLOCK_COMMENT = "/* this is a \n block comment \n spanning over \n multiple lines */";
    private final static String STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 = "CREATE TABLE";
    private final static String STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2 = "banana(_id INTEGER);";

    @Test
    public void givenSQLFile_whenParse_thenGetCorrectStatements() throws IOException {
        SQLFile file = givenSQLFileWhenParse(String.format("%s\n%s", STATEMENT_CREATE_TABLE, STATEMENT_ALTER_TABLE));

        String[] actual = getStatementsFromFile(file);
        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithSpaces_whenParse_thenGetCorrectTrimmedStatements() throws IOException {
        SQLFile file = givenSQLFileWhenParse(SPACE + STATEMENT_CREATE_TABLE + SPACE + NEW_LINE + SPACE + STATEMENT_ALTER_TABLE + SPACE);

        String[] actual = getStatementsFromFile(file);
        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithLineComments_whenParse_thenIgnoreComments() throws IOException {
        SQLFile file = givenSQLFileWhenParse(
                STATEMENT_CREATE_TABLE + SPACE + LINE_COMMENT + NEW_LINE + STATEMENT_ALTER_TABLE + SPACE + LINE_COMMENT
        );

        String[] actual = getStatementsFromFile(file);
        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithBlockComments_whenParse_thenIgnoreComments() throws IOException {
        SQLFile file = givenSQLFileWhenParse(
                STATEMENT_CREATE_TABLE + NEW_LINE + BLOCK_COMMENT + NEW_LINE + STATEMENT_ALTER_TABLE
        );

        String[] actual = getStatementsFromFile(file);
        assertArrayEquals(actual, EXPECTED_STATEMENTS);
    }

    @Test
    public void givenSQLFileWithLineCommentsWithinStatement_whenParse_thenIgnoreComments() throws IOException {
        SQLFile file = givenSQLFileWhenParse(
                STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 + NEW_LINE + LINE_COMMENT + NEW_LINE + STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2
        );

        String[] actual = getStatementsFromFile(file);
        assertEquals(actual.length, 1);
        assertEquals(actual[0], STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 + SPACE + STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2);
    }

    @Test
    public void givenSQLFileWithBlockCommentsWithinStatement_whenParse_thenIgnoreComments() throws IOException {
        SQLFile file = givenSQLFileWhenParse(
                STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 + NEW_LINE + BLOCK_COMMENT + NEW_LINE + STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2
        );

        String[] actual = getStatementsFromFile(file);
        assertEquals(actual.length, 1);
        assertEquals(actual[0], STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 + SPACE + STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2);
    }

    private SQLFile givenSQLFileWhenParse(String sql) throws IOException {
        SQLFile sqlFile = new SQLFile();
        sqlFile.parse(new StringReader(sql));
        return sqlFile;
    }

    private String[] getStatementsFromFile(SQLFile file) {
        List<String> statements = file.getStatements();
        return statements.toArray(new String[statements.size()]);
    }
}
