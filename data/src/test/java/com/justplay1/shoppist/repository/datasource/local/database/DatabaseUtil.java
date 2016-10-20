/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.repository.datasource.local.database;

import com.justplay1.shoppist.repository.datasource.local.LocalGetData;

import java.util.List;

import rx.observers.TestSubscriber;

/**
 * Created by Mkhytar Mkhoian.
 */

class DatabaseUtil {

    public static <T> List<T> checkDataInDatabase(LocalGetData<T> db) {
        TestSubscriber<List<T>> subscriber = new TestSubscriber<>();
        db.getItems().subscribe(subscriber);

        subscriber.assertNoErrors();
        List<List<T>> onNextEvents = subscriber.getOnNextEvents();
        return onNextEvents.get(0);
    }

    public static <T> T checkDataInDatabase(LocalGetData<T> db, String id) {
        TestSubscriber<T> subscriber = new TestSubscriber<>();
        db.getItem(id).subscribe(subscriber);

        subscriber.assertNoErrors();
        List<T> onNextEvents = subscriber.getOnNextEvents();
        return onNextEvents.get(0);
    }
}
