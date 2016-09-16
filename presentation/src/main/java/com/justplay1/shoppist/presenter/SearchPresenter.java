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
import android.support.annotation.ColorInt;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategory;
import com.justplay1.shoppist.interactor.goods.GetGoods;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.models.mappers.GoodsModelDataMapper;
import com.justplay1.shoppist.models.mappers.ListItemsModelDataMapper;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.presenter.base.BaseRxPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.SearchView;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class SearchPresenter extends BaseRxPresenter<SearchView, Router> {

    private final GoodsModelDataMapper mGoodsModelDataMapper;
    private final CategoryModelDataMapper mCategoryModelDataMapper;
    private final ListItemsModelDataMapper mListItemsModelDataMapper;

    private final GetCategory mGetCategory;
    private final GetGoods mGetGoods;
    private final AddListItems mAddListItems;

    private String mParentListId;
    private int mContextType;

    @Inject
    public SearchPresenter(GoodsModelDataMapper dataMapper,
                           GetGoods getGoods,
                           AddListItems addListItems,
                           GetCategory getCategory,
                           CategoryModelDataMapper categoryModelDataMapper,
                           ListItemsModelDataMapper listItemsModelDataMapper) {
        this.mGoodsModelDataMapper = dataMapper;
        this.mGetGoods = getGoods;
        this.mGetCategory = getCategory;
        this.mCategoryModelDataMapper = categoryModelDataMapper;
        this.mAddListItems = addListItems;
        this.mListItemsModelDataMapper = listItemsModelDataMapper;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mParentListId = arguments.getString(Const.PARENT_LIST_ID);
            mContextType = arguments.getInt(Const.SEARCH_CONTEXT_TYPE, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
        } else if (savedInstanceState != null) {
            mParentListId = savedInstanceState.getString(Const.PARENT_LIST_ID);
            mContextType = savedInstanceState.getInt(Const.SEARCH_CONTEXT_TYPE, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(Const.PARENT_LIST_ID, mParentListId);
        bundle.putInt(Const.SEARCH_CONTEXT_TYPE, mContextType);
    }

    public void init() {
        loadData();
    }

    public void loadData() {
        mSubscriptions.add(Observable.zip(loadDefaultCategory(), loadGoods(), (categoryViewModel, goods) -> {
            Map<String, ProductViewModel> data = new HashMap<>(goods.size());
            for (ProductViewModel product : goods) {
                if (product.isCategoryEmpty()) {
                    product.setCategory(categoryViewModel);
                }
                data.put(product.getName(), product);
            }
            return data;
        }).subscribe(new DefaultSubscriber<Map<String, ProductViewModel>>() {
            @Override
            public void onNext(Map<String, ProductViewModel> data) {
                showData(data);
            }
        }));
    }

    private Observable<List<ProductViewModel>> loadGoods() {
        return mGetGoods.get()
                .map(mGoodsModelDataMapper::transformToViewModel);
    }

    private Observable<CategoryViewModel> loadDefaultCategory() {
        mGetCategory.setId(CategoryViewModel.NO_CATEGORY_ID);
        return mGetCategory.get()
                .map(mCategoryModelDataMapper::transformToViewModel);
    }

    public void onListItemClick(ProductViewModel product) {
        switch (mContextType) {
            case Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST:
                addItem(product);
                break;
            case Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST:
                if (product.getId().equals(SearchView.JUST_NAME)) {
                    showEditGoodsDialog(product.getName());
                } else {
                    showEditGoodsDialog(product);
                }
                break;
        }
    }

    public void onListItemLongClick(ProductViewModel product) {

    }

    public void onNavigationClick() {
        closeSearch();
    }

    public void onTouch() {
        closeSearch();
    }

    private void addItem(ProductViewModel product) {
        ListItemViewModel listItem = new ListItemViewModel();
        listItem.setParentListId(mParentListId);
        listItem.setNote("");
        listItem.setName(product.getName());
        listItem.setTimeCreated(System.currentTimeMillis());
        listItem.setStatus(false);
        listItem.setId(UUID.nameUUIDFromBytes((System.currentTimeMillis() + "").getBytes()).toString());
        listItem.setUnit(product.getUnit());
        CurrencyViewModel currency = new CurrencyViewModel();
        currency.setId(CurrencyViewModel.NO_CURRENCY_ID);
        listItem.setCurrency(currency);
        listItem.setPrice(0);
        listItem.setQuantity(0);
        listItem.setCategory(product.getCategory());
        listItem.setPriority(Priority.NO_PRIORITY);

        mSubscriptions.add(
                Observable.fromCallable(() -> mListItemsModelDataMapper.transform(listItem))
                        .flatMap(item -> {
                            mAddListItems.setData(Collections.singletonList(item));
                            return mAddListItems.get();
                        }).subscribe(new DefaultSubscriber<Boolean>() {

                    @Override
                    public void onNext(Boolean result) {
                        fadeInSignal(listItem.getCategory().getColor());
                    }
                }));
    }

    private void fadeInSignal(@ColorInt int color) {
        if (isViewAttached()) {
            getView().fadeInSignal(color);
        }
    }

    private void closeSearch() {
        if (isViewAttached()) {
            getView().closeSearch();
        }
    }

    private void showEditGoodsDialog(ProductViewModel product) {
        if (isViewAttached()) {
            getView().showEditGoodsDialog(product);
        }
    }

    private void showEditGoodsDialog(String name) {
        if (isViewAttached()) {
            getView().showEditGoodsDialog(name);
        }
    }

    private void showData(Map<String, ProductViewModel> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }
}
