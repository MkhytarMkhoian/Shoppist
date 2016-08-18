package com.justplay1.shoppist.settings.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.BaseWithoutHeaderAdapter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mkhitar on 28.10.2014.
 */
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

    private static ShoppistPreferences sInstance = null;
    private static SharedPreferences mPreference;
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
    private boolean mSelectAnimation;
    private int mNeedShowRateDialog;
    private String mDefaultCurrency;
    private boolean mDiscolorPurchasedGoods;
    private boolean mCalculatePrice;
    private boolean mShowGoodsHeader;
    private boolean mCloseManualSortModeWithBackButton;
    private int mLongItemClickAction;
    private int mAddButtonClickAction;

    private boolean isManualSortEnableForShoppingLists;
    private boolean isManualSortEnableForShoppingListItems;
    private boolean isManualSortEnableForCategories;

    private int mLeftShoppingListItemSwipeAction;
    private int mRightShoppingListItemSwipeAction;

    private Set<String> mNeedUpdateTheme = new HashSet<>();

    private ShoppistPreferences(Context context) {
        mContext = context;
        mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        loadFromPreference();
    }

    private void loadFromPreference() {
        if (mPreference != null) {
            mColorPrimary = mPreference.getInt(COLOR_PRIMARY, mContext.getResources().getColor(R.color.red_color));
            mColorPrimaryDark = mPreference.getInt(COLOR_PRIMARY_DARK, mContext.getResources().getColor(R.color.red_800));
            mSortForShoppingLists = mPreference.getInt(SORT_FOR_SHOPPING_LISTS, 4);
            mSortForShoppingListItems = mPreference.getInt(SORT_FOR_SHOPPING_LIST_ITEMS, BaseWithoutHeaderAdapter.SORT_BY_CATEGORIES);
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
            mSelectAnimation = mPreference.getBoolean(SELECT_ANIMATION, true);
            mNeedShowRateDialog = mPreference.getInt(IS_NEED_SHOW_RATE_DIALOG, 0);
            mDefaultCurrency = mPreference.getString(DEFAULT_CURRENCY, "");
            mDiscolorPurchasedGoods = mPreference.getBoolean(DISCOLOR_PURCHASED_GOODS, true);
            mCalculatePrice = mPreference.getBoolean(CALCULATE_PRICE, true);
            mShowGoodsHeader = mPreference.getBoolean(SHOW_GOODS_HEADER, true);
            mCloseManualSortModeWithBackButton = mPreference.getBoolean(CLOSE_MANUAL_SORT_MODE_WITH_BACK_BUTTON, false);
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

    public static void initPreferences(Context context) {
        if (sInstance == null) {
            sInstance = new ShoppistPreferences(context);
        }
    }

    public static void release() {
        if (sInstance != null) {
            clear();
            sInstance = null;
        }
    }

    public static void clear() {
        mPreference.edit().clear().apply();
    }

    public static int getColorPrimaryDark() {
        if (sInstance != null) {
            return sInstance.mColorPrimaryDark;
        }
        return 0;
    }

    public static void setDefaultCurrency(String defaultCurrency) {
        if (sInstance != null) {
            sInstance.mDefaultCurrency = defaultCurrency;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putString(DEFAULT_CURRENCY, defaultCurrency);
            editor.apply();
        }
    }

    public static String getDefaultCurrency() {
        if (sInstance != null) {
            return sInstance.mDefaultCurrency;
        }
        return "";
    }

    public static void setColorPrimaryDark(int color) {
        if (sInstance != null) {
            sInstance.mColorPrimaryDark = color;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(COLOR_PRIMARY_DARK, color);
            editor.commit();
        }
    }

    public static long getLastUserSeenNotificationsTime() {
        if (sInstance != null) {
            return sInstance.mLastUserSeenNotificationsTime;
        }
        return 0;
    }

    public static void setLastUserSeenNotificationsTime(long time) {
        if (sInstance != null) {
            sInstance.mLastUserSeenNotificationsTime = time;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putLong(LAST_USER_SEEN_NOTIFICATIONS_TIME, time);
            editor.commit();
        }
    }

    public static long getParseUserIdHash() {
        if (sInstance != null) {
            return sInstance.mParseUserIdHash;
        }
        return 0;
    }

    public static void setParseUserIdHash(long hash) {
        if (sInstance != null) {
            sInstance.mParseUserIdHash = hash;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putLong(PARSE_USER_ID_HASH, hash);
            editor.apply();
        }
    }

    public static int getColorPrimary() {
        if (sInstance != null) {
            return sInstance.mColorPrimary;
        }
        return 0;
    }

    public static void setColorPrimary(int color) {
        if (sInstance != null) {
            sInstance.mColorPrimary = color;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(COLOR_PRIMARY, color);
            editor.commit();
        }
    }

    public static int getSortForGoods() {
        if (sInstance != null) {
            return sInstance.mSortForGoods;
        }
        return 0;
    }

    public static void setSortForGoods(int sort) {
        if (sInstance != null) {
            sInstance.mSortForGoods = sort;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(SORT_FOR_GOODS, sort);
            editor.apply();
        }
    }

    public static int getSortForShoppingLists() {
        if (sInstance != null) {
            return sInstance.mSortForShoppingLists;
        }
        return 0;
    }

    public static void setSortForShoppingLists(int sort) {
        if (sInstance != null) {
            sInstance.mSortForShoppingLists = sort;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(SORT_FOR_SHOPPING_LISTS, sort);
            editor.apply();
        }
    }

    public static int getSortForShoppingListItems() {
        if (sInstance != null) {
            return sInstance.mSortForShoppingListItems;
        }
        return 0;
    }

    public static void setSortForShoppingListItems(int sort) {
        if (sInstance != null) {
            sInstance.mSortForShoppingListItems = sort;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(SORT_FOR_SHOPPING_LIST_ITEMS, sort);
            editor.apply();
        }
    }

    public static int getSortForCategories() {
        if (sInstance != null) {
            return sInstance.mSortForCategories;
        }
        return -1;
    }

    public static void setSortForCategories(int sort) {
        if (sInstance != null) {
            sInstance.mSortForCategories = sort;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(SORT_FOR_CATEGORIES, sort);
            editor.apply();
        }
    }

    public static boolean isNeedShowConfirmDeleteDialog() {
        return sInstance != null && sInstance.mConfirmDeleteDialog;
    }

    public static void setConfirmDeleteDialog(boolean confirmDeleteDialog) {
        if (sInstance != null) {
            sInstance.mConfirmDeleteDialog = confirmDeleteDialog;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(CONFIRM_DELETE_DIALOG, confirmDeleteDialog);
            editor.apply();
        }
    }

    public static boolean isSelectAnimationEnable() {
        return sInstance != null && sInstance.mSelectAnimation;
    }

    public static void setSelectAnimation(boolean animation) {
        if (sInstance != null) {
            sInstance.mSelectAnimation = animation;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(SELECT_ANIMATION, animation);
            editor.apply();
        }
    }

    public static boolean isLockScreen() {
        return sInstance != null && sInstance.mLockScreen;
    }

    public static void setLockScreen(boolean lockScreen) {
        if (sInstance != null) {
            sInstance.mLockScreen = lockScreen;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(LOCK_SCREEN, lockScreen);
            editor.apply();
        }
    }

    public static boolean isNotificationEnable() {
        return sInstance != null && sInstance.isNotificationEnable;
    }

    public static void setNotificationEnable(boolean notificationEnable) {
        if (sInstance != null) {
            sInstance.isNotificationEnable = notificationEnable;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(NOTIFICATION_ENABLE, notificationEnable);
            editor.apply();
        }
    }

    public static boolean isNotificationVibration() {
        return sInstance != null && sInstance.isNotificationVibration;
    }

    public static void setNotificationVibration(boolean notificationVibration) {
        if (sInstance != null) {
            sInstance.isNotificationVibration = notificationVibration;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(NOTIFICATION_VIBRATION, notificationVibration);
            editor.apply();
        }
    }

    public static boolean isNotificationSound() {
        return sInstance != null && sInstance.isNotificationSound;
    }

    public static void setNotificationSound(boolean notificationSound) {
        if (sInstance != null) {
            sInstance.isNotificationSound = notificationSound;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(NOTIFICATION_SOUND, notificationSound);
            editor.apply();
        }
    }

    public static boolean isAvailableNewNotification() {
        return sInstance != null && sInstance.isAvailableNewNotification;
    }

    public static void setAvailableNewNotification(boolean available) {
        if (sInstance != null) {
            sInstance.isAvailableNewNotification = available;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(AVAILABLE_NEW_NOTIFICATION, available);
            editor.apply();
        }
    }

    public static boolean isAvailableDeleteNotification() {
        return sInstance != null && sInstance.isAvailableDeleteNotification;
    }

    public static void setAvailableDeleteNotification(boolean available) {
        if (sInstance != null) {
            sInstance.isAvailableDeleteNotification = available;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(AVAILABLE_DELETE_NOTIFICATION, available);
            editor.apply();
        }
    }

    public static boolean isAvailableUpdateNotification() {
        return sInstance != null && sInstance.isAvailableUpdateNotification;
    }

    public static void setAvailableUpdateNotification(boolean available) {
        if (sInstance != null) {
            sInstance.isAvailableUpdateNotification = available;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(AVAILABLE_UPDATE_NOTIFICATION, available);
            editor.apply();
        }
    }

    public static boolean isAvailableBoughtNotification() {
        return sInstance != null && sInstance.isAvailableBoughtNotification;
    }

    public static void setAvailableBoughtNotification(boolean available) {
        if (sInstance != null) {
            sInstance.isAvailableBoughtNotification = available;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(AVAILABLE_BOUGHT_NOTIFICATION, available);
            editor.apply();
        }
    }

    public static boolean isAvailableNotBoughtNotification() {
        return sInstance != null && sInstance.isAvailableNotBoughtNotification;
    }

    public static void setAvailableNotBoughtNotification(boolean available) {
        if (sInstance != null) {
            sInstance.isAvailableNotBoughtNotification = available;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(AVAILABLE_NOT_BOUGHT_NOTIFICATION, available);
            editor.apply();
        }
    }

    public static boolean isCalculatePrice() {
        return sInstance != null && sInstance.mCalculatePrice;
    }

    public static void setCalculatePrice(boolean calculatePrice) {
        if (sInstance != null) {
            sInstance.mCalculatePrice = calculatePrice;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(CALCULATE_PRICE, calculatePrice);
            editor.apply();
        }
    }

    public static boolean isDiscolorPurchasedGoods() {
        return sInstance != null && sInstance.mDiscolorPurchasedGoods;
    }

    public static void setDiscolorPurchasedGoods(boolean discolorPurchasedGoods) {
        if (sInstance != null) {
            sInstance.mDiscolorPurchasedGoods = discolorPurchasedGoods;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(DISCOLOR_PURCHASED_GOODS, discolorPurchasedGoods);
            editor.apply();
        }
    }

    public static boolean isShowGoodsHeader() {
        return sInstance != null && sInstance.mShowGoodsHeader;
    }

    public static void setShowGoodsHeader(boolean showGoodsHeader) {
        if (sInstance != null) {
            sInstance.mShowGoodsHeader = showGoodsHeader;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(SHOW_GOODS_HEADER, showGoodsHeader);
            editor.apply();
        }
    }

    public static boolean isNeedShowRateDialog() {
        boolean flag = false;
        if (sInstance != null) {
            if (sInstance.mNeedShowRateDialog == 30) {
                flag = true;
            } else if (sInstance.mNeedShowRateDialog > 30) {
                flag = false;
            } else {
                flag = false;
                sInstance.mNeedShowRateDialog++;
                setNeedShowRateDialog(sInstance.mNeedShowRateDialog);
            }
        }
        return flag;
    }

    public static void setNeedShowRateDialog(int counter) {
        if (sInstance != null) {
            sInstance.mNeedShowRateDialog = counter;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(IS_NEED_SHOW_RATE_DIALOG, counter);
            editor.apply();
        }
    }

    public static boolean isNeedUpdateTheme(String key) {
        return sInstance != null && sInstance.mNeedUpdateTheme.contains(key);
    }

    public static void deleteUpdateItem(String key) {
        if (sInstance != null) {
            sInstance.mNeedUpdateTheme.remove(key);
        }
    }

    public static void addUpdateItem(String key) {
        if (sInstance != null) {
            sInstance.mNeedUpdateTheme.add(key);
        }
    }

    public static boolean isManualSortEnableForCategories() {
        return sInstance != null && sInstance.isManualSortEnableForCategories;
    }

    public static void setManualSortEnableForCategories(boolean enableForCategories) {
        if (sInstance != null) {
            sInstance.isManualSortEnableForCategories = enableForCategories;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(IS_MANUAL_SORT_ENABLE_FOR_CATEGORIES, enableForCategories);
            editor.commit();
        }
    }

    public static boolean isManualSortEnableForShoppingListItems() {
        return sInstance != null && sInstance.isManualSortEnableForShoppingListItems;
    }

    public static void setManualSortEnableForShoppingListItems(boolean enableForShoppingListItems) {
        if (sInstance != null) {
            sInstance.isManualSortEnableForShoppingListItems = enableForShoppingListItems;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LIST_ITEMS, enableForShoppingListItems);
            editor.commit();
        }
    }

    public static boolean isManualSortEnableForShoppingLists() {
        return sInstance != null && sInstance.isManualSortEnableForShoppingLists;
    }

    public static void setManualSortEnableForShoppingLists(boolean enableForShoppingLists) {
        if (sInstance != null) {
            sInstance.isManualSortEnableForShoppingLists = enableForShoppingLists;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(IS_MANUAL_SORT_ENABLE_FOR_SHOPPING_LISTS, enableForShoppingLists);
            editor.commit();
        }
    }

    public static boolean isCloseManualSortModeWithBackButton() {
        return sInstance != null && sInstance.mCloseManualSortModeWithBackButton;
    }

    public static void setCloseManualSortModeWithBackButton(boolean close) {
        if (sInstance != null) {
            sInstance.mCloseManualSortModeWithBackButton = close;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean(CLOSE_MANUAL_SORT_MODE_WITH_BACK_BUTTON, close);
            editor.apply();
        }
    }

    public static int getLeftShoppingListItemSwipeAction() {
        if (sInstance != null) {
            return sInstance.mLeftShoppingListItemSwipeAction;
        }
        return 1;
    }

    public static void setLeftShoppingListItemSwipeAction(int action) {
        if (sInstance != null) {
            sInstance.mLeftShoppingListItemSwipeAction = action;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION, action);
            editor.apply();
        }
    }

    public static int getRightShoppingListItemSwipeAction() {
        if (sInstance != null) {
            return sInstance.mRightShoppingListItemSwipeAction;
        }
        return 1;
    }

    public static void setRightShoppingListItemSwipeAction(int action) {
        if (sInstance != null) {
            sInstance.mRightShoppingListItemSwipeAction = action;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION, action);
            editor.apply();
        }
    }

    public static int getLongItemClickAction() {
        if (sInstance != null) {
            return sInstance.mLongItemClickAction;
        }
        return 0;
    }

    public static void setLongItemClickAction(int action) {
        if (sInstance != null) {
            sInstance.mLongItemClickAction = action;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(LONG_ITEM_CLICK_ACTION, action);
            editor.apply();
        }
    }

    public static int getAddButtonClickAction() {
        if (sInstance != null) {
            return sInstance.mAddButtonClickAction;
        }
        return 0;
    }

    public static void setAddButtonClickAction(int action) {
        if (sInstance != null) {
            sInstance.mAddButtonClickAction = action;
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putInt(ADD_BUTTON_CLICK_ACTION, action);
            editor.apply();
        }
    }
}
