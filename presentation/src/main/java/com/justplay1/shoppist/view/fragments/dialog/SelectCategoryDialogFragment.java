package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
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

    public SelectCategoryDialogFragment() {
        // Empty constructor required for DialogFragment
    }

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
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_category_dialog, container);
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
