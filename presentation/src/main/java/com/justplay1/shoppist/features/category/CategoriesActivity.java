/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.category;

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
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.di.components.DaggerCategoryComponent;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.shared.base.activities.BaseListActivity;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoriesActivity extends BaseListActivity
        implements Toolbar.OnMenuItemClickListener, CategoryRouter {

    private CategoryFragment fragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CategoriesActivity.class);
    }

    public CategoryFragment createFragment() {
        return CategoryFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.layout_single_fragment);
        initToolbar();

        fragment = (CategoryFragment) getSupportFragmentManager().findFragmentByTag(CategoryFragment.class.getName());
        if (fragment == null) {
            fragment = createFragment();
        }
        replaceFragment(R.id.container, fragment, CategoryFragment.class.getName());
    }

    protected void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_category);
        toolbar.setBackgroundColor(preferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> finishActivity());
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.category_list_toolbar);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    private void createNewInjectorIfNeeded() {
        CategoryComponent component = getInjector(CategoryComponent.class.getName());
        if (component == null) {
            component = DaggerCategoryComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(CategoryComponent.class.getName(), component);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                fragment.onCheckAllItemsClick();
                break;
        }
        return true;
    }

    @Override
    public void openEditCategoryScreen(CategoryViewModel category) {
        navigator.navigateToAddCategoryScreen(this, category);
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
                fragment.onCheckAllItemsClick();
                break;
            case R.id.menu_uncheck_all:
                fragment.onUnCheckAllItemsClick();
                break;
            case R.id.menu_delete:
                fragment.onDeleteCheckedItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        super.onPrepareActionMode(actionMode, menu);
        MenuItem delete = menu.findItem(R.id.menu_delete);
        delete.setVisible(fragment.isDeleteButtonEnable());

        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        checkAll.setEnabled(fragment.isCheckAllButtonEnable());
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        fragment.onUnCheckAllItemsClick();
    }
}
