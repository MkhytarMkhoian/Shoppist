package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface UnitsView extends LoadDataView {

    void showData(List<UnitViewModel> data);

    void openUnitAddDialog(UnitViewModel unit);
}
