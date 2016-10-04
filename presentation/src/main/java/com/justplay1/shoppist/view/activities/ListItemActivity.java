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
import com.justplay1.shoppist.di.components.DaggerListItemsComponent;
import com.justplay1.shoppist.di.components.ListItemsComponent;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.navigation.ListItemsRouter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.ListItemsFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemActivity extends BaseListActivity
        implements Toolbar.OnMenuItemClickListener, ListItemsRouter {

    private ListItemsFragment fragment;
    private ListViewModel parentList;

    public static Intent getCallingIntent(Context context, ListViewModel parentList) {
        Intent callingIntent = new Intent(context, ListItemActivity.class);
        callingIntent.putExtra(ListViewModel.class.getName(), parentList);
        return callingIntent;
    }

    public ListItemsFragment createFragment() {
        return ListItemsFragment.newInstance(parentList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.layout_single_fragment);
        if (getIntent() != null) {
            parentList = getIntent().getParcelableExtra(ListViewModel.class.getName());
        }
        initToolbar();

        fragment = (ListItemsFragment) getSupportFragmentManager().findFragmentByTag(ListItemsFragment.class.getName());
        if (fragment == null) {
            fragment = createFragment();
        }
        replaceFragment(R.id.container, fragment, ListItemsFragment.class.getName());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(parentList.getName());
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        toolbar.setBackgroundColor(preferences.getColorPrimary());
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.inflateMenu(R.menu.shopping_list_toolbar);
        toolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
    }

    private void createNewInjectorIfNeeded() {
        ListItemsComponent component = getInjector(ListItemsComponent.class.getName());
        if (component == null) {
            component = DaggerListItemsComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(ListItemsComponent.class.getName(), component);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                fragment.onCheckAllItemsClick();
                break;
            case R.id.sort_by_name:
                fragment.onSortByNameClick();
                break;
            case R.id.sort_by_priority:
                fragment.onSortByPriorityClick();
                break;
            case R.id.sort_by_time_created:
                fragment.onSortByTimeCreatedClick();
                break;
            case R.id.sort_by_category:
                fragment.onSortByCategoryClick();
                break;
            case R.id.action_return_all_to_list:
                fragment.onReturnAllToListClick();
                break;
            case R.id.action_strike_out_all:
                fragment.onStrikeOutAllClick();
                break;
            case R.id.menu_expand_all:
                fragment.onExpandAll();
                break;
            case R.id.menu_collapse_all:
                fragment.onCollapseAll();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.list_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                fragment.onEditItemClick();
                break;
            case R.id.menu_delete:
                fragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                fragment.onCheckAllItemsClick();
                break;
            case R.id.action_move:
                fragment.onMoveItemsClick();
                break;
            case R.id.action_copy:
                fragment.onCopyItemsClick();
                break;
            case R.id.action_email_share:
                fragment.onEmailShareClick();
                break;
            case R.id.menu_uncheck_all:
                fragment.onUnCheckAllItemsClick();
                break;
            case R.id.action_return_to_list:
                fragment.onReturnCheckedItemsToListClick();
                break;
            case R.id.action_strike_out:
                fragment.onStrikeOutCheckedItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem move = menu.findItem(R.id.action_move);
        MenuItem copy = menu.findItem(R.id.action_copy);
        boolean enable = fragment.isMoveCopyButtonEnable();
        move.setEnabled(enable);
        copy.setEnabled(enable);

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
    public void openEditScreen(ListViewModel list, ListItemViewModel item) {
        navigator.navigateToAddListItemScreen(this, list, item);
    }

    @Override
    public void openQuickMode(String parentListId) {
        navigator.navigateToSearchScreen(this, Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST, parentListId);
    }

    @Override
    public void onBackPressed() {
        if (isActionModeShowing()) {
            closeActionMode();
        } else {
            super.onBackPressed();
        }
    }
}
