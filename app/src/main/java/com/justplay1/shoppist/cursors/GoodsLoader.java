package com.justplay1.shoppist.cursors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Unit;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class GoodsLoader extends AsyncTaskLoader<Map<String, Object>> {

    public static final int ID = 8;

    public GoodsLoader(Context context) {
        super(context);
    }

    @Override
    public Map<String, Object> loadInBackground() {
        Map<String, Object> data = new HashMap<>();
        data.put(Cursor.class.getName(), App.get().getProductsManager().getAllProductsCursor());
        data.put(Unit.class.getName(), App.get().getTablesHolder().getUnitTable().getNoUnit());
        data.put(Category.class.getName(), App.get().getTablesHolder().getCategoriesTable().getNoCategory());
        return data;
    }
}
