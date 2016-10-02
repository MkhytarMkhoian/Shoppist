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

package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
@NonConfigurationScope
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
            unitModel.setShortName(unit.getShortName());
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
            unit.setShortName(unitModel.getShortName());
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
