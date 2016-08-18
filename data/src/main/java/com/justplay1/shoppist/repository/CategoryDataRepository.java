package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.mappers.CategoryDAODataMapper;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.repository.datasource.local.LocalCategoryDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
@Singleton
public class CategoryDataRepository implements CategoryRepository {

    private final CategoryDAODataMapper mDataMapper;
    private final LocalCategoryDataStore mDataStore;

    @Inject
    public CategoryDataRepository(CategoryDAODataMapper dataMapper, LocalCategoryDataStore store) {
        mDataMapper = dataMapper;
        mDataStore = store;
    }

    @Override
    public Observable<List<CategoryModel>> getItems() {
        return mDataStore.getItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<CategoryModel>> getDirtyItems() {
        return mDataStore.getDirtyItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<List<CategoryModel>> getItems(long timestamp) {
        return mDataStore.getItems(timestamp)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public Observable<CategoryModel> getItem(String id) {
        return mDataStore.getItem(id)
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<CategoryModel> data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void save(CategoryModel data) throws Exception {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<CategoryModel> data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(CategoryModel data) throws Exception {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<CategoryModel> data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(CategoryModel data) throws Exception {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return mDataStore.clear();
    }
}
