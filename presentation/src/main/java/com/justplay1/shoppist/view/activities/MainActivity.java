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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class MainActivity extends BaseListActivity
        implements MenuFragment.MenuFragmentInteraction, Toolbar.OnMenuItemClickListener,
        ListRouter {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ListFragment listFragment;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout menuDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();

        listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(ListFragment.class.getName());
        if (listFragment == null) {
            listFragment = ListFragment.newInstance();
        }
        replaceFragment(R.id.container, listFragment, ListFragment.class.getName());
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
        toggle.syncState();
    }

    private void initToolbar() {
        toolbar.setTitle(R.string.title_activity_shopping_lists);
        toolbar.setBackgroundColor(preferences.getColorPrimary());
        toolbar.setOnMenuItemClickListener(this);
        toolbar.inflateMenu(R.menu.shopping_list_toolbar);
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));

        toolbar.getMenu().findItem(R.id.sort_by_category).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_strike_out_all).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_return_all_to_list).setVisible(false);

        menuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuDrawer.setStatusBarBackgroundColor(preferences.getColorPrimaryDark());
        toggle = new ActionBarDrawerToggle(this, menuDrawer, toolbar, R.string.open, R.string.close);
        menuDrawer.addDrawerListener(toggle);
    }

    public void refreshToolbarColor() {
        toolbar.setBackgroundColor(preferences.getColorPrimary());
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!menuDrawer.isDrawerOpen(GravityCompat.START)) {
                menuDrawer.openDrawer(GravityCompat.START);
            } else {
                menuDrawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (isActionModeShowing()) {
            closeActionMode();
        } else if (menuDrawer.isDrawerOpen(GravityCompat.START)) {
            menuDrawer.closeDrawer(GravityCompat.START);
        } else {
            finishActivity(this);
        }
    }

    @Override
    public void openEditScreen(ListViewModel list) {
        navigator.navigateToAddListScreen(this, list);
    }

    @Override
    public void openListDetailScreen(ListViewModel list) {
        navigator.navigateToListItemsScreen(this, list);
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                listFragment.onCheckAllItemsClick();
                break;
            case R.id.action_settings:
                navigator.navigateToSettingScreen(this, 0);
                break;
            case R.id.sort_by_name:
                listFragment.onSortByNameClick();
                break;
            case R.id.sort_by_priority:
                listFragment.onSortByPriorityClick();
                break;
            case R.id.sort_by_time_created:
                listFragment.onSortByTimeCreatedClick();
                break;
            case R.id.menu_expand_all:
                listFragment.onExpandAllClick();
                break;
            case R.id.menu_collapse_all:
                listFragment.onCollapseAllClick();
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
                listFragment.onEditItemClick();
                break;
            case R.id.menu_delete:
                listFragment.onDeleteCheckedItemsClick();
                break;
            case R.id.menu_check_all:
                listFragment.onCheckAllItemsClick();
                break;
            case R.id.action_email_share:
                listFragment.onEmailShareClick();
                break;
            case R.id.menu_uncheck_all:
                listFragment.onUnCheckAllItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        super.onPrepareActionMode(mode, menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        if (edit != null) {
            edit.setVisible(listFragment.isEditButtonEnable());
        }
        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        checkAll.setEnabled(listFragment.isCheckAllButtonEnable());
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        super.onDestroyActionMode(actionMode);
        listFragment.onUnCheckAllItemsClick();
    }

    @Override
    public void onCategoryClick() {
        menuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                navigator.navigateToCategoriesScreen(MainActivity.this);
                menuDrawer.removeDrawerListener(this);
            }
        });
        menuDrawer.closeDrawers();
    }

    @Override
    public void onCurrencyClick() {
        menuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                navigator.navigateToCurrencyScreen(MainActivity.this);
                menuDrawer.removeDrawerListener(this);
            }
        });
        menuDrawer.closeDrawers();
    }

    @Override
    public void onFeedbackClick() {
        menuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.app_email)});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) +
                        " Version: " + ShoppistUtils.getAppVersion(MainActivity.this));
                sendIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.send_mail_using)));
                menuDrawer.removeDrawerListener(this);
            }
        });
        menuDrawer.closeDrawers();
    }

    @Override
    public void onGoodsClick() {
        menuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                navigator.navigateToGoodsScreen(MainActivity.this);
                menuDrawer.removeDrawerListener(this);
            }
        });
        menuDrawer.closeDrawers();
    }

    @Override
    public void onUnitsClick() {
        menuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                navigator.navigateToUnitsScreen(MainActivity.this);
                menuDrawer.removeDrawerListener(this);
            }
        });
        menuDrawer.closeDrawers();
    }

    @Override
    public void onSettingClick(int settingId) {
        menuDrawer.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                navigator.navigateToSettingScreen(MainActivity.this, settingId);
                menuDrawer.removeDrawerListener(this);
            }
        });
        menuDrawer.closeDrawers();
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
