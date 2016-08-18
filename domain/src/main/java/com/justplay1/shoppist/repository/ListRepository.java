package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.ListModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface ListRepository {

    Observable<List<ListModel>> getItems();

    Observable<List<ListModel>> getDirtyItems();

    Observable<List<ListModel>> getItems(long timestamp);

    void markListItemsAsDeleted(String id);

    void deleteListItems(String id);

    void save(Collection<ListModel> data) throws Exception;

    void save(ListModel data) throws Exception;

    void delete(Collection<ListModel> data) throws Exception;

    void delete(ListModel data) throws Exception;

    void update(Collection<ListModel> data) throws Exception;

    void update(ListModel data) throws Exception;

    int clear();
}
