package com.justplay1.shoppist.view.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.fragments.ListFragment;
import com.justplay1.shoppist.view.fragments.MenuFragment;

public class MainActivity extends BaseListActivity
        implements MenuFragment.MenuFragmentInteraction, Toolbar.OnMenuItemClickListener,
        ListFragment.ListsFragmentInteractionListener {

    protected static final String FRAGMENT_TAG = "list_fragment";

    private MenuFragment mMenuFragment;
    private ListFragment mListFragment;

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mMenuDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMenuFragment = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);

        initToolbar();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();

        if (savedInstanceState != null) {
            mListFragment = (ListFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
        }
        if (mListFragment == null) {
            mListFragment = ListFragment.newInstance();
            if (mListFragment != null) {
                replaceFragment(R.id.container, mListFragment, FRAGMENT_TAG);
            }
        }
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

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!mMenuDrawer.isDrawerOpen(mMenuFragment.getView())) {
                mMenuDrawer.openDrawer(mMenuFragment.getView());
            } else {
                mMenuDrawer.closeDrawers();
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
        if (mListFragment.isManualSortModeEnable()) {
            mListFragment.disableManualSort();
        } else if (isActionModeShowing()) {
            closeActionMode();
        } else if (mMenuDrawer.isDrawerOpen(mMenuFragment.getView())) {
            mMenuDrawer.closeDrawers();
        } else {
            finishActivity(this);
        }
    }

    @Override
    public void openEditScreen(ListViewModel list) {
        mNavigator.navigateToAddListScreen(this, list);
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mListFragment.onCheckAllItemsClick();
                break;
            case R.id.action_settings:
                onSettingClick(0);
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
            case R.id.action_sort_manual_mode:
                mListFragment.onSortByManualClick();
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
    public void onCategoryClick() {
        mNavigator.navigateToCategoriesScreen(this);
    }

    @Override
    public void onCurrencyClick() {
        mNavigator.navigateToCurrencyScreen(this);
    }

    @Override
    public void onNotificationClick() {
        mNavigator.navigateToNotificationsScreen(this);
    }

    @Override
    public void onFeedbackClick() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.app_email)});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) +
                " Version: " + ShoppistUtils.getAppVersion(this));
        sendIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.send_mail_using)));
    }

    @Override
    public void onGoodsClick() {
        mNavigator.navigateToGoodsScreen(this);
    }

    @Override
    public void onUnitsClick() {
        mNavigator.navigateToUnitsScreen(this);
    }

    @Override
    public void onSettingClick(int settingId) {
        mNavigator.navigateToSettingScreen(this, settingId);
    }

    @Override
    public void onLoginClick() {
        mNavigator.navigateToSignInScreen(this);
    }
}
