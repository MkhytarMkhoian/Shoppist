package com.justplay1.shoppist.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.navigation.CategoryRouter;
import com.justplay1.shoppist.presenter.CategoryPresenter;
import com.justplay1.shoppist.view.CategoryView;
import com.justplay1.shoppist.view.adapters.CategoriesAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryFragment extends BaseListFragment<CategoryViewModel, CategoriesAdapter>
        implements CategoryView, ShoppistRecyclerView.OnItemClickListener<BaseItemHolder>, View.OnClickListener {

    @Inject
    CategoryPresenter presenter;

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();
        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachRouter((CategoryRouter) getActivity());
        presenter.attachView(this);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(CategoryComponent.class).inject(this);
    }

    @Override
    protected void initAdapter() {
        if (adapter != null) return;
        adapter = new CategoriesAdapter(getContext(), actionModeInteractionListener, recyclerView);
        adapter.setClickListener(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showData(List<CategoryViewModel> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        presenter.detachRouter();
    }

    @Override
    public void onClick(View v) {
        presenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        presenter.onListItemClick(adapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public boolean isDeleteButtonEnable() {
        boolean deleteFlag = true;
        for (CategoryViewModel category : adapter.getCheckedItems()) {
            if (category.getId().equals(CategoryViewModel.NO_CATEGORY_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !adapter.isAllItemsChecked();
    }

    public void onUnCheckAllItemsClick() {
        adapter.unCheckAllItems();
    }

    public void onCheckAllItemsClick() {
        adapter.checkAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_category),
                () -> adapter.deleteCheckedView(deleteItems -> presenter.deleteItems(deleteItems)));
    }
}
