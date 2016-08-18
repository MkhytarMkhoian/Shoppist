package com.justplay1.shoppist.communication.managers;

import android.database.Cursor;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.widget.ShoppistWidgetProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class CurrenciesManager {

    private TablesHolder mTablesHolder;

    public CurrenciesManager(TablesHolder cacheHolder) {
        this.mTablesHolder = cacheHolder;
    }

    public void getCurrency(final List<String> ids, ExecutorListener<Collection<Currency>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Currency>>() {
            @Override
            public Collection<Currency> call() throws Exception {
                return mTablesHolder.getCurrenciesTable().getCurrencies(ids).values();
            }
        }, listener);
    }

    public void getAllCurrency(ExecutorListener<Collection<Currency>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Currency>>() {
            @Override
            public Collection<Currency> call() throws Exception {
                return mTablesHolder.getCurrenciesTable().getAllCurrencies().values();
            }
        }, listener);
    }

    public Collection<Currency> getAllCurrency() {
        return mTablesHolder.getCurrenciesTable().getAllCurrencies(0).values();
    }

    public Collection<Currency> getDirtyCurrencies() {
        return mTablesHolder.getCurrenciesTable().getDirtyCurrencies().values();
    }

    public Collection<Currency> getAllCurrency(long timestamp) {
        return mTablesHolder.getCurrenciesTable().getAllCurrencies(timestamp).values();
    }

    public void add(final Currency currency, ExecutorListener<Currency> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Currency>() {
            @Override
            public Currency call() throws Exception {
                currency.setId(UUID.nameUUIDFromBytes((currency.getName() + UUID.randomUUID()).getBytes()).toString());
                mTablesHolder.getCurrenciesTable().put(currency);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return currency;
            }
        }, listener);
    }

    public void deleteAll(final Collection<Currency> currencies, ExecutorListener<Collection<Currency>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Currency>>() {
            @Override
            public Collection<Currency> call() throws Exception {
                List<Currency> toUpdate = new ArrayList<>();
                for (Currency currency : currencies) {
                    currency.setDirty(true);
                    currency.setDelete(true);
                    toUpdate.add(currency);
                }
                mTablesHolder.getCurrenciesTable().update(toUpdate);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return currencies;
            }
        }, listener);
    }

    public void update(final Currency newItem, ExecutorListener<Currency> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Currency>() {
            @Override
            public Currency call() throws Exception {
                mTablesHolder.getCurrenciesTable().update(newItem);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newItem;
            }
        }, listener);
    }

    public Cursor getAllCurrencyCursor() {
        return mTablesHolder.getCurrenciesTable().getAllCurrencyCursor();
    }

    public void updateLanguage(ExecutorListener<List<String>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                // TODO updateLanguage
                return null;
            }
        }, listener);
    }
}
