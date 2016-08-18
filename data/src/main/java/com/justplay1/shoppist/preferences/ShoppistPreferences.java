package com.justplay1.shoppist.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.SortTypeDAO;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhitar on 28.10.2014.
 */
@Singleton
public class ShoppistPreferences {

    public static final String PARSE_USER_ID_HASH = "parse_user_id_hash";
    public static final String COLOR_PRIMARY = "color_theme";
    public static final String COLOR_PRIMARY_DARK = "color_status_bar_theme";
    public static final String LOCK_SCREEN = "LockScreen";

    public static final String LAST_USER_SEEN_NOTIFICATIONS_TIME = "last_user_seen_notifications_time";
    public static final String NOTIFICATION_ENABLE = "NotificationEnable";
    public static final String NOTIFICATION_VIBRATION = "NotificationVibration";
    public static final String NOTIFICATION_SOUND = "NotificationSound";
    public static final String AVAILABLE_UPDATE_NOTIFICATION = "available_update_notification";
    public static final String AVAILABLE_DELETE_NOTIFICATION = "available_delete_notification";
    public static final String AVAILABLE_NEW_NOTIFICATION = "available_new_notification";
    public static final String AVAILABLE_BOUGHT_NOTIFICATION = "available_bought_notification";
    public static final String AVAILABLE_NOT_BOUGHT_NOTIFICATION = "available_not_bought_notification";

    public static final String CONFIRM_DELETE_DIALOG = "confirm_delete_dialog";
    public static final String SELECT_ANIMATION = "select_animation";
    public static final String IS_NEED_SHOW_RATE_DIALOG = "is_need_show_rate_dialog";
    public static final String DEFAULT_CURRENCY = "default_currency_id";
    public static final String DISCOLOR_PURCHASED_GOODS = "discolor_purchased_goods";
    public static final String CALCULATE_PRICE = "calculate_price";
    public static final String SHOW_GOODS_HEADER = "show_goods_header";
    public static final String CLOSE_MANUAL_SORT_MODE_WITH_BACK_BUTTON = "close_manual_sort_mode_with_back_button";
    public static final String LONG_ITEM_CLICK_ACTION = "long_item_click_action";
    public static final String ADD_BUTTON_CLICK_ACTION = "add_button_click_action";

    public static final String SORT_FOR_SHOPPING_LISTS = "sort_for_shopping_lists";
    public static final String SORT_FOR_SHOPPING_LIST_ITEMS = "sort_for_shopping_list_items";
    public static final String SORT_FOR_CATEGORIES = "sort_for_categories";
    public static final String SORT_FOR_GOODS = "sort_for_goods";

    public static final String IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LISTS = "is_manual_sort_enable_for_shopping_lists";
    public static final String IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LIST_ITEMS = "is_manual_sort_enable_for_shopping_list_items";
    public static final String IS_MANUAL_SORT_ENABLE_FOR_CATEGORIES = "is_manual_sort_enable_for_categories";

    public static final String LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION = "left_shopping_list_item_swipe_action";
    public static final String RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION = "right_shopping_list_item_swipe_action";

    private SharedPreferences mPreference;
    private Context mContext;

    /**
     * Mirror variables of preference
     */
    private long mParseUserIdHash;
    private int mColorPrimary;
    private int mColorPrimaryDark;
    private boolean mLockScreen;

    private int mSortForShoppingLists;
    private int mSortForShoppingListItems;
    private int mSortForCategories;
    private int mSortForGoods;

    private long mLastUserSeenNotificationsTime;
    private boolean isNotificationEnable;
    private boolean isNotificationVibration;
    private boolean isNotificationSound;
    private boolean isAvailableUpdateNotification;
    private boolean isAvailableNewNotification;
    private boolean isAvailableDeleteNotification;
    private boolean isAvailableBoughtNotification;
    private boolean isAvailableNotBoughtNotification;

    private boolean mConfirmDeleteDialog;
    private int mNeedShowRateDialog;
    private String mDefaultCurrency;
    private boolean mDiscolorPurchasedGoods;
    private boolean mCalculatePrice;
    private boolean mShowGoodsHeader;
    private int mLongItemClickAction;
    private int mAddButtonClickAction;

    private boolean isManualSortEnableForShoppingLists;
    private boolean isManualSortEnableForShoppingListItems;
    private boolean isManualSortEnableForCategories;

    private int mLeftShoppingListItemSwipeAction;
    private int mRightShoppingListItemSwipeAction;

    @Inject
    public ShoppistPreferences(Context context) {
        mContext = context;
    }

    private void loadFromPreference() {
        if (mPreference != null) {
            mColorPrimary = mPreference.getInt(COLOR_PRIMARY, mContext.getResources().getColor(R.color.red_color));
            mColorPrimaryDark = mPreference.getInt(COLOR_PRIMARY_DARK, mContext.getResources().getColor(R.color.red_800));
            mSortForShoppingLists = mPreference.getInt(SORT_FOR_SHOPPING_LISTS, 4);
            mSortForShoppingListItems = mPreference.getInt(SORT_FOR_SHOPPING_LIST_ITEMS, SortTypeDAO.SORT_BY_CATEGORIES);
            mSortForCategories = mPreference.getInt(SORT_FOR_CATEGORIES, -1);
            mSortForGoods = mPreference.getInt(SORT_FOR_GOODS, 3);
            mLockScreen = mPreference.getBoolean(LOCK_SCREEN, false);

            mLastUserSeenNotificationsTime = mPreference.getLong(LAST_USER_SEEN_NOTIFICATIONS_TIME, 0);
            isNotificationEnable = mPreference.getBoolean(NOTIFICATION_ENABLE, true);
            isNotificationVibration = mPreference.getBoolean(NOTIFICATION_VIBRATION, true);
            isNotificationSound = mPreference.getBoolean(NOTIFICATION_SOUND, true);
            isAvailableUpdateNotification = mPreference.getBoolean(AVAILABLE_UPDATE_NOTIFICATION, true);
            isAvailableNewNotification = mPreference.getBoolean(AVAILABLE_NEW_NOTIFICATION, true);
            isAvailableDeleteNotification = mPreference.getBoolean(AVAILABLE_DELETE_NOTIFICATION, true);
            isAvailableBoughtNotification = mPreference.getBoolean(AVAILABLE_BOUGHT_NOTIFICATION, true);
            isAvailableNotBoughtNotification = mPreference.getBoolean(AVAILABLE_NOT_BOUGHT_NOTIFICATION, true);

            mConfirmDeleteDialog = mPreference.getBoolean(CONFIRM_DELETE_DIALOG, false);
            mNeedShowRateDialog = mPreference.getInt(IS_NEED_SHOW_RATE_DIALOG, 0);
            mDefaultCurrency = mPreference.getString(DEFAULT_CURRENCY, "1");
            mDiscolorPurchasedGoods = mPreference.getBoolean(DISCOLOR_PURCHASED_GOODS, true);
            mCalculatePrice = mPreference.getBoolean(CALCULATE_PRICE, true);
            mShowGoodsHeader = mPreference.getBoolean(SHOW_GOODS_HEADER, true);
            mLongItemClickAction = mPreference.getInt(LONG_ITEM_CLICK_ACTION, 0);
            mParseUserIdHash = mPreference.getLong(PARSE_USER_ID_HASH, 0);
            mAddButtonClickAction = mPreference.getInt(ADD_BUTTON_CLICK_ACTION, 0);

            isManualSortEnableForShoppingLists = mPreference.getBoolean(IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LISTS, false);
            isManualSortEnableForShoppingListItems = mPreference.getBoolean(IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LIST_ITEMS, false);
            isManualSortEnableForCategories = mPreference.getBoolean(IS_MANUAL_SORT_ENABLE_FOR_CATEGORIES, false);

            mLeftShoppingListItemSwipeAction = mPreference.getInt(LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION, 0);
            mRightShoppingListItemSwipeAction = mPreference.getInt(RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION, 0);
        }
    }

    public void initPreferences() {
        mPreference = PreferenceManager.getDefaultSharedPreferences(mContext);
        loadFromPreference();
    }

    public void clear() {
        mPreference.edit().clear().apply();
    }

    public int getColorPrimaryDark() {
        return mColorPrimaryDark;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        mDefaultCurrency = defaultCurrency;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putString(DEFAULT_CURRENCY, defaultCurrency);
        editor.apply();
    }

    public String getDefaultCurrency() {
        return mDefaultCurrency;
    }

    public void setColorPrimaryDark(int color) {
        mColorPrimaryDark = color;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(COLOR_PRIMARY_DARK, color);
        editor.commit();

    }

    public long getLastUserSeenNotificationsTime() {
        return mLastUserSeenNotificationsTime;
    }

    public void setLastUserSeenNotificationsTime(long time) {
        mLastUserSeenNotificationsTime = time;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(LAST_USER_SEEN_NOTIFICATIONS_TIME, time);
        editor.commit();

    }

    public long getParseUserIdHash() {
        return mParseUserIdHash;
    }

    public void setParseUserIdHash(long hash) {
        mParseUserIdHash = hash;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putLong(PARSE_USER_ID_HASH, hash);
        editor.apply();
    }

    public int getColorPrimary() {
        return mColorPrimary;
    }

    public void setColorPrimary(int color) {
        mColorPrimary = color;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(COLOR_PRIMARY, color);
        editor.commit();
    }

    public int getSortForGoods() {
        return mSortForGoods;
    }

    public void setSortForGoods(int sort) {
        mSortForGoods = sort;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(SORT_FOR_GOODS, sort);
        editor.apply();
    }

    public int getSortForShoppingLists() {
        return mSortForShoppingLists;
    }

    public void setSortForShoppingLists(int sort) {
        mSortForShoppingLists = sort;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(SORT_FOR_SHOPPING_LISTS, sort);
        editor.apply();
    }

    public int getSortForShoppingListItems() {
        return mSortForShoppingListItems;
    }

    public void setSortForShoppingListItems(int sort) {
        mSortForShoppingListItems = sort;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(SORT_FOR_SHOPPING_LIST_ITEMS, sort);
        editor.apply();
    }

    public int getSortForCategories() {
        return mSortForCategories;
    }

    public void setSortForCategories(int sort) {
        mSortForCategories = sort;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(SORT_FOR_CATEGORIES, sort);
        editor.apply();
    }

    public boolean isNeedShowConfirmDeleteDialog() {
        return mConfirmDeleteDialog;
    }

    public void setConfirmDeleteDialog(boolean confirmDeleteDialog) {
        mConfirmDeleteDialog = confirmDeleteDialog;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(CONFIRM_DELETE_DIALOG, confirmDeleteDialog);
        editor.apply();
    }

    public boolean isLockScreen() {
        return mLockScreen;
    }

    public void setLockScreen(boolean lockScreen) {
        mLockScreen = lockScreen;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(LOCK_SCREEN, lockScreen);
        editor.apply();
    }

    public boolean isNotificationEnable() {
        return isNotificationEnable;
    }

    public void setNotificationEnable(boolean notificationEnable) {
        isNotificationEnable = notificationEnable;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(NOTIFICATION_ENABLE, notificationEnable);
        editor.apply();
    }

    public boolean isNotificationVibration() {
        return isNotificationVibration;
    }

    public void setNotificationVibration(boolean notificationVibration) {
        isNotificationVibration = notificationVibration;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(NOTIFICATION_VIBRATION, notificationVibration);
        editor.apply();
    }

    public boolean isNotificationSound() {
        return isNotificationSound;
    }

    public void setNotificationSound(boolean notificationSound) {
        isNotificationSound = notificationSound;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(NOTIFICATION_SOUND, notificationSound);
        editor.apply();
    }

    public boolean isAvailableNewNotification() {
        return isAvailableNewNotification;
    }

    public void setAvailableNewNotification(boolean available) {
        isAvailableNewNotification = available;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(AVAILABLE_NEW_NOTIFICATION, available);
        editor.apply();
    }

    public boolean isAvailableDeleteNotification() {
        return isAvailableDeleteNotification;
    }

    public void setAvailableDeleteNotification(boolean available) {
        isAvailableDeleteNotification = available;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(AVAILABLE_DELETE_NOTIFICATION, available);
        editor.apply();
    }

    public boolean isAvailableUpdateNotification() {
        return isAvailableUpdateNotification;
    }

    public void setAvailableUpdateNotification(boolean available) {
        isAvailableUpdateNotification = available;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(AVAILABLE_UPDATE_NOTIFICATION, available);
        editor.apply();
    }

    public boolean isAvailableBoughtNotification() {
        return isAvailableBoughtNotification;
    }

    public void setAvailableBoughtNotification(boolean available) {
        isAvailableBoughtNotification = available;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(AVAILABLE_BOUGHT_NOTIFICATION, available);
        editor.apply();
    }

    public boolean isAvailableNotBoughtNotification() {
        return isAvailableNotBoughtNotification;
    }

    public void setAvailableNotBoughtNotification(boolean available) {
        isAvailableNotBoughtNotification = available;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(AVAILABLE_NOT_BOUGHT_NOTIFICATION, available);
        editor.apply();
    }

    public boolean isCalculatePrice() {
        return mCalculatePrice;
    }

    public void setCalculatePrice(boolean calculatePrice) {
        mCalculatePrice = calculatePrice;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(CALCULATE_PRICE, calculatePrice);
        editor.apply();
    }

    public boolean isDiscolorPurchasedGoods() {
        return mDiscolorPurchasedGoods;
    }

    public void setDiscolorPurchasedGoods(boolean discolorPurchasedGoods) {
        mDiscolorPurchasedGoods = discolorPurchasedGoods;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(DISCOLOR_PURCHASED_GOODS, discolorPurchasedGoods);
        editor.apply();
    }

    public boolean isShowGoodsHeader() {
        return mShowGoodsHeader;
    }

    public void setShowGoodsHeader(boolean showGoodsHeader) {
        mShowGoodsHeader = showGoodsHeader;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(SHOW_GOODS_HEADER, showGoodsHeader);
        editor.apply();
    }

    public boolean isNeedShowRateDialog() {
        boolean flag = false;
        if (mNeedShowRateDialog == 30) {
            flag = true;
        } else if (mNeedShowRateDialog > 30) {
            flag = false;
        } else {
            flag = false;
            mNeedShowRateDialog++;
            setNeedShowRateDialog(mNeedShowRateDialog);
        }
        return flag;
    }

    public void setNeedShowRateDialog(int counter) {
        mNeedShowRateDialog = counter;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(IS_NEED_SHOW_RATE_DIALOG, counter);
        editor.apply();
    }

    public boolean isManualSortEnableForCategories() {
        return isManualSortEnableForCategories;
    }

    public void setManualSortEnableForCategories(boolean enableForCategories) {
        isManualSortEnableForCategories = enableForCategories;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(IS_MANUAL_SORT_ENABLE_FOR_CATEGORIES, enableForCategories);
        editor.commit();
    }

    public boolean isManualSortEnableForShoppingListItems() {
        return isManualSortEnableForShoppingListItems;
    }

    public void setManualSortEnableForShoppingListItems(boolean enableForShoppingListItems) {
        isManualSortEnableForShoppingListItems = enableForShoppingListItems;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LIST_ITEMS, enableForShoppingListItems);
        editor.commit();
    }

    public boolean isManualSortEnableForShoppingLists() {
        return isManualSortEnableForShoppingLists;
    }

    public void setManualSortEnableForShoppingLists(boolean enableForShoppingLists) {
        isManualSortEnableForShoppingLists = enableForShoppingLists;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LISTS, enableForShoppingLists);
        editor.commit();
    }

    public int getLeftShoppingListItemSwipeAction() {
        return mLeftShoppingListItemSwipeAction;
    }

    public void setLeftShoppingListItemSwipeAction(int action) {
        mLeftShoppingListItemSwipeAction = action;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION, action);
        editor.apply();
    }

    public int getRightShoppingListItemSwipeAction() {
        return mRightShoppingListItemSwipeAction;
    }

    public void setRightShoppingListItemSwipeAction(int action) {
        mRightShoppingListItemSwipeAction = action;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION, action);
        editor.apply();
    }

    public int getLongItemClickAction() {
        return mLongItemClickAction;
    }

    public void setLongItemClickAction(int action) {
        mLongItemClickAction = action;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(LONG_ITEM_CLICK_ACTION, action);
        editor.apply();
    }

    public int getAddButtonClickAction() {
        return mAddButtonClickAction;
    }

    public void setAddButtonClickAction(int action) {
        mAddButtonClickAction = action;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(ADD_BUTTON_CLICK_ACTION, action);
        editor.apply();

    }
}
