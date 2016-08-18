package com.justplay1.shoppist.communication.network;

/**
 * Created by Mkhytar on 01.11.2015.
 */
public interface ServerConstants {

    int CATEGORY = 1;
    int CURRENCY = 2;
    int UNIT = 3;
    int SHOPPING_LIST = 4;
    int SHOPPING_LIST_ITEM = 6;
    int GOODS = 8;

    String CATEGORY_TIMESTAMP = "CategoryTimestamp";
    String CURRENCY_TIMESTAMP = "CurrencyTimestamp";
    String UNIT_TIMESTAMP = "UnitTimestamp";
    String SHOPPING_LIST_TIMESTAMP = "ShoppingListTimestamp";
    String SHOPPING_LIST_ITEM_TIMESTAMP = "ShoppingListItemTimestamp";
    String GOODS_TIMESTAMP = "GoodsTimestamp";
    String SUBSCRIPTIONS = "Subscriptions";

    String SHOPPING_LISTS_TABLE = "ShoppingLists";
    String SHOPPING_LIST_ITEMS_TABLE = "ShoppingListItems";
    String GOODS_TABLE = "Goods";

    String TIMESTAMP = "timestamp";
    String IS_DELETED = "is_deleted";
    String CATEGORIES = "Categories";
    String UNITS = "Units";
    String CURRENCIES = "Currencies";
    String USER = "User";

    String CREATED_BY = "createdBy";
    String OBJECT_ID = "objectId";
    String USERNAME = "username";

    String USER_OBJECT_NAME_FIELD = "name";
    String USER_CHANEL_NAME_FIRST_PART = "user_";

    String VERIFY_DEVELOPER_PAYLOAD_FUNCTION = "verify_developer_payload_function";
    String GET_PUBLIC_KEY_FUNCTION = "get_public_key_function";
    String PAYLOAD = "payload";
}
