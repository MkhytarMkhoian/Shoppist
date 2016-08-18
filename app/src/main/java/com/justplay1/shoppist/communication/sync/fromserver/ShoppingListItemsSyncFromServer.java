package com.justplay1.shoppist.communication.sync.fromserver;

import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.alarm.builders.ShoppingListItemsNotificationBuilder;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Unit;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class ShoppingListItemsSyncFromServer extends SyncFromServer<ShoppingListItem> {
    @Override
    protected Collection<ShoppingListItem> getDataFromServer(long refreshTime) throws ParseException {
        List<ShoppingListItem> result = new ArrayList<>();
        List<ParseObject> objects = ServerRequests.getAllShoppingListItems(refreshTime);
        Map<String, Category> categoryMap = tablesHolder.getCategoriesTable().getAllCategories();
        Map<String, Unit> unitMap = tablesHolder.getUnitTable().getAllUnits();
        Map<String, Currency> currencyMap = tablesHolder.getCurrenciesTable().getAllCurrencies();
        for (ParseObject object : objects) {
            Category category = categoryMap.get(object.getString(ShoppingListContact.ShoppingListItemColumns.CATEGORY_ID));
            Unit unit = unitMap.get(object.getString(ShoppingListContact.ShoppingListItemColumns.UNIT_ID));
            Currency currency = currencyMap.get(object.getString(ShoppingListContact.ShoppingListItemColumns.CURRENCY_ID));
            result.add(new ShoppingListItem(object, category, unit, currency));
        }
        return result;
    }

    @Override
    protected Map<String, ShoppingListItem> getCache() {
        return tablesHolder.getShoppingListItemTable().getAllShoppingListItems();
    }

    @Override
    protected BaseTable<ShoppingListItem> getTable() {
        return tablesHolder.getShoppingListItemTable();
    }

    @Override
    protected NotificationBuilder<ShoppingListItem> getNotificationBuilder() {
        return new ShoppingListItemsNotificationBuilder(tablesHolder.getContext(), tablesHolder.getShoppingListTable().getAllShoppingLists().values());
    }

    @Override
    protected String getTag() {
        return "ShoppingListItems from";
    }
}
