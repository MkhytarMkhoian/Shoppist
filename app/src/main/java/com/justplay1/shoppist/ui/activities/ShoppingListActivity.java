package com.justplay1.shoppist.ui.activities;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.BaseAdapter;
import com.justplay1.shoppist.adapters.ShoppingListAdapter;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.communication.sync.ShoppistAuthenticator;
import com.justplay1.shoppist.cursors.ShoppingListCursorLoader;
import com.justplay1.shoppist.database.DBHelper;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.actionmode.ActionModeType;
import com.justplay1.shoppist.ui.views.actionmode.ShoppingListActionModeCallback;
import com.justplay1.shoppist.ui.views.animboxes.SelectGroupItemsManager;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseDraggableItemViewHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class ShoppingListActivity extends BaseListActivity<ShoppingList, BaseHeaderHolder, BaseDraggableItemViewHolder>
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mMenuDrawer;
    private NavigationView mNavigationView;
    private MenuItem mClickedMenuItem;

    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        if (ParseUser.getCurrentUser() != null) {
//            checkout.start(this);
//            ServerRequests.isUserSubscriptionsValid(new ExecutorListener<Boolean>() {
//                @Override
//                public void start() {
//                }
//
//                @Override
//                public void complete(Boolean result) {
//                    App.isSyncAvailable = result;
//                }
//
//                @Override
//                public void error(Exception e) {
//                    e.printStackTrace();
//                }
//            });
            App.get().mAccount = new Account(ParseUser.getCurrentUser().getUsername(), ShoppistAuthenticator.ACCOUNT_TYPE);
        }
        if (ShoppistPreferences.isNeedShowRateDialog()) {
            DialogUtils.showRateDialog(this);
        }

        initToolbar();
        initFrame(savedInstanceState);
        createUpdateDBReceiver();

        mNavigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser() == null) {
                    ActivityUtils.startNextActivity(ShoppingListActivity.this, LoginActivity.class, null, null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(SettingsActivity.SETTING_ID, SettingsActivity.ACCOUNT_SETTING);
                    ActivityUtils.startNextActivity(ShoppingListActivity.this, SettingsActivity.class, null, bundle);
                }
            }
        });
    }

    protected void initFrame(Bundle savedInstanceState) {
        super.initFrame(savedInstanceState);

        mNavigationView = (NavigationView) findViewById(R.id.drawer_menu);
        mNavigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        mNavigationView.addHeaderView(header);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_shopping_lists);
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.shopping_list_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));

        mToolbar.getMenu().findItem(R.id.sort_by_category).setVisible(false);
        mToolbar.getMenu().findItem(R.id.action_strike_out_all).setVisible(false);
        mToolbar.getMenu().findItem(R.id.action_return_all_to_list).setVisible(false);

        mMenuDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenuDrawer.setStatusBarBackgroundColor(ShoppistPreferences.getColorPrimaryDark());
        mToggle = new ActionBarDrawerToggle(this, mMenuDrawer, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (mClickedMenuItem != null) {
                    clickBtn(mClickedMenuItem);
                    mClickedMenuItem = null;
                }
            }
        };
        mMenuDrawer.setDrawerListener(mToggle);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ShoppingListAdapter(this, null, mChangeObserver);
        mSelectItemsManager = new SelectGroupItemsManager<>(this, getActionModeCallback(), mRecyclerView, mAdapter);
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!mMenuDrawer.isDrawerOpen(mNavigationView)) {
                mMenuDrawer.openDrawer(mNavigationView);
            } else {
                mMenuDrawer.closeDrawers();
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isManualSortModeEnable() && ShoppistPreferences.isCloseManualSortModeWithBackButton()) {
            mAdapter.setManualSortModeEnable(false);
            mAdapter.notifyDataSetChanged();
        } else if (mSelectItemsManager.isActionModeShowing()) {
            mSelectItemsManager.closeActionMode();
        } else if (mMenuDrawer.isDrawerOpen(mNavigationView)) {
            mMenuDrawer.closeDrawers();
        } else {
            ActivityUtils.finishActivity(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                ActivityUtils.startNextActivityForResult(this, AddListActivity.class, 0, mAddBtn, null);
                break;
        }
    }

    protected void clickBtn(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.goods_button:
                ActivityUtils.startNextActivity(this, GoodsActivity.class, null, null);
                break;
            case R.id.category_button:
                ActivityUtils.startNextActivity(this, CategoriesActivity.class, null, null);
                break;
            case R.id.currencies_button:
                ActivityUtils.startNextActivity(this, CurrencyActivity.class, null, null);
                break;
            case R.id.units_button:
                ActivityUtils.startNextActivity(this, UnitsActivity.class, null, null);
                break;
            case R.id.settings_button:
                ActivityUtils.startNextActivity(this, SettingsActivity.class, null, null);
                break;
            case R.id.feedback_button:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.app_email)});
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) +
                        " Version: " + ShoppistUtils.getAppVersion(this));
                sendIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(sendIntent, getString(R.string.send_mail_using)));
                break;
            case R.id.help_button:
                ActivityUtils.startNextActivity(this, MainHelpActivity.class, null, null);
                break;
            case R.id.notifications:
                ActivityUtils.startNextActivity(this, NotificationActivity.class, null, null);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (mMenuDrawer.isDrawerOpen(mNavigationView)) {
            mClickedMenuItem = menuItem;
            mMenuDrawer.closeDrawers();
        }
        return true;
    }

    @Override
    public boolean onMenuItemClick(final MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.action_help:
                ActivityUtils.startNextActivity(this, ShoppingListsHelpActivity.class, mRecyclerView, null);
                break;
            case R.id.action_synchronization:
                break;
            case R.id.action_settings:
                ActivityUtils.startNextActivity(this, SettingsActivity.class, mRecyclerView, null);
                break;
            case R.id.sort_by_name:
                ShoppistPreferences.setManualSortEnableForShoppingLists(false);
                ShoppistPreferences.setSortForShoppingLists(BaseAdapter.SORT_BY_NAME);
                mAdapter.sortByName();
                break;
            case R.id.sort_by_priority:
                ShoppistPreferences.setManualSortEnableForShoppingLists(false);
                ShoppistPreferences.setSortForShoppingLists(BaseAdapter.SORT_BY_PRIORITY);
                mAdapter.sortByPriority();
                break;
            case R.id.sort_by_time_created:
                ShoppistPreferences.setManualSortEnableForShoppingLists(false);
                ShoppistPreferences.setSortForShoppingLists(BaseAdapter.SORT_BY_TIME_CREATED);
                mAdapter.sortByTimeCreated();
                break;
            case R.id.action_sort_manual_mode:
                if (mAdapter.isManualSortModeEnable()) {
                    mAdapter.setManualSortModeEnable(false);
                } else {
                    mAdapter.setManualSortModeEnable(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_expand_all:
                mRecyclerViewExpandableItemManager.expandAll();
                break;
            case R.id.menu_collapse_all:
                mRecyclerViewExpandableItemManager.collapseAll();
                break;
        }
        return true;
    }

    @Override
    public void onHeaderClick(BaseHeaderHolder holder, int position, long id) {

    }

    @Override
    public boolean onHeaderLongClick(BaseHeaderHolder holder, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString(ShoppingListItemActivity.PARENT_LIST_ID, mAdapter.getChildItem(holder.groupPosition, holder.childPosition).getId());
        ActivityUtils.startNextActivityForResult(this, ShoppingListItemActivity.class, 0, holder.itemView, bundle);
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        if (ShoppistPreferences.getLongItemClickAction() == 0) {
            return super.onItemLongClick(holder, position, id);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(ShoppingListItemActivity.PARENT_LIST_ID, mAdapter.getChildItem(holder.groupPosition, holder.childPosition).getId());
            ActivityUtils.startNextActivityForResult(this, AddListActivity.class, 0, mRecyclerView, bundle);
            return true;
        }
    }

    @Override
    public void onItemClick(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_edit:
                if (mSelectItemsManager.getCheckedCount() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ShoppingList.class.getName(), mAdapter.getCheckedItems().iterator().next());
                    ActivityUtils.startNextActivityForResult(this, AddListActivity.class, 0, mRecyclerView, bundle);
                }
                break;
            case R.id.menu_delete:
                showDeleteDialog(getString(R.string.delete_the_list));
                break;
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.action_email_share:
                ThreadExecutor.doBackgroundTaskAsync(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        StringBuilder textToSend = new StringBuilder();
                        for (ShoppingList shoppingList : mAdapter.getCheckedItems()) {
                            textToSend.append(shoppingList.getName()).append("\n").append("\n");
                            List<ShoppingListItem> items = App.get().getTablesHolder().getShoppingListItemTable().getShoppingListItems(shoppingList.getId());
                            textToSend.append(ShoppistUtils.buildShareString(items));
                        }
                        return textToSend.toString();
                    }
                }, new ExecutorListener<String>() {
                    @Override
                    public void start() {
                        mProgressDialog.show();
                    }

                    @Override
                    public void complete(String result) {
                        mProgressDialog.dismiss();
                        share(result, getString(R.string.shopping_list));
                    }

                    @Override
                    public void error(Exception e) {
                        mProgressDialog.dismiss();
                    }
                });
                break;
            case R.id.action_share:

                break;
            case R.id.menu_uncheck_all:
                mSelectItemsManager.unCheckAllItems(true);
                break;
        }
    }

    @Override
    public void updateTheme() {
        if (ShoppistPreferences.isNeedUpdateTheme(getActivityClassName())) {
            mAdapter.notifyDataSetChanged();
        }
        super.updateTheme();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ShoppistPreferences.isManualSortEnableForShoppingLists()) {
            final List<ShoppingList> tmp = mAdapter.getItems();
            final int size = tmp.size();
            for (int i = 0; i < size; i++) {
                tmp.get(i).setPosition(i);
            }

            App.get().getShoppingListsManager().updateAll(tmp,
                    new ExecutorListener<Collection<ShoppingList>>() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void complete(Collection<ShoppingList> result) {

                        }

                        @Override
                        public void error(Exception e) {

                        }
                    });
        }

        unregisterReceiver(receiver);
    }

    @Override
    protected void deleteItem() {
        mSelectItemsManager.deleteCheckedView(new AnimationResultListener<ShoppingList>() {
            @Override
            public void onAnimationEnd(Collection<ShoppingList> deleteItems) {
                App.get().getShoppingListsManager().deleteAll(deleteItems, new ExecutorListener<Collection<ShoppingList>>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void complete(Collection<ShoppingList> result) {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
            }
        });
    }

    @Override
    protected String getActivityClassName() {
        return ShoppingListActivity.class.getName();
    }

    @Override
    protected ShoppingListActionModeCallback getActionModeCallback() {
        return new ShoppingListActionModeCallback(this, ActionModeType.SHOPPING_LIST, this);
    }


    @Override
    public void onCreateActionMode(ActionMode actionMode, Menu menu) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
            mEmptyView.showProgressBar();
        }
        return new ShoppingListCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEmptyView.hideProgressBar();
        mAdapter.changeCursor(data);
        mRecyclerViewExpandableItemManager.expandAll();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void loadData() {
        if (getSupportLoaderManager().getLoader(getMainCursorLoaderId()) == null) {
            getSupportLoaderManager().initLoader(getMainCursorLoaderId(), ActivityUtils.getBundleWithFlag(1), this);
        } else {
            getSupportLoaderManager().restartLoader(getMainCursorLoaderId(), ActivityUtils.getBundleWithFlag(0), this);
        }
    }

    @Override
    protected int getMainCursorLoaderId() {
        return ShoppingListCursorLoader.ID;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DBHelper.START_UPDATE_TO_VERSION_5);
        filter.addAction(DBHelper.FINISH_UPDATE_TO_VERSION_5);
        registerReceiver(receiver, filter);


        TextView username = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.username);
        if (ParseUser.getCurrentUser() != null) {
            if (!ParseUser.getCurrentUser().getUsername().equals(username.getText())) {
                username.setText(ParseUser.getCurrentUser().getUsername());
            }
        } else {
            username.setText(R.string.tap_to_sing_up_or_login);
        }

        App.get().getNotificationsManager().getNewNotificationCount(ShoppistPreferences.getLastUserSeenNotificationsTime(),
                new ExecutorListener<Integer>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void complete(Integer result) {
                        TextView view = (TextView) mNavigationView.getMenu().findItem(R.id.notifications).getActionView();
                        view.setText(result > 0 ? String.valueOf(result) : null);
                    }

                    @Override
                    public void error(Exception e) {
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra(ActivityUtils.NEED_FINISH_FLAG, false)) {
                data.removeExtra(ActivityUtils.NEED_FINISH_FLAG);
                finish();
            }
        }
    }

    public void createUpdateDBReceiver() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(DBHelper.START_UPDATE_TO_VERSION_5)) {
                    mProgressDialog.show();
                } else if (intent.getAction().equals(DBHelper.FINISH_UPDATE_TO_VERSION_5)) {
                    mProgressDialog.dismiss();
                }
            }
        };
    }
}
