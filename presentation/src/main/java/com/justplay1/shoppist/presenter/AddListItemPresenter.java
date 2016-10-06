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

    private final CategoryModelDataMapper categoryModelDataMapper;
    private final UnitsDataModelMapper unitsDataModelMapper;
    private final CurrencyModelDataMapper currencyModelDataMapper;
    private final GoodsModelDataMapper goodsModelDataMapper;
    private final ListItemsModelDataMapper listItemsModelDataMapper;

    private final AddListItems addListItems;
    private final UpdateListItems updateListItems;
    private final GetCategoryList getCategoryList;
    private final GetCurrencyList getCurrencyList;
    private final GetGoodsList getGoodsList;
    private final GetUnitsList getUnitsList;
    private final UpdateGoods updateGoods;

    private ListItemViewModel item;
    private String parentListId;
    private CategoryViewModel categoryModel;
    private UnitViewModel unitModel;
    private CurrencyViewModel currencyModel;
    private ProductViewModel productModel;
    private double price = 0;
    private double quantity = 0;
    private String note = "";
    private String name = "";
    private int priority = Priority.NO_PRIORITY;

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
                         UpdateGoods updateGoods) {
        this.categoryModelDataMapper = categoryModelDataMapper;
        this.unitsDataModelMapper = unitsDataModelMapper;
        this.currencyModelDataMapper = currencyModelDataMapper;
        this.goodsModelDataMapper = goodsModelDataMapper;
        this.listItemsModelDataMapper = listItemsModelDataMapper;
        this.addListItems = addListItems;
        this.updateListItems = updateListItems;
        this.getCategoryList = getCategoryList;
        this.getCurrencyList = getCurrencyList;
        this.getGoodsList = getGoodsList;
        this.getUnitsList = getUnitsList;
        this.updateGoods = updateGoods;

        loadCategories();
        loadUnits();
        loadCurrency();
        loadGoods();
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            item = arguments.getParcelable(ListItemViewModel.class.getName());
            parentListId = arguments.getString(Const.PARENT_LIST_ID);
        }
    }

    @Override
    public void attachView(AddListItemView view) {
        super.attachView(view);
        if (item != null) {
            name = item.getName();
            priority = item.getPriority();
            note = item.getNote();
            price = item.getPrice();
            quantity = item.getQuantity();
            setToolbarTitle(name);
        } else {
            setDefaultToolbarTitle();
        }
        setName(name);
        selectPriority(priority);
        setViewNote(String.valueOf(note));
        setViewPrice(String.valueOf(price));
        setViewQuantity(String.valueOf(quantity));

        subscriptions.add(categoryCache.subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {

            @Override
            public void onNext(List<CategoryViewModel> category) {
                setCategory(category);
                if (categoryModel != null) {
                    selectCategory(categoryModel.getId());
                } else if (item != null) {
                    selectCategory(item.getCategory().getId());
                } else {
                    selectCategory(CategoryViewModel.NO_CATEGORY_ID);
                }
            }
        }));

        subscriptions.add(unitsCache.subscribe(new DefaultSubscriber<List<UnitViewModel>>() {

            @Override
            public void onNext(List<UnitViewModel> unitViewModels) {
                setUnits(unitViewModels);
                if (unitModel != null) {
                    selectUnit(unitModel.getId());
                } else if (item != null) {
                    selectUnit(item.getUnit().getId());
                } else {
                    selectUnit(UnitViewModel.NO_UNIT_ID);
                }
            }
        }));

        subscriptions.add(currencyCache.subscribe(new DefaultSubscriber<List<CurrencyViewModel>>() {

            @Override
            public void onNext(List<CurrencyViewModel> currency) {
                setCurrency(currency);
                if (currencyModel != null) {
                    selectCurrency(currencyModel.getId());
                } else if (item != null) {
                    selectCurrency(item.getCurrency().getId());
                } else {
                    selectCurrency(CurrencyViewModel.NO_CURRENCY_ID);
                }
            }
        }));

        subscriptions.add(goodsCache.subscribe(new DefaultSubscriber<Map<String, ProductViewModel>>() {

            @Override
            public void onNext(Map<String, ProductViewModel> map) {
                setGoods(map);
            }
        }));
    }

    public void setPriority(int position) {
        switch (position) {
            case 0:
                priority = Priority.NO_PRIORITY;
                break;
            case 1:
                priority = Priority.LOW;
                break;
            case 2:
                priority = Priority.MEDIUM;
                break;
            case 3:
                priority = Priority.HIGH;
                break;
        }
        if (item != null) {
            item.setPriority(priority);
        }
    }

    public void setCategory(CategoryViewModel category) {
        categoryModel = category;
    }

    public void setUnit(UnitViewModel unit) {
        unitModel = unit;
    }

    public void setProduct(ProductViewModel product) {
        productModel = product;
    }

    public void setCurrency(CurrencyViewModel currency) {
        currencyModel = currency;
    }

    public void onProductClick(ProductViewModel product) {
        productModel = product;
        if (product != null) {
            selectUnit(product.getUnit().getId());
            selectCategory(product.getCategory().getId());
        } else {
            selectUnit(UnitViewModel.NO_UNIT_ID);
            selectCategory(CategoryViewModel.NO_CATEGORY_ID);
        }
    }

    public boolean isItemEdit() {
        return item != null;
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
        saveListItem(name, false);
    }

    public void onDoneButtonLongClick() {
        saveListItem(name, true);
    }

    public void selectName(String name) {
        this.name = name;
        if (item != null) {
            item.setName(name);
        }
    }

    public void setNote(String note) {
        this.note = note;
        if (item != null) {
            item.setNote(note);
        }
    }

    public void setPrice(String price) {
        if (!price.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(price);
            BigDecimal decimal = smallNumber.setScale(2, RoundingMode.HALF_UP);
            this.price = decimal.doubleValue();
        } else {
            this.price = 0;
        }
        if (item != null) {
            item.setPrice(this.price);
        }
    }

    public void setQuantity(String quantity) {
        if (!quantity.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(quantity);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            this.quantity = decimal.doubleValue();
        } else {
            this.quantity = 0;
        }
        if (item != null) {
            item.setQuantity(this.quantity);
        }
    }

    private void loadCategories() {
        getCategoryList.get()
                .map(categoryModelDataMapper::transformToViewModel)
                .subscribe(categoryCache);
    }

    private void loadUnits() {
        getUnitsList.get()
                .map(unitsDataModelMapper::transformToViewModel)
                .subscribe(unitsCache);
    }

    private void loadCurrency() {
        getCurrencyList.get()
                .map(currencyModelDataMapper::transformToViewModel)
                .subscribe(currencyCache);
    }

    private void loadGoods() {
        getGoodsList.get()
                .map(goodsModelDataMapper::transformToViewModel)
                .map(items -> {
                    Map<String, ProductViewModel> result = new HashMap<>();
                    for (ProductViewModel item : items) {
                        result.put(item.getName().toLowerCase(), item);
                    }
                    return result;
                })
                .subscribe(goodsCache);
    }

    private void addListItem(ListItemViewModel data, boolean isLongClick) {
        subscriptions.add(
                Observable.fromCallable(() -> listItemsModelDataMapper.transform(data))
                        .flatMap(item -> {
                            addListItems.setData(Collections.singletonList(item));
                            return addListItems.get();
                        }).subscribe(new SaveListItemSubscriber(isLongClick, true)));
    }

    private void updateListItem(ListItemViewModel data, boolean isLongClick) {
        subscriptions.add(
                Observable.fromCallable(() -> listItemsModelDataMapper.transform(data))
                        .flatMap(item -> {
                            updateListItems.setData(Collections.singletonList(item));
                            return updateListItems.get();
                        })
                        .flatMap(result -> {
                            if (productModel != null) {
                                productModel.setUnit(unitModel);
                                ProductModel product = goodsModelDataMapper.transform(productModel);
                                updateGoods.setData(Collections.singletonList(product));
                                return updateGoods.get();
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
        item = null;
        name = "";
        note = "";
        price = 0;
        quantity = 0;
        priority = Priority.NO_PRIORITY;
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 60) {
            return false;
        }
        if (note.length() > 200) {
            return false;
        }
        if (price > 10) {
            return false;
        }
        if (quantity > 10) {
            return false;
        }
        return true;
    }

    private ListItemViewModel buildList(String name) {
        ListItemViewModel listItem = new ListItemViewModel();
        listItem.setName(name);
        listItem.setPriority(priority);
        listItem.setParentListId(parentListId);
        listItem.setUnit(unitModel);
        listItem.setCurrency(currencyModel);
        listItem.setPrice(price);
        listItem.setQuantity(quantity);
        listItem.setCategory(categoryModel);
        listItem.setNote(note);

        if (item != null) {
            listItem.setId(item.getId());
            listItem.setChecked(item.isChecked());
            listItem.setTimeCreated(item.getTimeCreated());
            listItem.setStatus(item.getStatus());
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
            if (this.item != null) {
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
