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

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerGoodsComponent;
import com.justplay1.shoppist.di.components.GoodsComponent;
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.GoodsFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsActivity extends BaseListActivity
        implements Toolbar.OnMenuItemClickListener, GoodsRouter {

    private GoodsFragment fragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, GoodsActivity.class);
    }

    public GoodsFragment createFragment() {
        return GoodsFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.layout_single_fragment);
        initToolbar();

        fragment = (GoodsFragment) getSupportFragmentManager().findFragmentByTag(GoodsFragment.class.getName());
        if (fragment == null) {
            fragment = createFragment();
        }
        replaceFragment(R.id.container, fragment, GoodsFragment.class.getName());
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.goods);
        toolbar.setBackgroundColor(preferences.getColorPrimary());
        toolbar.inflateMenu(R.menu.goods_toolbar);
        toolbar.setOnMenuItemClickListener(this);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> finishActivity());
    }

    private void createNewInjectorIfNeeded() {
        GoodsComponent component = getInjector(GoodsComponent.class.getName());
        if (component == null) {
            component = DaggerGoodsComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(GoodsComponent.class.getName(), component);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_name:
                fragment.onSortByNameClick();
                break;
            case R.id.sort_by_time_created:
                fragment.onSortByTimeCreatedClick();
                break;
            case R.id.sort_by_category:
                fragment.onSortByCategoryClick();
                break;
            case R.id.menu_search:
                fragment.onSearchClick();
                break;
            case R.id.menu_check_all:
                fragment.onCheckAllItemsClick();
                break;
            case R.id.menu_expand_all:
                fragment.onExpandAllClick();
                break;
            case R.id.menu_collapse_all:
                fragment.onCollapseAllClick();
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
                fragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                fragment.onCheckAllItemsClick();
                break;
            case R.id.menu_uncheck_all:
                fragment.onUnCheckAllItemsClick();
                break;
            case R.id.menu_change_category:
                fragment.onChangeCategoryClick();
                break;
            case R.id.menu_change_unit:
                fragment.onChangeUnitClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        if (edit != null) {
            edit.setVisible(fragment.isEditButtonEnable());
        }

        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        checkAll.setEnabled(fragment.isCheckAllButtonEnable());
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        fragment.onUnCheckAllItemsClick();
    }

    @Override
    public void openSearchScreen() {
        navigator.navigateToSearchScreen(this, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST, null);
    }
}
