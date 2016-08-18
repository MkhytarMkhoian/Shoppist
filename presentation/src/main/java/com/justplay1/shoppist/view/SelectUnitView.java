package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface SelectUnitView extends LoadDataView {

    void setUnits(List<UnitViewModel> unit);

    void selectUnit(String id);

    void onComplete(UnitViewModel unit, boolean isUpdate);

    void closeDialog();

    void showUnitDialog(UnitViewModel unit);
}
