package com.justplay1.shoppist.models;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class CurrencyModel extends BaseModel {

    public static final String NO_CURRENCY_ID = "no_currency";

    public CurrencyModel() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof CurrencyModel)) return false;
        CurrencyModel item = (CurrencyModel) o;

        return item.getId().equals(this.getId());
    }

    @Override
    public int hashCode() {
        return 31 * id.hashCode();
    }
}
