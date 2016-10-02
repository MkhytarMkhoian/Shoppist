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
import com.justplay1.shoppist.exception.DefaultErrorBundle;
import com.justplay1.shoppist.exception.ErrorBundle;
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

    private final CategoryModelDataMapper mDataMapper;
    private final AddCategory mAddCategory;
    private final UpdateCategory mUpdateCategory;

    private CategoryViewModel mItem;
    private int mColor = Color.DKGRAY;

    @Inject
    public AddCategoryPresenter(CategoryModelDataMapper dataMapper, AddCategory addCategory, UpdateCategory updateCategory) {
        this.mAddCategory = addCategory;
        this.mUpdateCategory = updateCategory;
        this.mDataMapper = dataMapper;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(CategoryViewModel.class.getName());
        } else if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(CategoryViewModel.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(CategoryViewModel.class.getName(), mItem);
    }

    public void init() {
        if (mItem != null) {
            setToolbarTitle(mItem.getName());
            setName(mItem.getName());
            mColor = mItem.getColor();
        } else {
            setDefaultToolbarTitle();
        }
        setColorToButton(mColor);
    }

    public boolean isItemEdit() {
        return mItem != null;
    }

    public void onColorSelected(int color) {
        mColor = color;
    }

    public void onColorButtonClick() {
        showSelectColorDialog();
    }

    public void onDoneButtonClick(String name) {
        saveCategory(name, false);
    }

    public void onDoneButtonLongClick(String name) {
        saveCategory(name, true);
    }

    private void addCategory(CategoryViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(category -> {
                            mAddCategory.setData(Collections.singletonList(category));
                            return mAddCategory.get();
                        }).subscribe(new SaveCategorySubscriber(isLongClick, true)));
    }

    private void updateCategory(CategoryViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(category -> {
                            mUpdateCategory.setData(Collections.singletonList(category));
                            return mUpdateCategory.get();
                        }).subscribe(new SaveCategorySubscriber(isLongClick, false)));
    }

    protected void showSelectColorDialog() {
        if (isViewAttached()) {
            getView().showSelectColorDialog(mColor);
        }
    }

    protected void setColorToButton(int color) {
        if (isViewAttached()) {
            getView().setColorToButton(color);
        }
    }

    private void clearUI() {
        mColor = Color.DKGRAY;
        setName("");
        setColorToButton(mColor);
        mItem = null;
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
        category.setColor(mColor);

        if (mItem != null) {
            category.setId(mItem.getId());
            category.setCreateByUser(mItem.isCreateByUser());
        } else {
            category.setId(ModelUtils.generateId());
            category.setCreateByUser(true);
        }
        return category;
    }

    private void saveCategory(String name, boolean isLongClick) {
        if (checkDataForErrors(name)) {
            CategoryViewModel category = buildCategory(name);
            if (mItem != null) {
                updateCategory(category, isLongClick);
            } else {
                addCategory(category, isLongClick);
            }
        }
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
//        String errorMessage = ErrorMessageFactory.create(getView().context(), errorBundle.getException());
//        getView().showError(errorMessage);
    }

    private final class SaveCategorySubscriber extends DefaultSubscriber<Boolean> {

        private boolean isLongClick;
        private boolean isAddAction;

        public SaveCategorySubscriber(boolean isLongClick, boolean isAddAction) {
            this.isLongClick = isLongClick;
            this.isAddAction = isAddAction;
        }

        @Override
        public void onError(Throwable e) {
            showErrorMessage(new DefaultErrorBundle((Exception) e));
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
