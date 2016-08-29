package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.ItemShadowDecorator;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerGoodsComponent;
import com.justplay1.shoppist.di.components.GoodsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.GoodsModule;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.presenter.GoodsPresenter;
import com.justplay1.shoppist.utils.AnimationResultListener;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.GoodsView;
import com.justplay1.shoppist.view.adapters.GoodsAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectCategoryDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectUnitDialogFragment;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 26.03.2016.
 */
public class GoodsFragment extends BaseExpandableListFragment
        implements ShoppistRecyclerView.OnItemClickListener, View.OnClickListener, GoodsView {

    @Inject
    GoodsPresenter mPresenter;

    private GoodsComponent mComponent;
    private GoodsAdapter mAdapter;
    private FloatingActionButton mActionButton;
    private GoodsFragmentInteractionListener mListener;

    public static GoodsFragment newInstance() {

        Bundle args = new Bundle();

        GoodsFragment fragment = new GoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (GoodsFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
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
    protected RecyclerView.Adapter getAdapter() {
        return mAdapter;
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
        mComponent = DaggerGoodsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .goodsModule(new GoodsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void initAdapter() {
        mAdapter = new GoodsAdapter(getContext(), mActionModeOpenCloseListener, mRecyclerView, mPreferences);
        mAdapter.setClickListener(this);
    }

    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        super.initRecyclerView(view, savedInstanceState);
        initAdapter();

        final Parcelable eimSavedState = (savedInstanceState != null) ? savedInstanceState.getParcelable(SAVED_STATE_EXPANDABLE_ITEM_MANAGER) : null;
        mRecyclerViewExpandableItemManager = new RecyclerViewExpandableItemManager(eimSavedState);

        mWrappedAdapter = mRecyclerViewExpandableItemManager.createWrappedAdapter(mAdapter);       // wrap for expanding

        final GeneralItemAnimator animator = new RefactoredDefaultItemAnimator();
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mRecyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) getResources().getDrawable(R.drawable.material_shadow_z1)));
        }

        mRecyclerViewExpandableItemManager.attachRecyclerView(mRecyclerView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_with_button;
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemLongClick(null);
        return true;
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

    @Override
    public void showDeleteDialog() {
//        showDeleteDialog(getString(R.string.delete_goods));
    }

    public void onChangeCategoryClick() {
        mPresenter.onChangeCategoryClick(mAdapter.getCheckedItems());
    }

    public void onChangeUnitClick() {
        mPresenter.onChangeUnitClick(mAdapter.getCheckedItems());
    }

    public void onCheckAllClick() {
        mAdapter.checkAllItems();
    }

    public void onUnCheckAllClick() {
        mAdapter.unCheckAllItems(true);
    }

    public void onDeleteClick() {
        showDeleteDialog();
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getChildItem(holder.groupPosition, holder.childPosition));
    }

    @Override
    public void onClick(View v) {
        mPresenter.onAddButtonClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.getStringExtra(Const.OLD_ID) != null) {
                String oldId = data.getStringExtra(Const.OLD_ID);
                String newId = data.getStringExtra(Const.NEW_ID);

                boolean checked = mAdapter.isItemChecked(oldId);
                mAdapter.deleteItemFromChecked(oldId);
                mAdapter.addToChecked(newId, checked);

                data.removeExtra(Const.NEW_ID);
                data.removeExtra(Const.OLD_ID);
            }
        }
    }

    @Override
    public void showChangeCategoryDialog(final List<ProductViewModel> editProducts) {
        FragmentManager fm = getFragmentManager();
        final SelectCategoryDialogFragment dialog = SelectCategoryDialogFragment.newInstance();
        dialog.setCompleteListener((category, isUpdate) -> mPresenter.changeCategory(category, editProducts));
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showChangeUnitDialog(final List<ProductViewModel> editProducts) {
        FragmentManager fm = getFragmentManager();
        final SelectUnitDialogFragment dialog = SelectUnitDialogFragment.newInstance();
        dialog.setCompleteListener((unit, isUpdate) -> mPresenter.changeUnit(unit, editProducts));
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showEditGoodsDialog(final ProductViewModel editProduct) {
        FragmentManager fm = getFragmentManager();
        AddGoodsDialogFragment dialog = AddGoodsDialogFragment.newInstance(editProduct);
        dialog.setCompleteListener(isUpdate -> {
            if (isUpdate) {
                boolean checked = mAdapter.isItemChecked(editProduct.getId());
                mAdapter.deleteItemFromChecked(editProduct.getId());
                mAdapter.addToChecked(editProduct.getId(), checked);
            }
        });
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }


    protected void deleteItem() {
        mAdapter.deleteCheckedView(new AnimationResultListener<ProductViewModel>() {
            @Override
            public void onAnimationEnd(Collection<ProductViewModel> deleteItems) {
                mPresenter.deleteItems(deleteItems);
            }
        });
    }

    @Override
    public void showData(List<Pair<HeaderViewModel, List<ProductViewModel>>> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
        mRecyclerViewExpandableItemManager.expandAll();
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
    public Context context() {
        return getContext();
    }

    public void onExpandAllClick() {
        mRecyclerViewExpandableItemManager.expandAll();
    }

    public void onCollapseAllClick() {
        mRecyclerViewExpandableItemManager.collapseAll();
    }

    public void onSortByNameClick() {
        mPresenter.onSortByNameClick();
    }

    public void onSortByTimeCreatedClick() {
        mPresenter.onSortByTimeCreatedClick();
    }

    public void onSortByCategoryClick() {
        mPresenter.onSortByCategoryClick();
    }

    public void onSearchClick() {
        mPresenter.onSearchClick();
    }

    @Override
    public void openSearchScreen() {
        mListener.openSearchScreen();
    }

    public interface GoodsFragmentInteractionListener {

        void openSearchScreen();
    }
}
