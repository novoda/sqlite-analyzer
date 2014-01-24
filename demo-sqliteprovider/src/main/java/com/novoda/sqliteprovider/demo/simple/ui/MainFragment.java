package com.novoda.sqliteprovider.demo.simple.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.sqliteprovider.demo.simple.DB;
import com.novoda.sqliteprovider.demo.simple.R;
import com.novoda.sqliteprovider.demo.simple.provider.FireworkProvider;

public class MainFragment extends Fragment {

    /**
     * See /assets/migrations/1_SETUP.SQL for the database creation
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * You can save multiple ways - this is just an example of using Uri's
         * do not normally do this on the UI Thread
         */
        saveNewShopToDatabase();
        /**
         * You can retrieve from the database multiple ways - this is just an example of using Uri's
         */
        retrieveShopsFromDatabase();
    }

    private void saveNewShopToDatabase() {
        Uri table = FireworkProvider.SHOPS;
        ContentValues values = new ContentValues(1);
        DB.Shop.setName("MyNewShop" + System.currentTimeMillis(), values);
        DB.Shop.setPostcode("LN11YA", values);
        getActivity().getContentResolver().insert(table, values);
    }

    private void retrieveShopsFromDatabase() {
        getActivity().getLoaderManager()
                .initLoader(R.id.loader_shop, null, new LoaderManager.LoaderCallbacks<Cursor>() {

                    @Override
                    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                        return new ShopCursorLoader(getActivity());
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
                        if (!cursor.moveToFirst()) {
                            Log.d("demo", "Nothing in DB, returning early");
                        }

                        do {
                            String shopName = DB.Shop.getName(cursor);
                            String shopPostcode = DB.Shop.getPostcode(cursor);

                            Log.d("demo", "Found shop: " + shopName);
                            Log.d("demo", "Found postcode: " + shopPostcode);
                        } while (cursor.moveToNext());

                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> cursorLoader) {

                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private static class ShopCursorLoader extends CursorLoader {

        public ShopCursorLoader(Context context) {
            super(context, FireworkProvider.SHOPS, null, null, null, null);
        }
    }
}
