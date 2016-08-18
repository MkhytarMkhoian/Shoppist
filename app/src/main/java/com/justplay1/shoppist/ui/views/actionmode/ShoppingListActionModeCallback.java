package com.justplay1.shoppist.ui.views.actionmode;

import android.app.Activity;
import android.content.Context;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.ActivityUtils;

/**
 * Created by Mkhitar on 01.12.2014.
 */
public class ShoppingListActionModeCallback implements ActionMode.Callback {

    private Context mContext;
    private ActionModeType mModeType;
    private ActionModeListener mModeListener;

    public ShoppingListActionModeCallback(Context context, ActionModeType modeType, ActionModeListener modeListener) {
        mContext = context;
        mModeType = modeType;
        mModeListener = modeListener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater menuInflater = actionMode.getMenuInflater();
        switch (mModeType) {
            case SHOPPING_LIST:
                menuInflater.inflate(R.menu.shopping_list_action_mode, menu);
                disableActions(menu);
                break;
            case SHOPPING_LIST_ITEM:
                menuInflater.inflate(R.menu.shopping_list_action_mode, menu);
                break;
            case CATEGORIES:
                menuInflater.inflate(R.menu.category_list_action_mode, menu);
                break;
            case GOODS_LIST:
                menuInflater.inflate(R.menu.goods_action_mode, menu);
                break;
            case CURRENCY:
                menuInflater.inflate(R.menu.units_and_currency_action_mode, menu);
                break;
            case UNITS:
                menuInflater.inflate(R.menu.units_and_currency_action_mode, menu);
                break;
        }

        if (mModeListener != null) {
            mModeListener.onCreateActionMode(actionMode, menu);
        }
        return true;
    }

    private void disableActions(Menu menu) {
        menu.findItem(R.id.action_move).setVisible(false);
        menu.findItem(R.id.action_copy).setVisible(false);
        menu.findItem(R.id.action_strike_out).setVisible(false);
        menu.findItem(R.id.action_return_to_list).setVisible(false);
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        ActivityUtils.setStatusBarColor((Activity) mContext, mContext.getResources().getColor(R.color.grey_700));
        if (mModeListener != null) {
            mModeListener.onPrepareActionMode(actionMode, menu);
        }
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (mModeListener != null) {
            mModeListener.onItemClick(actionMode, menuItem);
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        ActivityUtils.setStatusBarColor((Activity) mContext);
        if (mModeListener != null) {
            mModeListener.onDestroyActionMode(actionMode);
        }
    }

}
