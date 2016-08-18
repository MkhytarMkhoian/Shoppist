package com.justplay1.shoppist.view;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AddCurrencyView extends LoadDataView {

    void setName(String name);

    void setDefaultUpdateTitle();

    void setDefaultNewTitle();

    void closeDialog();

    void showNameIsRequiredError();

    void onComplete(boolean isUpdate);
}
