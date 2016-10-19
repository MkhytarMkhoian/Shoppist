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

import android.graphics.Color;
import android.os.Bundle;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.AddCategory;
import com.justplay1.shoppist.interactor.category.UpdateCategory;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.presenter.base.BaseAddElementPresenter;
import com.justplay1.shoppist.utils.ModelUtils;
import com.justplay1.shoppist.view.AddCategoryView;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
public class AddCategoryPresenter extends BaseAddElementPresenter<AddCategoryView> {

    private final CategoryModelDataMapper dataMapper;
    private final AddCategory addCategory;
    private final UpdateCategory updateCategory;

    private CategoryViewModel item;
    private int color = Color.DKGRAY;
    private String name = "";

    @Inject
    AddCategoryPresenter(CategoryModelDataMapper dataMapper, AddCategory addCategory, UpdateCategory updateCategory) {
        this.addCategory = addCategory;
        this.updateCategory = updateCategory;
        this.dataMapper = dataMapper;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            item = arguments.getParcelable(CategoryViewModel.class.getName());
        }
    }

    @Override
    public void attachView(AddCategoryView view) {
        super.attachView(view);
        if (item != null) {
            color = item.getColor();
            name = item.getName();
            setToolbarTitle(name);
        } else {
            setDefaultToolbarTitle();
        }
        setColorToButton(color);
        setName(name);
    }

    public boolean isItemEdit() {
        return item != null;
    }

    public void selectColor(int color) {
        this.color = color;
        if (item != null) {
            item.setColor(color);
        }
    }

    public void selectName(String name) {
        this.name = name;
        if (item != null) {
            item.setName(name);
        }
    }

    public void onColorButtonClick() {
        showSelectColorDialog();
    }

    public void onDoneButtonClick() {
        saveCategory(name, false);
    }

    public void onDoneButtonLongClick() {
        saveCategory(name, true);
    }

    private void addCategory(CategoryViewModel data, boolean isLongClick) {
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(category -> addCategory.init(Collections.singletonList(category)).get())
                .subscribe(new SaveCategorySubscriber(isLongClick, true)));
    }

    private void updateCategory(CategoryViewModel data, boolean isLongClick) {
        addSubscription(Observable.fromCallable(() -> dataMapper.transform(data))
                .flatMap(category -> updateCategory.init(Collections.singletonList(category)).get())
                .subscribe(new SaveCategorySubscriber(isLongClick, false)));
    }

    private void showSelectColorDialog() {
        if (isViewAttached()) {
            getView().showSelectColorDialog(color);
        }
    }

    private void setColorToButton(int color) {
        if (isViewAttached()) {
            getView().setColorToButton(color);
        }
    }

    private void clearUI() {
        color = Color.DKGRAY;
        setName("");
        setColorToButton(color);
        item = null;
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 60) {
            return false;
        }
        return true;
    }

    private CategoryViewModel buildCategory(String name) {
        CategoryViewModel category = new CategoryViewModel();
        category.setName(name);
        category.setColor(color);

        if (item != null) {
            category.setId(item.getId());
            category.setCreateByUser(item.isCreateByUser());
        } else {
            category.setId(ModelUtils.generateId());
            category.setCreateByUser(true);
        }
        return category;
    }

    private void saveCategory(String name, boolean isLongClick) {
        if (checkDataForErrors(name)) {
            CategoryViewModel category = buildCategory(name);
            if (item != null) {
                updateCategory(category, isLongClick);
            } else {
                addCategory(category, isLongClick);
            }
        }
    }

    private final class SaveCategorySubscriber extends DefaultSubscriber<Boolean> {

        private boolean isLongClick;
        private boolean isAddAction;

        SaveCategorySubscriber(boolean isLongClick, boolean isAddAction) {
            this.isLongClick = isLongClick;
            this.isAddAction = isAddAction;
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
