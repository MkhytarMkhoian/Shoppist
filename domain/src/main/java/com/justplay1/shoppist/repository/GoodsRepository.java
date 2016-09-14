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

import com.justplay1.shoppist.models.ProductModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar Mkhoian.
 */
public interface GoodsRepository {

    Observable<List<ProductModel>> getItems();

    Observable<ProductModel> getItem(final String id);

    void save(Collection<ProductModel> data) throws Exception;

    void save(ProductModel data) throws Exception;

    void delete(Collection<ProductModel> data) throws Exception;

    void delete(ProductModel data) throws Exception;

    void update(Collection<ProductModel> data) throws Exception;

    void update(ProductModel data) throws Exception;

    int clear();
}
