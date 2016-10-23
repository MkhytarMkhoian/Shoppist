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

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategory;
import com.justplay1.shoppist.interactor.goods.DeleteGoods;
import com.justplay1.shoppist.interactor.goods.GetGoodsList;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.units.GetUnit;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.GoodsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.navigation.GoodsRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.presenter.base.BaseSortablePresenter;
import com.justplay1.shoppist.view.GoodsView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class GoodsPresenter extends BaseSortablePresenter<GoodsView, ProductViewModel, GoodsRouter> {

    private final BehaviorSubject<List<Pair<HeaderViewModel, List<ProductViewModel>>>> cache = BehaviorSubject.create();

    private final GoodsViewModelMapper goodsViewModelMapper;
    private final CategoryViewModelMapper categoryModelDataMapper;
    private final UnitsViewModelMapper unitsViewModelMapper;

    private final GetCategory getCategory;
    private final GetUnit getUnit;
    private final GetGoodsList getGoodsList;
    private final DeleteGoods deleteGoods;
    private final UpdateGoods updateGoods;
    private final AppPreferences preferences;

    @Inject
    GoodsPresenter(AppPreferences preferences,
                   GoodsViewModelMapper dataMapper,
                   GetGoodsList getGoodsList,
                   DeleteGoods deleteGoods,
                   UpdateGoods updateGoods,
                   GetCategory getCategory,
                   GetUnit getUnit,
                   CategoryViewModelMapper categoryModelDataMapper,
                   UnitsViewModelMapper unitsViewModelMapper) {
        this.preferences = preferences;
        this.goodsViewModelMapper = dataMapper;
        this.getGoodsList = getGoodsList;
        this.deleteGoods = deleteGoods;
        this.updateGoods = updateGoods;
        this.getCategory = getCategory;
        this.getUnit = getUnit;
        this.categoryModelDataMapper = categoryModelDataMapper;
        this.unitsViewModelMapper = unitsViewModelMapper;

        loadData();
    }

    @Override
    public void attachView(GoodsView view) {
        super.attachView(view);
        addSubscription(cache.subscribe(new DefaultSubscriber<List<Pair<HeaderViewModel, List<ProductViewModel>>>>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoading();
            }

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
        if (preferences.getSortForGoods() != SortType.SORT_BY_NAME){
            preferences.setSortForGoods(SortType.SORT_BY_NAME);
            showData(sort(data, SortType.SORT_BY_NAME));
        }
    }

    public void onSortByTimeCreatedClick(final List<ProductViewModel> data) {
        if (preferences.getSortForGoods() != SortType.SORT_BY_TIME_CREATED){
            preferences.setSortForGoods(SortType.SORT_BY_TIME_CREATED);
            showData(sort(data, SortType.SORT_BY_TIME_CREATED));
        }
    }

    public void onSortByCategoryClick(final List<ProductViewModel> data) {
        if (preferences.getSortForGoods() != SortType.SORT_BY_CATEGORIES){
            preferences.setSortForGoods(SortType.SORT_BY_CATEGORIES);
            showData(sort(data, SortType.SORT_BY_CATEGORIES));
        }
    }

    public void onSearchClick() {
        if (hasRouter()) {
            getRouter().openSearchScreen();
        }
    }

    private void loadData() {
        Observable.zip(loadDefaultUnit(), loadDefaultCategory(), (unit, category) -> {
            Map<String, BaseViewModel> map = new HashMap<>();
            map.put(CategoryViewModel.NO_CATEGORY_ID, category);
            map.put(UnitViewModel.NO_UNIT_ID, unit);
            return map;
        }).flatMap(map -> loadGoods()
                .map(goods -> {
                    CategoryViewModel category = (CategoryViewModel) map.get(CategoryViewModel.NO_CATEGORY_ID);
                    UnitViewModel unit = (UnitViewModel) map.get(UnitViewModel.NO_UNIT_ID);
                    for (Pair<HeaderViewModel, List<ProductViewModel>> pair : goods) {
                        for (ProductViewModel product : pair.second) {
                            if (product.isCategoryEmpty()) {
                                product.setCategory(category);
                            }
                            if (product.isUnitEmpty()) {
                                product.setUnit(unit);
                            }
                        }
                    }
                    return goods;
                }))
                .subscribe(cache);
    }

    @SuppressWarnings("ResourceType")
    private Observable<List<Pair<HeaderViewModel, List<ProductViewModel>>>> loadGoods() {
        return getGoodsList.get()
                .map(goodsViewModelMapper::transformToViewModel)
                .map(goods -> sort(goods, preferences.getSortForGoods()));
    }

    private Observable<UnitViewModel> loadDefaultUnit() {
        return getUnit.init(UnitViewModel.NO_UNIT_ID).get()
                .map(unitsViewModelMapper::transformToViewModel);
    }

    private Observable<CategoryViewModel> loadDefaultCategory() {
        return getCategory.init(CategoryViewModel.NO_CATEGORY_ID).get()
                .map(categoryModelDataMapper::transformToViewModel);
    }

    public void deleteItems(Collection<ProductViewModel> data) {
        addSubscription(Observable.fromCallable(() -> goodsViewModelMapper.transform(data))
                .flatMap(goods -> deleteGoods.init(goods).get())
                .subscribe(new DefaultSubscriber<>()));
    }

    public void changeUnit(UnitViewModel unit, List<ProductViewModel> editProducts) {
        addSubscription(Observable.fromCallable(() -> {
            for (ProductViewModel product : editProducts) {
                if (!unit.equals(product.getUnit())) {
                    product.setUnit(unit);
                }
            }
            return editProducts;
        }).map(goodsViewModelMapper::transform)
                .flatMap(data -> updateGoods.init(data).get())
                .subscribe(new DefaultSubscriber<Boolean>()));
    }

    public void changeCategory(CategoryViewModel category, List<ProductViewModel> editProducts) {
        addSubscription(Observable.fromCallable(() -> {
            for (ProductViewModel product : editProducts) {
                if (!category.equals(product.getCategory())) {
                    product.setCategory(category);
                }
            }
            return editProducts;
        }).map(goodsViewModelMapper::transform)
                .flatMap(data -> updateGoods.init(data).get())
                .subscribe(new DefaultSubscriber<Boolean>()));
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
