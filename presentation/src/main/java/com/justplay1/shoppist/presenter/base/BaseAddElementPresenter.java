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

package com.justplay1.shoppist.presenter.base;

import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.view.AddElementView;

import java.util.UUID;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseAddElementPresenter<V extends AddElementView> extends BaseRxPresenter<V, Router> {

    protected void setName(String name) {
        if (isViewAttached()) {
            getView().setName(name);
        }
    }

    protected void showElementUpdatedMessage() {
        if (isViewAttached()) {
            getView().showElementUpdatedMessage();
        }
    }

    protected void showNewElementAddedMessage() {
        if (isViewAttached()) {
            getView().showNewElementAddedMessage();
        }
    }

    protected void setDefaultToolbarTitle() {
        if (isViewAttached()) {
            getView().setDefaultToolbarTitle();
        }
    }

    protected void setToolbarTitle(String title) {
        if (isViewAttached()) {
            getView().setToolbarTitle(title);
        }
    }

    protected void showKeyboard() {
        if (isViewAttached()) {
            getView().showKeyboard();
        }
    }

    protected void showError(String message) {
        if (isViewAttached()) {
            getView().showError(message);
        }
    }

    protected void showNameIsRequiredError() {
        if (isViewAttached()) {
            getView().showNameIsRequiredError();
        }
    }

    protected void closeScreen() {
        if (isViewAttached()) {
            getView().closeScreen();
        }
    }
}
