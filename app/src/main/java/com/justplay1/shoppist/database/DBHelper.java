package com.justplay1.shoppist.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.RemoteException;
import android.util.Log;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.database.ShoppingListContact.Products;
import com.justplay1.shoppist.database.ShoppingListContact.ProductsColumns;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingListItems;
import com.justplay1.shoppist.database.ShoppingListContact.ShoppingLists;
import com.justplay1.shoppist.database.tables.CategoriesTable;
import com.justplay1.shoppist.database.tables.CurrenciesTable;
import com.justplay1.shoppist.database.tables.NotificationsTable;
import com.justplay1.shoppist.database.tables.ProductsTable;
import com.justplay1.shoppist.database.tables.ShoppingListItemTable;
import com.justplay1.shoppist.database.tables.ShoppingListTable;
import com.justplay1.shoppist.database.tables.UnitTable;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mkhitar on 21.07.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "shopping_list_db";
    public static final int DB_VERSION = 7;
    public static final String START_UPDATE_TO_VERSION_5 = "start_update_to_version_5";
    public static final String FINISH_UPDATE_TO_VERSION_5 = "finish_update_to_version_5";

    protected Context mContext;
    protected static DBHelper sInstance;

    public interface Tables {
        String PRODUCTS = "products";
        String CATEGORIES = "categories";
        String SHOPPING_LIST_ITEMS = "shopping_list_items";
        String SHOPPING_LISTS = "shopping_lists";
        String UNITS = "units";
        String CURRENCY = "currency";
        String NOTIFICATIONS = "notifications";
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    protected DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAndFillDatabase(db);
    }

    protected void createAndFillDatabase(SQLiteDatabase db) {
        try {
            createTables(db);
            fillDatabase(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void fillDatabase(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        String[] categories = mContext.getResources().getStringArray(R.array.categories);
        int[] categoriesColors = mContext.getResources().getIntArray(R.array.categories_colors);

        for (int i = 0; i < categories.length; i++) {
            String[] categoriesName = categories[i].split(" ! ");
            cv.clear();
            cv.put(ShoppingListContact.CategoriesColumns.CATEGORY_ID, categoriesName[1]);
            cv.put(ShoppingListContact.CategoriesColumns.TIMESTAMP, 0);
            cv.put(ShoppingListContact.CategoriesColumns.IS_DIRTY, 0);
            cv.put(ShoppingListContact.CategoriesColumns.IS_DELETED, 0);
            cv.put(ShoppingListContact.CategoriesColumns.NAME, categoriesName[0]);
            cv.put(ShoppingListContact.CategoriesColumns.COLOR, categoriesColors[i]);
            cv.put(ShoppingListContact.CategoriesColumns.ENABLE, true);
            db.insert(Tables.CATEGORIES, null, cv);
        }

        String[] units = mContext.getResources().getStringArray(R.array.units);
        for (String item : units) {
            cv.clear();
            String[] unit = item.split("/");
            String[] unitId = unit[1].split(" ! ");
            cv.put(ShoppingListContact.UnitColumns.FULL_NAME, unit[0]);
            cv.put(ShoppingListContact.UnitColumns.SHORT_NAME, unitId[0]);
            cv.put(ShoppingListContact.UnitColumns.TIMESTAMP, 0);
            cv.put(ShoppingListContact.UnitColumns.IS_DIRTY, 0);
            cv.put(ShoppingListContact.UnitColumns.UNIT_ID, unitId[1]);
            db.insert(Tables.UNITS, null, cv);
        }

        String[] currency = mContext.getResources().getStringArray(R.array.currency);
        List<Currency> currencies = new ArrayList<>(currency.length);
        for (String c : currency) {
            cv.clear();
            String[] currencyId = c.split(" ! ");
            cv.put(ShoppingListContact.Currencies.NAME, currencyId[0]);
            cv.put(ShoppingListContact.Currencies.TIMESTAMP, 0);
            cv.put(ShoppingListContact.Currencies.IS_DIRTY, 0);
            cv.put(ShoppingListContact.Currencies.CURRENCY_ID, currencyId[1]);
            db.insert(Tables.CURRENCY, null, cv);

            Currency currency1 = new Currency();
            currency1.setName(currencyId[0]);
            currency1.setId(currencyId[1]);
            currencies.add(currency1);
        }

        Currency c = ShoppistUtils.getLocaleCurrency(currencies);
        ShoppistPreferences.setDefaultCurrency(c.getId());

        String[] products = mContext.getResources().getStringArray(R.array.products);
        for (String product : products) {
            String[] productsName = product.split(" ! ");
            cv.clear();
            cv.put(ProductsColumns.PRODUCT_ID, UUID.nameUUIDFromBytes((productsName[0]).getBytes()).toString());
            cv.put(ProductsColumns.NAME, productsName[0]);
            cv.put(ProductsColumns.CATEGORY_ID, productsName[1]);
            cv.put(ProductsColumns.IS_CREATE_BY_USER, 0);
            cv.put(ProductsColumns.TIMESTAMP, 0);
            cv.put(ProductsColumns.IS_DIRTY, 0);
            cv.put(ProductsColumns.IS_DELETED, 0);
            cv.put(ProductsColumns.TIME_CREATED, System.currentTimeMillis());
            cv.put(ProductsColumns.UNIT_ID, Unit.NO_UNIT_ID);
            db.insert(Tables.PRODUCTS, null, cv);
        }
    }

    protected void createTables(SQLiteDatabase db) {
        try {
            NotificationsTable.create(db);
            ProductsTable.create(db);
            CategoriesTable.create(db);
            UnitTable.create(db);
            CurrenciesTable.create(db);

            ShoppingListTable.create(db);
            ShoppingListItemTable.create(db);
        } catch (SQLException e) {
            Log.e("SQL", e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        while (upgradeTo <= newVersion) {
            switch (upgradeTo) {
                case 2:
                    try {
                        db.beginTransaction();
                        db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                + ShoppingListContact.ProductsColumns.IS_CREATE_BY_USER + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.CATEGORIES + " ADD COLUMN "
                                + ShoppingListContact.CategoriesColumns.MANUAL_SORT_POSITION + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LISTS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListColumns.MANUAL_SORT_POSITION + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LIST_ITEMS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListItemColumns.MANUAL_SORT_POSITION + " integer DEFAULT 0");
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 3:
                    try {
                        db.beginTransaction();
                        long time = System.currentTimeMillis();
                        db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                + ProductsColumns.SERVER_ID + " text");
                        db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                + ProductsColumns.TIME_CREATED + " integer DEFAULT " + time);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 4:
                    try {
                        db.beginTransaction();
                        db.execSQL("ALTER TABLE " + Tables.UNITS + " ADD COLUMN "
                                + ShoppingListContact.UnitColumns.TIMESTAMP + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.UNITS + " ADD COLUMN "
                                + ShoppingListContact.UnitColumns.IS_DIRTY + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + Tables.CURRENCY + " ADD COLUMN "
                                + ShoppingListContact.CurrencyColumns.TIMESTAMP + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.CURRENCY + " ADD COLUMN "
                                + ShoppingListContact.CurrencyColumns.IS_DIRTY + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                + ProductsColumns.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                + ProductsColumns.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                + ProductsColumns.TIMESTAMP + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + Tables.CATEGORIES + " ADD COLUMN "
                                + ShoppingListContact.CategoriesColumns.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.CATEGORIES + " ADD COLUMN "
                                + ShoppingListContact.CategoriesColumns.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.CATEGORIES + " ADD COLUMN "
                                + ShoppingListContact.CategoriesColumns.TIMESTAMP + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LIST_ITEMS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListItemColumns.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LIST_ITEMS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListItemColumns.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LIST_ITEMS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListItemColumns.TIMESTAMP + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LISTS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListColumns.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LISTS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListColumns.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + Tables.SHOPPING_LISTS + " ADD COLUMN "
                                + ShoppingListContact.ShoppingListColumns.TIMESTAMP + " integer DEFAULT 0");

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 5:
                    try {
                        mContext.sendBroadcast(new Intent(START_UPDATE_TO_VERSION_5));

                        db.execSQL("DROP TABLE IF EXISTS to_do_lists");
                        db.execSQL("DROP TABLE IF EXISTS to_do_list_items");

                        updateCategoriesToVersion5(db);
                        updateCurrenciesToVersion5(db);
                        updateGoodsToVersion5(db);
                        updateUnitsToVersion5(db);

                        Collection<ShoppingList> shoppingLists = updateShoppingListToVersion5(db);
                        updateShoppingListItemsToVersion5(db, shoppingLists);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mContext.sendBroadcast(new Intent(FINISH_UPDATE_TO_VERSION_5));
                    }
                    break;
                case 6:
                    try {
                        db.beginTransaction();
                        if (!isColumnExist(db, Tables.PRODUCTS, ProductsColumns.UNIT_ID)) {
                            db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " ADD COLUMN "
                                    + ProductsColumns.UNIT_ID + " text DEFAULT " + Unit.NO_UNIT_ID);
                        } else {
                            Log.d("ALTER TABLE", ProductsColumns.UNIT_ID + " column already exists");
                        }

                        ContentValues cv = new ContentValues();
                        String[] units = mContext.getResources().getStringArray(R.array.units);
                        cv.clear();
                        String[] unit = units[0].split("/");
                        String[] unitId = unit[1].split(" ! ");
                        cv.put(ShoppingListContact.UnitColumns.FULL_NAME, unit[0]);
                        cv.put(ShoppingListContact.UnitColumns.SHORT_NAME, unitId[0]);
                        cv.put(ShoppingListContact.UnitColumns.TIMESTAMP, 0);
                        cv.put(ShoppingListContact.UnitColumns.IS_DIRTY, 1);
                        cv.put(ShoppingListContact.UnitColumns.UNIT_ID, unitId[1]);
                        db.insert(Tables.UNITS, null, cv);

                        String[] currency = mContext.getResources().getStringArray(R.array.currency);
                        cv.clear();
                        String[] currencyId = currency[0].split(" ! ");
                        cv.put(ShoppingListContact.Currencies.NAME, currencyId[0]);
                        cv.put(ShoppingListContact.Currencies.TIMESTAMP, 0);
                        cv.put(ShoppingListContact.Currencies.IS_DIRTY, 1);
                        cv.put(ShoppingListContact.Currencies.CURRENCY_ID, currencyId[1]);
                        db.insert(Tables.CURRENCY, null, cv);

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 7:
                    try {
                        db.beginTransaction();
                        if (!isColumnExist(db, Tables.UNITS, ShoppingListContact.UnitColumns.IS_DELETED)) {
                            db.execSQL("ALTER TABLE " + Tables.UNITS + " ADD COLUMN "
                                    + ShoppingListContact.UnitColumns.IS_DELETED + " integer DEFAULT 0");
                        } else {
                            Log.d("ALTER TABLE", ShoppingListContact.UnitColumns.IS_DELETED + " column already exists");
                        }

                        if (!isColumnExist(db, Tables.CURRENCY, ShoppingListContact.CurrencyColumns.IS_DELETED)) {
                            db.execSQL("ALTER TABLE " + Tables.CURRENCY + " ADD COLUMN "
                                    + ShoppingListContact.CurrencyColumns.IS_DELETED + " integer DEFAULT 0");
                        } else {
                            Log.d("ALTER TABLE", ShoppingListContact.CurrencyColumns.IS_DELETED + " column already exists");
                        }

                        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                        builder.setTables(Products.PRODUCTS);
                        Cursor cursor = builder.query(db, null, null, null, null, null, null);

                        Collection<Product> products = ProductsTable.getAllProducts(cursor).values();
                        for (Product product : products) {
                            String oldId = product.getId();
                            ContentValues cv = new ContentValues();
                            cv.put(ProductsColumns.PRODUCT_ID, UUID.nameUUIDFromBytes((product.getName()).getBytes()).toString());
                            db.update(Tables.PRODUCTS, cv, ProductsColumns.PRODUCT_ID + "=?", new String[]{oldId});
                        }

                        builder = new SQLiteQueryBuilder();
                        builder.setTables(Tables.CATEGORIES);
                        cursor = builder.query(db, null, null, null, null, null, null);

                        Collection<Category> categories = CategoriesTable.getAllCategories(cursor).values();
                        for (Category category : categories) {
                            ContentValues cv = new ContentValues();
                            cv.put(ShoppingListContact.Categories.IS_DIRTY, 0);
                            db.update(Tables.CATEGORIES, cv, ShoppingListContact.Categories.CATEGORY_ID + "=?", new String[]{category.getId()});
                        }

                        builder = new SQLiteQueryBuilder();
                        builder.setTables(Tables.CURRENCY);
                        cursor = builder.query(db, null, null, null, null, null, null);

                        Collection<Currency> currencies = CurrenciesTable.getAllCurrencies(cursor).values();
                        for (Currency currency : currencies) {
                            ContentValues cv = new ContentValues();
                            cv.put(ShoppingListContact.Currencies.IS_DIRTY, 0);
                            db.update(Tables.CURRENCY, cv, ShoppingListContact.Currencies.CURRENCY_ID + "=?", new String[]{currency.getId()});
                        }

                        builder = new SQLiteQueryBuilder();
                        builder.setTables(Tables.UNITS);
                        cursor = builder.query(db, null, null, null, null, null, null);

                        Collection<Unit> units = UnitTable.getAllUnits(cursor).values();
                        for (Unit unit : units) {
                            ContentValues cv = new ContentValues();
                            cv.put(ShoppingListContact.Units.IS_DIRTY, 0);
                            db.update(Tables.UNITS, cv, ShoppingListContact.Units.UNIT_ID + "=?", new String[]{unit.getId()});
                        }

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
            }
            upgradeTo++;
        }
    }

    private boolean isColumnExist(SQLiteDatabase db, String table, String column) {
        Cursor res = db.rawQuery("SELECT * FROM " + table + " LIMIT 1", null);
        try {
            if (res != null && !res.isClosed() && res.moveToFirst()) {
                int value = res.getColumnIndex(column);
                if (value == -1) {
                    return false;
                }
            }
        } finally {
            if (res != null) {
                res.close();
            }
        }
        return true;
    }

    public void updateCategoriesToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + Tables.CATEGORIES + " RENAME TO " + Tables.CATEGORIES + "_backup");
            CategoriesTable.create(db);
            db.execSQL("INSERT INTO " + Tables.CATEGORIES + " SELECT " +
                    ShoppingListContact.COLUMN_ID + "," +
                    ShoppingListContact.CategoriesColumns.NAME + "," +
                    ShoppingListContact.CategoriesColumns.COLOR + "," +
                    ShoppingListContact.CategoriesColumns.CREATE_BY_USER + "," +
                    ShoppingListContact.CategoriesColumns.IS_DELETED + "," +
                    ShoppingListContact.CategoriesColumns.MANUAL_SORT_POSITION + "," +
                    ShoppingListContact.CategoriesColumns.IS_DIRTY + "," +
                    ShoppingListContact.CategoriesColumns.TIMESTAMP + "," +
                    "CAST(" + ShoppingListContact.CategoriesColumns.CATEGORY_ID + " AS TEXT)," +
                    ShoppingListContact.CategoriesColumns.SERVER_ID + "," +
                    ShoppingListContact.CategoriesColumns.ENABLE +
                    " FROM " + Tables.CATEGORIES + "_backup;");
            db.execSQL("DROP TABLE " + Tables.CATEGORIES + "_backup;");
            db.setTransactionSuccessful();
            Log.d("upCategoriesToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public void updateUnitsToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + Tables.UNITS + " RENAME TO " + Tables.UNITS + "_backup");
            UnitTable.create(db);
            db.execSQL("INSERT INTO " + Tables.UNITS + " SELECT " +
                    ShoppingListContact.COLUMN_ID + "," +
                    ShoppingListContact.UnitColumns.FULL_NAME + "," +
                    ShoppingListContact.UnitColumns.SHORT_NAME + "," +
                    "CAST(" + ShoppingListContact.UnitColumns.UNIT_ID + " AS TEXT)," +
                    ShoppingListContact.UnitColumns.IS_DIRTY + "," +
                    ShoppingListContact.UnitColumns.TIMESTAMP + "," +
                    ShoppingListContact.UnitColumns.SERVER_ID +
                    " FROM " + Tables.UNITS + "_backup;");
            db.execSQL("DROP TABLE " + Tables.UNITS + "_backup;");
            db.setTransactionSuccessful();
            Log.d("updateUnitsToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public void updateCurrenciesToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + Tables.CURRENCY + " RENAME TO " + Tables.CURRENCY + "_backup");
            CurrenciesTable.create(db);
            db.execSQL("INSERT INTO " + Tables.CURRENCY + " SELECT " +
                    ShoppingListContact.COLUMN_ID + "," +
                    ShoppingListContact.CurrencyColumns.NAME + "," +
                    "CAST(" + ShoppingListContact.CurrencyColumns.CURRENCY_ID + " AS TEXT)," +
                    ShoppingListContact.CurrencyColumns.IS_DIRTY + "," +
                    ShoppingListContact.CurrencyColumns.TIMESTAMP + "," +
                    ShoppingListContact.CurrencyColumns.SERVER_ID +
                    " FROM " + Tables.CURRENCY + "_backup;");
            db.execSQL("DROP TABLE " + Tables.CURRENCY + "_backup;");
            db.setTransactionSuccessful();
            Log.d("upCurrenciesToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public void updateGoodsToVersion5(SQLiteDatabase db) throws RemoteException, OperationApplicationException {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + Tables.PRODUCTS + " RENAME TO " + Tables.PRODUCTS + "_backup");
            ProductsTable.create(db);

            String insert = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) ",
                    Tables.PRODUCTS,
                    ProductsColumns.NAME,
                    ProductsColumns.IS_CREATE_BY_USER,
                    ProductsColumns.TIME_CREATED,
                    ProductsColumns.IS_DELETED,
                    ProductsColumns.IS_DIRTY,
                    ProductsColumns.TIMESTAMP,
                    ProductsColumns.SERVER_ID,
                    ProductsColumns.CATEGORY_ID);

            db.execSQL(insert + "SELECT " +
                    ProductsColumns.NAME + "," +
                    ProductsColumns.IS_CREATE_BY_USER + "," +
                    ProductsColumns.TIME_CREATED + "," +
                    ProductsColumns.IS_DELETED + "," +
                    ProductsColumns.IS_DIRTY + "," +
                    ProductsColumns.TIMESTAMP + "," +
                    ProductsColumns.SERVER_ID + "," +
                    "CAST(" + ProductsColumns.CATEGORY_ID + " AS TEXT)" +
                    " FROM " + Tables.PRODUCTS + "_backup;");

            db.execSQL("DROP TABLE " + Tables.PRODUCTS + "_backup;");

            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(Products.PRODUCTS);
            Cursor cursor = builder.query(db, null, null, null, null, null, null);

            Collection<Product> products = ProductsTable.getAllProducts(cursor).values();
            for (Product product : products) {
                ContentValues cv = new ContentValues();
                cv.put(ProductsColumns.PRODUCT_ID, UUID.nameUUIDFromBytes((product.getName()).getBytes()).toString());
                db.update(Tables.PRODUCTS, cv, ProductsColumns.NAME + "=?", new String[]{product.getName()});
            }
            db.setTransactionSuccessful();
            Log.d("updateGoodsToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public Collection<ShoppingList> updateShoppingListToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + Tables.SHOPPING_LISTS + " ADD COLUMN "
                    + ShoppingLists.LIST_ID + " text");

            String request = "SELECT *, COUNT(i." + ShoppingListItems.STATUS + ") " + ShoppingLists.SIZE +
                    ", SUM(i." + ShoppingListItems.STATUS + ") " + ShoppingLists.BOUGHT_COUNT +
                    " FROM " + Tables.SHOPPING_LISTS + " s" +
                    " LEFT JOIN " + Tables.SHOPPING_LIST_ITEMS + " i" +
                    " ON s." + ShoppingLists.LIST_NAME + " = i.shopping_list_item_parent_list_name" +
                    " GROUP BY s." + ShoppingLists.LIST_NAME;
            Cursor cursor = db.rawQuery(request, null);

            Collection<ShoppingList> shoppingLists = ShoppingListTable.getAllShoppingLists(cursor).values();
            ContentValues cv = new ContentValues();
            for (ShoppingList list : shoppingLists) {
                list.setId(UUID.nameUUIDFromBytes((list.getName() + UUID.randomUUID()).getBytes()).toString());
                cv.put(ShoppingLists.LIST_ID, list.getId());
                db.update(Tables.SHOPPING_LISTS, cv, ShoppingLists.LIST_NAME + "=?", new String[]{list.getName()});
                cv.clear();
            }
            db.setTransactionSuccessful();
            Log.d("ShoppingListToVersion5", "OK");
            return shoppingLists;
        } finally {
            db.endTransaction();
        }
    }

    public void updateShoppingListItemsToVersion5(SQLiteDatabase db, Collection<ShoppingList> shoppingLists) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + Tables.SHOPPING_LIST_ITEMS + " RENAME TO " + Tables.SHOPPING_LIST_ITEMS + "_backup");
            ShoppingListItemTable.create(db);

            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(Tables.SHOPPING_LIST_ITEMS + "_backup");
            Cursor cursor = builder.query(db, null, null, null, null, null, null);
            try {
                ContentValues cv = new ContentValues();
                if (!cursor.isClosed() && cursor.moveToFirst()) {
                    do {
                        String parentListName = cursor.getString(cursor.getColumnIndex("shopping_list_item_parent_list_name"));
                        String name = cursor.getString(cursor.getColumnIndex(ShoppingListItems.LIST_ITEM_NAME));

                        for (ShoppingList parentList : shoppingLists) {
                            if (parentListName.equals(parentList.getName())) {
                                cv.put(ShoppingListItems.PARENT_LIST_ID, parentList.getId());
                            }
                        }
                        cv.put(ShoppingListItems.LIST_ITEM_ID, UUID.nameUUIDFromBytes((name + UUID.randomUUID()).getBytes()).toString());
                        cv.put(ShoppingListItems.LIST_ITEM_NAME, name);

                        cv.put(ShoppingListItems.SHORT_DESCRIPTION, cursor.getString(cursor.getColumnIndex(ShoppingListItems.SHORT_DESCRIPTION)));
                        cv.put(ShoppingListItems.PRICE, cursor.getDouble(cursor.getColumnIndex(ShoppingListItems.PRICE)));
                        cv.put(ShoppingListItems.TIME_CREATED, cursor.getLong(cursor.getColumnIndex(ShoppingListItems.TIME_CREATED)));
                        cv.put(ShoppingListItems.QUANTITY, cursor.getDouble(cursor.getColumnIndex(ShoppingListItems.QUANTITY)));
                        cv.put(ShoppingListItems.SERVER_ID, cursor.getString(cursor.getColumnIndex(ShoppingListItems.SERVER_ID)));
                        cv.put(ShoppingListItems.MANUAL_SORT_POSITION, cursor.getInt(cursor.getColumnIndex(ShoppingListItems.MANUAL_SORT_POSITION)));
                        cv.put(ShoppingListItems.IS_DELETED, cursor.getInt(cursor.getColumnIndex(ShoppingListItems.IS_DELETED)));
                        cv.put(ShoppingListItems.IS_DIRTY, cursor.getInt(cursor.getColumnIndex(ShoppingListItems.IS_DIRTY)));
                        cv.put(ShoppingListItems.TIMESTAMP, cursor.getLong(cursor.getColumnIndex(ShoppingListItems.TIMESTAMP)));
                        cv.put(ShoppingListItems.STATUS, cursor.getInt(cursor.getColumnIndex(ShoppingListItems.STATUS)));
                        cv.put(ShoppingListItems.PRIORITY, cursor.getInt(cursor.getColumnIndex(ShoppingListItems.PRIORITY)));

                        cv.put(ShoppingListItems.CATEGORY_ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(ShoppingListItems.CATEGORY_ID))));
                        cv.put(ShoppingListItems.UNIT_ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(ShoppingListItems.UNIT_ID))));
                        cv.put(ShoppingListItems.CURRENCY_ID, String.valueOf(cursor.getInt(cursor.getColumnIndex(ShoppingListItems.CURRENCY_ID))));

                        db.insert(Tables.SHOPPING_LIST_ITEMS, null, cv);
                        cv.clear();
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            db.execSQL("DROP TABLE " + Tables.SHOPPING_LIST_ITEMS + "_backup;");
            db.setTransactionSuccessful();
            Log.d("ShoppingListItems5", "OK");
        } finally {
            db.endTransaction();
        }
    }
}
