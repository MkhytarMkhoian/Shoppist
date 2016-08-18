package com.justplay1.shoppist.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
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
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.BaseAdapter;
import com.justplay1.shoppist.adapters.GoodsAdapter;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.cursors.GoodsLoader;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.GoodsEditorDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.SelectCategoryDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.SelectUnitDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.UnitsEditorDialogFragment;
import com.justplay1.shoppist.ui.views.actionmode.ActionModeType;
import com.justplay1.shoppist.ui.views.actionmode.ShoppingListActionModeCallback;
import com.justplay1.shoppist.ui.views.animboxes.SelectGroupItemsManager;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 25.09.2015.
 */
public class GoodsActivity extends BaseActivity<Product> implements ShoppistRecyclerView.OnItemClickListener,
        View.OnClickListener, Toolbar.OnMenuItemClickListener, LoaderManager.LoaderCallbacks<Map<String, Object>>{

    protected static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    private GoodsAdapter mAdapter;
    private RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        initToolbar();
        initFrame(savedInstanceState);
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.goods);
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.inflateMenu(R.menu.goods_toolbar);
        mToolbar.setOnMenuItemClickListener(this);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //TODO
        mToolbar.getMenu().findItem(R.id.menu_reset).setVisible(false);
    }

    protected void initRecyclerView(Bundle savedInstanceState) {
        super.initRecyclerView(savedInstanceState);
        initAdapter();

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(mAdapter);       // wrap for expanding

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z1)));
        }

        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);

        mAdapter.setClickListener(this);
        mAdapter.setRecyclerViewExpandableItemManager(mRecyclerViewExpandableItemManager);

        loadData();
    }

    @Override
    protected int getMainCursorLoaderId() {
        return GoodsLoader.ID;
    }

    @Override
    protected String getActivityClassName() {
        return GoodsActivity.class.getName();
    }

    @Override
    protected ShoppingListActionModeCallback getActionModeCallback() {
        return new ShoppingListActionModeCallback(this, ActionModeType.GOODS_LIST, this);
    }

    @Override
    protected void deleteItem() {
        mSelectItemsManager.deleteCheckedView(new AnimationResultListener<Product>() {
            @Override
            public void onAnimationEnd(Collection<Product> deleteItems) {
                App.get().getProductsManager().deleteAll(deleteItems,
                        new ExecutorListener<Collection<Product>>() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void complete(Collection<Product> result) {

                            }

                            @Override
                            public void error(Exception e) {

                            }
                        });
            }
        });
    }

    @Override
    protected void initAdapter() {
        mAdapter = new GoodsAdapter(this, null, mChangeObserver);
        mSelectItemsManager = new SelectGroupItemsManager<>(this, getActionModeCallback(), mRecyclerView, mAdapter);
    }

    @Override
    public void onItemClick(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_delete:
                showDeleteDialog(getString(R.string.delete_goods));
                break;
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.menu_uncheck_all:
                mSelectItemsManager.unCheckAllItems(true);
                break;
            case R.id.menu_change_category:
                showChangeCategoryDialog(mAdapter.getCheckedItems());
                break;
            case R.id.menu_change_unit:
                showChangeUnitDialog(mAdapter.getCheckedItems());
                break;
        }
    }

    @Override
    public void onCreateActionMode(ActionMode actionMode, Menu menu) {

    }

    private void showChangeCategoryDialog(final List<Product> editProducts) {
        FragmentManager fm = getSupportFragmentManager();
        final SelectCategoryDialogFragment dialog = SelectCategoryDialogFragment.newInstance();
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Category>() {
            @Override
            public void onComplete(Category category, boolean isUpdate) {
                for (Product product : editProducts) {
                    if (!category.equals(product.getCategory())) {
                        product.setCategory(category);
                        category.setTimestamp(product.getTimestamp());
                        category.setDirty(true);
                    }
                }
                App.get().getProductsManager().updateAll(editProducts, new ExecutorListener<List<Product>>() {
                    @Override
                    public void start() {
                        mProgressDialog.show();
                    }

                    @Override
                    public void complete(List<Product> result) {
                        mProgressDialog.dismiss();
                        dialog.dismiss();
                    }

                    @Override
                    public void error(Exception e) {
                        mProgressDialog.dismiss();
                        showError(e);
                    }
                });
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    private void showChangeUnitDialog(final List<Product> editProducts) {
        FragmentManager fm = getSupportFragmentManager();
        final SelectUnitDialogFragment dialog = SelectUnitDialogFragment.newInstance();
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Unit>() {
            @Override
            public void onComplete(Unit unit, boolean isUpdate) {
                for (Product product : editProducts) {
                    if (!unit.equals(product.getUnit())) {
                        product.setUnit(unit);
                        unit.setTimestamp(product.getTimestamp());
                        unit.setDirty(true);
                    }
                }
                App.get().getProductsManager().updateAll(editProducts, new ExecutorListener<List<Product>>() {
                    @Override
                    public void start() {
                        mProgressDialog.show();
                    }

                    @Override
                    public void complete(List<Product> result) {
                        mProgressDialog.dismiss();
                        dialog.dismiss();
                    }

                    @Override
                    public void error(Exception e) {
                        mProgressDialog.dismiss();
                        showError(e);
                    }
                });
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    private void showEditGoodsDialog(final Product editProduct) {
        FragmentManager fm = getSupportFragmentManager();
        GoodsEditorDialogFragment dialog = GoodsEditorDialogFragment.newInstance(editProduct);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Product>() {
            @Override
            public void onComplete(Product item, boolean isUpdate) {
                if (isUpdate) {
                    boolean checked = mSelectItemsManager.isItemChecked(item.getId());
                    mSelectItemsManager.deleteItemFromChecked(item.getId());
                    mSelectItemsManager.addToChecked(item.getId(), checked);
                }
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    private void showError(Exception e) {
        DialogUtils.showErrorDialog(this,
                ShoppistUtils.getParseMessageFromException(this, e.getMessage()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save current state to support screen rotation, etc...
        if (mRecyclerViewExpandableItemManager != null) {
            outState.putParcelable(
                    SAVED_STATE_EXPANDABLE_ITEM_MANAGER,
                    mRecyclerViewExpandableItemManager.getSavedState());
        }
    }

    @Override
    public void onDestroy() {
        if (mRecyclerViewExpandableItemManager != null) {
            mRecyclerViewExpandableItemManager.release();
            mRecyclerViewExpandableItemManager = null;
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
        mLayoutManager = null;
        super.onDestroy();
    }

    @Override
    public Loader<Map<String, Object>> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
            mEmptyView.showProgressBar();
        }
        switch (id) {
            case GoodsLoader.ID:
                return new GoodsLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> data) {
        switch (loader.getId()) {
            case GoodsLoader.ID:
                mAdapter.setDefaultCategory((Category) data.get(Category.class.getName()));
                mAdapter.setDefaultUnit((Unit) data.get(Unit.class.getName()));
                mAdapter.changeCursor((Cursor) data.get(Cursor.class.getName()));
                mRecyclerViewExpandableItemManager.expandAll();
                mEmptyView.hideProgressBar();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Map<String, Object>> loader) {

    }

    @Override
    protected void loadData() {
        if (getSupportLoaderManager().getLoader(getMainCursorLoaderId()) == null) {
            getSupportLoaderManager().initLoader(getMainCursorLoaderId(), ActivityUtils.getBundleWithFlag(1), this).forceLoad();
        } else {
            getSupportLoaderManager().restartLoader(getMainCursorLoaderId(), ActivityUtils.getBundleWithFlag(0), this).forceLoad();
        }
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        showEditGoodsDialog(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public void onClick(View v) {
        showEditGoodsDialog(null);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_name:
                mAdapter.sortByName();
                ShoppistPreferences.setSortForGoods(BaseAdapter.SORT_BY_NAME);
                break;
            case R.id.sort_by_time_created:
                mAdapter.sortByTimeCreated();
                ShoppistPreferences.setSortForGoods(BaseAdapter.SORT_BY_TIME_CREATED);
                break;
            case R.id.sort_by_category:
                mAdapter.sortByCategory();
                ShoppistPreferences.setSortForGoods(BaseAdapter.SORT_BY_CATEGORIES);
                break;
            case R.id.menu_search:
                Intent intent = new Intent(this, SearchActivity.class);
                intent.putExtra(SearchActivity.CONTEXT_TYPE, SearchActivity.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
                ActivityUtils.startSearchActivity(this, intent);
                break;
            case R.id.menu_reset:
                showConfirmDialog();
                break;
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
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

    private void showConfirmDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        App.get().getProductsManager().reset(GoodsActivity.this, new ExecutorListener<List<Product>>() {
                            @Override
                            public void start() {
                                dialog.dismiss();
                                mProgressDialog.show();
                            }

                            @Override
                            public void complete(List<Product> result) {
                                mProgressDialog.dismiss();
                            }

                            @Override
                            public void error(Exception e) {
                                mProgressDialog.dismiss();
                                DialogUtils.showErrorDialog(GoodsActivity.this, e.toString());
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
        builder.setMessage(getString(R.string.action_reset_goods) + "?");
        builder.setPositiveButton(R.string.reset, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }
}
