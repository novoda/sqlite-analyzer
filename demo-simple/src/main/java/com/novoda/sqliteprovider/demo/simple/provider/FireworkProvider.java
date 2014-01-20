package com.novoda.sqliteprovider.demo.simple.provider;

import android.net.Uri;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

import static com.novoda.sqliteprovider.demo.simple.DB.Tables;

public class FireworkProvider extends SQLiteContentProviderImpl {

    private static final String AUTHORITY = "content://com.novoda.demo.simple/";

    public static final Uri SHOPS = Uri.parse(AUTHORITY).buildUpon().appendPath(Tables.Shop).build();
}
