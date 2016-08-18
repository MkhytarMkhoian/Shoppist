package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.ListDAO;

import rx.Observable;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface LocalListDataStore extends LocalSetData<ListDAO>, LocalGetData<ListDAO> {

    void markListItemsAsDeleted(String id);

    void deleteListItems(String id);

    Observable<Long> getLastTimestamp();
}
