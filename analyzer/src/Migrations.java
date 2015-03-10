package $packageName;

import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class Migrations {

    private static final String TAG = Migrations.class.getSimpleName();
    private final SortedSet<String> migrations;

    private final int startDate;

    public Migrations() {
        this(-1);
    }

    public Migrations(int startDate) {
        this.startDate = startDate;
        migrations = new TreeSet<String>(comparator);
    }

    public boolean add(String migration) {
        if (shouldInsert(migration)) {
            return migrations.add(migration);
        }
        return false;
    }

    private boolean shouldInsert(String migration) {
        return extractDate(migration) > startDate;
    }

    private int extractDate(String migration) {
        try {
            return Integer.parseInt(migration.split("_", 0)[0]);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Invalid int, returning -1.", e);
            return -1;
        }
    }

    public SortedSet<String> getMigrationsFiles() {
        return migrations;
    }

    /**
     * Comparator against filename: <date>_create.sql vs <date2>_create.sql will compare date with date2
     */
    private final Comparator<String> comparator = new Comparator<String>() {
        @Override
        public int compare(String file, String another) {
            return Integer.valueOf(extractDate(file)).compareTo(Integer.valueOf(extractDate(another)));
        }
    };

    public static void migrate(SQLiteDatabase db, AssetManager manager, String assetLocation) throws IOException {

        if (infoLoggingEnabled()) {
            Log.i(TAG, "current DB version is: " + db.getVersion());
        }

        String[] sqls = manager.list(assetLocation);

        if (sqls.length == 0) {
            Log.w(TAG, "No SQL file found in asset folder");
            return;
        }

        Migrations migrations = new Migrations(db.getVersion());
        Reader reader;

        for (String sqlfile : sqls) {
            migrations.add(sqlfile);
        }

        for (String sql : migrations.getMigrationsFiles()) {
            reader = new InputStreamReader(manager.open(assetLocation + File.separator + sql, AssetManager.ACCESS_RANDOM));
            if (infoLoggingEnabled()) {
                Log.i(TAG, "executing SQL file: " + assetLocation + File.separator + sql);
            }
            try {
                db.beginTransaction();
                for (String insert : SQLFile.statementsFrom(reader)) {
                    if (TextUtils.isEmpty(insert.trim())) {
                        continue;
                    }
                    if (infoLoggingEnabled()) {
                        Log.i(TAG, "executing insert: " + insert);
                    }
                    db.execSQL(insert);
                }
                db.setTransactionSuccessful();

            } catch (SQLException exception) {
                Log.e(TAG, "error in migrate against file: " + sql, exception);
            } finally {
                db.endTransaction();
            }
        }

        if (migrations.getMigrationsFiles().size() > 0) {
            int v = migrations.extractDate(migrations.getMigrationsFiles().last());
            db.setVersion(v);
            if (infoLoggingEnabled()) {
                Log.i(TAG, "setting version of DB to: " + v);
            }
        }
    }

    private static boolean infoLoggingEnabled() {
        return Log.isLoggable(TAG, Log.INFO);
    }

    public static int getVersion(AssetManager assets, String migrationsPath) throws IOException {
        int version = 1;
        String[] sqls = assets.list(migrationsPath);
        if (sqls.length == 0) {
            Log.w(TAG, "You need to add atleast one SQL file in your assets/" + migrationsPath + " folder");
        } else {
            Migrations migrations = new Migrations(-1);
            for (String sqlfile : sqls) {
                migrations.add(sqlfile);
            }
            version = (migrations.extractDate(migrations.getMigrationsFiles().last()));
            if (infoLoggingEnabled()) {
                Log.i(TAG, "current migration file version is: " + version);
            }
        }
        return version;
    }


    /**
     * Parsing .sql files and get single statements suitable for insertion.
     */
    public static class SQLFile {

        private static final String STATEMENT_END_CHARACTER = ";";
        private static final String LINE_COMMENT_START_CHARACTERS = "--";
        private static final String BLOCK_COMMENT_START_CHARACTERS = "/*";
        private static final String BLOCK_COMMENT_END_CHARACTERS = "*/";
        private static final String LINE_CONCATENATION_CHARACTER = " ";

        private List<String> statements;

        private boolean inComment = false;

        public void parse(Reader in) throws IOException {
            BufferedReader reader = new BufferedReader(in);
            statements = new ArrayList<>();
            String line;
            StringBuilder statement = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = stripOffTrailingComment(line).trim();

                if (line.length() == 0) {
                    continue;
                }

                if (line.startsWith(BLOCK_COMMENT_START_CHARACTERS)) {
                    inComment = true;
                    continue;
                }

                if (inComment && line.endsWith(BLOCK_COMMENT_END_CHARACTERS)) {
                    inComment = false;
                    continue;
                }

                if (inComment) {
                    continue;
                }

                statement.append(line);
                if (!line.endsWith(STATEMENT_END_CHARACTER)) {
                    statement.append(LINE_CONCATENATION_CHARACTER);
                    continue;
                }

                statements.add(statement.toString());
                statement.setLength(0);
            }
            reader.close();
            if (statement.length() > 0) {
                throw new IOException("incomplete sql statement (missing semicolon?): " + statement.toString());
            }
        }

        private String stripOffTrailingComment(String line) {
            int commentStartIndex = line.indexOf(LINE_COMMENT_START_CHARACTERS);
            if (commentStartIndex != -1) {
                return line.substring(0, commentStartIndex);
            }
            return line;
        }

        public List<String> getStatements() {
            return statements;
        }

        public static List<String> statementsFrom(Reader reader) throws IOException {
            SQLFile file = new SQLFile();
            file.parse(reader);
            return file.getStatements();
        }
    }
}