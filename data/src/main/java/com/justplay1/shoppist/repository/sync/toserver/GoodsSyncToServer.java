package com.justplay1.shoppist.repository.sync.toserver;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteSetData;

import java.util.Collection;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 27.03.2016.
 */
@PerSync
public class GoodsSyncToServer extends SyncToServer<ProductDAO> {

    @Inject
    public GoodsSyncToServer(LocalDataStore<ProductDAO> localDataStore, RemoteSetData<ProductDAO> remoteSetData) {
        super(localDataStore, remoteSetData);
    }

    @Override
    protected void filter(Collection<ProductDAO> data, Set<ProductDAO> toUpdate, Set<ProductDAO> toAdd) {
        for (ProductDAO product : data) {
            if (product.getServerId() == null) {
                toAdd.add(product);
            } else {
                toUpdate.add(product);
            }
        }
    }

    @Override
    protected String getTag() {
        return "Goods to";
    }
}
