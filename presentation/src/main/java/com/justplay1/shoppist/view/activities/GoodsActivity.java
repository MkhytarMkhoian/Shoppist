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
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.GoodsFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsActivity extends SingleListFragmentActivity<GoodsFragment>
        implements Toolbar.OnMenuItemClickListener, GoodsRouter {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.goods);
        toolbar.setBackgroundColor(mPreferences.getColorPrimary());
        toolbar.inflateMenu(R.menu.goods_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> finishActivity());
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
                mFragment.onCheckAllItemsClick();
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
                mFragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                mFragment.onCheckAllItemsClick();
                break;
            case R.id.menu_uncheck_all:
                mFragment.onUnCheckAllItemsClick();
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
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        mFragment.onUnCheckAllItemsClick();
    }

    @Override
    public void openSearchScreen() {
        mNavigator.navigateToSearchScreen(this, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST, null);
    }
}
