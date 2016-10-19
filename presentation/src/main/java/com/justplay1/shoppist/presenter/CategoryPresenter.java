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

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.DeleteCategory;
import com.justplay1.shoppist.interactor.category.GetCategoryList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.navigation.CategoryRouter;
import com.justplay1.shoppist.presenter.base.BaseRouterPresenter;
import com.justplay1.shoppist.view.CategoryView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class CategoryPresenter extends BaseRouterPresenter<CategoryView, CategoryRouter> {

    private final BehaviorSubject<List<CategoryViewModel>> cache = BehaviorSubject.create();

    private final CategoryModelDataMapper dataMapper;
    private final GetCategoryList getCategoryList;
    private final DeleteCategory deleteCategory;

    @Inject
    CategoryPresenter(GetCategoryList getCategoryList, DeleteCategory deleteCategory,
                      CategoryModelDataMapper dataMapper) {
        this.getCategoryList = getCategoryList;
        this.deleteCategory = deleteCategory;
        this.dataMapper = dataMapper;

        loadData();
    }

    private void loadData() {
        getCategoryList.get()
                .map(dataMapper::transformToViewModel)
                .subscribe(cache);
    }

    @Override
    public void attachView(CategoryView view) {
        super.attachView(view);
        addSubscription(cache.subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoading();
            }

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

    public void deleteItems(Collection<CategoryViewModel> data) {
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(items -> deleteCategory.init(items).get())
                .subscribe(new DefaultSubscriber<>()));
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
