package com.justplay1.shoppist.communication.network;

import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.communication.sync.ShoppistAuthenticator;
import com.justplay1.shoppist.communication.sync.ShoppistServerAuthenticate;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.database.ShoppingListContact.CategoriesColumns;
import com.justplay1.shoppist.database.ShoppingListContact.CurrencyColumns;
import com.justplay1.shoppist.database.ShoppingListContact.ProductsColumns;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingListColumns;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingListItemColumns;
import com.justplay1.shoppist.database.ShoppingListContact.UnitColumns;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ServerModel;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by Mkhitar on 13.06.2015.
 */
public class ServerRequests implements ServerConstants {

    public static void logout(final Activity activity, ExecutorListener<Boolean> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                deleteSyncAccount(activity);
                ShoppistServerAuthenticate.logout();
                return true;
            }
        }, listener);
    }

    private static void deleteSyncAccount(Activity activity) throws OperationCanceledException, IOException, AuthenticatorException {
        if (App.get().mAccount != null) {
            AccountManager accountManager = AccountManager.get(activity);
            String authToken = accountManager.blockingGetAuthToken(App.get().mAccount, ShoppistAuthenticator.AUTHTOKEN_TYPE_FULL_ACCESS, false);
            accountManager.invalidateAuthToken(ShoppistAuthenticator.ACCOUNT_TYPE, authToken);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccount(App.get().mAccount, activity, null, null);
            } else {
                accountManager.removeAccount(App.get().mAccount, null, null);
            }
        }
    }

    public static void deleteAccount(final Activity activity, ExecutorListener<Boolean> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                List<ParseObject> toDelete = new ArrayList<>();
                toDelete.addAll(ParseUser.getCurrentUser().getRelation(CATEGORIES).getQuery().find());
                if (toDelete.size() > 0) {
                    ParseObject.deleteAll(toDelete);
                }
                toDelete.clear();
                toDelete.addAll(ParseUser.getCurrentUser().getRelation(UNITS).getQuery().find());
                if (toDelete.size() > 0) {
                    ParseObject.deleteAll(toDelete);
                }
                toDelete.clear();
                toDelete.addAll(ParseUser.getCurrentUser().getRelation(CURRENCIES).getQuery().find());
                if (toDelete.size() > 0) {
                    ParseObject.deleteAll(toDelete);
                }
                toDelete.clear();
                toDelete.addAll(ParseUser.getCurrentUser().getRelation(SHOPPING_LISTS_TABLE).getQuery().find());
                if (toDelete.size() > 0) {
                    ParseObject.deleteAll(toDelete);
                }
                toDelete.clear();
                toDelete.addAll(ParseUser.getCurrentUser().getRelation(SHOPPING_LIST_ITEMS_TABLE).getQuery().find());
                if (toDelete.size() > 0) {
                    ParseObject.deleteAll(toDelete);
                }
                toDelete.clear();
                toDelete.addAll(ParseUser.getCurrentUser().getRelation(GOODS_TABLE).getQuery().find());
                if (toDelete.size() > 0) {
                    ParseObject.deleteAll(toDelete);
                }

                deleteSyncAccount(activity);
                Task<Void> task = ParsePush.unsubscribeInBackground(ServerConstants.USER_CHANEL_NAME_FIRST_PART + ParseUser.getCurrentUser().getObjectId());
                task.waitForCompletion();
                ParseUser.getCurrentUser().delete();
                ParseUser.logOut();
                ShoppistUtils.restoreData();
                Log.d("Shoppist", "account delete and data restored");
                return true;
            }
        }, listener);
    }

    public static void fullSync() {
        App.get().getSyncLimitFilter().requestSync(false);
    }

    public static void singUpSync(ExecutorListener<Boolean> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                long userStoredHash = ShoppistPreferences.getParseUserIdHash();
                long currentUserHash = ParseUser.getCurrentUser().getObjectId().hashCode();

                if (userStoredHash != 0 && currentUserHash != userStoredHash) {
                    ShoppistUtils.restoreData();
                    ShoppistPreferences.setParseUserIdHash(currentUserHash);
                }
                App.get().getSyncLimitFilter().requestSync(false);
                return true;
            }
        }, listener);
    }

    private static ParseObject fillObject(ParseObject objectToFill, Product product) {
        objectToFill.put(ProductsColumns.PRODUCT_ID, product.getId());
        objectToFill.put(ProductsColumns.IS_DELETED, product.isDelete());
        objectToFill.put(ProductsColumns.NAME, product.getName());
        objectToFill.put(ProductsColumns.IS_CREATE_BY_USER, product.isCreateByUser());
        objectToFill.put(ProductsColumns.TIME_CREATED, product.getTimeCreated());
        if (!product.isCategoryEmpty()){
            objectToFill.put(ProductsColumns.CATEGORY_ID, product.getCategory().getId());
        } else {
            objectToFill.put(ProductsColumns.CATEGORY_ID, Category.NO_CATEGORY_ID);
        }
        if (!product.isUnitEmpty()){
            objectToFill.put(ProductsColumns.UNIT_ID, product.getUnit().getId());
        } else {
            objectToFill.put(ProductsColumns.UNIT_ID, Unit.NO_UNIT_ID);
        }
        return objectToFill;
    }

    private static ParseObject fillObject(ParseObject objectToFill, Category category) {
        objectToFill.put(CategoriesColumns.IS_DELETED, category.isDelete());
        objectToFill.put(CategoriesColumns.CATEGORY_ID, category.getId());
        objectToFill.put(CategoriesColumns.NAME, category.getName());
        objectToFill.put(CategoriesColumns.COLOR, category.getColor());
        objectToFill.put(CategoriesColumns.CREATE_BY_USER, category.isCreateByUser());
        objectToFill.put(CategoriesColumns.ENABLE, category.isEnable());
        return objectToFill;
    }

    private static ParseObject fillObject(ParseObject objectToFill, Currency currency) {
        objectToFill.put(CurrencyColumns.NAME, currency.getName());
        objectToFill.put(CurrencyColumns.CURRENCY_ID, currency.getId());
        objectToFill.put(CurrencyColumns.IS_DELETED, currency.isDelete());
        return objectToFill;
    }

    private static ParseObject fillObject(ParseObject objectToFill, Unit unit) {
        objectToFill.put(UnitColumns.UNIT_ID, unit.getId());
        objectToFill.put(UnitColumns.SHORT_NAME, unit.getShortName());
        objectToFill.put(UnitColumns.FULL_NAME, unit.getName());
        objectToFill.put(UnitColumns.IS_DELETED, unit.isDelete());
        return objectToFill;
    }

    private static ParseObject fillObject(ParseObject objectToFill, ShoppingList list) {
        objectToFill.put(ShoppingListColumns.LIST_ID, list.getId());
        objectToFill.put(ShoppingListColumns.IS_DELETED, list.isDelete());
        objectToFill.put(ShoppingListColumns.LIST_NAME, list.getName());
        objectToFill.put(ShoppingListColumns.COLOR, list.getColor());
        objectToFill.put(ShoppingListColumns.PRIORITY, list.getPriority().ordinal());
        objectToFill.put(ShoppingListColumns.TIME_CREATED, list.getTimeCreated());
        return objectToFill;
    }

    private static ParseObject fillObject(ParseObject objectToFill, ShoppingListItem item) {
        objectToFill.put(ShoppingListItemColumns.IS_DELETED, item.isDelete());
        objectToFill.put(ShoppingListItemColumns.LIST_ITEM_NAME, item.getName());
        objectToFill.put(ShoppingListItemColumns.LIST_ITEM_ID, item.getId());
        objectToFill.put(ShoppingListItemColumns.PARENT_LIST_ID, item.getParentListId());
        objectToFill.put(ShoppingListItemColumns.PRIORITY, item.getPriority().ordinal());
        objectToFill.put(ShoppingListItemColumns.TIME_CREATED, item.getTimeCreated());
        objectToFill.put(ShoppingListItemColumns.PRICE, item.getPrice());
        objectToFill.put(ShoppingListItemColumns.SHORT_DESCRIPTION, item.getNote());
        objectToFill.put(ShoppingListItemColumns.STATUS, item.getStatus().ordinal());
        objectToFill.put(ShoppingListItemColumns.QUANTITY, item.getQuantity());

        if (!item.isCategoryEmpty()){
            objectToFill.put(ShoppingListItemColumns.CATEGORY_ID, item.getCategory().getId());
        } else {
            objectToFill.put(ShoppingListItemColumns.CATEGORY_ID, Category.NO_CATEGORY_ID);
        }
        if (!item.isUnitEmpty()){
            objectToFill.put(ShoppingListItemColumns.UNIT_ID, item.getUnit().getId());
        } else {
            objectToFill.put(ShoppingListItemColumns.UNIT_ID, Unit.NO_UNIT_ID);
        }
        if (!item.isCurrencyEmpty()){
            objectToFill.put(ShoppingListItemColumns.CURRENCY_ID, item.getCurrency().getId());
        } else {
            objectToFill.put(ShoppingListItemColumns.CURRENCY_ID, Currency.NO_CURRENCY_ID);
        }
        return objectToFill;
    }

    private static ParseObject fillObject(ParseObject objectToFill, ServerModel item, int flag) {
        switch (flag) {
            case CATEGORY:
                return fillObject(objectToFill, (Category) item);
            case CURRENCY:
                return fillObject(objectToFill, (Currency) item);
            case SHOPPING_LIST:
                return fillObject(objectToFill, (ShoppingList) item);
            case SHOPPING_LIST_ITEM:
                return fillObject(objectToFill, (ShoppingListItem) item);
            case UNIT:
                return fillObject(objectToFill, (Unit) item);
            case GOODS:
                return fillObject(objectToFill, (Product) item);
        }
        return objectToFill;
    }

    private static List<ParseObject> getAllItemsFromUser(String relationKey, long updateTime) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        ParseRelation<ParseObject> relation = user.getRelation(relationKey);
        ParseQuery<ParseObject> query = relation.getQuery();
        query.whereGreaterThan(TIMESTAMP, updateTime);
        switch (relationKey) {
            case CATEGORIES:
                query.orderByAscending(ShoppingListContact.Categories.CATEGORY_ID);
                break;
            case CURRENCIES:
                query.orderByAscending(ShoppingListContact.Currencies.CURRENCY_ID);
                break;
            case UNITS:
                query.orderByAscending(ShoppingListContact.Units.UNIT_ID);
                break;
        }

        return query.find();
    }

    private static ParseObject addList(BaseModel list, String table, int flag) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(table);

        ParseObject object = new ParseObject(table);
        fillObject(object, list, flag);
        object.put(TIMESTAMP, refreshTime);
        object.save();
        relation.add(object);

        setRefreshTime(user, table, refreshTime);
        user.save();
        return object;
    }

    private static <T extends ServerModel> List<ParseObject> addItems(Collection<T> items, String table, int flag) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        List<ParseObject> toSave = new ArrayList<>(items.size());

        for (ServerModel item : items) {
            ParseObject object = new ParseObject(table);
            fillObject(object, item, flag);
            object.put(TIMESTAMP, refreshTime);
            toSave.add(object);
        }
        ParseObject.saveAll(toSave);

        ParseRelation<ParseObject> relation = user.getRelation(table);
        for (ParseObject item : toSave) {
            relation.add(item);
        }
        setRefreshTime(user, table, refreshTime);
        user.save();
        return toSave;
    }

    private static <T extends BaseModel> List<ParseObject> updateItems(Collection<T> list, String table, int flag) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        List<ParseObject> toSave = new ArrayList<>(list.size());

        for (BaseModel item : list) {
            if (item.getServerId() != null) {
                ParseObject object = ParseObject.createWithoutData(table, item.getServerId());
                fillObject(object, item, flag);
                object.put(TIMESTAMP, refreshTime);
                toSave.add(object);
                item.setTimestamp(refreshTime);
            }
        }
        if (toSave.size() > 0) {
            ParseObject.saveAll(toSave);
            setRefreshTime(user, table, refreshTime);
            user.save();
        }
        return toSave;
    }

    private static <T extends BaseModel> Collection<T> deleteItems(Collection<T> lists, String table) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(table);
        List<ParseObject> toDelete = new ArrayList<>(lists.size());
        for (BaseModel item : lists) {
            if (item.getServerId() != null) {
                ParseObject object = ParseObject.createWithoutData(table, item.getServerId());
                toDelete.add(object);
                relation.remove(object);
            }
        }
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
            setRefreshTime(user, table, refreshTime);
            user.save();
        }
        return lists;
    }

    private static ParseObject getItem(String table, String parseId) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(table);
        query.whereContains(ShoppingListColumns.SERVER_ID, parseId);
        return query.get(parseId);
    }

    private static <T extends BaseModel> Collection<T> deleteLists(Collection<T> lists, String table) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(table);

        List<ParseObject> toDelete = new ArrayList<>(lists.size());
        for (T item : lists) {
            if (item.getServerId() != null) {
                ParseObject list = ParseObject.createWithoutData(table, item.getServerId());
                list.fetch();

                toDelete.add(list);
                relation.remove(list);
            }
        }
        if (toDelete.size() > 0) {
            ParseObject.deleteAll(toDelete);
            setRefreshTime(user, table, refreshTime);
            user.save();
        }
        return lists;
    }

    public static List<ParseObject> getAllCategories(long updateTime) throws ParseException {
        return getAllItemsFromUser(CATEGORIES, updateTime);
    }

    public static ParseObject getCategory(String parseId) throws ParseException {
        return getItem(CATEGORIES, parseId);
    }

    public static List<ParseObject> addCategories(Collection<Category> categories) throws ParseException {
        return addItems(categories, CATEGORIES, CATEGORY);
    }

    public static List<ParseObject> addProducts(Collection<Product> products) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        List<ParseObject> toSave = new ArrayList<>(products.size());
        if (user == null) return toSave;

        final long refreshTime = System.currentTimeMillis();

        for (Product item : products) {
            ParseObject object = new ParseObject(GOODS_TABLE);
            fillObject(object, item);
            object.put(TIMESTAMP, refreshTime);
            object.put(CREATED_BY, user);
            toSave.add(object);
        }
        if (toSave.size() > 0) {
            ParseObject.saveAll(toSave);

            ParseRelation<ParseObject> relation = user.getRelation(GOODS_TABLE);
            for (ParseObject object : toSave) {
                relation.add(object);
            }
            setRefreshTime(user, GOODS_TABLE, refreshTime);
            user.save();
        }
        return toSave;
    }

    public static List<ParseObject> updateProducts(Collection<Product> products) throws ParseException {
        return updateItems(products, GOODS_TABLE, GOODS);
    }

    public static List<ParseObject> updateCategories(Collection<Category> categories) throws ParseException {
        return updateItems(categories, CATEGORIES, CATEGORY);
    }

    public static Collection<Category> deleteCategories(Collection<Category> lists) throws ParseException {
        return deleteItems(lists, CATEGORIES);
    }

    public static void deleteCategories() throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return;

        final long refreshTime = System.currentTimeMillis();
        ParseObject.deleteAll(user.getRelation(CATEGORIES).getQuery().find());

        setRefreshTime(user, CATEGORIES, refreshTime);
        user.save();
    }

    public static List<ParseObject> getAllUnits() throws ParseException {
        return getAllUnits(0);
    }

    public static List<ParseObject> getAllUnits(long updateTime) throws ParseException {
        return getAllItemsFromUser(UNITS, updateTime);
    }

    public static List<ParseObject> addUnits(Collection<Unit> units) throws ParseException {
        return addItems(units, UNITS, UNIT);
    }

    public static List<ParseObject> updateUnits(Collection<Unit> units) throws ParseException {
        return updateItems(units, UNITS, UNIT);
    }

    public static Collection<Unit> deleteUnits(Collection<Unit> lists) throws ParseException {
        return deleteItems(lists, UNITS);
    }

    public static List<ParseObject> getAllCurrencies() throws ParseException {
        return getAllCurrencies(0);
    }

    public static List<ParseObject> getAllCurrencies(long updateTime) throws ParseException {
        return getAllItemsFromUser(CURRENCIES, updateTime);
    }

    public static List<ParseObject> addCurrencies(Collection<Currency> currencies) throws ParseException {
        return addItems(currencies, CURRENCIES, CURRENCY);
    }

    public static List<ParseObject> updateCurrencies(Collection<Currency> currencies) throws ParseException {
        return updateItems(currencies, CURRENCIES, CURRENCY);
    }

    public static Collection<Currency> deleteCurrencies(Collection<Currency> lists) throws ParseException {
        return deleteItems(lists, CURRENCIES);
    }

    public static ParseObject getShoppingList(String parseId) throws ParseException {
        return getItem(SHOPPING_LISTS_TABLE, parseId);
    }

    public static ParseObject addShoppingList(ShoppingList list) throws ParseException {
        return addList(list, SHOPPING_LISTS_TABLE, SHOPPING_LIST);
    }

    public static List<ParseObject> addShoppingLists(Collection<ShoppingList> lists) throws ParseException {
        return addItems(lists, SHOPPING_LISTS_TABLE, SHOPPING_LIST);
    }

    public static List<ParseObject> getAllShoppingLists() throws ParseException {
        return getAllShoppingLists(0);
    }

    public static List<ParseObject> getAllShoppingLists(long updateTime) throws ParseException {
        return getAllItemsFromUser(SHOPPING_LISTS_TABLE, updateTime);
    }

    public static List<ParseObject> updateShoppingLists(Collection<ShoppingList> list) throws ParseException {
        return updateItems(list, SHOPPING_LISTS_TABLE, SHOPPING_LIST);
    }

    public static Collection<ShoppingList> deleteShoppingLists(Collection<ShoppingList> lists) throws ParseException {
        return deleteLists(lists, SHOPPING_LISTS_TABLE);
    }

    public static List<ParseObject> addShoppingListItems(Collection<ShoppingListItem> items) throws ParseException {
        List<ParseObject> toSave = new ArrayList<>(items.size());

        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return toSave;

        final long refreshTime = System.currentTimeMillis();

        for (ShoppingListItem item : items) {
            if (item.getServerId() != null) continue;

            ParseObject object = new ParseObject(SHOPPING_LIST_ITEMS_TABLE);
            fillObject(object, item);
            object.put(TIMESTAMP, refreshTime);
            object.put(CREATED_BY, user);
            toSave.add(object);
        }
        if (toSave.size() > 0) {
            ParseObject.saveAll(toSave);

            ParseRelation<ParseObject> relation = user.getRelation(SHOPPING_LIST_ITEMS_TABLE);
            for (ParseObject object : toSave) {
                relation.add(object);
            }
            setRefreshTime(user, SHOPPING_LIST_ITEMS_TABLE, refreshTime);
            user.save();
        }
        return toSave;
    }

    public static List<ParseObject> updateShoppingListItems(Collection<ShoppingListItem> items) throws ParseException {
        return updateItems(items, SHOPPING_LIST_ITEMS_TABLE, SHOPPING_LIST_ITEM);
    }

    public static Collection<ShoppingListItem> deleteShoppingListItems(Collection<ShoppingListItem> items, String parentParseId) throws ParseException {
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) return null;

        final long refreshTime = System.currentTimeMillis();
        ParseRelation<ParseObject> relation = user.getRelation(SHOPPING_LIST_ITEMS_TABLE);

        List<ParseObject> toDelete = new ArrayList<>(items.size());
        for (ShoppingListItem item : items) {
            ParseObject object = ParseObject.createWithoutData(SHOPPING_LIST_ITEMS_TABLE, item.getServerId());
            toDelete.add(object);
            relation.remove(object);
        }
        ParseObject.deleteAll(toDelete);

        setRefreshTime(user, SHOPPING_LISTS_TABLE, refreshTime);
        user.save();
        return items;
    }

    public static void loadAllData() {
        long userStoredHash = ShoppistPreferences.getParseUserIdHash();
        long currentUserHash = ParseUser.getCurrentUser().getObjectId().hashCode();

        if (currentUserHash != 0 && currentUserHash != userStoredHash) {
            ShoppistPreferences.setParseUserIdHash(currentUserHash);
            App.get().getSyncLimitFilter().requestSync(true, false);
        } else {
            App.get().getSyncLimitFilter().requestSync(false);
        }
    }

    public static List<ParseObject> getAllShoppingListItems() throws ParseException {
        return getAllShoppingListItems(0);
    }

    public static List<ParseObject> getAllProducts(long updateTime) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(GOODS_TABLE);
        query.whereEqualTo(CREATED_BY, ParseUser.getCurrentUser());
        query.whereGreaterThan(TIMESTAMP, updateTime);
        return query.find();
    }

    public static List<ParseObject> getAllShoppingListItems(long updateTime) throws ParseException {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(SHOPPING_LIST_ITEMS_TABLE);
        query.whereEqualTo(CREATED_BY, ParseUser.getCurrentUser());
        query.whereGreaterThan(TIMESTAMP, updateTime);
        return query.find();
    }

    private static void setRefreshTime(ParseUser user, String table, final long time) {
        switch (table) {
            case SHOPPING_LISTS_TABLE:
                user.put(SHOPPING_LIST_TIMESTAMP, time);
                break;
            case SHOPPING_LIST_ITEMS_TABLE:
                user.put(SHOPPING_LIST_ITEM_TIMESTAMP, time);
                break;
            case CATEGORIES:
                user.put(CATEGORY_TIMESTAMP, time);
                break;
            case CURRENCIES:
                user.put(CURRENCY_TIMESTAMP, time);
                break;
            case UNITS:
                user.put(UNIT_TIMESTAMP, time);
                break;
            case GOODS_TABLE:
                user.put(GOODS_TIMESTAMP, time);
                break;
        }
    }

//    public static void setUserSubscriptions(@NonNull final Purchase purchase, ExecutorListener<ParseUser> listener) {
//        ThreadExecutor.doNetworkTaskAsync(new Callable<ParseUser>() {
//            @Override
//            public ParseUser call() throws Exception {
//                ParseUser user = ParseUser.getCurrentUser();
//                user.put(ServerConstants.SUBSCRIPTIONS, purchase.toJson(true));
//                user.save();
//                return user;
//            }
//        }, listener);
//    }
//
//    public static void isUserSubscriptionsValid(ExecutorListener<Boolean> listener) {
//        ThreadExecutor.doNetworkTaskAsync(new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//                return isSubscriptionsValid(getSubscription());
//            }
//        }, listener);
//    }
//
//    public static void getSubscription(ExecutorListener<UserPurchase> listener) {
//        ThreadExecutor.doNetworkTaskAsync(new Callable<UserPurchase>() {
//            @Override
//            public UserPurchase call() throws Exception {
//                return getSubscription();
//            }
//        }, listener);
//    }
//
//    public static UserPurchase getSubscription() throws ParseException, JSONException {
//        ParseUser user = ParseUser.getCurrentUser();
//        if (user == null) return null;
//        user.fetch();
//        String data = user.getString(ServerConstants.SUBSCRIPTIONS);
//        if (data == null) return null;
//        final JSONObject json = new JSONObject(data);
//        return UserPurchase.fromJson(data, json.optString("signature"));
//    }
//
//    public static boolean isSubscriptionsValid(UserPurchase purchase) {
//        if (purchase == null) return false;
//        if (purchase.state != Purchase.State.PURCHASED) return false;
//        long period = System.currentTimeMillis() - purchase.time;
//        switch (purchase.sku) {
//            case BaseInAppActivity.SKU_SYNC_MONTHLY:
//                return period <= TimeUnit.DAYS.toDays(30);
//            case BaseInAppActivity.SKU_SYNC_YEARLY:
//                return period <= TimeUnit.DAYS.toDays(365);
//        }
//        return false;
//    }
}
