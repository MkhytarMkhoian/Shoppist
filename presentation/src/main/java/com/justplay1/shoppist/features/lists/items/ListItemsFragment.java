/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.lists.items;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.ListItemsComponent;
import com.justplay1.shoppist.features.lists.items.move.MoveListItemsDialogFragment;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.shared.base.adapters.BaseGroupSwipeableItemAdapter;
import com.justplay1.shoppist.shared.base.fragments.BaseEDSListFragment;
import com.justplay1.shoppist.shared.widget.recyclerview.DeleteSwipeResultListener;
import com.justplay1.shoppist.shared.widget.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ShoppistUtils;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemsFragment extends BaseEDSListFragment<ListItemViewModel, ListItemAdapter>
        implements BaseGroupSwipeableItemAdapter.SwipeEventListener<ListItemViewModel>,
        ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, View.OnClickListener, View.OnLongClickListener,
        ListItemsView, ListItemAdapter.NoteClickListener {

    @Inject
    ListItemsPresenter presenter;

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
        presenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.attachRouter((ListItemsRouter) getActivity());
    }

    @Override
    protected void init(View view) {
        super.init(view);
        actionButton.setOnLongClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        presenter.detachRouter();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(ListItemsComponent.class).inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        adapter = new ListItemAdapter(getContext(), actionModeInteractionListener, recyclerView, preferences);
        adapter.setClickListener(this);
        adapter.setSwipeEventListener(this);
        adapter.setNoteClickListener(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        adapter.setExpandableItemManager(recyclerViewExpandableItemManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                presenter.onAddButtonClick();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        presenter.onAddButtonLongClick();
        return true;
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        onExpandAll();
    }

    @Override
    public void showLoading() {
        emptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        emptyView.hideProgressBar();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        presenter.onListItemClick(adapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    @Override
    public void openMoveGoodsDialog(ListViewModel currentList, List<ListItemViewModel> data) {
        openMoveGoodsDialog(currentList, data, false);
    }

    @Override
    public void openCopyGoodsDialog(ListViewModel currentList, List<ListItemViewModel> data) {
        openMoveGoodsDialog(currentList, data, true);
    }

    private void openMoveGoodsDialog(ListViewModel currentList, List<ListItemViewModel> data, boolean isCopy) {
        FragmentManager fm = getFragmentManager();
        MoveListItemsDialogFragment
            dialog = MoveListItemsDialogFragment.newInstance(currentList, data, isCopy);
        dialog.setCompleteListener(() -> actionModeInteractionListener.closeActionMode());
        dialog.show(fm, MoveListItemsDialogFragment.class.getName());
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_item),
                () -> adapter.deleteCheckedView(deleteItems -> presenter.deleteItems(deleteItems)));
    }

    public void onReturnCheckedItemsToListClick() {
        presenter.returnItemsToList(adapter.getCheckedItems());
    }

    public void onStrikeOutCheckedItemsClick() {
        presenter.strikeOutItems(adapter.getCheckedItems());
    }

    public void onReturnAllToListClick() {
        presenter.returnItemsToList(adapter.getItems());
    }

    public void onStrikeOutAllClick() {
        presenter.strikeOutItems(adapter.getItems());
    }

    public void onExpandAll() {
        recyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAll() {
        recyclerViewExpandableItemManager.collapseAll();
    }

    public void onUnCheckAllItemsClick() {
        adapter.unCheckAllItems();
    }

    public void onCheckAllItemsClick() {
        adapter.checkAllItems();
    }

    public void onSortByNameClick() {
        presenter.onSortByNameClick(adapter.getItems());
    }

    public void onSortByPriorityClick() {
        presenter.onSortByPriorityClick(adapter.getItems());
    }

    public void onSortByCategoryClick() {
        presenter.onSortByCategoryClick(adapter.getItems());
    }

    public void onSortByTimeCreatedClick() {
        presenter.onSortByTimeCreatedClick(adapter.getItems());
    }

    @Override
    public void showEmailShareDialog(String listName) {
        String textToSend = listName + "\n" + "\n" +
                ShoppistUtils.buildShareString(adapter.getCheckedItems());
        share(textToSend, getString(R.string.shopping_list));
    }

    protected void swipeDeleteConfirm(String message, final ListItemViewModel item,
                                      final DeleteSwipeResultListener<ListItemViewModel> resultListener) {
        if (preferences.isNeedShowConfirmDeleteDialog()) {
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
            adapter.updateCount(false);
        }
    }

    @Override
    public void onChildItemRemoved(final ListItemViewModel removeItem, final int groupPosition, final int childPosition) {
        swipeDeleteConfirm(getString(R.string.delete_the_item), removeItem, new DeleteSwipeResultListener<ListItemViewModel>() {
            @Override
            public void onDelete(final ListItemViewModel deleteItem) {
                presenter.deleteItems(Collections.singletonList(deleteItem));
            }

            @Override
            public void onCancel(ListItemViewModel deleteItem) {
                deleteItem.setPinned(false);
                recyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition);
            }
        });
    }

    @Override
    public void onChildItemEdit(ListItemViewModel editItem, final int groupPosition, final int childPosition) {
        presenter.onChildItemEdit(editItem);
        recyclerView.postDelayed(() -> recyclerViewExpandableItemManager.notifyChildItemChanged(groupPosition, childPosition), 300);
    }

    @Override
    public void onChildItemMoved(ListItemViewModel moveItem) {
        presenter.onChildItemMoved(moveItem);
    }

    public void onCopyItemsClick() {
        presenter.onCopyItemsClick(adapter.getCheckedItems());
    }

    public void onMoveItemsClick() {
        presenter.onMoveItemsClick(adapter.getCheckedItems());
    }

    public void onEmailShareClick() {
        presenter.onEmailShareClick();
    }

    public void onEditItemClick() {
        presenter.onEditItemClick(adapter.getCheckedItems().get(0));
    }

    public boolean isMoveCopyButtonEnable() {
        return true;
    }

    public boolean isEditButtonEnable() {
        boolean editFlag = true;
        if (adapter.getCheckedItemsCount() != 1) {
            editFlag = false;
        }
        return editFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !adapter.isAllItemsChecked();
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
