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
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.navigation.CategoryRouter;
import com.justplay1.shoppist.view.fragments.CategoryFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoriesActivity extends SingleListFragmentActivity<CategoryFragment>
        implements Toolbar.OnMenuItemClickListener, CategoryRouter {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CategoriesActivity.class);
    }

    @Override
    public CategoryFragment createFragment() {
        return CategoryFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_category);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishActivity());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.category_list_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mFragment.onCheckAllItemsClick();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mFragment.isManualSortModeEnable()) {
            mFragment.disableManualSort();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void openEditCategoryScreen(CategoryViewModel category) {
        mNavigator.navigateToAddCategoryScreen(this, category);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.category_list_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_check_all:
                mFragment.onCheckAllItemsClick();
                break;
            case R.id.menu_uncheck_all:
                mFragment.onUnCheckAllItemsClick();
                break;
            case R.id.menu_delete:
                mFragment.onDeleteCheckedItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        super.onPrepareActionMode(actionMode, menu);
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
