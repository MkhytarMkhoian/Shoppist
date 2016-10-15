/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.entity.mappers.GoodsDAODataMapper;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.repository.datasource.local.LocalGoodsDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class GoodsDataRepository implements GoodsRepository {

    private final LocalGoodsDataStore dataStore;
    private final GoodsDAODataMapper dataMapper;

    @Inject
    public GoodsDataRepository(GoodsDAODataMapper dataMapper, LocalGoodsDataStore dataStore) {
        this.dataStore = dataStore;
        this.dataMapper = dataMapper;
    }

    @Override
    public Observable<List<ProductModel>> getItems() {
        return dataStore.getItems()
                .map(dataMapper::transformFromDAO);
    }

    @Override
    public Observable<ProductModel> getItem(String id) {
        return dataStore.getItem(id)
                .map(dataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<ProductModel> data) {
        dataStore.save(dataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<ProductModel> data) {
        dataStore.delete(dataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<ProductModel> data) {
        dataStore.update(dataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return dataStore.clear();
    }
}
