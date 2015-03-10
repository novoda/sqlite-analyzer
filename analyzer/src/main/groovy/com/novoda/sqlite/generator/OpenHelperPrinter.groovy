package com.novoda.sqlite.generator

import com.novoda.sqlite.model.Database
import groovy.text.GStringTemplateEngine

public class OpenHelperPrinter {

    private static final String TEMPLATE = '''\
package $packageName;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.novoda.lib.sqliteprovider.util.Migrations;

import java.io.IOException;

public final class DBOpenHelper extends SQLiteOpenHelper {
    private static final String MIGRATIONS_PATH = "migrations";
    private static final String TAG = DBOpenHelper.class.getSimpleName();

    private final Context context;

    public DBOpenHelper(Context context) throws IOException {
        this(context, null);
    }

    public DBOpenHelper(Context context, CursorFactory factory) throws IOException {
        this(context, context.getPackageName() + ".db", factory, Migrations.getVersion(context.getAssets(), MIGRATIONS_PATH));
    }

    public DBOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Migrations.migrate(db, context.getAssets(), MIGRATIONS_PATH);
        } catch (IOException e) {
            Log.e(TAG, e.toString(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
'''
    String packageName
    private final Database database
    private final File targetDir

    OpenHelperPrinter(Database database, File targetDir) {
        this.targetDir = targetDir
        this.database = database
    }

    public void print() throws IOException {
        File targetFile = new File(makeFileDir(), "DBOpenHelper.java")
        targetFile.text = emitClass()
    }

    private String emitClass(def printers) {
        use(TableJavaCategory, ColumnJavaCategory, ColumnAndroidCategory) {
            new GStringTemplateEngine().createTemplate(TEMPLATE)
                    .make(packageName: packageName)
                    .toString()
        }
    }

    private File makeFileDir() {
        String packageAsDir = packageName.replaceAll(~/\./, "/")
        def fileDir = new File(targetDir, packageAsDir)
        fileDir.mkdirs()
        return fileDir
    }

}
