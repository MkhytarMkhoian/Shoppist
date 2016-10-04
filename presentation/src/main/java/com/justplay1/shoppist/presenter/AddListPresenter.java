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
@NonConfigurationScope
public class AddListPresenter extends BaseAddElementPresenter<AddListView> {

    private final ListModelDataMapper dataMapper;
    private final AddList addList;
    private final UpdateLists updateLists;

    private ListViewModel item;
    private int priority = Priority.NO_PRIORITY;
    private int color = Color.DKGRAY;
    private String name = "";

    @Inject
    AddListPresenter(ListModelDataMapper dataMapper, AddList addList, UpdateLists updateLists) {
        this.dataMapper = dataMapper;
        this.addList = addList;
        this.updateLists = updateLists;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            item = arguments.getParcelable(ListViewModel.class.getName());
        }
    }

    @Override
    public void attachView(AddListView view) {
        super.attachView(view);
        if (item != null) {
            color = item.getColor();
            name = item.getName();
            priority = item.getPriority();
            setToolbarTitle(name);
        } else {
            setDefaultToolbarTitle();
        }
        setName(name);
        setPriority(priority);
        if (color == Color.DKGRAY) {
            setRandomColor();
        } else {
            setColorToButton(color);
        }
    }

    public void selectPriority(@Priority int priority) {
        this.priority = priority;
        if (item != null) {
            item.setPriority(priority);
        }
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
        return item != null;
    }

    public void onColorButtonClick() {
        showSelectColorDialog();
    }

    public void onDoneButtonClick() {
        saveList(name, false);
    }

    public void onDoneButtonLongClick() {
        saveList(name, true);
    }

    private void addList(ListViewModel data, boolean isLongClick) {
        subscriptions.add(
                Observable.fromCallable(() -> dataMapper.transform(data))
                        .flatMap(list -> {
                            addList.setData(Collections.singletonList(list));
                            return addList.get();
                        }).subscribe(new SaveListSubscriber(isLongClick, true)));
    }

    private void updateList(ListViewModel data, boolean isLongClick) {
        subscriptions.add(
                Observable.fromCallable(() -> dataMapper.transform(data))
                        .flatMap(list -> {
                            updateLists.setData(Collections.singletonList(list));
                            return updateLists.get();
                        }).subscribe(new SaveListSubscriber(isLongClick, false)));
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
        setPriority(Priority.NO_PRIORITY);
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

    private ListViewModel buildList(String name) {
        ListViewModel list = new ListViewModel();
        list.setName(name);
        list.setColor(color);
        list.setPriority(priority);

        if (item != null) {
            list.setId(item.getId());
            list.setChecked(item.isChecked());
            list.setTimeCreated(item.getTimeCreated());
        } else {
            list.setId(ModelUtils.generateId());
            list.setTimeCreated(System.currentTimeMillis());
            list.setId(ModelUtils.generateId());
        }
        return list;
    }

    private void saveList(String name, boolean isLongClick) {
        if (checkDataForErrors(name)) {
            ListViewModel category = buildList(name);
            if (item != null) {
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
