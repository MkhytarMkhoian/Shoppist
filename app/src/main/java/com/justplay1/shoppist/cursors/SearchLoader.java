package com.justplay1.shoppist.cursors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Product;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Mkhytar on 14.02.2016.
 */
public class SearchLoader extends AsyncTaskLoader<Map<String, Product>> {

    public static final int ID = 7;

    public SearchLoader(Context context) {
        super(context);
    }

    @Override
    public Map<String, Product> loadInBackground() {
        Map<String, Product> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        });
        Cursor cursor = App.get().getProductsManager().getAllProductsCursor();
        Category noCategory = App.get().getTablesHolder().getCategoriesTable().getNoCategory();
        if (!cursor.isClosed() && cursor.moveToFirst()) {
            do {
                Product item = new Product(cursor);
                if (item.isCategoryEmpty()) {
                    item.setCategory(noCategory);
                }
                map.put(item.getName(), item);
            } while (cursor.moveToNext());
        }
        return map;
    }
}
