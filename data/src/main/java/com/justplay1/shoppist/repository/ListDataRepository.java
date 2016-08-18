package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.mappers.ListDAODataMapper;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class ListDataRepository implements ListRepository {

    private final ListDAODataMapper mDataMapper;
    private final LocalListDataStore mDataStore;

    @Inject
    public ListDataRepository(ListDAODataMapper dataMapper, LocalListDataStore store) {
        mDataMapper = dataMapper;
        mDataStore = store;
    }

    @Override
    public void markListItemsAsDeleted(String id) {
        mDataStore.markListItemsAsDeleted(id);
    }

    @Override
    public void deleteListItems(String id) {
        mDataStore.deleteListItems(id);
    }

    @Override
    public Observable<List<ListModel>> getItems() {
        return mDataStore.getItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<ListModel>> getDirtyItems() {
        return mDataStore.getDirtyItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<ListModel>> getItems(long timestamp) {
        return mDataStore.getItems(timestamp)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<ListModel> data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void save(ListModel data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<ListModel> data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(ListModel data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<ListModel> data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(ListModel data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return mDataStore.clear();
    }
}
