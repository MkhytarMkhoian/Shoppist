package com.justplay1.shoppist.repository.sync.toserver;

import com.justplay1.shoppist.di.scopes.PerSync;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.repository.datasource.local.LocalDataStore;
import com.justplay1.shoppist.repository.datasource.remote.RemoteSetData;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 27.03.2016.
 */
@PerSync
public class ListItemsSyncToServer extends SyncToServer<ListItemDAO> {

    @Inject
    public ListItemsSyncToServer(LocalDataStore<ListItemDAO> localDataStore,
                                 RemoteSetData<ListItemDAO> remoteDataStore) {
        super(localDataStore, remoteDataStore);
    }

    @Override
    protected String getTag() {
        return "ShoppingListItems to";
    }
}
