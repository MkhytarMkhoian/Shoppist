package com.justplay1.shoppist.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.di.components.DaggerCategoryComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
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
public class CategoryFragment extends BaseListFragment
        implements CategoryView, ShoppistRecyclerView.OnItemClickListener, View.OnClickListener {

    @Inject
    CategoryPresenter mPresenter;

    private CategoryComponent mComponent;
    private CategoriesAdapter mAdapter;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.takeRouter((CategoryRouter) getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerCategoryComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .categoryModule(new CategoryModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new CategoriesAdapter(getContext(), mActionModeInteractionListener, mRecyclerView);
        mAdapter.setClickListener(this);
    }

    @Override
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showData(List<CategoryViewModel> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
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
        mPresenter.dropRouter((CategoryRouter) getActivity());
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        holder.toggle();
        return true;
    }

    public boolean isDeleteButtonEnable() {
        boolean deleteFlag = true;
        for (CategoryViewModel category : mAdapter.getCheckedItems()) {
            if (category.getId().equals(CategoryViewModel.NO_CATEGORY_ID)) {
                deleteFlag = false;
            }
        }
        return deleteFlag;
    }

    public boolean isCheckAllButtonEnable() {
        return !mAdapter.isAllItemsChecked();
    }

    public void onUnCheckAllItemsClick() {
        mAdapter.unCheckAllItems();
    }

    public void onCheckAllItemsClick() {
        mAdapter.checkAllItems();
    }

    public void onDeleteCheckedItemsClick() {
        deleteItems(getString(R.string.delete_the_category),
                () -> mAdapter.deleteCheckedView(deleteItems -> mPresenter.deleteItems(deleteItems)));
    }

    public boolean isManualSortModeEnable() {
        return mAdapter.isManualSortModeEnable();
    }

    public void disableManualSort() {
        mAdapter.setManualSortModeEnable(false);
        mAdapter.notifyDataSetChanged();
    }
}
