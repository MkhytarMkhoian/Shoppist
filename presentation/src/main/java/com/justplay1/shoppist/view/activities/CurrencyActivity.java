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
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.di.components.DaggerCurrencyComponent;
import com.justplay1.shoppist.view.fragments.CurrencyFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyActivity extends BaseListActivity
        implements Toolbar.OnMenuItemClickListener {

    private CurrencyFragment mFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CurrencyActivity.class);
    }

    public CurrencyFragment createFragment() {
        return CurrencyFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.layout_single_fragment);
        initToolbar();

        mFragment = (CurrencyFragment) getSupportFragmentManager().findFragmentByTag(CurrencyFragment.class.getName());
        if (mFragment == null) {
            mFragment = createFragment();
        }
        replaceFragment(R.id.container, mFragment, CurrencyFragment.class.getName());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.currency);
        toolbar.setBackgroundColor(mPreferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> finishActivity());
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.units_and_currency_menu);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    private void createNewInjectorIfNeeded() {
        CurrencyComponent component = getInjector(CurrencyComponent.class.getName());
        if (component == null) {
            component = DaggerCurrencyComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(CurrencyComponent.class.getName(), component);
        }
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
                mFragment.onUnCheckAllItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
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
