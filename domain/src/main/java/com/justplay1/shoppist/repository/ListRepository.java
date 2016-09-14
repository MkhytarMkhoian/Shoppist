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

import com.justplay1.shoppist.models.ListModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface ListRepository {

    Observable<List<ListModel>> getItems();

    void deleteListItems(String id);

    void save(Collection<ListModel> data) throws Exception;

    void save(ListModel data) throws Exception;

    void delete(Collection<ListModel> data) throws Exception;

    void delete(ListModel data) throws Exception;

    void update(Collection<ListModel> data) throws Exception;

    void update(ListModel data) throws Exception;

    int clear();
}
