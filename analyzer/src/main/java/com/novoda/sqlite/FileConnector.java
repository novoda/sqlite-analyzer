package com.novoda.sqlite;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FileConnector implements Connector {

    private final File dbFile;

    public FileConnector(File dbFile) {
        this.dbFile = dbFile;
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
        String dbPath = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        return DriverManager.getConnection(dbPath);
    }
}
