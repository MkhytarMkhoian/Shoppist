package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.CategoryModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface CategoryRepository {

    Observable<List<CategoryModel>> getItems();

    Observable<List<CategoryModel>> getDirtyItems();

    Observable<List<CategoryModel>> getItems(long timestamp);

    Observable<CategoryModel> getItem(final String id);

    void save(Collection<CategoryModel> data) throws Exception;

    void save(CategoryModel data) throws Exception;

    void delete(Collection<CategoryModel> data) throws Exception;

    void delete(CategoryModel data) throws Exception;

    void update(Collection<CategoryModel> data) throws Exception;

    void update(CategoryModel data) throws Exception;

    int clear();
}
