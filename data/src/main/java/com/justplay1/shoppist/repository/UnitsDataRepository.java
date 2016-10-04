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

import com.justplay1.shoppist.entity.mappers.UnitsDAODataMapper;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.repository.datasource.local.LocalUnitsDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
@Singleton
public class UnitsDataRepository implements UnitsRepository {

    private final UnitsDAODataMapper dataMapper;
    private final LocalUnitsDataStore dataStore;

    @Inject
    public UnitsDataRepository(UnitsDAODataMapper dataMapper, LocalUnitsDataStore store) {
        this.dataMapper = dataMapper;
        this.dataStore = store;
    }

    @Override
    public Observable<List<UnitModel>> getItems() {
        return dataStore.getItems()
                .map(dataMapper::transformFromDAO);
    }

    @Override
    public Observable<UnitModel> getItem(String id) {
        return dataStore.getItem(id)
                .map(dataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<UnitModel> data) {
        dataStore.save(dataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<UnitModel> data) {
        dataStore.delete(dataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<UnitModel> data) {
        dataStore.update(dataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return dataStore.clear();
    }
}
