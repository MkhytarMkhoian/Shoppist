package com.justplay1.shoppist.presenter.base;

import com.justplay1.shoppist.view.AddElementView;

import java.util.UUID;

/**
 * Created by Mkhytar on 31.07.2016.
 */
public abstract class BaseAddElementPresenter<V extends AddElementView> extends BaseRxPresenter<V> {

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

    protected String generateId(String name) {
        return UUID.nameUUIDFromBytes((name + UUID.randomUUID()).getBytes()).toString();
    }
}
