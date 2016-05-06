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
        SQLFile sqlFile = givenSQLFileWhenParse(STATEMENT_CREATE_TABLE + NEW_LINE + STATEMENT_ALTER_TABLE);

        String[] actual = getStatementsFromFile(sqlFile);
        assertArrayEquals(EXPECTED_STATEMENTS, actual);
    }

    @Test
    public void givenSQLFileWithSpaces_whenParse_thenGetCorrectTrimmedStatements() throws IOException {
        SQLFile sqlFile = givenSQLFileWhenParse(SPACE + STATEMENT_CREATE_TABLE + SPACE + NEW_LINE + SPACE + STATEMENT_ALTER_TABLE + SPACE);

        String[] actual = getStatementsFromFile(sqlFile);
        assertArrayEquals(EXPECTED_STATEMENTS, actual);
    }

    @Test
    public void givenSQLFileWithLineComments_whenParse_thenIgnoreComments() throws IOException {
        SQLFile sqlFile = givenSQLFileWhenParse(
                STATEMENT_CREATE_TABLE + SPACE + LINE_COMMENT + NEW_LINE + STATEMENT_ALTER_TABLE + SPACE + LINE_COMMENT
        );

        String[] actual = getStatementsFromFile(sqlFile);
        assertArrayEquals(EXPECTED_STATEMENTS, actual);
    }

    @Test
    public void givenSQLFileWithBlockComments_whenParse_thenIgnoreComments() throws IOException {
        SQLFile sqlFile = givenSQLFileWhenParse(
                STATEMENT_CREATE_TABLE + NEW_LINE + BLOCK_COMMENT + NEW_LINE + STATEMENT_ALTER_TABLE
        );

        String[] actual = getStatementsFromFile(sqlFile);
        assertArrayEquals(EXPECTED_STATEMENTS, actual);
    }

    @Test
    public void givenSQLFileWithLineCommentsWithinStatement_whenParse_thenLeaveCommentsInStatement() throws IOException {
        String sqlContent = STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 + NEW_LINE + LINE_COMMENT + NEW_LINE + STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2;
        SQLFile sqlFile = givenSQLFileWhenParse(sqlContent);

        String[] actual = getStatementsFromFile(sqlFile);
        assertEquals(actual.length, 1);
        assertEquals(sqlContent, actual[0]);
    }

    @Test
    public void givenSQLFileWithBlockCommentsWithinStatement_whenParse_thenLeaveCommentsInStatement() throws IOException {
        String sqlContent = STATEMENT_FOR_COMMENTS_WITHIN_TESTS_1 + NEW_LINE + BLOCK_COMMENT + NEW_LINE + STATEMENT_FOR_COMMENTS_WITHIN_TESTS_2;
        SQLFile sqlFile = givenSQLFileWhenParse(sqlContent);

        String[] actual = getStatementsFromFile(sqlFile);
        assertEquals(actual.length, 1);
        assertEquals(sqlContent, actual[0]);
    }

    @Test
    public void givenSQLFileWithTable_andTableWithDoubleDashes_whenParse_thenDoNotTreatDashesAsComment() throws IOException {
        String sqlContent = "CREATE TABLE `all--pizzas`(flavor TEXT);";
        SQLFile sqlFile = givenSQLFileWhenParse(sqlContent);

        String[] actual = getStatementsFromFile(sqlFile);
        assertEquals(actual.length, 1);
        assertEquals(sqlContent, actual[0]);
    }

    @Test
    public void givenSQLFileWithMultipleStatementsInOneLine_whenParse_thenGetTwoStatements() throws IOException {
        String sqlContent = "CREATE TABLE `one`(id INTEGER); CREATE TABLE `two`(id INTEGER);";
        SQLFile sqlFile = givenSQLFileWhenParse(sqlContent);

        String[] actual = getStatementsFromFile(sqlFile);
        assertEquals(actual.length, 2);
    }

    private SQLFile givenSQLFileWhenParse(String sql) throws IOException {
        SQLFile sqlFile = new SQLFile();
        sqlFile.parse(new StringReader(sql));
        return sqlFile;
    }

    private String[] getStatementsFromFile(SQLFile sqlFile) {
        List<String> statements = sqlFile.getStatements();
        return statements.toArray(new String[statements.size()]);
    }
}
