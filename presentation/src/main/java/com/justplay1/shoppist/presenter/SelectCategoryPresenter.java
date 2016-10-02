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

package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategoryList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.SelectCategoryView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class SelectCategoryPresenter extends BaseRxPresenter<SelectCategoryView, Router> {

    private final CategoryModelDataMapper mDataMapper;
    private final GetCategoryList mGetCategoryList;

    private CategoryViewModel mItem;

    @Inject
    SelectCategoryPresenter(CategoryModelDataMapper dataMapper, GetCategoryList getCategoryList) {
        this.mDataMapper = dataMapper;
        this.mGetCategoryList = getCategoryList;
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
        closeDialog();
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }


    public void loadCategories() {
        mSubscriptions.add(mGetCategoryList.get()
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
