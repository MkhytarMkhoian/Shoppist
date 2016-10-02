/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.justplay1.shoppist.data.R;
import com.justplay1.shoppist.entity.SortTypeDAO;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class AppPreferences {

    private static final String COLOR_PRIMARY = "color_theme";
    private static final String COLOR_PRIMARY_DARK = "color_status_bar_theme";
    private static final String LOCK_SCREEN = "LockScreen";

    private static final String CONFIRM_DELETE_DIALOG = "confirm_delete_dialog";
    private static final String IS_NEED_SHOW_RATE_DIALOG = "is_need_show_rate_dialog";
    private static final String DEFAULT_CURRENCY = "default_currency_id";
    private static final String CALCULATE_PRICE = "calculate_price";
    private static final String ADD_BUTTON_CLICK_ACTION = "add_button_click_action";

    private static final String SORT_FOR_SHOPPING_LISTS = "sort_for_shopping_lists";
    private static final String SORT_FOR_SHOPPING_LIST_ITEMS = "sort_for_shopping_list_items";
    private static final String SORT_FOR_GOODS = "sort_for_goods";

    private static final String LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION = "left_shopping_list_item_swipe_action";
    private static final String RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION = "right_shopping_list_item_swipe_action";

    private SharedPreferences mPreference;
    private Context mContext;

    /**
     * Mirror variables of preference
     */
    private int mColorPrimary;
    private int mColorPrimaryDark;
    private boolean mLockScreen;

    private int mSortForShoppingLists;
    private int mSortForShoppingListItems;
    private int mSortForGoods;

    private boolean mConfirmDeleteDialog;
    private int mNeedShowRateDialog;
    private String mDefaultCurrency;
    private boolean mCalculatePrice;
    private int mAddButtonClickAction;

    private int mLeftShoppingListItemSwipeAction;
    private int mRightShoppingListItemSwipeAction;

    @Inject
    public AppPreferences(Context context) {
        mContext = context;
    }

    private void loadFromPreference() {
        if (mPreference != null) {
            mColorPrimary = mPreference.getInt(COLOR_PRIMARY, mContext.getResources().getColor(R.color.red_color));
            mColorPrimaryDark = mPreference.getInt(COLOR_PRIMARY_DARK, mContext.getResources().getColor(R.color.red_800));
            mSortForShoppingLists = mPreference.getInt(SORT_FOR_SHOPPING_LISTS, 4);
            mSortForShoppingListItems = mPreference.getInt(SORT_FOR_SHOPPING_LIST_ITEMS, SortTypeDAO.SORT_BY_CATEGORIES);
            mSortForGoods = mPreference.getInt(SORT_FOR_GOODS, 3);
            mLockScreen = mPreference.getBoolean(LOCK_SCREEN, false);

            mConfirmDeleteDialog = mPreference.getBoolean(CONFIRM_DELETE_DIALOG, false);
            mNeedShowRateDialog = mPreference.getInt(IS_NEED_SHOW_RATE_DIALOG, 0);
            mDefaultCurrency = mPreference.getString(DEFAULT_CURRENCY, "1");
            mCalculatePrice = mPreference.getBoolean(CALCULATE_PRICE, true);
            mAddButtonClickAction = mPreference.getInt(ADD_BUTTON_CLICK_ACTION, 0);

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

    public int getSortForLists() {
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

    public boolean isCalculatePrice() {
        return mCalculatePrice;
    }

    public void setCalculatePrice(boolean calculatePrice) {
        mCalculatePrice = calculatePrice;
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putBoolean(CALCULATE_PRICE, calculatePrice);
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
