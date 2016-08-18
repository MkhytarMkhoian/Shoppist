package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.ProductModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface GoodsRepository {

    Observable<List<ProductModel>> getItems();

    Observable<List<ProductModel>> getDirtyItems();

    Observable<List<ProductModel>> getItems(long timestamp);

    Observable<ProductModel> getItem(final String id);

    void save(Collection<ProductModel> data) throws Exception;

    void save(ProductModel data) throws Exception;

    void delete(Collection<ProductModel> data) throws Exception;

    void delete(ProductModel data) throws Exception;

    void update(Collection<ProductModel> data) throws Exception;

    void update(ProductModel data) throws Exception;

    int clear();
}
