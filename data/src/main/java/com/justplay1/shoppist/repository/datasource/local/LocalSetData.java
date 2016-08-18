package com.justplay1.shoppist.repository.datasource.local;

import java.util.Collection;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface LocalSetData<T> {

    void save(Collection<T> data);

    void save(T data);

    void delete(Collection<T> data);

    void delete(T data);

    void update(Collection<T> data);

    void update(T data);

    int clear();
}
