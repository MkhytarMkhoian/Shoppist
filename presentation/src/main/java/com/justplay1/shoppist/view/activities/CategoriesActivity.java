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
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.view.fragments.CategoryFragment;

/**
 * Created by Mkhitar on 13.11.2014.
 */
public class CategoriesActivity extends SingleListFragmentActivity<CategoryFragment>
        implements Toolbar.OnMenuItemClickListener, CategoryFragment.CategoryFragmentListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, CategoriesActivity.class);
    }

    @Override
    public CategoryFragment createFragment() {
        return CategoryFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_category);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishActivity());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.category_list_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
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
            case R.id.action_sort_manual_mode:
                mFragment.onSortByManualClick();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mFragment.isManualSortModeEnable()) {
            mFragment.disableManualSort();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void openAddCategoryScreen(CategoryViewModel category) {
        mNavigator.navigateToAddCategoryScreen(this, category);
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
                mFragment.onCheckAllItemsClick();
                break;
            case R.id.menu_uncheck_all:
                mFragment.onUnCheckAllItemsClick();
                break;
            case R.id.menu_delete:
                mFragment.onDeleteCheckedItemsClick();
                break;
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        super.onPrepareActionMode(actionMode, menu);
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
