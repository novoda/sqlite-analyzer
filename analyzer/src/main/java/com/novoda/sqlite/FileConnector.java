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
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String dbPath = "jdbc:sqlite:" + dbFile.getAbsolutePath();
        return DriverManager.getConnection(dbPath);
    }
}
