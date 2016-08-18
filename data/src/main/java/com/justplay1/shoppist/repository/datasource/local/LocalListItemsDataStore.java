package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.ListItemDAO;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface LocalListItemsDataStore extends LocalSetData<ListItemDAO>, LocalGetData<ListItemDAO> {

    Observable<List<ListItemDAO>> getItems(String parentId);

    Observable<Long> getLastTimestamp();
}
