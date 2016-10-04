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

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerListsComponent;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.navigation.ListRouter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.fragments.ListFragment;
import com.justplay1.shoppist.view.fragments.MenuFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class MainActivity extends BaseListActivity
        implements MenuFragment.MenuFragmentInteraction, Toolbar.OnMenuItemClickListener,
        ListRouter {

    protected static final String FRAGMENT_TAG = "list_fragment";

    private ListFragment mListFragment;

    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mMenuDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.activity_main);
        initToolbar();

        mListFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(ListFragment.class.getName());
        if (mListFragment == null) {
            mListFragment = ListFragment.newInstance();
        }
        replaceFragment(R.id.container, mListFragment, FRAGMENT_TAG);
    }

    private void createNewInjectorIfNeeded() {
        ListsComponent component = getInjector(ListsComponent.class.getName());
        if (component == null) {
            component = DaggerListsComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(ListsComponent.class.getName(), component);
        }
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mListFragment);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_shopping_lists);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.shopping_list_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));

        mToolbar.getMenu().findItem(R.id.sort_by_category).setVisible(false);
        mToolbar.getMenu().findItem(R.id.action_strike_out_all).setVisible(false);
        mToolbar.getMenu().findItem(R.id.action_return_all_to_list).setVisible(false);

        mMenuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenuDrawer.setStatusBarBackgroundColor(mPreferences.getColorPrimaryDark());
        mToggle = new ActionBarDrawerToggle(this, mMenuDrawer, mToolbar, R.string.open, R.string.close);
        mMenuDrawer.addDrawerListener(mToggle);
    }

    public void refreshToolbarColor() {
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!mMenuDrawer.isDrawerOpen(GravityCompat.START)) {
                mMenuDrawer.openDrawer(GravityCompat.START);
            } else {
                mMenuDrawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (isActionModeShowing()) {
            closeActionMode();
        } else if (mMenuDrawer.isDrawerOpen(GravityCompat.START)) {
            mMenuDrawer.closeDrawer(GravityCompat.START);
        } else {
            finishActivity(this);
        }
    }

    @Override
    public void openEditScreen(ListViewModel list) {
        mNavigator.navigateToAddListScreen(this, list);
    }

    @Override
    public void openListDetailScreen(ListViewModel list) {
        mNavigator.navigateToListItemsScreen(this, list);
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mListFragment.onCheckAllItemsClick();
                break;
            case R.id.action_settings:
                mNavigator.navigateToSettingScreen(this, 0);
                break;
            case R.id.sort_by_name:
                mListFragment.onSortByNameClick();
                break;
            case R.id.sort_by_priority:
                mListFragment.onSortByPriorityClick();
                break;
            case R.id.sort_by_time_created:
                mListFragment.onSortByTimeCreatedClick();
                break;
            case R.id.menu_expand_all:
                mListFragment.onExpandAllClick();
                break;
            case R.id.menu_collapse_all:
                mListFragment.onCollapseAllClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater menuInflater = mode.getMenuInflater();
        menuInflater.inflate(R.menu.list_action_mode, menu);
        menu.findItem(R.id.action_move).setVisible(false);
        menu.findItem(R.id.action_copy).setVisible(false);
        menu.findItem(R.id.action_strike_out).setVisible(false);
        menu.findItem(R.id.action_return_to_list).setVisible(false);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                mListFragment.onEditItemClick();
                break;
            case R.id.menu_delete:
                mListFragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                mListFragment.onCheckAllItemsClick();
                break;
            case R.id.action_email_share:
                mListFragment.onEmailShareClick();
                break;
            case R.id.menu_uncheck_all:
                mListFragment.onUnCheckAllItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        if (edit != null) {
            edit.setVisible(mListFragment.isEditButtonEnable());
        }
        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        checkAll.setEnabled(mListFragment.isCheckAllButtonEnable());
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        mListFragment.onUnCheckAllItemsClick();
    }

    @Override
    public void onCategoryClick() {
        mMenuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mNavigator.navigateToCategoriesScreen(MainActivity.this);
                mMenuDrawer.removeDrawerListener(this);
            }
        });
        mMenuDrawer.closeDrawers();
    }

    @Override
    public void onCurrencyClick() {
        mMenuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mNavigator.navigateToCurrencyScreen(MainActivity.this);
                mMenuDrawer.removeDrawerListener(this);
            }
        });
        mMenuDrawer.closeDrawers();
    }

    @Override
    public void onFeedbackClick() {
        mMenuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.app_email)});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) +
                        " Version: " + ShoppistUtils.getAppVersion(MainActivity.this));
                sendIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.send_mail_using)));
                mMenuDrawer.removeDrawerListener(this);
            }
        });
        mMenuDrawer.closeDrawers();
    }

    @Override
    public void onGoodsClick() {
        mMenuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mNavigator.navigateToGoodsScreen(MainActivity.this);
                mMenuDrawer.removeDrawerListener(this);
            }
        });
        mMenuDrawer.closeDrawers();
    }

    @Override
    public void onUnitsClick() {
        mMenuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mNavigator.navigateToUnitsScreen(MainActivity.this);
                mMenuDrawer.removeDrawerListener(this);
            }
        });
        mMenuDrawer.closeDrawers();
    }

    @Override
    public void onSettingClick(int settingId) {
        mMenuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                mNavigator.navigateToSettingScreen(MainActivity.this, settingId);
                mMenuDrawer.removeDrawerListener(this);
            }
        });
        mMenuDrawer.closeDrawers();
    }

    private static abstract class DrawerListener implements DrawerLayout.DrawerListener {

        DrawerListener() {
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
