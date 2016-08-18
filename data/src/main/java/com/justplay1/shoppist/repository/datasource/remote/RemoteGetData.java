package com.justplay1.shoppist.repository.datasource.remote;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public interface RemoteGetData<T> {

    Collection<T> getItems() throws Exception;

    Collection<T> getItems(long timestamp) throws Exception;
}
