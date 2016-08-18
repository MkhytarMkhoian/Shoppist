package com.justplay1.shoppist.view;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AddUnitView extends LoadDataView {

    void setFullName(String name);

    void setShortName(String name);

    void setDefaultUpdateTitle();

    void setDefaultNewTitle();

    void closeDialog();

    void showFullNameIsRequiredError();

    void showShortNameIsRequiredError();

    void onComplete(boolean isUpdate);
}
