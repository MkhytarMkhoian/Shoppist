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
import com.justplay1.shoppist.interactor.category.GetCategories;
import com.justplay1.shoppist.interactor.category.UpdateCategory;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ItemType;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.CategoryView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class CategoryPresenter extends BaseRxPresenter<CategoryView> {

    private final CategoryModelDataMapper mDataMapper;
    private final ShoppistPreferences mPreferences;
    private final UpdateCategory mUpdateCategory;
    private final GetCategories mGetCategories;
    private final DeleteCategory mDeleteCategory;

    @Inject
    public CategoryPresenter(ShoppistPreferences preferences, UpdateCategory updateCategory,
                             GetCategories getCategories, DeleteCategory deleteCategory,
                             CategoryModelDataMapper dataMapper) {
        this.mPreferences = preferences;
        this.mUpdateCategory = updateCategory;
        this.mGetCategories = getCategories;
        this.mDeleteCategory = deleteCategory;
        this.mDataMapper = dataMapper;
    }

    public void init() {
        loadData();
    }

    public void onSortByName() {
        mPreferences.setSortForCategories(SortType.SORT_BY_NAME);
        mPreferences.setManualSortEnableForCategories(false);
    }

    public void onAddButtonClick() {
        openAddCategoryScreen(null);
    }

    public void onListItemClick(CategoryViewModel category) {
        openAddCategoryScreen(category);
    }

    public void onListItemLongClick(CategoryViewModel category) {

    }

    public void savePosition(final List<CategoryViewModel> categories) {
        mSubscriptions.add(
                Observable.fromCallable(() -> {
                    if (mPreferences.isManualSortEnableForCategories()) {
                        final int size = categories.size();
                        for (int i = 0; i < size; i++) {
                            categories.get(i).setPosition(i);
                        }
                    }
                    return categories;
                }).map((Func1<List<CategoryViewModel>, Collection<CategoryModel>>) mDataMapper::transform)
                        .flatMap(categoryList -> {
                            mUpdateCategory.setData(categoryList);
                            return mUpdateCategory.get();
                        }).subscribe());
    }

    public void loadData() {
        showLoading();
        mSubscriptions.add(mGetCategories.get()
                .map(mDataMapper::transformToViewModel)
                .map(categoryModels -> {
                    if (mPreferences.isManualSortEnableForCategories()) {
                        return sortByManually(categoryModels);
                    } else if (mPreferences.getSortForCategories() == SortType.SORT_BY_NAME) {
                        return sortByName(categoryModels);
                    }
                    return categoryModels;
                })
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

    private void showData(List<CategoryViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void setManualSortEnable(boolean manualSortEnable) {
        if (isViewAttached()) {
            getView().setManualSortEnable(manualSortEnable);
        }
    }

    private void openAddCategoryScreen(CategoryViewModel data) {
        if (isViewAttached()) {
            getView().openAddCategoryScreen(data);
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

    private <T extends BaseViewModel> List<T> sortByName(List<T> data) {
        Collections.sort(data, new Comparator<BaseViewModel>() {
            @Override
            public int compare(BaseViewModel lhs, BaseViewModel rhs) {
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });

        String headerName = "";
        for (int i = 0; i < data.size(); i++) {
            String firstCharacter = ShoppistUtils.getFirstCharacter(data.get(i).getName()).toUpperCase();
            if (!headerName.equals(firstCharacter)) {
                headerName = firstCharacter;
                HeaderViewModel header = new HeaderViewModel();
                header.setName(headerName);
                header.setItemType(ItemType.HEADER_ITEM);
                data.add(i, (T) header);
            }
        }
        return data;
    }

    private List<CategoryViewModel> sortByManually(List<CategoryViewModel> data) {
        Collections.sort(data, (lhs, rhs) ->
                lhs.getPosition() < rhs.getPosition() ? -1 : (lhs.getPosition() == rhs.getPosition() ? 0 : 1));
        return data;
    }
}
