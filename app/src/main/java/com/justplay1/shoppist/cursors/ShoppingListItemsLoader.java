package com.justplay1.shoppist.cursors;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class ShoppingListItemsLoader extends AsyncTaskLoader<Map<String, Object>> {

    public static final int ID = 5;

    private String parentListId;

    public ShoppingListItemsLoader(Context context, String parentListId) {
        super(context);
        this.parentListId = parentListId;
    }

    @Override
    public Map<String, Object> loadInBackground() {
        Map<String, Object> data = new HashMap<>();

        ShoppingList shoppingList = App.get().getShoppingListsManager().getShoppingList(parentListId);
        if (shoppingList == null) return data;

        Currency currency = App.get().getTablesHolder().getCurrenciesTable()
                .getCurrencies(Collections.singletonList(ShoppistPreferences.getDefaultCurrency())).get(ShoppistPreferences.getDefaultCurrency());

        if (currency == null) {
            currency = App.get().getTablesHolder().getCurrenciesTable().getNoCurrency();
        }
        data.put(Currency.class.getName(), currency);

        data.put(Cursor.class.getName(), App.get().getShoppingListItemsManager().getShoppingListItemsCursor(shoppingList));
        data.put(Unit.class.getName(), App.get().getTablesHolder().getUnitTable().getNoUnit());
        data.put(Category.class.getName(), App.get().getTablesHolder().getCategoriesTable().getNoCategory());
        data.put(ShoppingList.class.getName(), shoppingList);
        return data;
    }

    @Override
    public void deliverResult(Map<String, Object> data) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            return;
        }
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
