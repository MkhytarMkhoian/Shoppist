package com.justplay1.shoppist.ui.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.adapters.CurrencyAdapter;
import com.justplay1.shoppist.cursors.CurrenciesCursorLoader;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.ui.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.CurrencyEditorDialogFragment;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;
import com.justplay1.shoppist.ui.views.actionmode.ActionModeType;
import com.justplay1.shoppist.ui.views.actionmode.ShoppingListActionModeCallback;
import com.justplay1.shoppist.ui.views.animboxes.SelectItemsManager;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;

import java.util.Collection;

/**
 * Created by Mkhytar on 30.01.2016.
 */
public class CurrencyActivity extends BaseActivity<Currency> implements LoaderManager.LoaderCallbacks<Cursor>{

    private CurrencyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        initializeToolbar();
        initFrame(savedInstanceState);
    }

    protected void initFrame(Bundle savedInstanceState) {
        super.initFrame(savedInstanceState);
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.please_wait));

    }

    private void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.currency);
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.finishActivity(CurrencyActivity.this);
            }
        });
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.units_and_currency_menu);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    protected void initRecyclerView(Bundle savedInstanceState) {
        super.initRecyclerView(savedInstanceState);
        initAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setClickListener(this);

        loadData();
    }

    @Override
    protected int getMainCursorLoaderId() {
        return CurrenciesCursorLoader.ID;
    }

    @Override
    protected String getActivityClassName() {
        return CurrencyActivity.class.getName();
    }

    @Override
    protected ShoppingListActionModeCallback getActionModeCallback() {
        return new ShoppingListActionModeCallback(this, ActionModeType.CURRENCY, this);
    }

    @Override
    protected void deleteItem() {
        mSelectItemsManager.deleteCheckedView(new AnimationResultListener<Currency>() {
            @Override
            public void onAnimationEnd(Collection<Currency> deleteItems) {
                App.get().getCurrenciesManager().deleteAll(deleteItems,
                        new ExecutorListener<Collection<Currency>>() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void complete(Collection<Currency> result) {

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
        mAdapter = new CurrencyAdapter(this, null, mChangeObserver);
        mSelectItemsManager = new SelectItemsManager<>(this, getActionModeCallback(), mRecyclerView, mAdapter);
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
        }
    }

    @Override
    public void onCreateActionMode(ActionMode actionMode, Menu menu) {

    }

    @Override
    public void onPrepareActionMode(ActionMode actionMode, Menu menu) {
        super.onPrepareActionMode(actionMode, menu);
        MenuItem delete = menu.findItem(R.id.menu_delete);
        boolean deleteFlag = true;
        for (Currency currency : mAdapter.getCheckedItems()) {
            if (currency.getId().equals(Currency.NO_CURRENCY_ID)) {
                deleteFlag = false;
            }
        }
        delete.setVisible(deleteFlag);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
            mEmptyView.showProgressBar();
        }
        return new CurrenciesCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        mEmptyView.hideProgressBar();
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
    public void onClick(View v) {
        showCurrencyDialog(null);
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        showCurrencyDialog(mAdapter.getItem(position));
    }

    private void showCurrencyDialog(final Currency editCurrency) {
        FragmentManager fm = getSupportFragmentManager();
        CurrencyEditorDialogFragment dialog = CurrencyEditorDialogFragment.newInstance(editCurrency);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Currency>() {
            @Override
            public void onComplete(Currency item, boolean isUpdate) {
                getSupportLoaderManager().restartLoader(getMainCursorLoaderId(), null, CurrencyActivity.this);
            }
        });
        dialog.show(fm, CurrencyEditorDialogFragment.class.getName());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
        }
        return true;
    }
}
