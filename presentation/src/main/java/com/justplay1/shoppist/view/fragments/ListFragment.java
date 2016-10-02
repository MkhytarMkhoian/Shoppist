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
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.bus.ThemeUpdatedEvent;
import com.justplay1.shoppist.bus.UiEventBus;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.navigation.ListRouter;
import com.justplay1.shoppist.presenter.ListPresenter;
import com.justplay1.shoppist.view.ListView;
import com.justplay1.shoppist.view.activities.MainActivity;
import com.justplay1.shoppist.view.adapters.ListAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListFragment extends BaseEDSListFragment
        implements ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, ListView, View.OnClickListener {

    @Inject
    ListPresenter mPresenter;

    private Subscription mUiBusSubscription;

    private ListAdapter mAdapter;

    public static ListFragment newInstance() {
        Bundle args = new Bundle();

        ListFragment fragment = new ListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.attachRouter((ListRouter) getActivity());
        mPresenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUiBusSubscription != null){
            mUiBusSubscription.unsubscribe();
        }
        mPresenter.detachView();
        mPresenter.attachRouter((ListRouter) getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        UiEventBus.instanceOf().filteredObservable(ThemeUpdatedEvent.class);
        mUiBusSubscription = UiEventBus.instanceOf().observable().subscribe(o -> {
            mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
            ((MainActivity) getActivity()).refreshToolbarColor();
            ((MainActivity) getActivity()).setStatusBarColor();
            mAdapter.notifyDataSetChanged();
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
        mAdapter = new ListAdapter(getContext(), mActionModeInteractionListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onItemClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public void onSortByNameClick() {
        mPresenter.sortByName(mAdapter.getItems());
    }

    public void onSortByPriorityClick() {
        mPresenter.sortByPriority(mAdapter.getItems());
    }

    public void onSortByTimeCreatedClick() {
        mPresenter.sortByTimeCreated(mAdapter.getItems());
    }

    public void onExpandAllClick() {
        mRecyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAllClick() {
        mRecyclerViewExpandableItemManager.collapseAll();
    }

    @Override
    public void share(String share) {
        share(share, getString(R.string.shopping_list));
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        onExpandAllClick();
    }

    @Override
    public void showRateDialog() {
        showRateDialog(getContext());
    }

    @Override
    public void showLoading() {
        mEmptyView.showProgressBar();
    }

    @Override
    public void hideLoading() {
        mEmptyView.hideProgressBar();
    }

    @Override
    public void showLoadingDialog() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
        mProgressDialog.dismiss();
    }

    public void onEditItemClick() {
        mPresenter.onEditItemClick(mAdapter.getCheckedItems().get(0));
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_list),
                () -> mAdapter.deleteCheckedView(deleteItems -> mPresenter.deleteItems(deleteItems)));
    }

    public void onEmailShareClick() {
        mPresenter.emailShare(mAdapter.getCheckedItems());
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

    private void showRateDialog(final Context context) {
        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        context.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        context.startActivity(intent);
                    }
                    mPreferences.setNeedShowRateDialog(31);
                    dialog.dismiss();
                case Dialog.BUTTON_NEGATIVE:
                    mPreferences.setNeedShowRateDialog(31);
                    dialog.dismiss();
                    break;
                case Dialog.BUTTON_NEUTRAL:
                    mPreferences.setNeedShowRateDialog(0);
                    dialog.dismiss();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.rate_shoppist);
        builder.setMessage(R.string.rate_info);
        builder.setNeutralButton(R.string.no_later, listener);
        builder.setPositiveButton(R.string.rate, listener);
        builder.setNegativeButton(R.string.no, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEUTRAL).setTextColor(mPreferences.getColorPrimary());
    }
}
