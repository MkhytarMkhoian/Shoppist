package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.mappers.ListItemsDAODataMapper;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.repository.datasource.local.LocalListItemsDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class ListItemsDataRepository implements ListItemsRepository {

    private final ListItemsDAODataMapper mDataMapper;
    private final LocalListItemsDataStore mDataStore;

    @Inject
    public ListItemsDataRepository(ListItemsDAODataMapper dataMapper, LocalListItemsDataStore store) {
        mDataMapper = dataMapper;
        mDataStore = store;
    }

    @Override
    public Observable<List<ListItemModel>> getItems(String parentId) {
        return mDataStore.getItems(parentId)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<ListItemModel>> getItems() {
        return mDataStore.getItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<ListItemModel>> getDirtyItems() {
        return mDataStore.getDirtyItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<ListItemModel>> getItems(long timestamp) {
        return mDataStore.getItems(timestamp)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<ListItemModel> getItem(String id) {
        return mDataStore.getItem(id)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<ListItemModel> data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void save(ListItemModel data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<ListItemModel> data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(ListItemModel data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<ListItemModel> data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(ListItemModel data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return mDataStore.clear();
    }
}
