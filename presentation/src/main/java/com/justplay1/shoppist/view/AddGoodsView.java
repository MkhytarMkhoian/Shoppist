package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AddGoodsView extends LoadDataView {

    void setName(String name);

    void setDefaultUpdateTitle();

    void setDefaultNewTitle();

    void closeDialog();

    void showNameIsRequiredError();

    void onComplete(boolean isUpdate);

    void showUnitDialog(UnitViewModel editUnit);

    void selectCategory(String id);

    void setCategory(List<CategoryViewModel> category);

    void selectUnit(String id);

    void setUnits(List<UnitViewModel> unit);
}
