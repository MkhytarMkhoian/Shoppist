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
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.fragments.ListItemsFragment;

/**
 * Created by Mkhitar on 20.11.2014.
 */
public class ListItemActivity extends SingleListFragmentActivity<ListItemsFragment>
        implements Toolbar.OnMenuItemClickListener, ListItemsFragment.ListItemsFragmentInteractionListener {

    private ListViewModel mParentList;

    public static Intent getCallingIntent(Context context, ListViewModel parentList) {
        Intent callingIntent = new Intent(context, ListItemActivity.class);
        callingIntent.putExtra(ListViewModel.class.getName(), parentList);
        return callingIntent;
    }

    @Override
    public ListItemsFragment createFragment() {
        return ListItemsFragment.newInstance(mParentList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        if (getIntent() != null) {
            mParentList = getIntent().getParcelableExtra(ListViewModel.class.getName());
        }
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mParentList.getName());
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishWithResult());
        mToolbar.inflateMenu(R.menu.shopping_list_toolbar);
        mToolbar.getMenu().findItem(R.id.action_settings).setVisible(false);
//        if (mData == null) {
//            mToolbar.getMenu().findItem(R.id.menu_sort).setEnabled(true);
//            mToolbar.getMenu().findItem(R.id.action_menu).setEnabled(true);
//        }
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
            case R.id.action_sort_manual_mode:
                mFragment.onSortByManualClick();
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
                mFragment.onReturnAllToListClick();
                break;
            case R.id.action_strike_out:
                mFragment.onStrikeOutAllClick();
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
    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void openStandardMode(ListViewModel list, ListItemViewModel item) {
        mNavigator.navigateToAddListItemScreen(this, list, item);
    }

    @Override
    public void openQuickMode(String parentListId) {
        mNavigator.navigateToSearchScreen(this, Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST, parentListId);
    }

    @Override
    public void onBackPressed() {
        if (mFragment.isManualSortModeEnable()) {
            mFragment.disableManualSort();
        } else if (isActionModeShowing()) {
            closeActionMode();
        } else {
            finishWithResult();
        }
    }

//    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> data) {
//
//        ListItemAdapter adapter = (ListItemAdapter) mAdapter;
//        if (mData == null) {
//            if (data.get(ListViewModel.class.getName()) != null) {
//                mData = (ListViewModel) data.get(ListViewModel.class.getName());
//            }
//            mToolbar.setTitle(mData.getName());
//            mToolbar.getMenu().findItem(R.id.menu_sort).setEnabled(true);
//            mToolbar.getMenu().findItem(R.id.action_menu).setEnabled(true);
//        }
//        if (data.get(Cursor.class.getName()) != null) {
//            adapter.changeCursor((Cursor) data.get(Cursor.class.getName()));
//            mRecyclerViewExpandableItemManager.expandAll();
//        } else {
//            adapter.invalidData();
//        }
//    }

    private void finishWithResult() {
        Intent data = new Intent();
        data.putExtra(Const.NEW_DATA, true);
        finishActivityWithResult(this, RESULT_OK, data);
    }
}
