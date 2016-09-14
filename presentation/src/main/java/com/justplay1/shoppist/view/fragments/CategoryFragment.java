package com.justplay1.shoppist.view.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.di.components.DaggerCategoryComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.presenter.CategoryPresenter;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.view.CategoryView;
import com.justplay1.shoppist.view.adapters.CategoriesAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;

import java.util.Collection;
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
    private FloatingActionButton mActionButton;
    private RecyclerViewDragDropManager mRecyclerViewDragDropManager;
    private CategoriesAdapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private CategoryFragmentListener mListener;

    public static CategoryFragment newInstance() {

        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CategoryFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
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
        mActionButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
        mActionButton.setOnClickListener(this);
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
        mAdapter = new CategoriesAdapter(getContext(), mActionModeInteractionListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
    }

    @Override
    public void openAddCategoryScreen(CategoryViewModel category) {
        mListener.openAddCategoryScreen(category);
    }

    @Override
    public void setManualSortEnable(boolean manualSortEnable) {
        mAdapter.setManualSortModeEnable(manualSortEnable);
        mAdapter.notifyDataSetChanged();
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
    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);

        // drag & drop manager
        mRecyclerViewDragDropManager = new RecyclerViewDragDropManager();
        mRecyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z3));

        mWrappedAdapter = mRecyclerViewDragDropManager.createWrappedAdapter(mAdapter);
        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z1)));
        }

        mRecyclerViewDragDropManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    public void onPause() {
        mRecyclerViewDragDropManager.cancelDrag();
        super.onPause();
        mPresenter.savePosition(mAdapter.getItemsWithoutHeaders());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();

        if (mRecyclerViewDragDropManager != null) {
            mRecyclerViewDragDropManager.release();
            mRecyclerViewDragDropManager = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onSortByNameClick() {
        mPresenter.onSortByName();
    }

    public void onSortByManualClick() {
        if (mAdapter.isManualSortModeEnable()) {
            mAdapter.setManualSortModeEnable(false);
        } else {
            mAdapter.setManualSortModeEnable(true);
        }
        mAdapter.notifyDataSetChanged();
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
        mAdapter.deleteCheckedView(new AnimationResultListener<CategoryViewModel>() {
            @Override
            public void onAnimationEnd(Collection<CategoryViewModel> deleteItems) {

            }
        });
//        showDeleteDialog(getString(R.string.delete_the_category));
    }

    public boolean isManualSortModeEnable() {
        return mAdapter.isManualSortModeEnable();
    }

    public void disableManualSort() {
        mAdapter.setManualSortModeEnable(false);
        mAdapter.notifyDataSetChanged();
    }

    public interface CategoryFragmentListener {

        void openAddCategoryScreen(CategoryViewModel category);
    }
}
