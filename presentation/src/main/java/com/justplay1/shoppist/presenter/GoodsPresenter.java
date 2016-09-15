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

import android.support.v4.util.Pair;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategory;
import com.justplay1.shoppist.interactor.goods.DeleteGoods;
import com.justplay1.shoppist.interactor.goods.GetGoods;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.units.GetUnit;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.models.mappers.GoodsModelDataMapper;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.presenter.base.BaseSortablePresenter;
import com.justplay1.shoppist.view.GoodsView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class GoodsPresenter extends BaseSortablePresenter<GoodsView, ProductViewModel, GoodsRouter> {

    private final GoodsModelDataMapper mGoodsModelDataMapper;
    private final CategoryModelDataMapper mCategoryModelDataMapper;
    private final UnitsDataModelMapper mUnitsDataModelMapper;

    private final GetCategory mGetCategory;
    private final GetUnit mGetUnit;
    private final GetGoods mGetGoods;
    private final DeleteGoods mDeleteGoods;
    private final UpdateGoods mUpdateGoods;

    @Inject
    public GoodsPresenter(AppPreferences preferences,
                          GoodsModelDataMapper dataMapper,
                          GetGoods getGoods,
                          DeleteGoods deleteGoods,
                          UpdateGoods updateGoods,
                          GetCategory getCategory,
                          GetUnit getUnit,
                          CategoryModelDataMapper categoryModelDataMapper,
                          UnitsDataModelMapper unitsDataModelMapper) {
        super(preferences);
        this.mGoodsModelDataMapper = dataMapper;
        this.mGetGoods = getGoods;
        this.mDeleteGoods = deleteGoods;
        this.mUpdateGoods = updateGoods;
        this.mGetCategory = getCategory;
        this.mGetUnit = getUnit;
        this.mCategoryModelDataMapper = categoryModelDataMapper;
        this.mUnitsDataModelMapper = unitsDataModelMapper;
    }

    public void init() {
        loadData();
    }

    public void onAddButtonClick() {
        showEditGoodsDialog(null);
    }

    public void onListItemClick(ProductViewModel product) {
        showEditGoodsDialog(product);
    }

    public void onChangeCategoryClick(List<ProductViewModel> data) {
        showChangeCategoryDialog(data);
    }

    public void onChangeUnitClick(List<ProductViewModel> data) {
        showChangeUnitDialog(data);
    }

    public void onSortByNameClick(final List<ProductViewModel> data) {
        mPreferences.setSortForGoods(SortType.SORT_BY_NAME);
        showData(sort(data, SortType.SORT_BY_NAME));
    }

    public void onSortByTimeCreatedClick(final List<ProductViewModel> data) {
        mPreferences.setSortForGoods(SortType.SORT_BY_TIME_CREATED);
        showData(sort(data, SortType.SORT_BY_TIME_CREATED));
    }

    public void onSortByCategoryClick(final List<ProductViewModel> data) {
        mPreferences.setSortForGoods(SortType.SORT_BY_CATEGORIES);
        showData(sort(data, SortType.SORT_BY_CATEGORIES));
    }

    public void onSearchClick() {
        if (hasRouter()) {
            getRouter().openSearchScreen();
        }
    }

    public void loadData() {
        showLoading();
        mSubscriptions.add(Observable.zip(loadDefaultUnit(), loadDefaultCategory(), loadGoods(), (unitViewModel, categoryViewModel, goods) -> {
            for (Pair<HeaderViewModel, List<ProductViewModel>> pair : goods) {
                for (ProductViewModel product : pair.second) {
                    if (product.isCategoryEmpty()) {
                        product.setCategory(categoryViewModel);
                    }
                    if (product.isUnitEmpty()) {
                        product.setUnit(unitViewModel);
                    }
                }
            }
            return goods;
        }).subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ProductViewModel>>>>() {
            @Override
            public void onNext(List<Pair<HeaderViewModel, List<ProductViewModel>>> data) {
                hideLoading();
                showData(data);
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                e.printStackTrace();
            }
        }));
    }

    @Override
    public boolean isManualSortEnable() {
        return false;
    }

    @SuppressWarnings("ResourceType")
    private Observable<List<Pair<HeaderViewModel, List<ProductViewModel>>>> loadGoods() {
        return mGetGoods.get()
                .map(mGoodsModelDataMapper::transformToViewModel)
                .map(goods -> sort(goods, mPreferences.getSortForGoods()));
    }

    private Observable<UnitViewModel> loadDefaultUnit() {
        mGetUnit.setId(UnitViewModel.NO_UNIT_ID);
        return mGetUnit.get()
                .map(mUnitsDataModelMapper::transformToViewModel);
    }

    private Observable<CategoryViewModel> loadDefaultCategory() {
        mGetCategory.setId(CategoryViewModel.NO_CATEGORY_ID);
        return mGetCategory.get()
                .map(mCategoryModelDataMapper::transformToViewModel);
    }

    public void deleteItems(Collection<ProductViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> mGoodsModelDataMapper.transform(data))
                .flatMap(goods -> {
                    mDeleteGoods.setData(goods);
                    return mDeleteGoods.get();
                }).subscribe(new DefaultSubscriber<Boolean>()));
    }

    public void changeUnit(UnitViewModel unit, List<ProductViewModel> editProducts) {
        mSubscriptions.add(Observable.fromCallable(() -> {
            for (ProductViewModel product : editProducts) {
                if (!unit.equals(product.getUnit())) {
                    product.setUnit(unit);
                }
            }
            return editProducts;
        }).map(mGoodsModelDataMapper::transform)
                .flatMap(data -> {
                    mUpdateGoods.setData(data);
                    return mUpdateGoods.get();
                }).subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
    }

    public void changeCategory(CategoryViewModel category, List<ProductViewModel> editProducts) {
        mSubscriptions.add(Observable.fromCallable(() -> {
            for (ProductViewModel product : editProducts) {
                if (!category.equals(product.getCategory())) {
                    product.setCategory(category);
                }
            }
            return editProducts;
        }).map(mGoodsModelDataMapper::transform)
                .flatMap(data -> {
                    mUpdateGoods.setData(data);
                    return mUpdateGoods.get();
                }).subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                }));
    }

    private void showData(List<Pair<HeaderViewModel, List<ProductViewModel>>> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void showChangeCategoryDialog(List<ProductViewModel> editProducts) {
        if (isViewAttached()) {
            getView().showChangeCategoryDialog(editProducts);
        }
    }

    private void showChangeUnitDialog(List<ProductViewModel> editProducts) {
        if (isViewAttached()) {
            getView().showChangeUnitDialog(editProducts);
        }
    }

    private void showEditGoodsDialog(ProductViewModel editProduct) {
        if (isViewAttached()) {
            getView().showEditGoodsDialog(editProduct);
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
