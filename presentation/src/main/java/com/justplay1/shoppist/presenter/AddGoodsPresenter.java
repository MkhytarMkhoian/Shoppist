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
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.models.mappers.GoodsModelDataMapper;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
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
public class AddGoodsPresenter extends BaseRxPresenter<AddGoodsView, Router> {

    private final BehaviorSubject<List<UnitViewModel>> unitsCache = BehaviorSubject.create();
    private final BehaviorSubject<List<CategoryViewModel>> categoryCache = BehaviorSubject.create();

    private final GoodsModelDataMapper mGoodsModelDataMapper;
    private final CategoryModelDataMapper mCategoryModelDataMapper;
    private final UnitsDataModelMapper mUnitsDataModelMapper;

    private final UpdateGoods mUpdateGoods;
    private final AddGoods mAddGoods;
    private final GetUnitsList mGetUnitsList;
    private final GetCategoryList mGetCategoryList;

    private CategoryViewModel mCategoryModel;
    private UnitViewModel mUnitModel;
    private ProductViewModel mItem;
    private String mName = "";

    @Inject
    AddGoodsPresenter(GoodsModelDataMapper dataMapper,
                      UpdateGoods updateGoods,
                      AddGoods addGoods,
                      GetUnitsList getUnitsList,
                      GetCategoryList getCategoryList,
                      CategoryModelDataMapper categoryModelDataMapper,
                      UnitsDataModelMapper unitsDataModelMapper) {
        this.mGoodsModelDataMapper = dataMapper;
        this.mUpdateGoods = updateGoods;
        this.mAddGoods = addGoods;
        this.mGetUnitsList = getUnitsList;
        this.mGetCategoryList = getCategoryList;
        this.mUnitsDataModelMapper = unitsDataModelMapper;
        this.mCategoryModelDataMapper = categoryModelDataMapper;

        loadCategories();
        loadUnits();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mName = arguments.getString(Const.NEW_NAME);
            mItem = arguments.getParcelable(ProductViewModel.class.getName());
        }
    }

    @Override
    public void attachView(AddGoodsView view) {
        super.attachView(view);
        if (mItem != null) {
            mName = mItem.getName();
            setDefaultUpdateTitle();
        } else {
            setDefaultNewTitle();
        }
        setViewName(mName);

        mSubscriptions.add(unitsCache.subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

            @Override
            public void onNext(List<UnitViewModel> unitViewModels) {
                setUnits(unitViewModels);
                if (mUnitModel != null) {
                    selectUnit(mUnitModel.getId());
                } else if (mItem != null && !mItem.isUnitEmpty()) {
                    selectUnit(mItem.getUnit().getId());
                } else {
                    selectUnit(UnitViewModel.NO_UNIT_ID);
                }
            }
        }));

        mSubscriptions.add(categoryCache.subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {

            @Override
            public void onNext(List<CategoryViewModel> category) {
                setCategory(category);
                if (mCategoryModel != null) {
                    selectCategory(mCategoryModel.getId());
                } else if (mItem != null && !mItem.isCategoryEmpty()) {
                    selectCategory(mItem.getCategory().getId());
                } else {
                    selectCategory(CategoryViewModel.NO_CATEGORY_ID);
                }
            }
        }));
    }

    public void onPositiveButtonClick() {
        saveGoods(mName);
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
        mName = name;
        if (mItem != null) {
            mItem.setName(name);
        }
    }

    public void setCategory(CategoryViewModel category) {
        mCategoryModel = category;
    }

    public void setUnit(UnitViewModel unit) {
        mUnitModel = unit;
    }

    private void loadCategories() {
        mGetCategoryList.get()
                .map(mCategoryModelDataMapper::transformToViewModel)
                .subscribe(categoryCache);
    }

    private void loadUnits() {
        mGetUnitsList.get()
                .map(mUnitsDataModelMapper::transformToViewModel)
                .subscribe(unitsCache);
    }

    private void saveGoods(String name) {
        if (checkDataForErrors(name)) {
            ProductViewModel product = new ProductViewModel();
            product.setName(name);
            product.setCategory(mCategoryModel);
            product.setUnit(mUnitModel);

            if (mItem != null) {
                product.setId(mItem.getId());
                product.setTimeCreated(mItem.getTimeCreated());
                product.setCreateByUser(mItem.isCreateByUser());
                if (!mItem.getCategory().getId().equals(product.getCategory().getId())
                        || !mItem.getUnit().getId().equals(product.getUnit().getId())
                        || !mItem.getName().equals(product.getName())) {
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
        mSubscriptions.add(
                Observable.fromCallable(() -> mGoodsModelDataMapper.transform(data))
                        .flatMap(currency -> {
                            mAddGoods.setData(Collections.singletonList(currency));
                            return mAddGoods.get();
                        }).subscribe(new SaveGoodsSubscriber(true)));
    }

    private void updateGoods(ProductViewModel data) {
        showLoading();
        mSubscriptions.add(
                Observable.fromCallable(() -> mGoodsModelDataMapper.transform(data))
                        .flatMap(currency -> {
                            mUpdateGoods.setData(Collections.singletonList(currency));
                            return mUpdateGoods.get();
                        }).subscribe(new SaveGoodsSubscriber(false)));
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
