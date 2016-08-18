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
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.GoodsFragment;

/**
 * Created by Mkhytar on 25.09.2015.
 */
public class GoodsActivity extends SingleListFragmentActivity<GoodsFragment>
        implements Toolbar.OnMenuItemClickListener, GoodsFragment.GoodsFragmentInteractionListener {

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, GoodsActivity.class);
        return callingIntent;
    }

    @Override
    public GoodsFragment createFragment() {
        return GoodsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.goods);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.inflateMenu(R.menu.goods_toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishActivity());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_name:
                mFragment.onSortByNameClick();
                break;
            case R.id.sort_by_time_created:
                mFragment.onSortByTimeCreatedClick();
                break;
            case R.id.sort_by_category:
                mFragment.onSortByCategoryClick();
                break;
            case R.id.menu_search:
                mFragment.onSearchClick();
                break;
            case R.id.menu_check_all:
                mFragment.onCheckAllClick();
                break;
            case R.id.menu_expand_all:
                mFragment.onExpandAllClick();
                break;
            case R.id.menu_collapse_all:
                mFragment.onCollapseAllClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.goods_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                mFragment.onDeleteClick();
                break;
            case R.id.menu_check_all:
                mFragment.onCheckAllClick();
                break;
            case R.id.menu_uncheck_all:
                mFragment.onUnCheckAllClick();
                break;
            case R.id.menu_change_category:
                mFragment.onChangeCategoryClick();
                break;
            case R.id.menu_change_unit:
                mFragment.onChangeUnitClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        if (edit != null) {
            edit.setVisible(mFragment.isEditButtonEnable());
        }

        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        checkAll.setEnabled(mFragment.isCheckAllButtonEnable());
        return true;
    }

    @Override
    public void openSearchScreen() {
        mNavigator.navigateToSearchScreen(this, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST, null);
    }
}
