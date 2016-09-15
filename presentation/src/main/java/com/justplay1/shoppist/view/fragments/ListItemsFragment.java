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

package com.justplay1.shoppist.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerListItemsComponent;
import com.justplay1.shoppist.di.components.ListItemsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.navigation.ListItemsRouter;
import com.justplay1.shoppist.presenter.ListItemsPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.ListItemsView;
import com.justplay1.shoppist.view.adapters.BaseListItemGroupAdapter;
import com.justplay1.shoppist.view.adapters.ListItemAdapter;
import com.justplay1.shoppist.view.component.recyclerview.DeleteSwipeResultListener;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseHeaderHolder;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemsFragment extends BaseEDSListFragment
        implements ShoppistRecyclerView.OnHeaderClickListener, BaseListItemGroupAdapter.SwipeEventListener<ListItemViewModel>,
        ShoppistRecyclerView.OnItemClickListener, View.OnClickListener, View.OnLongClickListener, ListItemsView, ListItemAdapter.NoteClickListener {

    @Inject
    ListItemsPresenter mPresenter;

    private ListItemsComponent mComponent;
    private ListItemAdapter mAdapter;

    public static ListItemsFragment newInstance(ListViewModel parentList) {
        Bundle args = new Bundle();
        args.putParcelable(ListViewModel.class.getName(), parentList);
        ListItemsFragment fragment = new ListItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.takeRouter((ListItemsRouter) getActivity());
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    protected void init(View view) {
        super.init(view);
        mActionButton.setOnLongClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.dropRouter((ListItemsRouter) getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.savePosition(mAdapter.getItems());
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerListItemsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .listItemsModule(new ListItemsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        mAdapter = new ListItemAdapter(getContext(), mActionModeInteractionListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
        mAdapter.setHeaderClickListener(this);
        mAdapter.setSwipeEventListener(this);
        mAdapter.setNoteClickListener(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                mPresenter.onAddButtonClick();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        mPresenter.onAddButtonLongClick();
        return true;
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        onExpandAll();
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        if (mPreferences.getLongItemClickAction() == 0) {
            holder.toggle();
        } else {
            mPresenter.onListItemLongClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
        }
        return true;
    }

    private void showListDialog(final boolean copy, List<ListViewModel> lists) {
//        ArrayList<Map<String, Object>> data = new ArrayList<>(lists.size());
//        Map<String, Object> m;
//        for (ListViewModel item : lists) {
//            if (item.getName().equals(mData.getName())) continue;
//            m = new HashMap<>();
//            m.put("name", item.getName());
//            m.put("object", item);
//            data.add(m);
//        }
//
//        String[] from = {"name"};
//        int[] to = {android.R.id.text1};
//
//        final SimpleAdapter adapter = new SimpleAdapter(getContext(), data, R.layout.dialog_list_item, from, to);
//        adapter.setDropDownViewResource(R.layout.dialog_list_item);
//
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_dialog, null, false);
//        final ListView listView = (ListView) view.findViewById(R.id.list_view);
//        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
//        listView.setAdapter(adapter);
//
//        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(final DialogInterface dialog, int which) {
//                switch (which) {
//                    case Dialog.BUTTON_POSITIVE:
//                        ListViewModel newList = (ListViewModel) (((Map<String, Object>) adapter.getItem(listView.getCheckedItemPosition())).get("object"));
//                        List<ListItemViewModel> items = new ArrayList<>(mAdapter.getCheckedItems());
//
//                        if (copy) {
//                            mPresenter.copyItems(newList, items);
//                            App.get().getShoppingListItemsManager().copyTo(newList, items,
//                                    new ExecutorListener<List<ListItemViewModel>>() {
//                                        @Override
//                                        public void start() {
//
//                                        }
//
//                                        @Override
//                                        public void complete(List<ListItemViewModel> result) {
//                                            dialog.dismiss();
//                                            mSelectItemsManager.closeActionMode();
//                                        }
//
//                                        @Override
//                                        public void error(Exception e) {
//
//                                        }
//                                    });
//                        } else {
//                            App.get().getShoppingListItemsManager().moveTo(newList, items,
//                                    new ExecutorListener<List<ListItemViewModel>>() {
//                                        @Override
//                                        public void start() {
//
//                                        }
//
//                                        @Override
//                                        public void complete(List<ListItemViewModel> result) {
//                                            dialog.dismiss();
//                                            mSelectItemsManager.closeActionMode(true);
//                                        }
//
//                                        @Override
//                                        public void error(Exception e) {
//
//                                        }
//                                    });
//                        }
//                        break;
//                    case Dialog.BUTTON_NEGATIVE:
//                        dialog.dismiss();
//                        break;
//                }
//            }
//        };
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setView(view);
//        builder.setTitle(R.string.title_activity_shopping_lists);
//        builder.setPositiveButton(R.string.choose, listener);
//        builder.setNegativeButton(R.string.cancel, listener);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
//        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }

    @Override
    public void onHeaderClick(BaseHeaderHolder holder, int position, long id) {

    }

    @Override
    public boolean onHeaderLongClick(BaseHeaderHolder holder, int position, long id) {
        return false;
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_item),
                () -> mAdapter.deleteCheckedView(deleteItems -> mPresenter.deleteItems(deleteItems)));
    }

    public void onReturnAllToListClick() {
        mPresenter.onReturnAllToListClick(mAdapter.getCheckedItems());
    }

    public void onStrikeOutAllClick() {
        mPresenter.onStrikeOutAllClick(mAdapter.getCheckedItems());
    }

    public void onExpandAll() {
        mRecyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAll() {
        mRecyclerViewExpandableItemManager.collapseAll();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems();
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onSortByNameClick() {
        mPresenter.onSortByNameClick();
    }

    public void onSortByPriorityClick() {
        mPresenter.onSortByPriorityClick();
    }

    public void onSortByCategoryClick() {
        mPresenter.onSortByCategoryClick();
    }

    public void onSortByTimeCreatedClick() {
        mPresenter.onSortByTimeCreatedClick();
    }

    public void onSortByManualClick() {
        mPresenter.onSortByManualClick();
    }

    @Override
    public void setManualSortModeEnable(boolean enable) {
        mAdapter.setManualSortModeEnable(enable);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmailShareDialog(String listName) {
        StringBuilder textToSend = new StringBuilder();
        textToSend.append(listName).append("\n").append("\n");
        textToSend.append(ShoppistUtils.buildShareString(mAdapter.getCheckedItems()));
        share(textToSend.toString(), getString(R.string.shopping_list));
    }

    protected void swipeDeleteConfirm(String message, final ListItemViewModel item,
                                      final DeleteSwipeResultListener<ListItemViewModel> resultListener) {
        if (mPreferences.isNeedShowConfirmDeleteDialog()) {
            showConfirmDeleteDialog(message, (dialog, which) -> {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        unCheckItem(item);
                        resultListener.onDelete(item);
                        dialog.dismiss();
                    case Dialog.BUTTON_NEGATIVE:
                        resultListener.onCancel(item);
                        dialog.dismiss();
                        break;
                }
            });
        } else {
            unCheckItem(item);
            resultListener.onDelete(item);
        }
    }

    private void unCheckItem(ListItemViewModel item) {
        if (item.isChecked()) {
            mAdapter.updateCount(false);
        }
    }

    @Override
    public void onChildItemRemoved(final ListItemViewModel removeItem, final int groupPosition, final int childPosition) {
        swipeDeleteConfirm(getString(R.string.delete_the_item), removeItem, new DeleteSwipeResultListener<ListItemViewModel>() {
            @Override
            public void onDelete(final ListItemViewModel deleteItem) {
                mPresenter.deleteItems(Collections.singletonList(deleteItem));
            }

            @Override
            public void onCancel(ListItemViewModel deleteItem) {
                deleteItem.setPinned(false);
                mRecyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
            }
        });
    }

    @Override
    public void onChildItemEdit(ListItemViewModel editItem, final int groupPosition, final int childPosition) {
        mPresenter.onChildItemEdit(editItem);
        mRecyclerView.postDelayed(() -> mRecyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition), 300);
    }

    @Override
    public void onChildItemMoved(ListItemViewModel moveItem) {
        mPresenter.onChildItemMoved(moveItem);
    }

    public void onCopyItemsClick() {

    }

    public void onMoveItemsClick() {

    }

    public void onEmailShareClick() {
        mPresenter.onEmailShareClick();
    }

    public void onEditItemClick() {

    }

    public boolean isMoveCopyButtonEnable() {
//        if (getDataSize() > 1) {
//            return true;
//        }
        return false;
    }

    public boolean isEditButtonEnable() {
        boolean editFlag = true;
        if (mAdapter.getCheckedCount() != 1) {
            editFlag = false;
        }
        return editFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !mAdapter.isAllItemsChecked();
    }

    public boolean isManualSortModeEnable() {
        return mAdapter.isManualSortModeEnable();
    }

    public void disableManualSort() {
        mAdapter.setManualSortModeEnable(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNoteClick(String note) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.note));
        builder.setMessage(note);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
