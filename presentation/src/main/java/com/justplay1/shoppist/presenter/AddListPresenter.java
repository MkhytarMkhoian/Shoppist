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

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.lists.AddList;
import com.justplay1.shoppist.interactor.lists.UpdateLists;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.mappers.ListModelDataMapper;
import com.justplay1.shoppist.presenter.base.BaseAddElementPresenter;
import com.justplay1.shoppist.utils.ModelUtils;
import com.justplay1.shoppist.view.AddListView;

import java.util.Collections;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
public class AddListPresenter extends BaseAddElementPresenter<AddListView> {

    private final ListModelDataMapper mDataMapper;
    private final AddList mAddList;
    private final UpdateLists mUpdateLists;

    private ListViewModel mItem;
    private int mPriority;
    private int mColor = Color.DKGRAY;

    @Inject
    public AddListPresenter(ListModelDataMapper dataMapper, AddList addList, UpdateLists updateLists) {
        this.mDataMapper = dataMapper;
        this.mAddList = addList;
        this.mUpdateLists = updateLists;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(ListViewModel.class.getName());
        } else if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(ListViewModel.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(ListViewModel.class.getName(), mItem);
    }

    public void init() {
        if (mItem != null) {
            setToolbarTitle(mItem.getName());
            setName(mItem.getName());
            setPriority(mItem.getPriority());
            mColor = mItem.getColor();
        } else {
            setRandomColor();
            setPriority(Priority.NO_PRIORITY);
            setDefaultToolbarTitle();
        }
        setColorToButton(mColor);
    }

    public void selectPriority(@Priority int priority) {
        mPriority = priority;
    }

    private void setRandomColor() {
        if (isViewAttached()) {
            getView().setRandomColor();
        }
    }

    private void setPriority(@Priority int priority) {
        if (isViewAttached()) {
            getView().setPriority(priority);
        }
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
        saveList(name, false);
    }

    public void onDoneButtonLongClick(String name) {
        saveList(name, true);
    }

    private void addList(ListViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(list -> {
                            mAddList.setData(list);
                            return mAddList.get();
                        }).subscribe(new SaveListSubscriber(isLongClick, true)));
    }

    private void updateList(ListViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mDataMapper.transform(data))
                        .flatMap(list -> {
                            mUpdateLists.setData(Collections.singletonList(list));
                            return mUpdateLists.get();
                        }).subscribe(new SaveListSubscriber(isLongClick, false)));
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
        setPriority(Priority.NO_PRIORITY);
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

    private ListViewModel buildList(String name) {
        ListViewModel list = new ListViewModel();
        list.setName(name);
        list.setColor(mColor);
        list.setPriority(mPriority);

        if (mItem != null) {
            list.setId(mItem.getId());
            list.setChecked(mItem.isChecked());
            list.setTimeCreated(mItem.getTimeCreated());
        } else {
            list.setTimeCreated(System.currentTimeMillis());
            list.setId(ModelUtils.generateId());
        }
        return list;
    }

    private void saveList(String name, boolean isLongClick) {
        if (checkDataForErrors(name)) {
            ListViewModel category = buildList(name);
            if (mItem != null) {
                updateList(category, isLongClick);
            } else {
                addList(category, isLongClick);
            }
        }
    }

    protected final class SaveListSubscriber extends DefaultSubscriber<Boolean> {

        private boolean isLongClick;
        private boolean isAddAction;

        public SaveListSubscriber(boolean isLongClick, boolean isAddAction) {
            this.isLongClick = isLongClick;
            this.isAddAction = isAddAction;
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
//            showError(new DefaultErrorBundle((Exception) e));
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
