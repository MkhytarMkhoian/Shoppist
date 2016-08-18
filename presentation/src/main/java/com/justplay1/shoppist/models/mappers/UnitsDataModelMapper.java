package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@PerActivity
public class UnitsDataModelMapper {

    @Inject
    public UnitsDataModelMapper() {
    }

    public UnitViewModel transformToViewModel(UnitModel unit) {
        UnitViewModel unitModel = null;
        if (unit != null) {
            unitModel = new UnitViewModel();
            unitModel.setId(unit.getId());
            unitModel.setName(unit.getName());
            unitModel.setServerId(unit.getServerId());
            unitModel.setShortName(unit.getShortName());
            unitModel.setDelete(unit.isDelete());
            unitModel.setTimestamp(unit.getTimestamp());
            unitModel.setDirty(unit.isDirty());
        }
        return unitModel;
    }

    public List<UnitViewModel> transformToViewModel(Collection<UnitModel> units) {
        List<UnitViewModel> unitModels = new ArrayList<>();
        UnitViewModel unit;
        for (UnitModel unitEntity : units) {
            unit = transformToViewModel(unitEntity);
            if (unit != null) {
                unitModels.add(unit);
            }
        }
        return unitModels;
    }

    public UnitModel transform(UnitViewModel unitModel) {
        UnitModel unit = null;
        if (unitModel != null) {
            unit = new UnitModel();
            unit.setId(unitModel.getId());
            unit.setName(unitModel.getName());
            unit.setServerId(unitModel.getServerId());
            unit.setShortName(unitModel.getShortName());
            unit.setDelete(unitModel.isDelete());
            unit.setTimestamp(unitModel.getTimestamp());
            unit.setDirty(unitModel.isDirty());
        }
        return unit;
    }

    public List<UnitModel> transform(Collection<UnitViewModel> unitModels) {
        List<UnitModel> units = new ArrayList<>();
        UnitModel unit;
        for (UnitViewModel unitModel : unitModels) {
            unit = transform(unitModel);
            if (unit != null) {
                units.add(unit);
            }
        }
        return units;
    }

}
