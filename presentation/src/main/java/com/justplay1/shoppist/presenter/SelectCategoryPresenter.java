package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategories;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SelectCategoryView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 04.07.2016.
 */
public class SelectCategoryPresenter extends BaseRxPresenter<SelectCategoryView> {

    private final CategoryModelDataMapper mDataMapper;
    private final GetCategories mGetCategories;

    private CategoryViewModel mItem;

    @Inject
    public SelectCategoryPresenter(CategoryModelDataMapper dataMapper, GetCategories getCategories) {
        this.mDataMapper = dataMapper;
        this.mGetCategories = getCategories;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(CategoryViewModel.class.getName());
        } else if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(CategoryViewModel.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(CategoryViewModel.class.getName(), mItem);
    }

    public void init() {
        loadCategories();
    }

    public void onPositiveButtonClick(CategoryViewModel category) {
        onComplete(category, false);
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }


    public void loadCategories() {
        mSubscriptions.add(mGetCategories.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {

                    @Override
                    public void onNext(List<CategoryViewModel> category) {
                        setCategory(category);
                        if (mItem != null) {
                            selectCategory(mItem.getCategory().getId());
                        } else {
                            selectCategory(CategoryViewModel.NO_CATEGORY_ID);
                        }
                    }
                }));
    }

    private void setCategory(List<CategoryViewModel> category) {
        if (isViewAttached()) {
            getView().setCategory(category);
        }
    }

    private void selectCategory(String id) {
        if (isViewAttached()) {
            getView().selectCategory(id);
        }
    }

    private void onComplete(CategoryViewModel category, boolean isUpdate) {
        if (isViewAttached()) {
            getView().onComplete(category, isUpdate);
        }
    }

    private void closeDialog() {
        if (isViewAttached()) {
            getView().closeDialog();
        }
    }
}