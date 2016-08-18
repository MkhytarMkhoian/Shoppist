package com.justplay1.shoppist.repository.datasource.local;

import java.util.Map;

import rx.Observable;

/**
 * Created by Mkhytar on 16.05.2016.
 */
public interface LocalDataStore<T> extends LocalSetData<T>, LocalGetData<T> {

    Observable<Map<String, T>> getDefaultData();
}
