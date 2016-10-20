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
import com.justplay1.shoppist.interactor.goods.AddGoods;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.GoodsViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRouterPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ModelUtils;
import com.justplay1.shoppist.view.AddGoodsView;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class AddGoodsPresenter extends BaseRouterPresenter<AddGoodsView, Router> {

    private final BehaviorSubject<List<UnitViewModel>> unitsCache = BehaviorSubject.create();
    private final BehaviorSubject<List<CategoryViewModel>> categoryCache = BehaviorSubject.create();

    private final GoodsViewModelMapper goodsViewModelMapper;
    private final CategoryViewModelMapper categoryModelDataMapper;
    private final UnitsViewModelMapper unitsViewModelMapper;

    private final UpdateGoods updateGoods;
    private final AddGoods addGoods;
    private final GetUnitsList getUnitsList;
    private final GetCategoryList getCategoryList;

    private CategoryViewModel categoryModel;
    private UnitViewModel unitModel;
    private ProductViewModel item;
    private String name = "";

    @Inject
    AddGoodsPresenter(GoodsViewModelMapper dataMapper,
                      UpdateGoods updateGoods,
                      AddGoods addGoods,
                      GetUnitsList getUnitsList,
                      GetCategoryList getCategoryList,
                      CategoryViewModelMapper categoryModelDataMapper,
                      UnitsViewModelMapper unitsViewModelMapper) {
        this.goodsViewModelMapper = dataMapper;
        this.updateGoods = updateGoods;
        this.addGoods = addGoods;
        this.getUnitsList = getUnitsList;
        this.getCategoryList = getCategoryList;
        this.unitsViewModelMapper = unitsViewModelMapper;
        this.categoryModelDataMapper = categoryModelDataMapper;

        loadCategories();
        loadUnits();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            name = arguments.getString(Const.NEW_NAME);
            item = arguments.getParcelable(ProductViewModel.class.getName());
        }
    }

    @Override
    public void attachView(AddGoodsView view) {
        super.attachView(view);
        if (item != null) {
            name = item.getName();
            setDefaultUpdateTitle();
        } else {
            setDefaultNewTitle();
        }
        setViewName(name);

        addSubscription(unitsCache.subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

            @Override
            public void onNext(List<UnitViewModel> unitViewModels) {
                setUnits(unitViewModels);
                if (unitModel != null) {
                    selectUnit(unitModel.getId());
                } else if (item != null && !item.isUnitEmpty()) {
                    selectUnit(item.getUnit().getId());
                } else {
                    selectUnit(UnitViewModel.NO_UNIT_ID);
                }
            }
        }));

        addSubscription(categoryCache.subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {

            @Override
            public void onNext(List<CategoryViewModel> category) {
                setCategory(category);
                if (categoryModel != null) {
                    selectCategory(categoryModel.getId());
                } else if (item != null && !item.isCategoryEmpty()) {
                    selectCategory(item.getCategory().getId());
                } else {
                    selectCategory(CategoryViewModel.NO_CATEGORY_ID);
                }
            }
        }));
    }

    public void onPositiveButtonClick() {
        saveGoods(name);
    }

    public void onNegativeButtonClick() {
        closeDialog();
    }

    public void onAddUnitClick() {
        showUnitDialog(null);
    }

    public void onEditUnitClick(UnitViewModel unit) {
        showUnitDialog(unit);
    }

    public void setName(String name) {
        this.name = name;
        if (item != null) {
            item.setName(name);
        }
    }

    public void setCategory(CategoryViewModel category) {
        categoryModel = category;
    }

    public void setUnit(UnitViewModel unit) {
        unitModel = unit;
    }

    private void loadCategories() {
        getCategoryList.get()
                .map(categoryModelDataMapper::transformToViewModel)
                .subscribe(categoryCache);
    }

    private void loadUnits() {
        getUnitsList.get()
                .map(unitsViewModelMapper::transformToViewModel)
                .subscribe(unitsCache);
    }

    private void saveGoods(String name) {
        if (checkDataForErrors(name)) {
            ProductViewModel product = new ProductViewModel();
            product.setName(name);
            product.setCategory(categoryModel);
            product.setUnit(unitModel);

            if (item != null) {
                product.setId(item.getId());
                product.setTimeCreated(item.getTimeCreated());
                product.setCreateByUser(item.isCreateByUser());
                if (!item.getCategory().getId().equals(product.getCategory().getId())
                        || !item.getUnit().getId().equals(product.getUnit().getId())
                        || !item.getName().equals(product.getName())) {
                    product.setCreateByUser(true);
                }
                updateGoods(product);
            } else {
                product.setId(ModelUtils.generateId());
                product.setTimeCreated(System.currentTimeMillis());
                product.setCreateByUser(true);
                addGoods(product);
            }
        }
    }

    private void addGoods(ProductViewModel data) {
        showLoading();
        addSubscription(Observable.fromCallable(() -> goodsViewModelMapper.transform(data))
                .flatMap(item -> addGoods.init(Collections.singletonList(item)).get())
                .subscribe(new SaveGoodsSubscriber(true)));
    }

    private void updateGoods(ProductViewModel data) {
        showLoading();
        addSubscription(Observable.fromCallable(() -> goodsViewModelMapper.transform(data))
                .flatMap(item -> updateGoods.init(Collections.singletonList(item)).get())
                .subscribe(new SaveGoodsSubscriber(false)));
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 40) {
            return false;
        }
        return true;
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

    private void onComplete(boolean isUpdate) {
        if (isViewAttached()) {
            getView().onComplete(isUpdate);
        }
    }

    private void showNameIsRequiredError() {
        if (isViewAttached()) {
            getView().showNameIsRequiredError();
        }
    }

    private void setViewName(String name) {
        if (isViewAttached()) {
            getView().setViewName(name);
        }
    }

    private void setDefaultNewTitle() {
        if (isViewAttached()) {
            getView().setDefaultNewTitle();
        }
    }

    private void setDefaultUpdateTitle() {
        if (isViewAttached()) {
            getView().setDefaultUpdateTitle();
        }
    }

    private void closeDialog() {
        if (isViewAttached()) {
            getView().closeDialog();
        }
    }

    private void showUnitDialog(UnitViewModel editUnit) {
        if (isViewAttached()) {
            getView().showUnitDialog(editUnit);
        }
    }

    private void setCategory(List<CategoryViewModel> category) {
        if (isViewAttached()) {
            getView().setCategory(category);
        }
    }

    private void setUnits(List<UnitViewModel> units) {
        if (isViewAttached()) {
            getView().setUnits(units);
        }
    }

    private void selectCategory(String id) {
        if (isViewAttached()) {
            getView().selectCategory(id);
        }
    }

    private void selectUnit(String id) {
        if (isViewAttached()) {
            getView().selectUnit(id);
        }
    }

    private final class SaveGoodsSubscriber extends DefaultSubscriber<Boolean> {

        private boolean isAddAction;

        SaveGoodsSubscriber(boolean isAddAction) {
            this.isAddAction = isAddAction;
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            hideLoading();
        }

        @Override
        public void onNext(Boolean result) {
            hideLoading();
            if (result) {
                if (isAddAction) {
                    onComplete(false);
                } else {
                    onComplete(true);
                }
                closeDialog();
            }
        }
    }
}
