package com.justplay1.shoppist.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.adapters.BaseAdapter;
import com.justplay1.shoppist.adapters.ShoppingListItemAdapter;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.cursors.ShoppingListItemsLoader;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.actionmode.ActionModeType;
import com.justplay1.shoppist.ui.views.actionmode.ShoppingListActionModeCallback;
import com.justplay1.shoppist.ui.views.animboxes.SelectGroupItemsManager;
import com.justplay1.shoppist.ui.views.recyclerview.DeleteSwipeResultListener;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseDraggableSwipeableItemViewHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhitar on 20.11.2014.
 */
public class ShoppingListItemActivity extends BaseListItemActivity<ShoppingListItem, ShoppingList, BaseHeaderHolder, BaseDraggableSwipeableItemViewHolder>
        implements View.OnLongClickListener, LoaderManager.LoaderCallbacks<Map<String, Object>> {

    public static final String PARENT_LIST_ID = "parent_list_id";
    private String mParentListId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_item);

        if (getIntent() != null) {
            Bundle data = getIntent().getBundleExtra(ActivityUtils.DATA);
            if (data != null) {
                mParentListId = data.getString(PARENT_LIST_ID);
            }
        }
        initFrame(savedInstanceState);
        initToolbar();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        if (mData == null) {
            mToolbar.getMenu().findItem(R.id.menu_sort).setEnabled(true);
            mToolbar.getMenu().findItem(R.id.action_menu).setEnabled(true);
        }
    }

    @Override
    protected void initFrame(Bundle savedInstanceState) {
        super.initFrame(savedInstanceState);
        mAddBtn.setOnLongClickListener(this);
    }

    @Override
    public void onChildItemRemoved(final ShoppingListItem removeItem, final int groupPosition, final int childPosition) {
        swipeDeleteConfirm(getString(R.string.delete_the_item), removeItem, new DeleteSwipeResultListener<ShoppingListItem>() {
            @Override
            public void onDelete(final ShoppingListItem deleteItem) {
                App.get().getShoppingListItemsManager().deleteAll(Collections.singletonList(deleteItem),
                        new ExecutorListener<Collection<ShoppingListItem>>() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void complete(Collection<ShoppingListItem> result) {

                            }

                            @Override
                            public void error(Exception e) {

                            }
                        });
            }

            @Override
            public void onCancel(ShoppingListItem deleteItem) {
                deleteItem.setPinned(false);
                mRecyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
            }
        });
    }

    @Override
    public void onChildItemEdit(ShoppingListItem editItem, final int groupPosition, final int childPosition) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShoppingList.class.getName(), mData);
        bundle.putParcelable(ShoppingListItem.class.getName(), editItem);
        ActivityUtils.startNextActivityForResult(this, AddListItemActivity.class, 0, mRecyclerView, bundle);

        editItem.setPinned(false);
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRecyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
            }
        }, 300);
    }

    @Override
    public void onChildItemMoved(ShoppingListItem moveItem) {
        moveItem.setDirty(true);
        moveItem.setTimestamp(System.currentTimeMillis());
        if (moveItem.getStatus() == Status.DONE) {
            moveItem.setStatus(Status.NOT_DONE);
        } else {
            moveItem.setStatus(Status.DONE);
        }
        App.get().getShoppingListItemsManager().update(moveItem,
                new ExecutorListener<ShoppingListItem>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void complete(ShoppingListItem result) {

                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ShoppingListItemAdapter(ShoppingListItemActivity.this, null, mChangeObserver);
        mSelectItemsManager = new SelectGroupItemsManager<>(this, getActionModeCallback(), mRecyclerView, mAdapter);
    }

    @Override
    protected String getActivityClassName() {
        return ShoppingListItemActivity.class.getName();
    }

    @Override
    protected int getDataSize() {
        return App.get().getShoppingListsManager().getShoppingLists().size();
    }

    @Override
    protected ShoppingListActionModeCallback getActionModeCallback() {
        return new ShoppingListActionModeCallback(this, ActionModeType.SHOPPING_LIST_ITEM, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ShoppistPreferences.isManualSortEnableForShoppingListItems()) {
            final List<ShoppingListItem> tmp = mAdapter.getItems();
            final int size = tmp.size();
            for (int i = 0; i < size; i++) {
                tmp.get(i).setPosition(i);
            }
            App.get().getShoppingListItemsManager().updateAll(tmp, tmp,
                    new ExecutorListener<List<ShoppingListItem>>() {
                        @Override
                        public void start() {

                        }

                        @Override
                        public void complete(List<ShoppingListItem> result) {

                        }

                        @Override
                        public void error(Exception e) {

                        }
                    });
        }
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.action_help:
                ActivityUtils.startNextActivity(this, ShoppingListItemsHelpActivity.class, mRecyclerView, null);
                break;
            case R.id.action_synchronization:
                break;
            case R.id.sort_by_name:
                ShoppistPreferences.setManualSortEnableForShoppingListItems(false);
                mAdapter.sortByName();
                ShoppistPreferences.setSortForShoppingListItems(BaseAdapter.SORT_BY_NAME);
                break;
            case R.id.sort_by_priority:
                ShoppistPreferences.setManualSortEnableForShoppingListItems(false);
                mAdapter.sortByPriority();
                ShoppistPreferences.setSortForShoppingListItems(BaseAdapter.SORT_BY_PRIORITY);
                break;
            case R.id.sort_by_time_created:
                ShoppistPreferences.setManualSortEnableForShoppingListItems(false);
                mAdapter.sortByTimeCreated();
                ShoppistPreferences.setSortForShoppingListItems(BaseAdapter.SORT_BY_TIME_CREATED);
                break;
            case R.id.sort_by_category:
                ShoppistPreferences.setManualSortEnableForShoppingListItems(false);
                mAdapter.sortByCategory();
                ShoppistPreferences.setSortForShoppingListItems(BaseAdapter.SORT_BY_CATEGORIES);
                break;
            case R.id.action_sort_manual_mode:
                if (mAdapter.isManualSortModeEnable()) {
                    mAdapter.setManualSortModeEnable(false);
                } else {
                    mAdapter.setManualSortModeEnable(true);
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.action_return_all_to_list:
                strikeOut(mAdapter.getItems(), false);
                break;
            case R.id.action_strike_out_all:
                strikeOut(mAdapter.getItems(), true);
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

    private void strikeOut(List<ShoppingListItem> items, boolean toShoppingCart) {
        List<ShoppingListItem> itemsToMove = new ArrayList<>();
        for (ShoppingListItem item : items) {
            if (toShoppingCart) {
                if (item.getStatus() == Status.NOT_DONE) {
                    item.setStatus(Status.DONE);
                    itemsToMove.add(item);
                }
            } else {
                if (item.getStatus() == Status.DONE) {
                    item.setStatus(Status.NOT_DONE);
                    itemsToMove.add(item);
                }
            }
            item.setDirty(true);
            item.setTimestamp(System.currentTimeMillis());
        }
        if (itemsToMove.size() == 0) return;
        App.get().getShoppingListItemsManager().updateAll(itemsToMove, itemsToMove,
                new ExecutorListener<List<ShoppingListItem>>() {
                    @Override
                    public void start() {

                    }

                    @Override
                    public void complete(List<ShoppingListItem> result) {
                    }

                    @Override
                    public void error(Exception e) {
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                clickAction(false);
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        clickAction(true);
        return true;
    }

    private void clickAction(boolean isLongClick) {
        switch (ShoppistPreferences.getAddButtonClickAction()) {
            case 0:
                if (!isLongClick) {
                    openStandardMode();
                } else {
                    openQuickMode();
                }
                break;
            case 1:
                if (!isLongClick) {
                    openQuickMode();
                } else {
                    openStandardMode();
                }
                break;
        }
    }

    private void openStandardMode() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShoppingList.class.getName(), mData);
        ActivityUtils.startNextActivityForResult(this, AddListItemActivity.class, 0, mAddBtn, bundle);
    }

    private void openQuickMode() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(ShoppingList.class.getName(), mData);
        intent.putExtra(SearchActivity.CONTEXT_TYPE, SearchActivity.CONTEXT_QUICK_ADD_GOODS_TO_LIST);
        ActivityUtils.startSearchActivity(this, intent);
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShoppingList.class.getName(), mData);
        bundle.putParcelable(ShoppingListItem.class.getName(), mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
        ActivityUtils.startNextActivityForResult(this, AddListItemActivity.class, 0, mRecyclerView, bundle);
    }

    @Override
    public void onHeaderClick(BaseHeaderHolder holder, int position, long id) {

    }

    @Override
    public boolean onHeaderLongClick(BaseHeaderHolder holder, int position, long id) {
        return false;
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
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        if (ShoppistPreferences.getLongItemClickAction() == 0) {
            return super.onItemLongClick(holder, position, id);
        } else {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ShoppingList.class.getName(), mData);
            bundle.putParcelable(ShoppingListItem.class.getName(), mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
            ActivityUtils.startNextActivityForResult(this, AddListItemActivity.class, 0, mRecyclerView, bundle);
            return true;
        }
    }

    @Override
    public void onItemClick(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_edit:
                if (mSelectItemsManager.getCheckedCount() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ShoppingList.class.getName(), mData);
                    bundle.putParcelable(ShoppingListItem.class.getName(), mAdapter.getCheckedItems().get(0));
                    ActivityUtils.startNextActivityForResult(this, AddListItemActivity.class, 0, mRecyclerView, bundle);
                }
                break;
            case R.id.menu_delete:
                showDeleteDialog(getString(R.string.delete_the_item));
                break;
            case R.id.menu_check_all:
                mSelectItemsManager.checkAllItems();
                break;
            case R.id.action_move:
                showListDialog(false);
                break;
            case R.id.action_copy:
                showListDialog(true);
                break;
            case R.id.action_synchronization:
                break;
            case R.id.action_email_share:
                StringBuilder textToSend = new StringBuilder();
                textToSend.append(mData.getName()).append("\n").append("\n");
                textToSend.append(ShoppistUtils.buildShareString(mAdapter.getCheckedItems()));
                share(textToSend.toString(), getString(R.string.shopping_list));
                break;
            case R.id.menu_uncheck_all:
                mSelectItemsManager.unCheckAllItems(true);
                break;
            case R.id.action_return_to_list:
                strikeOut(mAdapter.getCheckedItems(), false);
                break;
            case R.id.action_strike_out:
                strikeOut(mAdapter.getCheckedItems(), true);
                break;
        }
    }

    @Override
    protected void deleteItem() {
        mSelectItemsManager.deleteCheckedView(new AnimationResultListener<ShoppingListItem>() {
            @Override
            public void onAnimationEnd(Collection<ShoppingListItem> deleteItems) {
                App.get().getShoppingListItemsManager().deleteAll(deleteItems,
                        new ExecutorListener<Collection<ShoppingListItem>>() {
                            @Override
                            public void start() {

                            }

                            @Override
                            public void complete(Collection<ShoppingListItem> result) {

                            }

                            @Override
                            public void error(Exception e) {

                            }
                        });
            }
        });
    }

    private void showListDialog(final boolean copy) {
        List<ShoppingList> lists = new ArrayList<>(App.get().getShoppingListsManager().getShoppingLists());

        ArrayList<Map<String, Object>> data = new ArrayList<>(lists.size());
        Map<String, Object> m;
        for (ShoppingList item : lists) {
            if (item.getName().equals(mData.getName())) continue;
            m = new HashMap<>();
            m.put("name", item.getName());
            m.put("object", item);
            data.add(m);
        }

        String[] from = {"name"};
        int[] to = {android.R.id.text1};

        final SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.dialog_list_item, from, to);
        adapter.setDropDownViewResource(R.layout.dialog_list_item);

        View view = getLayoutInflater().inflate(R.layout.list_dialog, null, false);
        final ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ShoppingList newList = (ShoppingList) (((Map<String, Object>) adapter.getItem(listView.getCheckedItemPosition())).get("object"));
                        List<ShoppingListItem> items = new ArrayList<>(mAdapter.getCheckedItems());

                        if (copy) {
                            App.get().getShoppingListItemsManager().copyTo(newList, items,
                                    new ExecutorListener<List<ShoppingListItem>>() {
                                        @Override
                                        public void start() {

                                        }

                                        @Override
                                        public void complete(List<ShoppingListItem> result) {
                                            dialog.dismiss();
                                            mSelectItemsManager.closeActionMode();
                                        }

                                        @Override
                                        public void error(Exception e) {

                                        }
                                    });
                        } else {
                            App.get().getShoppingListItemsManager().moveTo(newList, items,
                                    new ExecutorListener<List<ShoppingListItem>>() {
                                        @Override
                                        public void start() {

                                        }

                                        @Override
                                        public void complete(List<ShoppingListItem> result) {
                                            dialog.dismiss();
                                            mSelectItemsManager.closeActionMode(true);
                                        }

                                        @Override
                                        public void error(Exception e) {

                                        }
                                    });
                        }
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setTitle(R.string.title_activity_shopping_lists);
        builder.setPositiveButton(R.string.choose, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    @Override
    public Loader<Map<String, Object>> onCreateLoader(int id, Bundle args) {
        int flag = ActivityUtils.getFlagFromBundle(args);
        if (flag == 1) {
            mEmptyView.showProgressBar();
        }
        return new ShoppingListItemsLoader(this, mParentListId);
    }

    @Override
    public void onLoadFinished(Loader<Map<String, Object>> loader, Map<String, Object> data) {
        mEmptyView.hideProgressBar();
        ShoppingListItemAdapter adapter = (ShoppingListItemAdapter) mAdapter;
        if (mData == null) {
            if (data.get(ShoppingList.class.getName()) != null) {
                mData = (ShoppingList) data.get(ShoppingList.class.getName());
            }
            mToolbar.setTitle(mData.getName());
            mToolbar.getMenu().findItem(R.id.menu_sort).setEnabled(true);
            mToolbar.getMenu().findItem(R.id.action_menu).setEnabled(true);
        }

        adapter.setDefaultCategory((Category) data.get(Category.class.getName()));
        adapter.setDefaultCurrency((Currency) data.get(Currency.class.getName()));
        adapter.setDefaultUnit((Unit) data.get(Unit.class.getName()));
        if (data.get(Cursor.class.getName()) != null) {
            adapter.changeCursor((Cursor) data.get(Cursor.class.getName()));
            mRecyclerViewExpandableItemManager.expandAll();
        } else {
            adapter.invalidData();
        }
    }

    @Override
    public void onLoaderReset(Loader<Map<String, Object>> loader) {
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
        return ShoppingListItemsLoader.ID;
    }
}
