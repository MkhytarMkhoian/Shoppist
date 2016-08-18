package com.justplay1.shoppist.repository.datasource.remote;

import java.util.Collection;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface RemoteSetData<T> {

    Collection<T> save(Collection<T> data) throws Exception;

    void delete(Collection<T> data) throws Exception;

    void update(Collection<T> data) throws Exception;

    int clear() throws Exception;
}
