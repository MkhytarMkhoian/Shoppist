package com.justplay1.shoppist.repository.datasource.local;

import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface LocalGetData<T> {

    Observable<List<T>> getItems();

    Observable<List<T>> getDirtyItems();

    Observable<List<T>> getItems(long timestamp);

    Observable<T> getItem(final String id);
}
