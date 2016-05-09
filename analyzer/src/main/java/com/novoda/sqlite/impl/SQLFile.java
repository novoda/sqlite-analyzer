package com.novoda.sqlite.impl;

import com.novoda.sqlite.grammar.SQLiteLexer;
import com.novoda.sqlite.grammar.SQLiteParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Parsing .sql files and get single statements suitable for insertion.
 */
public class SQLFile {

    private List<String> statements;

    public void parse(Reader in) throws IOException {
        String sqlContent = getStringFromReader(in);

        SQLiteLexer lexer = new SQLiteLexer(new ANTLRInputStream(sqlContent));
        SQLiteParser parser = new SQLiteParser(new CommonTokenStream(lexer));

        // parse() contains a list of sql_stmt_list
        List<SQLiteParser.Sql_stmt_listContext> statementListContext = parser.parse().sql_stmt_list();

        // every sql_stmt_list contains one or more sql_stmt, we need to flatten it
        List<SQLiteParser.Sql_stmtContext> statementList = new ArrayList<SQLiteParser.Sql_stmtContext>();
        for (SQLiteParser.Sql_stmt_listContext statements : statementListContext) {
            statementList.addAll(statements.sql_stmt());
        }

        this.statements = new ArrayList<String>(statementList.size());
        for (SQLiteParser.Sql_stmtContext statement : statementList) {
            int startIndex = statement.getStart().getStartIndex();
            int stopIndex = statement.getStop().getStopIndex();
            String textStatement = sqlContent.substring(startIndex, stopIndex + 1) + ";";
            this.statements.add(textStatement);
        }
    }

    private String getStringFromReader(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line;

        BufferedReader bufferedReader = new BufferedReader(reader);
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }

    public List<String> getStatements() {
        return statements;
    }

    public static List<String> statementsFrom(File sqlFile) throws IOException {
        FileReader reader = null;
        try {
            reader = new FileReader(sqlFile);
            return statementsFrom(reader);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static List<String> statementsFrom(Reader reader) throws IOException {
        SQLFile file = new SQLFile();
        file.parse(reader);
        return file.getStatements();
    }
}
