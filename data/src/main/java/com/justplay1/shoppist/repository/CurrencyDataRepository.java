package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.mappers.CurrencyDAODataMapper;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.repository.datasource.local.LocalCurrencyDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class CurrencyDataRepository implements CurrencyRepository {

    private final CurrencyDAODataMapper mDataMapper;
    private final LocalCurrencyDataStore mDataStore;

    @Inject
    public CurrencyDataRepository(CurrencyDAODataMapper dataMapper, LocalCurrencyDataStore store) {
        mDataMapper = dataMapper;
        mDataStore = store;
    }

    @Override
    public Observable<List<CurrencyModel>> getItems() {
        return mDataStore.getItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<CurrencyModel>> getDirtyItems() {
        return mDataStore.getDirtyItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<CurrencyModel>> getItems(long timestamp) {
        return mDataStore.getItems(timestamp)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<CurrencyModel> getItem(String id) {
        return mDataStore.getItem(id)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<CurrencyModel> data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void save(CurrencyModel data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<CurrencyModel> data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(CurrencyModel data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<CurrencyModel> data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(CurrencyModel data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return mDataStore.clear();
    }
}
