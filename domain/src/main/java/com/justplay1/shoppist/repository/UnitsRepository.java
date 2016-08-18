package com.justplay1.shoppist.repository;

import com.justplay1.shoppist.models.UnitModel;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by Mkhytar on 28.04.2016.
 */
public interface UnitsRepository {

    Observable<List<UnitModel>> getItems();

    Observable<List<UnitModel>> getDirtyItems();

    Observable<List<UnitModel>> getItems(long timestamp);

    Observable<UnitModel> getItem(final String id);

    void save(Collection<UnitModel> data) throws Exception;

    void save(UnitModel data) throws Exception;

    void delete(Collection<UnitModel> data) throws Exception;

    void delete(UnitModel data) throws Exception;

    void update(Collection<UnitModel> data) throws Exception;

    void update(UnitModel data) throws Exception;

    int clear();
}
