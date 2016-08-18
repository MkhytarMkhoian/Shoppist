package com.justplay1.shoppist.view;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AddElementView extends LoadDataView {

    void setName(String name);

    void setDefaultToolbarTitle();

    void setToolbarTitle(String title);

    void showNameIsRequiredError();

    void showKeyboard();

    void closeScreen();

    void showNewElementAddedMessage();

    void showElementUpdatedMessage();
}
