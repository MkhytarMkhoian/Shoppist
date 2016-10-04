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

    private ListItemsFragment mFragment;
    private ListViewModel mParentList;

    public static Intent getCallingIntent(Context context, ListViewModel parentList) {
        Intent callingIntent = new Intent(context, ListItemActivity.class);
        callingIntent.putExtra(ListViewModel.class.getName(), parentList);
        return callingIntent;
    }

    public ListItemsFragment createFragment() {
        return ListItemsFragment.newInstance(mParentList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.layout_single_fragment);
        if (getIntent() != null) {
            mParentList = getIntent().getParcelableExtra(ListViewModel.class.getName());
        }
        initToolbar();

        mFragment = (ListItemsFragment) getSupportFragmentManager().findFragmentByTag(ListItemsFragment.class.getName());
        if (mFragment == null) {
            mFragment = createFragment();
        }
        replaceFragment(R.id.container, mFragment, ListItemsFragment.class.getName());
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mParentList.getName());
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        toolbar.setBackgroundColor(mPreferences.getColorPrimary());
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
                mFragment.onCheckAllItemsClick();
                break;
            case R.id.sort_by_name:
                mFragment.onSortByNameClick();
                break;
            case R.id.sort_by_priority:
                mFragment.onSortByPriorityClick();
                break;
            case R.id.sort_by_time_created:
                mFragment.onSortByTimeCreatedClick();
                break;
            case R.id.sort_by_category:
                mFragment.onSortByCategoryClick();
                break;
            case R.id.action_return_all_to_list:
                mFragment.onReturnAllToListClick();
                break;
            case R.id.action_strike_out_all:
                mFragment.onStrikeOutAllClick();
                break;
            case R.id.menu_expand_all:
                mFragment.onExpandAll();
                break;
            case R.id.menu_collapse_all:
                mFragment.onCollapseAll();
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
                mFragment.onEditItemClick();
                break;
            case R.id.menu_delete:
                mFragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                mFragment.onCheckAllItemsClick();
                break;
            case R.id.action_move:
                mFragment.onMoveItemsClick();
                break;
            case R.id.action_copy:
                mFragment.onCopyItemsClick();
                break;
            case R.id.action_email_share:
                mFragment.onEmailShareClick();
                break;
            case R.id.menu_uncheck_all:
                mFragment.onUnCheckAllItemsClick();
                break;
            case R.id.action_return_to_list:
                mFragment.onReturnCheckedItemsToListClick();
                break;
            case R.id.action_strike_out:
                mFragment.onStrikeOutCheckedItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem move = menu.findItem(R.id.action_move);
        MenuItem copy = menu.findItem(R.id.action_copy);
        boolean enable = mFragment.isMoveCopyButtonEnable();
        move.setEnabled(enable);
        copy.setEnabled(enable);

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
    public void openEditScreen(ListViewModel list, ListItemViewModel item) {
        mNavigator.navigateToAddListItemScreen(this, list, item);
    }

    @Override
    public void openQuickMode(String parentListId) {
        mNavigator.navigateToSearchScreen(this, Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST, parentListId);
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
