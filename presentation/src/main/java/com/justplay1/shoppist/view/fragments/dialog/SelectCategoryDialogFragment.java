package com.justplay1.shoppist.view.fragments.dialog;

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
import com.justplay1.shoppist.presenter.SelectCategoryPresenter;
import com.justplay1.shoppist.view.SelectCategoryView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 01.02.2016.
 */
public class SelectCategoryDialogFragment extends BaseSelectItemDialogFragment<CategoryViewModel>
        implements SelectCategoryView {

    @Inject
    SelectCategoryPresenter mPresenter;
    private CategoryComponent mComponent;

    public static SelectCategoryDialogFragment newInstance() {
        return newInstance(null);
    }

    public static SelectCategoryDialogFragment newInstance(CategoryViewModel category) {
        Bundle args = new Bundle();
        args.putParcelable(CategoryViewModel.class.getName(), category);
        SelectCategoryDialogFragment fragment = new SelectCategoryDialogFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_category_dialog;
    }

    @Override
    public void init(View view) {
        super.init(view);
        mPositiveButton.setText(R.string.ok);
        getDialog().setTitle(R.string.change_category);

        mSelectView.setAddBtnVisibility(View.GONE);
        mSelectView.setEditBtnVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                mPresenter.onPositiveButtonClick(mSelectView.getSelectedItem());
                break;
            case R.id.negative_button:
                mPresenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void closeDialog() {
        dismiss();
    }

    @Override
    public void onComplete(CategoryViewModel category, boolean isUpdate) {
        if (mCompleteListener != null) {
            mCompleteListener.onComplete(category, isUpdate);
        }
    }

    @Override
    public void setCategory(List<CategoryViewModel> category) {
        mSelectView.setData(category);
    }

    @Override
    public void selectCategory(String id) {
        mSelectView.selectItem(id);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }
}
