package com.justplay1.shoppist.repository.datasource.local;

import com.justplay1.shoppist.entity.ProductDAO;

import rx.Observable;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface LocalGoodsDataStore extends LocalDataStore<ProductDAO> {

    Observable<Long> getLastTimestamp() throws Exception;
}
