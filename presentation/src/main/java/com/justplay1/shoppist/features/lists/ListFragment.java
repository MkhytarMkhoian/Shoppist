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

package com.justplay1.shoppist.features.lists;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.shared.base.fragments.BaseEDSListFragment;
import com.justplay1.shoppist.shared.eventbus.ThemeUpdatedEvent;
import com.justplay1.shoppist.shared.eventbus.UiEventBus;
import com.justplay1.shoppist.shared.widget.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.shared.widget.recyclerview.holders.BaseItemHolder;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListFragment extends BaseEDSListFragment<ListViewModel, ListAdapter>
        implements ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, ListView, View.OnClickListener {

    @Inject
    ListPresenter presenter;
    private Subscription uiBusSubscription;

    public static ListFragment newInstance() {
        Bundle args = new Bundle();
        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.attachRouter((ListRouter) getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (uiBusSubscription != null) {
            uiBusSubscription.unsubscribe();
        }
        presenter.detachView();
        presenter.attachRouter((ListRouter) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        UiEventBus.instanceOf().filteredObservable(ThemeUpdatedEvent.class);
        uiBusSubscription = UiEventBus.instanceOf().observable().subscribe(o -> {
            actionButton.setBackgroundTintList(ColorStateList.valueOf(preferences.getColorPrimary()));
            ((MainActivity) getActivity()).refreshToolbarColor();
            ((MainActivity) getActivity()).setStatusBarColor();
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(ListsComponent.class).inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    protected void initAdapter() {
        adapter = new ListAdapter(getContext(), actionModeInteractionListener, recyclerView, preferences);
        adapter.setClickListener(this);
    }

    @Override
    public void onClick(View v) {
        presenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        presenter.onItemClick(adapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public void onSortByNameClick() {
        presenter.sortByName(adapter.getItems());
    }

    public void onSortByPriorityClick() {
        presenter.sortByPriority(adapter.getItems());
    }

    public void onSortByTimeCreatedClick() {
        presenter.sortByTimeCreated(adapter.getItems());
    }

    public void onExpandAllClick() {
        recyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAllClick() {
        recyclerViewExpandableItemManager.collapseAll();
    }

    @Override
    public void share(String share) {
        share(share, getString(R.string.shopping_list));
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        onExpandAllClick();
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
    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        progressDialog.dismiss();
    }

    public void onEditItemClick() {
        presenter.onEditItemClick(adapter.getCheckedItems().get(0));
    }

    public void onCheckAllItemsClick() {
        adapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        adapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_list),
                () -> adapter.deleteCheckedView(deleteItems -> presenter.deleteItems(deleteItems)));
    }

    public void onEmailShareClick() {
        presenter.emailShare(adapter.getCheckedItems());
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
}
