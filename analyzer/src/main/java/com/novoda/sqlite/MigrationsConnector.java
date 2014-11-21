package com.novoda.sqlite;

import com.novoda.sqlite.impl.SQLFile;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MigrationsConnector implements Connector {

    private final Iterable<File> migrations;

    public MigrationsConnector(Migrations migrations) {
        this.migrations = migrations.asIterable();
    }

    @Override
    public Connection connect() throws SQLException {
        try {
            // Dynamically load the class in order to correctly initialise JDBC drivers, as described in
            // http://stackoverflow.com/questions/6740601/what-does-class-fornameorg-sqlite-jdbc-do/6740632#6740632
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        executeMigrations(connection);
        return connection;
    }

    private void executeMigrations(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        for (File migration : migrations) {
            try {
                List<String> statements = null;
                statements = SQLFile.statementsFrom(migration);
                for (String sqlCommand : statements) {
                    statement.executeUpdate(sqlCommand);
                }
            } catch (IOException e) {
                throw new SQLException("error processing migration file: " + migration.toString(), e);
            }
        }
        statement.close();
    }

}
