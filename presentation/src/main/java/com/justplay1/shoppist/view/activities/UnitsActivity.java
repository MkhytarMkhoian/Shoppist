package com.justplay1.shoppist.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.fragments.UnitFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitsActivity extends SingleListFragmentActivity<UnitFragment> implements Toolbar.OnMenuItemClickListener {

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, UnitsActivity.class);
        return callingIntent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initializeToolbar();
    }

    @Override
    public UnitFragment createFragment() {
        return UnitFragment.newInstance();
    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.units);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishActivity());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.units_and_currency_menu);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_check_all:
                mFragment.onCheckAllItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.units_and_currency_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mFragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                mFragment.onCheckAllItemsClick();
                break;
            case R.id.menu_uncheck_all:
                closeActionMode();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem delete = menu.findItem(R.id.menu_delete);
        delete.setVisible(mFragment.isDeleteButtonEnable());

        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        checkAll.setEnabled(mFragment.isCheckAllButtonEnable());
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        mFragment.onUnCheckAllItemsClick();
    }
}
