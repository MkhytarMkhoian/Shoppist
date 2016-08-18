package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.ListItemModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface ListItemsRepository {

    Observable<List<ListItemModel>> getItems();

    Observable<List<ListItemModel>> getItems(String parentId);

    Observable<List<ListItemModel>> getDirtyItems();

    Observable<List<ListItemModel>> getItems(long timestamp);

    Observable<ListItemModel> getItem(final String id);

    void save(Collection<ListItemModel> data) throws Exception;

    void save(ListItemModel data) throws Exception;

    void delete(Collection<ListItemModel> data) throws Exception;

    void delete(ListItemModel data) throws Exception;

    void update(Collection<ListItemModel> data) throws Exception;

    void update(ListItemModel data) throws Exception;

    int clear();
}
