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

import android.content.Context;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.GoodsComponent;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.presenter.GoodsPresenter;
import com.justplay1.shoppist.view.GoodsView;
import com.justplay1.shoppist.view.adapters.GoodsAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectCategoryDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectUnitDialogFragment;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsFragment extends BaseExpandableListFragment<ProductViewModel, GoodsAdapter>
        implements ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, View.OnClickListener, GoodsView {

    @Inject
    GoodsPresenter presenter;

    public static GoodsFragment newInstance() {
        Bundle args = new Bundle();
        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.attachRouter((GoodsRouter) getActivity());
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
        getInjector(GoodsComponent.class).inject(this);
    }

    @Override
    protected void initAdapter() {
        adapter = new GoodsAdapter(getContext(), actionModeInteractionListener, recyclerView, preferences);
        adapter.setClickListener(this);
    }

    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        recyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);

        wrappedAdapter = recyclerViewExpandableItemManager.createWrappedAdapter(adapter);       // wrap for expanding

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        recyclerView.setAdapter(wrappedAdapter);  // requires *wrapped* adapter
        recyclerView.setItemAnimator(animator);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z1)));
        }

        recyclerViewExpandableItemManager.attachRecyclerView(recyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
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

    public void onChangeCategoryClick() {
        presenter.onChangeCategoryClick(adapter.getCheckedItems());
    }

    public void onChangeUnitClick() {
        presenter.onChangeUnitClick(adapter.getCheckedItems());
    }

    public void onCheckAllItemsClick() {
        adapter.checkAllItems();
    }

    public void onUnCheckAllItemsClick() {
        adapter.unCheckAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_goods),
                () -> adapter.deleteCheckedView(deleteItems -> presenter.deleteItems(deleteItems)));
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        presenter.onListItemClick(adapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public void onClick(View v) {
        presenter.onAddButtonClick();
    }

    @Override
    public void showChangeCategoryDialog(final List<ProductViewModel> editProducts) {
        FragmentManager fm = getFragmentManager();
        final SelectCategoryDialogFragment dialog = SelectCategoryDialogFragment.newInstance();
        dialog.setCompleteListener((category, isUpdate) -> presenter.changeCategory(category, editProducts));
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showChangeUnitDialog(final List<ProductViewModel> editProducts) {
        FragmentManager fm = getFragmentManager();
        final SelectUnitDialogFragment dialog = SelectUnitDialogFragment.newInstance();
        dialog.setCompleteListener((unit, isUpdate) -> presenter.changeUnit(unit, editProducts));
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showEditGoodsDialog(final ProductViewModel editProduct) {
        FragmentManager fm = getFragmentManager();
        AddGoodsDialogFragment dialog = AddGoodsDialogFragment.newInstance(editProduct);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ProductViewModel>>> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        recyclerViewExpandableItemManager.expandAll();
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
    public Context context() {
        return getContext();
    }

    public void onExpandAllClick() {
        recyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAllClick() {
        recyclerViewExpandableItemManager.collapseAll();
    }

    public void onSortByNameClick() {
        presenter.onSortByNameClick(adapter.getItems());
    }

    public void onSortByTimeCreatedClick() {
        presenter.onSortByTimeCreatedClick(adapter.getItems());
    }

    public void onSortByCategoryClick() {
        presenter.onSortByCategoryClick(adapter.getItems());
    }

    public void onSearchClick() {
        presenter.onSearchClick();
    }
}
