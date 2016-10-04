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
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.AddListItemsComponent;
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.di.components.DaggerAddListItemsComponent;
import com.justplay1.shoppist.di.components.DaggerCategoryComponent;
import com.justplay1.shoppist.di.components.DaggerListsComponent;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.models.AddElementType;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.view.fragments.AddCategoryFragment;
import com.justplay1.shoppist.view.fragments.AddListFragment;
import com.justplay1.shoppist.view.fragments.AddListItemFragment;
import com.justplay1.shoppist.view.fragments.BaseAddElementFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddElementActivity extends BaseActivity
        implements BaseAddElementFragment.AddElementListener, AddListItemFragment.AddListItemListener {

    private Toolbar toolbar;
    @AddElementType private int elementType;

    private CategoryViewModel categoryModel;
    private ListViewModel listModel;
    private ListItemViewModel listItemModel;

    public static Intent getCallingIntent(Context context, @AddElementType int type,
                                          CategoryViewModel category, ListViewModel list,
                                          ListItemViewModel listItem) {
        Intent callingIntent = new Intent(context, AddElementActivity.class);
        callingIntent.putExtra(AddElementType.class.getName(), type);
        callingIntent.putExtra(CategoryViewModel.class.getName(), category);
        callingIntent.putExtra(ListViewModel.class.getName(), list);
        callingIntent.putExtra(ListItemViewModel.class.getName(), listItem);
        return callingIntent;
    }

    public BaseAddElementFragment createFragment() {
        switch (elementType) {
            case AddElementType.CATEGORY:
                return AddCategoryFragment.newInstance(categoryModel);
            case AddElementType.LIST:
                return AddListFragment.newInstance(listModel);
            case AddElementType.LIST_ITEM:
                return AddListItemFragment.newInstance(listModel.getId(), listItemModel);
        }
        return null;
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();

        if (getIntent() != null) {
            elementType = getIntent().getIntExtra(AddElementType.class.getName(), 0);
            categoryModel = getIntent().getParcelableExtra(CategoryViewModel.class.getName());
            listModel = getIntent().getParcelableExtra(ListViewModel.class.getName());
            listItemModel = getIntent().getParcelableExtra(ListItemViewModel.class.getName());
        }
        createNewInjectorIfNeeded();

        BaseAddElementFragment fragment = (BaseAddElementFragment) getSupportFragmentManager()
                .findFragmentByTag(BaseAddElementFragment.class.getName());
        if (fragment == null) {
            fragment = createFragment();
        }
        replaceFragment(R.id.container, fragment, BaseAddElementFragment.class.getName());
    }

    private void createNewInjectorIfNeeded() {
        Object component;
        switch (elementType) {
            case AddElementType.CATEGORY:
                component = getInjector(CategoryComponent.class.getName());
                if (component == null) {
                    component = DaggerCategoryComponent.builder()
                            .appComponent(App.get().getAppComponent())
                            .build();
                    putInjector(CategoryComponent.class.getName(), component);
                }
                break;
            case AddElementType.LIST:
                component = getInjector(ListsComponent.class.getName());
                if (component == null) {
                    component = DaggerListsComponent.builder()
                            .appComponent(App.get().getAppComponent())
                            .build();
                    putInjector(ListsComponent.class.getName(), component);
                }
                break;
            case AddElementType.LIST_ITEM:
                component = getInjector(AddListItemsComponent.class.getName());
                if (component == null) {
                    component = DaggerAddListItemsComponent.builder()
                            .appComponent(App.get().getAppComponent())
                            .build();
                    putInjector(AddListItemsComponent.class.getName(), component);
                }
                break;
        }
    }

    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(preferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> finishWithResult());
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public void setTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public void closeScreen() {
        finishWithResult();
    }

    @Override
    public void openAddCategoryScreen(CategoryViewModel category) {
        navigator.navigateToAddCategoryScreen(this, category);
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
    }

    protected void finishWithResult() {
        Intent data = new Intent();
        finishActivityWithResult(this, RESULT_OK, data);
    }
}
