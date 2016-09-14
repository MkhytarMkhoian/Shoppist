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

package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.models.UnitModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class UnitsDAODataMapper {

    @Inject
    public UnitsDAODataMapper() {
    }

    public UnitModel transformFromDAO(UnitDAO unitEntity) {
        UnitModel unit = null;
        if (unitEntity != null) {
            unit = new UnitModel();
            unit.setId(unitEntity.getId());
            unit.setName(unitEntity.getName());
            unit.setShortName(unitEntity.getShortName());
        }
        return unit;
    }

    public List<UnitModel> transformFromDAO(Collection<UnitDAO> unitEntities) {
        List<UnitModel> units = new ArrayList<>();
        UnitModel unit;
        for (UnitDAO unitEntity : unitEntities) {
            unit = transformFromDAO(unitEntity);
            if (unit != null) {
                units.add(unit);
            }
        }
        return units;
    }

    public UnitDAO transformToDAO(UnitModel unit) {
        UnitDAO unitEntity = null;
        if (unit != null) {
            unitEntity = new UnitDAO(unit.getId(),
                    unit.getName(),
                    unit.getShortName());
        }
        return unitEntity;
    }

    public List<UnitDAO> transformToDAO(Collection<UnitModel> unitEntities) {
        List<UnitDAO> units = new ArrayList<>();
        UnitDAO unit;
        for (UnitModel unitEntity : unitEntities) {
            unit = transformToDAO(unitEntity);
            if (unit != null) {
                units.add(unit);
            }
        }
        return units;
    }
}
