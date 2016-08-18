package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.models.UnitModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mkhytar on 28.04.2016.
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
            unit.setServerId(unitEntity.getServerId());
            unit.setShortName(unitEntity.getShortName());
            unit.setDelete(unitEntity.isDelete());
            unit.setTimestamp(unitEntity.getTimestamp());
            unit.setDirty(unitEntity.isDirty());
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
                    unit.getServerId(),
                    unit.getName(),
                    unit.getTimestamp(),
                    unit.isDirty(),
                    unit.isDelete(),
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
