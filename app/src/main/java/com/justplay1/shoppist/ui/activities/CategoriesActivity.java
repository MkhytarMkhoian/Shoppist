package com.justplay1.shoppist.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.CategoriesAdapter;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.cursors.CategoriesCursorLoader;
import com.justplay1.shoppist.adapters.BaseWithoutHeaderAdapter;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;
import com.justplay1.shoppist.ui.views.actionmode.ActionModeType;
import com.justplay1.shoppist.ui.views.actionmode.ShoppingListActionModeCallback;
import com.justplay1.shoppist.ui.views.animboxes.SelectItemsManager;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Mkhitar on 13.11.2014.
 */
public class CategoriesActivity extends BaseActivity<Category> implements LoaderManager.LoaderCallbacks<Cursor>{

    protected RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    protected BaseWithoutHeaderAdapter<Category> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        initToolbar();
        initFrame(savedInstanceState);
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.title_activity_category);
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity(CategoriesActivity.this);
            }
        });
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.category_list_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));

        //TODO
        mToolbar.getMenu().findItem(R.id.menu_reset).setVisible(false);
    }

    protected void initFrame(Bundle savedInstanceState) {
        super.initFrame(savedInstanceState);
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.please_wait));
    }

    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();

        if (ShoppistPreferences.isManualSortEnableForCategories()) {
            final int size = mAdapter.getItems().size();
            for (int i = 0; i < size; i++) {
                mAdapter.getItem(i).setPosition(i);
            }

            App.get().getCategoriesManager().updateAll(mAdapter.getItems(),
                    new ExecutorListener<Collection<Category>>() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void complete(Collection<Category> result) {

                        }

                        @Override
                        public void error(Exception e) {

                        }
                    });
        }
    }

    @Override
    protected void initAdapter() {
        mAdapter = new CategoriesAdapter(this, null, mChangeObserver);
        mSelectItemsManager = new SelectItemsManager<>(this, getActionModeCallback(), mRecyclerView, mAdapter);
    }

    @Override
    protected void initRecyclerView(Bundle savedInstanceState) {
        super.initRecyclerView(savedInstanceState);
        initAdapter();

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z3));

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mAdapter);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z1)));
        }

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
        mAdapter.setClickListener(this);
        loadData();
    }

    @Override
    protected int getMainCursorLoaderId() {
        return CategoriesCursorLoader.ID;
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.menu_reset:
                showConfirmDialog();
                break;
            case R.id.action_help:
                ActivityUtils.startNextActivity(this, CategoriesHelpActivity.class, mRecyclerView, null);
                break;
            case R.id.sort_by_name:
                mAdapter.sortByName();
                ShoppistPreferences.setSortForCategories(BaseWithoutHeaderAdapter.SORT_BY_NAME);
                ShoppistPreferences.setManualSortEnableForCategories(false);
                break;
            case R.id.action_sort_manual_mode:
                if (mAdapter.isManualSortModeEnable()) {
                    mAdapter.setManualSortModeEnable(false);
                } else {
                    mAdapter.setManualSortModeEnable(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public void updateTheme() {
        super.updateTheme();
        mProgressDialog.refreshColor();
    }

    @Override
    public void onClick(View v) {
        ActivityUtils.startNextActivityForResult(this, AddCategoryActivity.class, 0, mAddBtn, null);
    }

    @Override
    public void onItemClick(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.menu_uncheck_all:
                mSelectItemsManager.unCheckAllItems(true);
                break;
            case R.id.action_enable:
                setCategoryEnable(true);
                mSelectItemsManager.closeActionMode();
                break;
            case R.id.action_disable:
                setCategoryEnable(false);
                mSelectItemsManager.closeActionMode();
                break;
            case R.id.menu_delete:
                showDeleteDialog(getString(R.string.delete_the_category));
                break;
            case R.id.action_help:
                ActivityUtils.startNextActivity(this, CategoriesHelpActivity.class, mRecyclerView, null);
                break;
        }
    }

    @Override
    protected void deleteItem() {
        mSelectItemsManager.deleteCheckedView(new AnimationResultListener<Category>() {
            @Override
            public void onAnimationEnd(Collection<Category> deleteItems) {
                App.get().getCategoriesManager().deleteAll(deleteItems, new ExecutorListener<Collection<Category>>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void complete(Collection<Category> result) {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
            }
        });
    }

    private void setCategoryEnable(boolean enable) {
        List<Category> changedCategory = new ArrayList<>();

        for (Category category : mAdapter.getCheckedItems()) {
            if (category.isEnable() != enable) {
                category.setEnable(enable);
                category.setDirty(true);
                changedCategory.add(category);
            }
        }
        if (changedCategory.size() > 0) {
            App.get().getCategoriesManager().updateAll(changedCategory, new ExecutorListener<Collection<Category>>() {
                @Override
                public void start() {

                }

                @Override
                public void complete(Collection<Category> result) {
                }

                @Override
                public void error(Exception e) {

                }
            });
        }
    }

    @Override
    public void onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuItem delete = menu.findItem(R.id.menu_delete);
        boolean deleteFlag = true;
        boolean disableFlag = true;
        for (Category category : mAdapter.getCheckedItems()) {
            if (category.getId().equals(Category.NO_CATEGORY_ID)) {
                disableFlag = false;
                deleteFlag = false;
            }
        }
        delete.setVisible(deleteFlag);
        menu.findItem(R.id.action_disable).setVisible(disableFlag);
        menu.findItem(R.id.action_enable).setVisible(disableFlag);

        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        if (mSelectItemsManager.getCheckedCount() == mAdapter.getCountWithoutHeaders()) {
            checkAll.setEnabled(false);
        } else {
            checkAll.setEnabled(true);
        }
    }

    @Override
    public void onCreateActionMode(ActionMode actionMode, Menu menu) {

    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Category.class.getName(), mAdapter.getItem(position));
        ActivityUtils.startNextActivityForResult(this, AddCategoryActivity.class, 0, holder.itemView, bundle);
    }

    private void showConfirmDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        App.get().getCategoriesManager().reset(CategoriesActivity.this, new ExecutorListener<List<Category>>() {
                            @Override
                            public void start() {
                                dialog.dismiss();
                                mProgressDialog.show();
                            }

                            @Override
                            public void complete(List<Category> result) {
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void error(Exception e) {
                                mProgressDialog.dismiss();
                                DialogUtils.showErrorDialog(CategoriesActivity.this, e.toString());
                            }
                        });
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.action_reset_categories) + "?");
        builder.setPositiveButton(R.string.reset, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    @Override
    protected String getActivityClassName() {
        return CategoriesActivity.class.getName();
    }

    @Override
    protected ShoppingListActionModeCallback getActionModeCallback() {
        return new ShoppingListActionModeCallback(this, ActionModeType.CATEGORIES, this);
    }

    @Override
    public void onBackPressed() {
        if (mAdapter.isManualSortModeEnable() && ShoppistPreferences.isCloseManualSortModeWithBackButton()) {
            mAdapter.setManualSortModeEnable(false);
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
            mEmptyView.showProgressBar();
        }
        return new CategoriesCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mEmptyView.hideProgressBar();
        mAdapter.changeCursor(data);
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
}
