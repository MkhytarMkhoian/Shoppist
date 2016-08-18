package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.UnitDAO;

import rx.Observable;

/**
 * Interface that represents a data store from where data is retrieved.
 */
public interface LocalUnitsDataStore extends LocalDataStore<UnitDAO> {

    Observable<Long> getLastTimestamp() throws Exception;
}
