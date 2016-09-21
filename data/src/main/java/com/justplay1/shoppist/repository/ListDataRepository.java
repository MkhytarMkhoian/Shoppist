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

import com.justplay1.shoppist.entity.mappers.ListDAODataMapper;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.repository.datasource.local.LocalListDataStore;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
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
    public void deleteListItems(String id) {
        mDataStore.deleteListItems(id);
    }

    @Override
    public Observable<List<ListModel>> getItems() {
        return mDataStore.getItems()
                .map(mDataMapper::transformFromDAO);
    }

    @Override
    public void save(Collection<ListModel> data) {
        mDataStore.save(mDataMapper.transformToDAO(data));
    }

    @Override
    public void delete(Collection<ListModel> data) {
        mDataStore.delete(mDataMapper.transformToDAO(data));
    }

    @Override
    public void update(Collection<ListModel> data) {
        mDataStore.update(mDataMapper.transformToDAO(data));
    }

    @Override
    public int clear() {
        return mDataStore.clear();
    }
}
