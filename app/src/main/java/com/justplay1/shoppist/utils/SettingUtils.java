package com.justplay1.shoppist.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.ui.fragments.settings.ShoppingListsSettingFragment;
import com.justplay1.shoppist.settings.SwipeActionDialogCallback;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class SettingUtils {

    public static void showChooseActionDialog(Context context, final String id, final SwipeActionDialogCallback callback) {
        String[] actions = {context.getString(R.string.move_item_to_cart),
                context.getString(R.string.delete_item), context.getString(R.string.edit_item)};

        final int[] selectedItem = {-1};
        switch (id) {
            case ShoppingListsSettingFragment.SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                selectedItem[0] = ShoppistPreferences.getLeftShoppingListItemSwipeAction();
                break;
            case ShoppingListsSettingFragment.SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                selectedItem[0] = ShoppistPreferences.getRightShoppingListItemSwipeAction();
                break;
            case ShoppingListsSettingFragment.ADD_BUTTON_CLICK_ACTION_ID:
                selectedItem[0] = ShoppistPreferences.getAddButtonClickAction();
                actions = new String[] {context.getString(R.string.standart_mode), context.getString(R.string.quick_mode)};
                break;
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.dialog_list_item, actions);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(adapter, selectedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem[0] = which;
            }
        });

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        callback.onClickPositiveBtn(id, selectedItem, adapter);
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        builder.setTitle(R.string.choose_actions);
        builder.setPositiveButton(R.string.choose, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }
}
