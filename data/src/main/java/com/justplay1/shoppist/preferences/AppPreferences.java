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

    private static final String MESSAGE_DIALOG = "message_dialog";

    private static final String COLOR_PRIMARY = "color_theme";
    private static final String COLOR_PRIMARY_DARK = "color_status_bar_theme";
    private static final String LOCK_SCREEN = "LockScreen";

    private static final String CONFIRM_DELETE_DIALOG = "confirm_delete_dialog";
    private static final String ADD_BUTTON_CLICK_ACTION = "add_button_click_action";

    private static final String SORT_FOR_SHOPPING_LISTS = "sort_for_shopping_lists";
    private static final String SORT_FOR_SHOPPING_LIST_ITEMS = "sort_for_shopping_list_items";
    private static final String SORT_FOR_GOODS = "sort_for_goods";

    private static final String LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION = "left_shopping_list_item_swipe_action";
    private static final String RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION = "right_shopping_list_item_swipe_action";

    private SharedPreferences sharedPreferences;
    private Context context;

    /**
     * Mirror variables of preference
     */

    private boolean messageDialog;

    private int colorPrimary;
    private int colorPrimaryDark;
    private boolean lockScreen;

    private int sortForShoppingLists;
    private int sortForShoppingListItems;
    private int sortForGoods;

    private boolean confirmDeleteDialog;
    private int addButtonClickAction;

    private int leftShoppingListItemSwipeAction;
    private int rightShoppingListItemSwipeAction;

    @Inject
    public AppPreferences(Context context) {
        this.context = context;
    }

    private void loadFromPreference() {
        if (sharedPreferences != null) {
            messageDialog = sharedPreferences.getBoolean(MESSAGE_DIALOG, true);

            colorPrimary = sharedPreferences.getInt(COLOR_PRIMARY, context.getResources().getColor(R.color.red_color));
            colorPrimaryDark = sharedPreferences.getInt(COLOR_PRIMARY_DARK, context.getResources().getColor(R.color.red_800));
            sortForShoppingLists = sharedPreferences.getInt(SORT_FOR_SHOPPING_LISTS, 4);
            sortForShoppingListItems = sharedPreferences.getInt(SORT_FOR_SHOPPING_LIST_ITEMS, SortTypeDAO.SORT_BY_CATEGORIES);
            sortForGoods = sharedPreferences.getInt(SORT_FOR_GOODS, 3);
            lockScreen = sharedPreferences.getBoolean(LOCK_SCREEN, false);

            confirmDeleteDialog = sharedPreferences.getBoolean(CONFIRM_DELETE_DIALOG, false);
            addButtonClickAction = sharedPreferences.getInt(ADD_BUTTON_CLICK_ACTION, 0);

            leftShoppingListItemSwipeAction = sharedPreferences.getInt(LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION, 0);
            rightShoppingListItemSwipeAction = sharedPreferences.getInt(RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION, 0);
        }
    }

    public void initPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        loadFromPreference();
    }

    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    public int getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(int color) {
        colorPrimaryDark = color;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COLOR_PRIMARY_DARK, color);
        editor.commit();

    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(int color) {
        colorPrimary = color;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(COLOR_PRIMARY, color);
        editor.commit();
    }

    public int getSortForGoods() {
        return sortForGoods;
    }

    public void setSortForGoods(int sort) {
        sortForGoods = sort;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_FOR_GOODS, sort);
        editor.apply();
    }

    public int getSortForLists() {
        return sortForShoppingLists;
    }

    public void setSortForShoppingLists(int sort) {
        sortForShoppingLists = sort;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_FOR_SHOPPING_LISTS, sort);
        editor.apply();
    }

    public int getSortForShoppingListItems() {
        return sortForShoppingListItems;
    }

    public void setSortForShoppingListItems(int sort) {
        sortForShoppingListItems = sort;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SORT_FOR_SHOPPING_LIST_ITEMS, sort);
        editor.apply();
    }

    public boolean isNeedShowConfirmDeleteDialog() {
        return confirmDeleteDialog;
    }

    public void setConfirmDeleteDialog(boolean confirmDeleteDialog) {
        this.confirmDeleteDialog = confirmDeleteDialog;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CONFIRM_DELETE_DIALOG, confirmDeleteDialog);
        editor.apply();
    }

    public boolean isLockScreen() {
        return lockScreen;
    }

    public void setLockScreen(boolean lockScreen) {
        this.lockScreen = lockScreen;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOCK_SCREEN, lockScreen);
        editor.apply();
    }

    public int getLeftShoppingListItemSwipeAction() {
        return leftShoppingListItemSwipeAction;
    }

    public void setLeftShoppingListItemSwipeAction(int action) {
        leftShoppingListItemSwipeAction = action;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LEFT_SHOPPING_LIST_ITEM_SWIPE_ACTION, action);
        editor.apply();
    }

    public int getRightShoppingListItemSwipeAction() {
        return rightShoppingListItemSwipeAction;
    }

    public void setRightShoppingListItemSwipeAction(int action) {
        rightShoppingListItemSwipeAction = action;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RIGHT_SHOPPING_LIST_ITEM_SWIPE_ACTION, action);
        editor.apply();
    }

    public int getAddButtonClickAction() {
        return addButtonClickAction;
    }

    public void setAddButtonClickAction(int action) {
        addButtonClickAction = action;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(ADD_BUTTON_CLICK_ACTION, action);
        editor.apply();

    }

    public boolean isNeedShowMessageDialog() {
        return messageDialog;
    }

    public void setMessageDialog(boolean messageDialog) {
        this.messageDialog = messageDialog;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MESSAGE_DIALOG, messageDialog);
        editor.apply();
    }
}
