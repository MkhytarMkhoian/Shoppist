package com.justplay1.shoppist.communication.sync.toserver;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Currency;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class CurrencySyncToServer extends SyncToServer<Currency> {
    @Override
    protected void addItemsToServer(Set<Currency> toAdd) throws ParseException {
        if (toAdd.size() > 0) {
            List<ParseObject> object = ServerRequests.addCurrencies(toAdd);
            toAdd.clear();
            for (ParseObject item : object) {
                Currency currency = new Currency(item);
                toAdd.add(currency);
            }
        }
    }

    @Override
    protected void updateItemsToServer(Set<Currency> toUpdate) throws ParseException {
        ServerRequests.updateCurrencies(toUpdate);
    }

    @Override
    protected BaseTable<Currency> getTable() {
        return App.get().getTablesHolder().getCurrenciesTable();
    }

    @Override
    protected String getTag() {
        return "Currency to";
    }
}
