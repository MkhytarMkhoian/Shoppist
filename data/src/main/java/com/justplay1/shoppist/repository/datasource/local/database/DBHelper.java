package com.justplay1.shoppist.repository.datasource.local.database;

import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.RemoteException;
import android.util.Log;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.entity.NotificationDAO;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.UnitDAO;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhitar on 21.07.2015.
 */
@Singleton
public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "shopping_list_db";
    public static final int DB_VERSION = 7;

    private static final String COLUMN_ID = "_id";

    private static final String CREATE_PRODUCTS_TABLE = "create table " + ProductDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            ProductDAO.PRODUCT_ID + " text, " +
            ProductDAO.NAME + " text, " +
            ProductDAO.IS_CREATE_BY_USER + " integer DEFAULT 0, " +
            ProductDAO.TIME_CREATED + " integer, " +
            ProductDAO.IS_DELETED + " integer DEFAULT 0, " +
            ProductDAO.IS_DIRTY + " integer DEFAULT 0, " +
            ProductDAO.TIMESTAMP + " integer DEFAULT 0, " +
            ProductDAO.SERVER_ID + " text, " +
            ProductDAO.CATEGORY_ID + " text, " +
            ProductDAO.UNIT_ID + " text DEFAULT " + UnitDAO.NO_UNIT_ID +
            ");";

    private static final String CREATE_CATEGORIES_TABLE = "create table " + CategoryDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            CategoryDAO.NAME + " text, " +
            CategoryDAO.COLOR + " integer, " +
            CategoryDAO.CREATE_BY_USER + " integer DEFAULT 0, " +
            CategoryDAO.IS_DELETED + " integer DEFAULT 0, " +
            CategoryDAO.MANUAL_SORT_POSITION + " integer DEFAULT 0, " +
            CategoryDAO.IS_DIRTY + " integer DEFAULT 0, " +
            CategoryDAO.TIMESTAMP + " integer DEFAULT 0, " +
            CategoryDAO.CATEGORY_ID + " text, " +
            CategoryDAO.SERVER_ID + " text " +
            ");";

    private static final String CREATE_UNITS_TABLE = "create table " + UnitDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            UnitDAO.FULL_NAME + " text, " +
            UnitDAO.SHORT_NAME + " text, " +
            UnitDAO.UNIT_ID + " text, " +
            UnitDAO.IS_DIRTY + " integer DEFAULT 0, " +
            UnitDAO.TIMESTAMP + " integer DEFAULT 0, " +
            UnitDAO.IS_DELETED + " integer DEFAULT 0, " +
            UnitDAO.SERVER_ID + " text" +
            ");";

    private static final String CREATE_CURRENCIES_TABLE = "create table " + CurrencyDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            CurrencyDAO.NAME + " text, " +
            CurrencyDAO.CURRENCY_ID + " text, " +
            CurrencyDAO.IS_DIRTY + " integer DEFAULT 0, " +
            CurrencyDAO.TIMESTAMP + " integer DEFAULT 0, " +
            CurrencyDAO.IS_DELETED + " integer DEFAULT 0, " +
            CurrencyDAO.SERVER_ID + " text" +
            ");";

    private static final String CREATE_LIST_ITEMS_TABLE = "create table " + ListItemDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            ListItemDAO.LIST_ITEM_ID + " text, " +
            ListItemDAO.PARENT_LIST_ID + " text, " +
            ListItemDAO.LIST_ITEM_NAME + " text, " +
            ListItemDAO.SERVER_ID + " text, " +
            ListItemDAO.SHORT_DESCRIPTION + " text, " +
            ListItemDAO.STATUS + " integer, " +
            ListItemDAO.PRIORITY + " integer, " +
            ListItemDAO.PRICE + " real, " +
            ListItemDAO.QUANTITY + " real, " +
            ListItemDAO.UNIT_ID + " text, " +
            ListItemDAO.CURRENCY_ID + " text, " +
            ListItemDAO.CATEGORY_ID + " text, " +
            ListItemDAO.TIME_CREATED + " integer, " +
            ListItemDAO.MANUAL_SORT_POSITION + " integer DEFAULT 0, " +
            ListItemDAO.IS_DELETED + " integer DEFAULT 0, " +
            ListItemDAO.IS_DIRTY + " integer DEFAULT 0, " +
            ListItemDAO.TIMESTAMP + " integer DEFAULT 0 " +
            ");";

    private static final String CREATE_LISTS_TABLE = "create table " + ListDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            ListDAO.LIST_ID + " text, " +
            ListDAO.LIST_NAME + " text, " +
            ListDAO.SERVER_ID + " text, " +
            ListDAO.PRIORITY + " integer, " +
            ListDAO.COLOR + " integer, " +
            ListDAO.MANUAL_SORT_POSITION + " integer DEFAULT 0, " +
            ListDAO.IS_DELETED + " integer DEFAULT 0, " +
            ListDAO.IS_DIRTY + " integer DEFAULT 0, " +
            ListDAO.TIMESTAMP + " integer DEFAULT 0, " +
            ListDAO.TIME_CREATED + " integer " +
            ");";

    public static final String CREATE_NOTIFICATIONS_TABLE = "create table " + NotificationDAO.TABLE + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            NotificationDAO.NOTIFICATION_ID + " text, " +
            NotificationDAO.TITLE + " text, " +
            NotificationDAO.ITEM_NAMES + " text, " +
            NotificationDAO.STATUS + " integer, " +
            NotificationDAO.TYPE + " integer, " +
            NotificationDAO.TIME + " integer DEFAULT 0 " +
            ");";

    private Context mContext;

    @Inject
    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAndFillDatabase(db);
    }

    private void createAndFillDatabase(SQLiteDatabase db) {
        try {
            createTables(db);
            fillDatabase(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillDatabase(SQLiteDatabase db) {
        String[] categories = mContext.getResources().getStringArray(R.array.categories);
        int[] categoriesColors = mContext.getResources().getIntArray(R.array.categories_colors);

        for (int i = 0; i < categories.length; i++) {
            String[] categoriesName = categories[i].split(" ! ");
            db.insert(CategoryDAO.TABLE, null, new CategoryDAO.Builder()
                    .id(categoriesName[1])
                    .timestamp(0)
                    .color(categoriesColors[i])
                    .name(categoriesName[0])
                    .isDelete(false)
                    .isDirty(false)
                    .build());
        }

        String[] units = mContext.getResources().getStringArray(R.array.units);
        for (String item : units) {
            String[] unit = item.split("/");
            String[] unitId = unit[1].split(" ! ");
            db.insert(UnitDAO.TABLE, null, new UnitDAO.Builder()
                    .isDirty(false)
                    .id(unitId[1])
                    .timestamp(0)
                    .fullName(unit[0])
                    .shortName(unitId[0])
                    .build());
        }

        String[] currency = mContext.getResources().getStringArray(R.array.currency);
        for (String c : currency) {
            String[] currencyId = c.split(" ! ");
            db.insert(CurrencyDAO.TABLE, null, new CurrencyDAO.Builder()
                    .timestamp(0)
                    .id(currencyId[1])
                    .isDirty(false)
                    .name(currencyId[0])
                    .build());
        }

        String[] products = mContext.getResources().getStringArray(R.array.products);
        for (String product : products) {
            String[] productsName = product.split(" ! ");
            db.insert(ProductDAO.TABLE, null, new ProductDAO.Builder()
                    .id(UUID.nameUUIDFromBytes((productsName[0]).getBytes()).toString())
                    .name(productsName[0])
                    .categoryId(productsName[1])
                    .isCreateByUser(false)
                    .timestamp(0)
                    .isDirty(false)
                    .isDelete(false)
                    .timeCreated(System.currentTimeMillis())
                    .unitId(UnitDAO.NO_UNIT_ID)
                    .build());
        }
    }

    protected void createTables(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL(CREATE_NOTIFICATIONS_TABLE);
            db.execSQL(CREATE_PRODUCTS_TABLE);
            db.execSQL(CREATE_CATEGORIES_TABLE);
            db.execSQL(CREATE_CURRENCIES_TABLE);
            db.execSQL(CREATE_UNITS_TABLE);
            db.execSQL(CREATE_LISTS_TABLE);
            db.execSQL(CREATE_LIST_ITEMS_TABLE);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e("SQL", e.getMessage());
        } finally {
            db.endTransaction();
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
                        db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                + ProductDAO.IS_CREATE_BY_USER + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + CategoryDAO.TABLE + " ADD COLUMN "
                                + CategoryDAO.MANUAL_SORT_POSITION + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ListDAO.TABLE + " ADD COLUMN "
                                + ListDAO.MANUAL_SORT_POSITION + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ListItemDAO.TABLE + " ADD COLUMN "
                                + ListItemDAO.MANUAL_SORT_POSITION + " integer DEFAULT 0");
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 3:
                    try {
                        db.beginTransaction();
                        long time = System.currentTimeMillis();
                        db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                + ProductDAO.SERVER_ID + " text");
                        db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                + ProductDAO.TIME_CREATED + " integer DEFAULT " + time);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 4:
                    try {
                        db.beginTransaction();
                        db.execSQL("ALTER TABLE " + UnitDAO.TABLE + " ADD COLUMN "
                                + UnitDAO.TIMESTAMP + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + UnitDAO.TABLE + " ADD COLUMN "
                                + UnitDAO.IS_DIRTY + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + CurrencyDAO.TABLE + " ADD COLUMN "
                                + CurrencyDAO.TIMESTAMP + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + CurrencyDAO.TABLE + " ADD COLUMN "
                                + CurrencyDAO.IS_DIRTY + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                + ProductDAO.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                + ProductDAO.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                + ProductDAO.TIMESTAMP + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + CategoryDAO.TABLE + " ADD COLUMN "
                                + CategoryDAO.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + CategoryDAO.TABLE + " ADD COLUMN "
                                + CategoryDAO.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + CategoryDAO.TABLE + " ADD COLUMN "
                                + CategoryDAO.TIMESTAMP + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + ListItemDAO.TABLE + " ADD COLUMN "
                                + ListItemDAO.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ListItemDAO.TABLE + " ADD COLUMN "
                                + ListItemDAO.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ListItemDAO.TABLE + " ADD COLUMN "
                                + ListItemDAO.TIMESTAMP + " integer DEFAULT 0");

                        db.execSQL("ALTER TABLE " + ListDAO.TABLE + " ADD COLUMN "
                                + ListDAO.IS_DELETED + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ListDAO.TABLE + " ADD COLUMN "
                                + ListDAO.IS_DIRTY + " integer DEFAULT 0");
                        db.execSQL("ALTER TABLE " + ListDAO.TABLE + " ADD COLUMN "
                                + ListDAO.TIMESTAMP + " integer DEFAULT 0");

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 5:
                    try {
                        db.execSQL("DROP TABLE IF EXISTS to_do_lists");
                        db.execSQL("DROP TABLE IF EXISTS to_do_list_items");

                        updateCategoriesToVersion5(db);
                        updateCurrenciesToVersion5(db);
                        updateGoodsToVersion5(db);
                        updateUnitsToVersion5(db);

                        Map<String, String> shoppingLists = updateShoppingListToVersion5(db);
                        updateShoppingListItemsToVersion5(db, shoppingLists);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        db.beginTransaction();
                        if (!isColumnExist(db, ProductDAO.TABLE, ProductDAO.UNIT_ID)) {
                            db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " ADD COLUMN "
                                    + ProductDAO.UNIT_ID + " text DEFAULT " + UnitDAO.NO_UNIT_ID);
                        } else {
                            Log.d("ALTER TABLE", ProductDAO.UNIT_ID + " column already exists");
                        }

                        String[] units = mContext.getResources().getStringArray(R.array.units);
                        String[] unit = units[0].split("/");
                        String[] unitId = unit[1].split(" ! ");
                        db.insert(UnitDAO.TABLE, null, new UnitDAO.Builder()
                                .isDirty(true)
                                .id(unitId[1])
                                .timestamp(0)
                                .fullName(unit[0])
                                .shortName(unitId[0])
                                .build());

                        String[] currency = mContext.getResources().getStringArray(R.array.currency);
                        String[] currencyId = currency[0].split(" ! ");
                        db.insert(CurrencyDAO.TABLE, null, new CurrencyDAO.Builder()
                                .timestamp(0)
                                .id(currencyId[1])
                                .isDirty(true)
                                .name(currencyId[0])
                                .build());

                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    break;
                case 7:
                    try {
                        db.beginTransaction();
                        if (!isColumnExist(db, UnitDAO.TABLE, UnitDAO.IS_DELETED)) {
                            db.execSQL("ALTER TABLE " + UnitDAO.TABLE + " ADD COLUMN "
                                    + UnitDAO.IS_DELETED + " integer DEFAULT 0");
                        } else {
                            Log.d("ALTER TABLE", UnitDAO.IS_DELETED + " column already exists");
                        }

                        if (!isColumnExist(db, CurrencyDAO.TABLE, CurrencyDAO.IS_DELETED)) {
                            db.execSQL("ALTER TABLE " + CurrencyDAO.TABLE + " ADD COLUMN "
                                    + CurrencyDAO.IS_DELETED + " integer DEFAULT 0");
                        } else {
                            Log.d("ALTER TABLE", CurrencyDAO.IS_DELETED + " column already exists");
                        }

                        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                        builder.setTables("SELECT * FROM " + ProductDAO.TABLE);
                        Cursor cursor = builder.query(db, null, null, null, null, null, null);

                        try {
                            if (!cursor.isClosed() && cursor.moveToFirst()) {
                                do {
                                    String oldId = DbUtil.getString(cursor, ProductDAO.PRODUCT_ID);
                                    db.update(ProductDAO.TABLE,
                                            new ProductDAO.Builder()
                                                    .id(UUID.nameUUIDFromBytes((DbUtil.getString(cursor, ProductDAO.NAME)).getBytes()).toString())
                                                    .build(),
                                            ProductDAO.PRODUCT_ID + "=?", new String[]{oldId});
                                } while (cursor.moveToNext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }

                        builder = new SQLiteQueryBuilder();
                        builder.setTables(CategoryDAO.TABLE);
                        cursor = builder.query(db, null, null, null, null, null, null);

                        try {
                            if (!cursor.isClosed() && cursor.moveToFirst()) {
                                do {
                                    String id = DbUtil.getString(cursor, CategoryDAO.CATEGORY_ID);
                                    db.update(CategoryDAO.TABLE,
                                            new CategoryDAO.Builder()
                                                    .isDirty(false)
                                                    .build(),
                                            CategoryDAO.CATEGORY_ID + "=?", new String[]{id});
                                } while (cursor.moveToNext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }

                        builder = new SQLiteQueryBuilder();
                        builder.setTables(CurrencyDAO.TABLE);
                        cursor = builder.query(db, null, null, null, null, null, null);

                        try {
                            if (!cursor.isClosed() && cursor.moveToFirst()) {
                                do {
                                    String id = DbUtil.getString(cursor, CurrencyDAO.CURRENCY_ID);
                                    db.update(CurrencyDAO.TABLE,
                                            new CurrencyDAO.Builder()
                                                    .isDirty(false)
                                                    .build(),
                                            CurrencyDAO.CURRENCY_ID + "=?", new String[]{id});
                                } while (cursor.moveToNext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
                        }

                        builder = new SQLiteQueryBuilder();
                        builder.setTables(UnitDAO.TABLE);
                        cursor = builder.query(db, null, null, null, null, null, null);

                        try {
                            if (!cursor.isClosed() && cursor.moveToFirst()) {
                                do {
                                    String id = DbUtil.getString(cursor, UnitDAO.UNIT_ID);
                                    db.update(UnitDAO.TABLE,
                                            new UnitDAO.Builder()
                                                    .isDirty(false)
                                                    .build(),
                                            UnitDAO.UNIT_ID + "=?", new String[]{id});
                                } while (cursor.moveToNext());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (cursor != null) {
                                cursor.close();
                            }
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
            db.execSQL("ALTER TABLE " + CategoryDAO.TABLE + " RENAME TO " + CategoryDAO.TABLE + "_backup");
            db.execSQL(CREATE_CATEGORIES_TABLE);
            db.execSQL("INSERT INTO " + CategoryDAO.TABLE + " SELECT " +
                    COLUMN_ID + "," +
                    CategoryDAO.NAME + "," +
                    CategoryDAO.COLOR + "," +
                    CategoryDAO.CREATE_BY_USER + "," +
                    CategoryDAO.IS_DELETED + "," +
                    CategoryDAO.MANUAL_SORT_POSITION + "," +
                    CategoryDAO.IS_DIRTY + "," +
                    CategoryDAO.TIMESTAMP + "," +
                    "CAST(" + CategoryDAO.CATEGORY_ID + " AS TEXT)," +
                    CategoryDAO.SERVER_ID +
                    " FROM " + CategoryDAO.TABLE + "_backup;");
            db.execSQL("DROP TABLE " + CategoryDAO.TABLE + "_backup;");
            db.setTransactionSuccessful();
            Log.d("upCategoriesToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public void updateUnitsToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + UnitDAO.TABLE + " RENAME TO " + UnitDAO.TABLE + "_backup");
            db.execSQL(CREATE_UNITS_TABLE);
            db.execSQL("INSERT INTO " + UnitDAO.TABLE + " SELECT " +
                    COLUMN_ID + "," +
                    UnitDAO.FULL_NAME + "," +
                    UnitDAO.SHORT_NAME + "," +
                    "CAST(" + UnitDAO.UNIT_ID + " AS TEXT)," +
                    UnitDAO.IS_DIRTY + "," +
                    UnitDAO.TIMESTAMP + "," +
                    UnitDAO.SERVER_ID +
                    " FROM " + UnitDAO.TABLE + "_backup;");
            db.execSQL("DROP TABLE " + UnitDAO.TABLE + "_backup;");
            db.setTransactionSuccessful();
            Log.d("updateUnitsToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public void updateCurrenciesToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + CurrencyDAO.TABLE + " RENAME TO " + CurrencyDAO.TABLE + "_backup");
            db.execSQL(CREATE_CURRENCIES_TABLE);
            db.execSQL("INSERT INTO " + CurrencyDAO.TABLE + " SELECT " +
                    COLUMN_ID + "," +
                    CurrencyDAO.NAME + "," +
                    "CAST(" + CurrencyDAO.CURRENCY_ID + " AS TEXT)," +
                    CurrencyDAO.IS_DIRTY + "," +
                    CurrencyDAO.TIMESTAMP + "," +
                    CurrencyDAO.SERVER_ID +
                    " FROM " + CurrencyDAO.TABLE + "_backup;");
            db.execSQL("DROP TABLE " + CurrencyDAO.TABLE + "_backup;");
            db.setTransactionSuccessful();
            Log.d("upCurrenciesToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public void updateGoodsToVersion5(SQLiteDatabase db) throws RemoteException, OperationApplicationException {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + ProductDAO.TABLE + " RENAME TO " + ProductDAO.TABLE + "_backup");
            db.execSQL(CREATE_PRODUCTS_TABLE);

            String insert = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s) ",
                    ProductDAO.TABLE,
                    ProductDAO.NAME,
                    ProductDAO.IS_CREATE_BY_USER,
                    ProductDAO.TIME_CREATED,
                    ProductDAO.IS_DELETED,
                    ProductDAO.IS_DIRTY,
                    ProductDAO.TIMESTAMP,
                    ProductDAO.SERVER_ID,
                    ProductDAO.CATEGORY_ID);

            db.execSQL(insert + "SELECT " +
                    ProductDAO.NAME + "," +
                    ProductDAO.IS_CREATE_BY_USER + "," +
                    ProductDAO.TIME_CREATED + "," +
                    ProductDAO.IS_DELETED + "," +
                    ProductDAO.IS_DIRTY + "," +
                    ProductDAO.TIMESTAMP + "," +
                    ProductDAO.SERVER_ID + "," +
                    "CAST(" + ProductDAO.CATEGORY_ID + " AS TEXT)" +
                    " FROM " + ProductDAO.TABLE + "_backup;");

            db.execSQL("DROP TABLE " + ProductDAO.TABLE + "_backup;");

            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables("SELECT * FROM " + ProductDAO.TABLE);
            Cursor cursor = builder.query(db, null, null, null, null, null, null);

            try {
                if (!cursor.isClosed() && cursor.moveToFirst()) {
                    do {
                        String name = DbUtil.getString(cursor, ProductDAO.NAME);
                        db.update(ProductDAO.TABLE,
                                new ProductDAO.Builder()
                                        .id(UUID.nameUUIDFromBytes((name).getBytes()).toString())
                                        .build(),
                                ProductDAO.NAME + "=?", new String[]{name});
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();
            Log.d("updateGoodsToVersion5", "OK");
        } finally {
            db.endTransaction();
        }
    }

    public Map<String, String> updateShoppingListToVersion5(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + ListDAO.TABLE + " ADD COLUMN "
                    + ListDAO.LIST_ID + " text");

            String request = "SELECT *, COUNT(i." + ListItemDAO.STATUS + ") " + ListDAO.SIZE +
                    ", SUM(i." + ListItemDAO.STATUS + ") " + ListDAO.BOUGHT_COUNT +
                    " FROM " + ListDAO.TABLE + " s" +
                    " LEFT JOIN " + ListItemDAO.TABLE + " i" +
                    " ON s." + ListDAO.LIST_NAME + " = i.shopping_list_item_parent_list_name" +
                    " GROUP BY s." + ListDAO.LIST_NAME;
            Cursor cursor = db.rawQuery(request, null);

            Map<String, String> shoppingLists = new HashMap<>();
            try {
                if (!cursor.isClosed() && cursor.moveToFirst()) {
                    do {
                        String id = DbUtil.getString(cursor, ListDAO.LIST_ID);
                        String name = DbUtil.getString(cursor, ListDAO.LIST_NAME);
                        shoppingLists.put(id, name);
                        db.update(ListDAO.TABLE, new ListDAO.Builder()
                                        .id(UUID.nameUUIDFromBytes((name + UUID.randomUUID()).getBytes()).toString())
                                        .build(),
                                ListDAO.LIST_NAME + "=?", new String[]{name});
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            db.setTransactionSuccessful();
            Log.d("ShoppingListToVersion5", "OK");
            return shoppingLists;
        } finally {
            db.endTransaction();
        }
    }

    public void updateShoppingListItemsToVersion5(SQLiteDatabase db, Map<String, String> shoppingLists) {
        try {
            db.beginTransaction();
            db.execSQL("ALTER TABLE " + ListItemDAO.TABLE + " RENAME TO " + ListItemDAO.TABLE + "_backup");
            db.execSQL(CREATE_LIST_ITEMS_TABLE);

            final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
            builder.setTables(ListItemDAO.TABLE + "_backup");
            Cursor cursor = builder.query(db, null, null, null, null, null, null);
            try {
                if (!cursor.isClosed() && cursor.moveToFirst()) {
                    do {
                        String parentListName = cursor.getString(cursor.getColumnIndex("shopping_list_item_parent_list_name"));
                        String name = cursor.getString(cursor.getColumnIndex(ListItemDAO.LIST_ITEM_NAME));

                        ListItemDAO.Builder cv = new ListItemDAO.Builder();

                        for (Map.Entry<String, String> parentList : shoppingLists.entrySet()) {
                            if (parentListName.equals(parentList.getValue())) {
                                cv.parentId(parentList.getKey());
                            }
                        }
                        cv.id(UUID.nameUUIDFromBytes((name + UUID.randomUUID()).getBytes()).toString());
                        cv.name(name);
                        cv.description(DbUtil.getString(cursor, ListItemDAO.SHORT_DESCRIPTION));
                        cv.price(DbUtil.getDouble(cursor, ListItemDAO.PRICE));
                        cv.timeCreated(DbUtil.getLong(cursor, ListItemDAO.TIME_CREATED));
                        cv.quantity(DbUtil.getDouble(cursor, ListItemDAO.QUANTITY));
                        cv.serverId(DbUtil.getString(cursor, ListItemDAO.SERVER_ID));
                        cv.position(DbUtil.getInt(cursor, ListItemDAO.MANUAL_SORT_POSITION));
                        cv.isDelete(DbUtil.getBoolean(cursor, ListItemDAO.IS_DELETED));
                        cv.isDirty(DbUtil.getBoolean(cursor, ListItemDAO.IS_DIRTY));
                        cv.timestamp(DbUtil.getLong(cursor, ListItemDAO.TIMESTAMP));
                        cv.status(DbUtil.getBoolean(cursor, ListItemDAO.STATUS));
                        cv.priority(DbUtil.getInt(cursor, ListItemDAO.PRIORITY));
                        cv.categoryId(DbUtil.getString(cursor, ListItemDAO.CATEGORY_ID));
                        cv.unitId(DbUtil.getString(cursor, ListItemDAO.UNIT_ID));
                        cv.currencyId(DbUtil.getString(cursor, ListItemDAO.CURRENCY_ID));

                        db.insert(ListItemDAO.TABLE, null, cv.build());
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            db.execSQL("DROP TABLE " + ListItemDAO.TABLE + "_backup;");
            db.setTransactionSuccessful();
            Log.d("ShoppingListItems5", "OK");
        } finally {
            db.endTransaction();
        }
    }
}
