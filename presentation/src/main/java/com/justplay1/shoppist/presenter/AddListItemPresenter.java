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
import com.justplay1.shoppist.interactor.currency.GetCurrencyList;
import com.justplay1.shoppist.interactor.goods.GetGoodsList;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.models.mappers.GoodsModelDataMapper;
import com.justplay1.shoppist.models.mappers.ListItemsModelDataMapper;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.presenter.base.BaseAddElementPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ModelUtils;
import com.justplay1.shoppist.view.AddListItemView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
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
public class AddListItemPresenter extends BaseAddElementPresenter<AddListItemView> {

    private final BehaviorSubject<List<CategoryViewModel>> categoryCache = BehaviorSubject.create();
    private final BehaviorSubject<List<CurrencyViewModel>> currencyCache = BehaviorSubject.create();
    private final BehaviorSubject<List<UnitViewModel>> unitsCache = BehaviorSubject.create();
    private final BehaviorSubject<Map<String, ProductViewModel>> goodsCache = BehaviorSubject.create();

    private final CategoryModelDataMapper mCategoryModelDataMapper;
    private final UnitsDataModelMapper mUnitsDataModelMapper;
    private final CurrencyModelDataMapper mCurrencyModelDataMapper;
    private final GoodsModelDataMapper mGoodsModelDataMapper;
    private final ListItemsModelDataMapper mListItemsModelDataMapper;

    private final AddListItems mAddListItems;
    private final UpdateListItems mUpdateListItems;
    private final GetCategoryList mGetCategoryList;
    private final GetCurrencyList mGetCurrencyList;
    private final GetGoodsList mGetGoodsList;
    private final GetUnitsList mGetUnitsList;
    private final UpdateGoods mUpdateGoods;

    private final AppPreferences mPreferences;

    private ListItemViewModel mItem;
    private String mParentListId;
    private CategoryViewModel mCategoryModel;
    private UnitViewModel mUnitModel;
    private CurrencyViewModel mCurrencyModel;
    private ProductViewModel mProductModel;
    private double mPrice = 0;
    private double mQuantity = 0;
    private String mNote = "";
    private String mName = "";
    private int mPriority = Priority.NO_PRIORITY;

    @Inject
    AddListItemPresenter(CategoryModelDataMapper categoryModelDataMapper,
                         UnitsDataModelMapper unitsDataModelMapper,
                         CurrencyModelDataMapper currencyModelDataMapper,
                         GoodsModelDataMapper goodsModelDataMapper,
                         ListItemsModelDataMapper listItemsModelDataMapper,
                         AddListItems addListItems,
                         UpdateListItems updateListItems,
                         GetCategoryList getCategoryList,
                         GetCurrencyList getCurrencyList,
                         GetGoodsList getGoodsList,
                         GetUnitsList getUnitsList,
                         UpdateGoods updateGoods,
                         AppPreferences preferences) {
        this.mCategoryModelDataMapper = categoryModelDataMapper;
        this.mUnitsDataModelMapper = unitsDataModelMapper;
        this.mCurrencyModelDataMapper = currencyModelDataMapper;
        this.mGoodsModelDataMapper = goodsModelDataMapper;
        this.mListItemsModelDataMapper = listItemsModelDataMapper;
        this.mAddListItems = addListItems;
        this.mUpdateListItems = updateListItems;
        this.mGetCategoryList = getCategoryList;
        this.mGetCurrencyList = getCurrencyList;
        this.mGetGoodsList = getGoodsList;
        this.mGetUnitsList = getUnitsList;
        this.mUpdateGoods = updateGoods;
        this.mPreferences = preferences;

        loadCategories();
        loadUnits();
        loadCurrency();
        loadGoods();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(ListItemViewModel.class.getName());
            mParentListId = arguments.getString(Const.PARENT_LIST_ID);
        }
    }

    @Override
    public void attachView(AddListItemView view) {
        super.attachView(view);
        if (mItem != null) {
            mName = mItem.getName();
            mPriority = mItem.getPriority();
            mNote = mItem.getNote();
            mPrice = mItem.getPrice();
            mQuantity = mItem.getQuantity();
            setToolbarTitle(mName);
        } else {
            setDefaultToolbarTitle();
        }
        setName(mName);
        selectPriority(mPriority);
        setViewNote(String.valueOf(mNote));
        setViewPrice(String.valueOf(mPrice));
        setViewQuantity(String.valueOf(mQuantity));

        mSubscriptions.add(categoryCache.subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {

            @Override
            public void onNext(List<CategoryViewModel> category) {
                setCategory(category);
                if (mItem != null) {
                    selectCategory(mItem.getCategory().getId());
                } else if (mCategoryModel != null) {
                    selectCategory(mCategoryModel.getId());
                } else {
                    selectCategory(CategoryViewModel.NO_CATEGORY_ID);
                }
            }
        }));

        mSubscriptions.add(unitsCache.subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

            @Override
            public void onNext(List<UnitViewModel> unitViewModels) {
                setUnits(unitViewModels);
                if (mItem != null) {
                    selectUnit(mItem.getUnit().getId());
                } else if (mUnitModel != null) {
                    selectUnit(mUnitModel.getId());
                } else {
                    selectUnit(UnitViewModel.NO_UNIT_ID);
                }
            }
        }));

        mSubscriptions.add(currencyCache.subscribe(new DefaultSubscriber<List<CurrencyViewModel>>() {

            @Override
            public void onNext(List<CurrencyViewModel> currency) {
                setCurrency(currency);
                if (mItem != null) {
                    selectCurrency(mItem.getCurrency().getId());
                } else if (mCurrencyModel != null) {
                    selectCurrency(mCurrencyModel.getId());
                } else {
                    selectCurrency(CurrencyViewModel.NO_CURRENCY_ID);
                }
            }
        }));

        mSubscriptions.add(goodsCache.subscribe(new DefaultSubscriber<Map<String, ProductViewModel>>() {

            @Override
            public void onNext(Map<String, ProductViewModel> map) {
                setGoods(map);
            }
        }));
    }

    public void setPriority(int position) {
        switch (position) {
            case 0:
                mPriority = Priority.NO_PRIORITY;
                break;
            case 1:
                mPriority = Priority.LOW;
                break;
            case 2:
                mPriority = Priority.MEDIUM;
                break;
            case 3:
                mPriority = Priority.HIGH;
                break;
        }
        if (mItem != null) {
            mItem.setPriority(mPriority);
        }
    }

    public void setCategory(CategoryViewModel category) {
        mCategoryModel = category;
    }

    public void setUnit(UnitViewModel unit) {
        mUnitModel = unit;
    }

    public void setProduct(ProductViewModel product) {
        mProductModel = product;
    }

    public void setCurrency(CurrencyViewModel currency) {
        mCurrencyModel = currency;
    }

    public void onProductClick(ProductViewModel product) {
        mProductModel = product;
        if (product != null) {
            selectUnit(product.getUnit().getId());
            selectCategory(product.getCategory().getId());
        } else {
            selectUnit(UnitViewModel.NO_UNIT_ID);
            selectCategory(CategoryViewModel.NO_CATEGORY_ID);
        }
    }

    public boolean isItemEdit() {
        return mItem != null;
    }

    public void onIncrementPriceClick(String price) {
        setViewPrice(String.format("%s", incrementValue(price)));
    }

    public void onDecrementPriceClick(String price) {
        setViewPrice(String.format("%s", decrementValue(price)));
    }

    public void onIncrementQuantityClick(String quantity) {
        setViewQuantity(String.format("%s", incrementValue(quantity)));
    }

    public void onDecrementQuantityClick(String quantity) {
        setViewQuantity(String.format("%s", decrementValue(quantity)));
    }

    public void onDoneButtonClick() {
        saveListItem(mName, false);
    }

    public void onDoneButtonLongClick() {
        saveListItem(mName, true);
    }

    public void selectName(String name) {
        mName = name;
        if (mItem != null) {
            mItem.setName(name);
        }
    }

    public void setNote(String note) {
        mNote = note;
        if (mItem != null) {
            mItem.setNote(note);
        }
    }

    public void setPrice(String price) {
        if (!price.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(price);
            BigDecimal decimal = smallNumber.setScale(2, RoundingMode.HALF_UP);
            mPrice = decimal.doubleValue();
        } else {
            mPrice = 0;
        }
        if (mItem != null) {
            mItem.setPrice(mPrice);
        }
    }

    public void setQuantity(String quantity) {
        if (!quantity.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(quantity);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            mQuantity = decimal.doubleValue();
        } else {
            mQuantity = 0;
        }
        if (mItem != null) {
            mItem.setQuantity(mQuantity);
        }
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

    private void loadCurrency() {
        mGetCurrencyList.get()
                .map(mCurrencyModelDataMapper::transformToViewModel)
                .subscribe(currencyCache);
    }

    private void loadGoods() {
        mGetGoodsList.get()
                .map(mGoodsModelDataMapper::transformToViewModel)
                .map(productViewModels -> {
                    Map<String, ProductViewModel> result = new HashMap<>();
                    for (ProductViewModel item : productViewModels) {
                        result.put(item.getName().toLowerCase(), item);
                    }
                    return result;
                })
                .subscribe(goodsCache);
    }

    private void addListItem(ListItemViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mListItemsModelDataMapper.transform(data))
                        .flatMap(list -> {
                            mAddListItems.setData(Collections.singletonList(list));
                            return mAddListItems.get();
                        }).subscribe(new SaveListItemSubscriber(isLongClick, true)));
    }

    private void updateListItem(ListItemViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mListItemsModelDataMapper.transform(data))
                        .flatMap(list -> {
                            mUpdateListItems.setData(Collections.singletonList(list));
                            return mUpdateListItems.get();
                        })
                        .flatMap(result -> {
                            if (mProductModel != null) {
                                mProductModel.setUnit(mUnitModel);
                                ProductModel product = mGoodsModelDataMapper.transform(mProductModel);
                                mUpdateGoods.setData(Collections.singletonList(product));
                                return mUpdateGoods.get();
                            } else {
                                return Observable.just(result);
                            }
                        })
                        .subscribe(new SaveListItemSubscriber(isLongClick, false)));
    }

    private void clearUI() {
        setName("");
        selectPriority(Priority.NO_PRIORITY);
        setViewNote("");
        setDefaultUnit();
        setDefaultCurrency();
        setDefaultCategory();
        setViewPrice("0");
        setViewQuantity("0");
        mItem = null;
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 60) {
            return false;
        }
        if (mNote.length() > 200) {
            return false;
        }
        if (mPrice > 10) {
            return false;
        }
        if (mQuantity > 10) {
            return false;
        }
        return true;
    }

    private ListItemViewModel buildList(String name) {
        ListItemViewModel listItem = new ListItemViewModel();
        listItem.setName(name);
        listItem.setPriority(mPriority);
        listItem.setParentListId(mParentListId);
        listItem.setUnit(mUnitModel);
        listItem.setCurrency(mCurrencyModel);
        listItem.setPrice(mPrice);
        listItem.setQuantity(mQuantity);
        listItem.setCategory(mCategoryModel);
        listItem.setNote(mNote);

        if (mItem != null) {
            listItem.setId(mItem.getId());
            listItem.setChecked(mItem.isChecked());
            listItem.setTimeCreated(mItem.getTimeCreated());
            listItem.setStatus(mItem.getStatus());
        } else {
            listItem.setId(ModelUtils.generateId());
            listItem.setStatus(false);
            listItem.setTimeCreated(System.currentTimeMillis());
            listItem.setId(ModelUtils.generateId());
        }
        return listItem;
    }

    private void saveListItem(String name, boolean isLongClick) {
        if (checkDataForErrors(name)) {
            ListItemViewModel item = buildList(name);
            if (mItem != null) {
                updateListItem(item, isLongClick);
            } else {
                addListItem(item, isLongClick);
            }
        }
    }

    private double incrementValue(String valueTxt) {
        try {
            double value = Double.valueOf(valueTxt);
            value++;
            BigDecimal smallNumber = new BigDecimal(value);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }

    private double decrementValue(String valueTxt) {
        try {
            double value = Double.valueOf(valueTxt);
            if (value < 1) return value;
            value--;
            BigDecimal smallNumber = new BigDecimal(value);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }

    private void selectPriority(@Priority int priority) {
        if (isViewAttached()) {
            getView().selectPriority(priority);
        }
    }

    private void setViewNote(String note) {
        if (isViewAttached()) {
            getView().setNote(note);
        }
    }

    private void setViewPrice(String price) {
        if (isViewAttached()) {
            getView().setPrice(price);
        }
    }

    private void setViewQuantity(String quantity) {
        if (isViewAttached()) {
            getView().setQuantity(quantity);
        }
    }

    private void setCategory(List<CategoryViewModel> category) {
        if (isViewAttached()) {
            getView().setCategory(category);
        }
    }

    private void setCurrency(List<CurrencyViewModel> currency) {
        if (isViewAttached()) {
            getView().setCurrency(currency);
        }
    }

    private void setUnits(List<UnitViewModel> units) {
        if (isViewAttached()) {
            getView().setUnits(units);
        }
    }

    private void setGoods(Map<String, ProductViewModel> goods) {
        if (isViewAttached()) {
            getView().setGoods(goods);
        }
    }

    private void selectCategory(String id) {
        if (isViewAttached()) {
            getView().selectCategory(id);
        }
    }

    private void selectCurrency(String id) {
        if (isViewAttached()) {
            getView().selectCurrency(id);
        }
    }

    private void selectUnit(String id) {
        if (isViewAttached()) {
            getView().selectUnit(id);
        }
    }

    private void setDefaultCategory() {
        if (isViewAttached()) {
            getView().setDefaultCategory();
        }
    }

    private void setDefaultCurrency() {
        if (isViewAttached()) {
            getView().setDefaultCurrency();
        }
    }

    private void setDefaultUnit() {
        if (isViewAttached()) {
            getView().setDefaultUnit();
        }
    }

    private final class SaveListItemSubscriber extends DefaultSubscriber<Boolean> {

        private boolean isLongClick;
        private boolean isAddAction;

        SaveListItemSubscriber(boolean isLongClick, boolean isAddAction) {
            this.isLongClick = isLongClick;
            this.isAddAction = isAddAction;
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Boolean result) {
            if (result) {
                if (!isLongClick) {
                    closeScreen();
                } else {
                    if (isAddAction) {
                        showNewElementAddedMessage();
                    } else {
                        showElementUpdatedMessage();
                    }
                    setDefaultToolbarTitle();
                    clearUI();
                    showKeyboard();
                }
            }
        }
    }
}
