package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.CurrencyModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface CurrencyRepository {

    Observable<List<CurrencyModel>> getItems();

    Observable<List<CurrencyModel>> getDirtyItems();

    Observable<List<CurrencyModel>> getItems(long timestamp);

    Observable<CurrencyModel> getItem(final String id);

    void save(Collection<CurrencyModel> data) throws Exception;

    void save(CurrencyModel data) throws Exception;

    void delete(Collection<CurrencyModel> data) throws Exception;

    void delete(CurrencyModel data) throws Exception;

    void update(Collection<CurrencyModel> data) throws Exception;

    void update(CurrencyModel data) throws Exception;

    int clear();
}
