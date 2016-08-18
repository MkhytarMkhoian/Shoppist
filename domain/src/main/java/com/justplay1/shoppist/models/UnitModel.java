package com.justplay1.shoppist.models;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class UnitModel extends BaseModel {

    public static final String NO_UNIT_ID = "no_unit";

    private String shortName;

    public UnitModel() {
        shortName = "";
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UnitModel)) return false;
        UnitModel item = (UnitModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
