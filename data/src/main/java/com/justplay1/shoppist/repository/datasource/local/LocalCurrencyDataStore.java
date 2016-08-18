package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.CurrencyDAO;

import rx.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface LocalCurrencyDataStore extends LocalDataStore<CurrencyDAO> {

    Observable<Long> getLastTimestamp() throws Exception;
}
