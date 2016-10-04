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

package com.justplay1.shoppist.view.fragments.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListsSettingFragment extends BaseSettingFragment {

    public static final String SHOPPING_LIST_LEFT_SWIPE_ACTION_ID = "shopping_list_left_swipe_action_id";
    public static final String SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID = "shopping_list_right_swipe_action_id";
    public static final String ADD_BUTTON_CLICK_ACTION_ID = "add_button_click_action";

    private Preference shoppingListLeftSwipeActionBtn;
    private Preference shoppingListRightSwipeActionBtn;
    private Preference addButtonClickAction;

    private AlertDialog dialog;

    public static ListsSettingFragment newInstance() {
        return new ListsSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.shopping_lists_setting;
    }

    protected void initFrame() {
        super.initFrame();
        updateShoppingListCheckBox();
        shoppingListLeftSwipeActionBtn.setOnPreferenceClickListener(this);
        shoppingListRightSwipeActionBtn.setOnPreferenceClickListener(this);
        addButtonClickAction.setOnPreferenceClickListener(this);
    }

    public void onClickPositiveBtn(String id, int[] selectedItem, ArrayAdapter<String> adapter) {
        switch (id) {
            case SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                preferences.setLeftShoppingListItemSwipeAction(selectedItem[0]);
                shoppingListLeftSwipeActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                break;
            case SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                preferences.setRightShoppingListItemSwipeAction(selectedItem[0]);
                shoppingListRightSwipeActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                break;
            case ADD_BUTTON_CLICK_ACTION_ID:
                preferences.setAddButtonClickAction(selectedItem[0]);
                addButtonClickAction.setSummary(adapter.getItem(selectedItem[0]));
                break;
        }
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        onPreferenceClick(preference.getKey());
        return true;
    }

    private void onPreferenceClick(String key) {
        switch (key) {
            case SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                showChooseActionDialog(getActivity(), SHOPPING_LIST_LEFT_SWIPE_ACTION_ID);
                break;
            case SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                showChooseActionDialog(getActivity(), SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID);
                break;
            case ADD_BUTTON_CLICK_ACTION_ID:
                showChooseActionDialog(getActivity(), ADD_BUTTON_CLICK_ACTION_ID);
                break;
        }
    }

    protected void updateShoppingListCheckBox() {
        if (shoppingListLeftSwipeActionBtn == null) {
            shoppingListLeftSwipeActionBtn = new Preference(getActivity());
            shoppingListLeftSwipeActionBtn.setKey(SHOPPING_LIST_LEFT_SWIPE_ACTION_ID);
            shoppingListLeftSwipeActionBtn.setTitle(R.string.left_swipe_action);
            switch (preferences.getLeftShoppingListItemSwipeAction()) {
                case 0:
                    shoppingListLeftSwipeActionBtn.setSummary(getString(R.string.move_item_to_cart));
                    break;
                case 1:
                    shoppingListLeftSwipeActionBtn.setSummary(getString(R.string.delete_item));
                    break;
                case 2:
                    shoppingListLeftSwipeActionBtn.setSummary(getString(R.string.edit_item));
                    break;
            }
        }
        getPreferenceScreen().removePreference(shoppingListLeftSwipeActionBtn);
        getPreferenceScreen().addPreference(shoppingListLeftSwipeActionBtn);

        if (shoppingListRightSwipeActionBtn == null) {
            shoppingListRightSwipeActionBtn = new Preference(getActivity());
            shoppingListRightSwipeActionBtn.setKey(SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID);
            shoppingListRightSwipeActionBtn.setTitle(R.string.right_swipe_action);
            switch (preferences.getRightShoppingListItemSwipeAction()) {
                case 0:
                    shoppingListRightSwipeActionBtn.setSummary(getString(R.string.move_item_to_cart));
                    break;
                case 1:
                    shoppingListRightSwipeActionBtn.setSummary(getString(R.string.delete_item));
                    break;
                case 2:
                    shoppingListRightSwipeActionBtn.setSummary(getString(R.string.edit_item));
                    break;
            }
        }
        getPreferenceScreen().removePreference(shoppingListRightSwipeActionBtn);
        getPreferenceScreen().addPreference(shoppingListRightSwipeActionBtn);

        if (addButtonClickAction == null) {
            addButtonClickAction = new Preference(getActivity());
            addButtonClickAction.setKey(ADD_BUTTON_CLICK_ACTION_ID);
            addButtonClickAction.setTitle(R.string.add_button_click_action);
            switch (preferences.getAddButtonClickAction()) {
                case 0:
                    addButtonClickAction.setSummary(R.string.standart_mode);
                    break;
                case 1:
                    addButtonClickAction.setSummary(R.string.quick_mode);
                    break;
            }
        }
        getPreferenceScreen().removePreference(addButtonClickAction);
        getPreferenceScreen().addPreference(addButtonClickAction);
    }

    private void showChooseActionDialog(Context context, final String id) {
        String[] actions = {context.getString(R.string.move_item_to_cart),
                context.getString(R.string.delete_item), context.getString(R.string.edit_item)};

        final int[] selectedItem = {-1};
        switch (id) {
            case ListsSettingFragment.SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                selectedItem[0] = preferences.getLeftShoppingListItemSwipeAction();
                break;
            case ListsSettingFragment.SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                selectedItem[0] = preferences.getRightShoppingListItemSwipeAction();
                break;
            case ListsSettingFragment.ADD_BUTTON_CLICK_ACTION_ID:
                selectedItem[0] = preferences.getAddButtonClickAction();
                actions = new String[]{context.getString(R.string.standart_mode), context.getString(R.string.quick_mode)};
                break;
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.dialog_list_item, actions);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(adapter, selectedItem[0], (dialog, which) -> {
            selectedItem[0] = which;
        });

        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    onClickPositiveBtn(id, selectedItem, adapter);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };

        builder.setTitle(R.string.choose_actions);
        builder.setPositiveButton(R.string.choose, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(preferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(preferences.getColorPrimary());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
