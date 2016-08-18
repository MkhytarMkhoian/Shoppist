package com.justplay1.shoppist.communication.sync.fromserver;

import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.justplay1.shoppist.communication.alarm.builders.CurrenciesNotificationBuilder;
import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class CurrencySyncFromServer extends SyncFromServer<Currency> {
    @Override
    protected Collection<Currency> getDataFromServer(long refreshTime) throws ParseException {
        List<Currency> result = new ArrayList<>();
        List<ParseObject> objects = ServerRequests.getAllCurrencies(refreshTime);
        for (ParseObject object : objects) {
            result.add(new Currency(object));
        }
        return result;
    }

    @Override
    protected boolean deleteDataIfNeeded(Collection<Currency> result) throws RemoteException, OperationApplicationException {
        getTable().clear();
        Map<String, Currency> currencies = ShoppistUtils.getStandardCurrencies();
        if (result.size() > 0) {
            for (Currency itemFromServer : result) {
                if (itemFromServer.isDelete()) continue;
                Currency currency = currencies.get(itemFromServer.getId());
                if (currency != null) {
                    currencies.remove(currency.getId());
                    currencies.put(currency.getId(), itemFromServer);
                } else {
                    currencies.put(itemFromServer.getId(), itemFromServer);
                }
            }
        }
        getTable().put(currencies.values());
        return false;
    }

    @Override
    protected Map<String, Currency> getCache() {
        return tablesHolder.getCurrenciesTable().getAllCurrencies();
    }

    @Override
    protected BaseTable<Currency> getTable() {
        return tablesHolder.getCurrenciesTable();
    }

    @Override
    protected NotificationBuilder<Currency> getNotificationBuilder() {
        return new CurrenciesNotificationBuilder(tablesHolder.getContext());
    }

    @Override
    protected String getTag() {
        return "Currency from";
    }
}
