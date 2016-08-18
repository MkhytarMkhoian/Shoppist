package com.justplay1.shoppist.database;

import android.net.Uri;

import com.justplay1.shoppist.database.DBHelper.Tables;

/**
 * Created by Mkhitar on 21.07.2015.
 */
public class ShoppingListContact {

    public static final String AUTHORITY = "com.justplay1.shoppist." + DBHelper.DB_NAME;
    public static final String SCHEME = "content://";
    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);
    public static final String COLUMN_ID = "_id";

    private ShoppingListContact() {
    }

    public interface NotificationsColumns {
        String NOTIFICATION_ID = "notification_id";
        String TITLE = "notifications_title";
        String TIME = "notifications_time";
        String ITEM_NAMES = "notifications_item_names";
        String STATUS = "notifications_status";
        String TYPE = "notifications_type";

        String WHERE_STRING = NOTIFICATION_ID + "=?";
    }

    public interface ProductsColumns {
        String PRODUCT_ID = "product_id";
        String NAME = "product_name";
        String CATEGORY_ID = "product_category_id";
        String IS_CREATE_BY_USER = "product_is_create_by_user";
        String SERVER_ID = "product_parse_id";
        String TIME_CREATED = "product_time_created";
        String UNIT_ID = "product_unit_id";
        String IS_DELETED = "product_is_deleted";
        String TIMESTAMP = "product_timestamp";
        String IS_DIRTY = "product_is_dirty";

        String WHERE_PRODUCT_ID = PRODUCT_ID + "=?";
    }

    public interface CategoriesColumns {
        String SERVER_ID = "category_parse_id";
        String NAME = "category_name";
        String CATEGORY_ID = "main_category_id";
        String COLOR = "category_color";
        String ENABLE = "category_enable";
        String CREATE_BY_USER = "category_create_by_user";
        String MANUAL_SORT_POSITION = "category_manual_sort_position";
        String IS_DELETED = "category_is_deleted";
        String TIMESTAMP = "category_timestamp";
        String IS_DIRTY = "category_is_dirty";

        String WHERE_CATEGORY_ID = CATEGORY_ID + " IN(?)";
    }

    public interface ShoppingListColumns {
        String LIST_ID = "shopping_list_id";
        String SERVER_ID = "shopping_list_parse_id";
        String LIST_NAME = "shopping_list_name";
        String COLOR = "shopping_list_color";
        String PRIORITY = "shopping_list_priority";
        String TIME_CREATED = "shopping_list_time_created";
        String BOUGHT_COUNT = "shopping_list_bought_count";
        String SIZE = "shopping_list_size";
        String MANUAL_SORT_POSITION = "shopping_list_manual_sort_position";
        String IS_DELETED = "shopping_list_is_deleted";
        String TIMESTAMP = "shopping_list_timestamp";
        String IS_DIRTY = "shopping_list_is_dirty";

        String WHERE_STRING = LIST_ID + " IN(?)";
    }

    public interface ShoppingListItemColumns {
        String LIST_ITEM_ID = "shopping_list_item_id";
        String SERVER_ID = "shopping_list_item_parse_id";
        String PARENT_LIST_ID = "shopping_list_item_parent_list_id";
        String LIST_ITEM_NAME = "shopping_list_item_name";
        String SHORT_DESCRIPTION = "shopping_list_item_short_description";
        String STATUS = "shopping_list_item_status";
        String PRIORITY = "shopping_list_item_priority";
        String PRICE = "shopping_list_item_price";
        String QUANTITY = "shopping_list_item_quantity";
        String UNIT_ID = "shopping_list_item_unit_id";
        String CURRENCY_ID = "shopping_list_item_currency_id";
        String TIME_CREATED = "shopping_list_item_time_created";
        String CATEGORY_ID = "shopping_list_item_category_id";
        String MANUAL_SORT_POSITION = "shopping_list_item_manual_sort_position";
        String IS_DELETED = "shopping_list_item_is_deleted";
        String TIMESTAMP = "shopping_list_item_timestamp";
        String IS_DIRTY = "shopping_list_item_is_dirty";

        String WHERE_STRING = PARENT_LIST_ID + "=? and " + LIST_ITEM_ID + " IN(?)";
    }

    public interface CurrencyColumns {
        String SERVER_ID = "currency_parse_id";
        String CURRENCY_ID = "main_currency_id";
        String NAME = "currency_name";
        String TIMESTAMP = "currency_timestamp";
        String IS_DIRTY = "currency_is_dirty";
        String IS_DELETED = "currency_is_deleted";

        String WHERE_STRING = CURRENCY_ID + " IN(?)";
    }

    public interface UnitColumns {
        String SERVER_ID = "unit_parse_id";
        String UNIT_ID = "main_unit_id";
        String FULL_NAME = "unit_full_name";
        String SHORT_NAME = "unit_short_name";
        String TIMESTAMP = "unit_timestamp";
        String IS_DIRTY = "unit_is_dirty";
        String IS_DELETED = "unit_is_deleted";

        String WHERE_STRING = UNIT_ID + " IN(?)";
    }

    public static class Products implements ProductsColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.PRODUCTS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.PRODUCTS;

        public static final String LAST_TIMESTAMP = "products_last_timestamp";
        public static final String WITHOUT_DELETED = IS_DELETED + "<1";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.PRODUCTS).build();

        public static final Uri LAST_TIMESTAMP_URI =
                CONTENT_URI.buildUpon().appendPath(LAST_TIMESTAMP).build();

        public static final String PRODUCTS = Tables.PRODUCTS +
                " LEFT OUTER JOIN " + Tables.CATEGORIES + " ON " + Products.CATEGORY_ID + " = " + Tables.CATEGORIES + "." + Categories.CATEGORY_ID +
                " LEFT OUTER JOIN " + Tables.UNITS + " ON " + Products.UNIT_ID + " = " + Tables.UNITS + "." + Units.UNIT_ID;

        public static Uri buildProductsUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static class Categories implements CategoriesColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.CATEGORIES;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.CATEGORIES;

        public static final String LAST_TIMESTAMP = "categories_last_timestamp";
        public static final String WITHOUT_DELETED = IS_DELETED + "<1";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.CATEGORIES).build();

        public static final Uri LAST_TIMESTAMP_URI =
                CONTENT_URI.buildUpon().appendPath(LAST_TIMESTAMP).build();

        public static Uri buildCategoriesUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

    }

    public static class Units implements UnitColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.UNITS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.UNITS;

        public static final String LAST_TIMESTAMP = "units_last_timestamp";
        public static final String WITHOUT_DELETED = IS_DELETED + "<1";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.UNITS).build();

        public static final Uri LAST_TIMESTAMP_URI =
                CONTENT_URI.buildUpon().appendPath(LAST_TIMESTAMP).build();

        public static Uri buildUnitUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static class Currencies implements CurrencyColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.CURRENCY;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.CURRENCY;

        public static final String LAST_TIMESTAMP = "currencies_last_timestamp";
        public static final String WITHOUT_DELETED = IS_DELETED + "<1";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.CURRENCY).build();

        public static final Uri LAST_TIMESTAMP_URI =
                CONTENT_URI.buildUpon().appendPath(LAST_TIMESTAMP).build();

        public static Uri buildCurrencyUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static class ShoppingListItems implements ShoppingListItemColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.SHOPPING_LIST_ITEMS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.SHOPPING_LIST_ITEMS;

        public static final String LAST_TIMESTAMP = "shopping_list_items_last_timestamp";
        public static final String MARK_SHOPPING_LIST_ITEMS_AS_DELETED = "mark_shopping_list_items_as_deleted";

        public static final String WITHOUT_DELETED = IS_DELETED + "<1";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.SHOPPING_LIST_ITEMS).build();

        public static final Uri LAST_TIMESTAMP_URI =
                CONTENT_URI.buildUpon().appendPath(LAST_TIMESTAMP).build();

        public static final String ALL_SHOPPING_LIST_ITEMS = Tables.SHOPPING_LIST_ITEMS +
                " LEFT OUTER JOIN " + Tables.CATEGORIES + " ON " + ShoppingListItems.CATEGORY_ID + " = " + Tables.CATEGORIES + "." + Categories.CATEGORY_ID +
                " LEFT OUTER JOIN " + Tables.UNITS + " ON " + ShoppingListItems.UNIT_ID + " = " + Tables.UNITS + "." + Units.UNIT_ID +
                " LEFT OUTER JOIN " + Tables.CURRENCY + " ON " + ShoppingListItems.CURRENCY_ID + " = " + Tables.CURRENCY + "." + Currencies.CURRENCY_ID;

        public static final String SHOPPING_LIST_ITEMS = ALL_SHOPPING_LIST_ITEMS +
                " WHERE " + ShoppingListItems.PARENT_LIST_ID + "=? AND " + WITHOUT_DELETED;

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildItemIdUri(String parentListId, String itemId) {
            return CONTENT_URI.buildUpon().appendPath(parentListId).appendPath(itemId).build();
        }

        public static Uri buildItemsUri(String parentListId) {
            return CONTENT_URI.buildUpon().appendPath(parentListId).build();
        }

        public static Uri buildMarkItemsAsDeletedUri(String parentListId) {
            return CONTENT_URI.buildUpon().appendPath(MARK_SHOPPING_LIST_ITEMS_AS_DELETED).appendPath(parentListId).build();
        }
    }

    public static class ShoppingLists implements ShoppingListColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.SHOPPING_LISTS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.SHOPPING_LISTS;

        public static final String LAST_TIMESTAMP = "shopping_lists_last_timestamp";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.SHOPPING_LISTS).build();

        public static final Uri LAST_TIMESTAMP_URI =
                CONTENT_URI.buildUpon().appendPath(LAST_TIMESTAMP).build();

        public static final String WITHOUT_DELETED = IS_DELETED + "<1";

        public static String buildGetShoppingList(String selection) {
            return "SELECT *, COUNT(i." + ShoppingListItems.STATUS + ") " + SIZE + ", SUM(i." + ShoppingListItems.STATUS + ") " + BOUGHT_COUNT +
                    " FROM " + Tables.SHOPPING_LISTS + " s" +
                    " LEFT OUTER JOIN " + Tables.SHOPPING_LIST_ITEMS + " i" +
                    " ON s." + LIST_ID + " = i." + ShoppingListItems.PARENT_LIST_ID +
                    " AND " + "i." + ShoppingListItems.IS_DELETED + "=0" +
                    " WHERE " + selection +
                    " GROUP BY s." + LIST_ID;
        }

        public static Uri buildShoppingListsUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }

    public static class Notifications implements NotificationsColumns {
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + AUTHORITY + "." + Tables.NOTIFICATIONS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + AUTHORITY + "." + Tables.NOTIFICATIONS;

        public static final String NEW_NOTIFICATIONS_COUNT = "new_notifications_count";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(Tables.NOTIFICATIONS).build();

        public static final Uri NEW_NOTIFICATIONS_COUNT_URI =
                CONTENT_URI.buildUpon().appendPath(NEW_NOTIFICATIONS_COUNT).build();

        public static Uri buildNotificationsUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static Uri buildLogUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }
    }
}
