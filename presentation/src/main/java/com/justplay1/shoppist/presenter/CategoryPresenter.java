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

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.DeleteCategory;
import com.justplay1.shoppist.interactor.category.GetCategoryList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.navigation.CategoryRouter;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.view.CategoryView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class CategoryPresenter extends BaseRxPresenter<CategoryView, CategoryRouter> {

    private final CategoryModelDataMapper mDataMapper;
    private final GetCategoryList mGetCategoryList;
    private final DeleteCategory mDeleteCategory;

    @Inject
    public CategoryPresenter(GetCategoryList getCategoryList, DeleteCategory deleteCategory,
                             CategoryModelDataMapper dataMapper) {
        this.mGetCategoryList = getCategoryList;
        this.mDeleteCategory = deleteCategory;
        this.mDataMapper = dataMapper;
    }

    public void init() {
        loadData();
    }

    public void onAddButtonClick() {
        if (hasRouter()) {
            getRouter().openEditCategoryScreen(null);
        }
    }

    public void onListItemClick(CategoryViewModel category) {
        if (hasRouter()) {
            getRouter().openEditCategoryScreen(category);
        }
    }

    public void loadData() {
        showLoading();
        mSubscriptions.add(mGetCategoryList.get()
                .map(mDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {
                    @Override
                    public void onNext(List<CategoryViewModel> categoryViewModels) {
                        hideLoading();
                        showData(categoryViewModels);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hideLoading();
                        e.printStackTrace();
                    }
                }));
    }

    public void deleteItems(Collection<CategoryViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> mDataMapper.transform(data))
                .flatMap(items -> {
                    mDeleteCategory.setData(items);
                    return mDeleteCategory.get();
                }).subscribe(new DefaultSubscriber<Boolean>()));
    }

    private void showData(List<CategoryViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void showLoading() {
        if (isViewAttached()) {
            getView().showLoading();
        }
    }

    private void hideLoading() {
        if (isViewAttached()) {
            getView().hideLoading();
        }
    }
}
