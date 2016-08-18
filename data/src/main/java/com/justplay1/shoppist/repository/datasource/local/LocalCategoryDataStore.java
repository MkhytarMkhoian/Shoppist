package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.CategoryDAO;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface LocalCategoryDataStore extends LocalDataStore<CategoryDAO> {

    Observable<Long> getLastTimestamp();

}
