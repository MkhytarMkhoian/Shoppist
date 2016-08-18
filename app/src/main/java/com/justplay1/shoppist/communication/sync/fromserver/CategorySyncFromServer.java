package com.justplay1.shoppist.communication.sync.fromserver;

import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.justplay1.shoppist.communication.alarm.builders.CategoriesNotificationBuilder;
import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class CategorySyncFromServer extends SyncFromServer<Category> {

    @Override
    protected Collection<Category> getDataFromServer(final long refreshTime) throws ParseException {
        List<Category> result = new ArrayList<>();
        List<ParseObject> objects = ServerRequests.getAllCategories(refreshTime);
        for (ParseObject object : objects) {
            result.add(new Category(object));
        }
        return result;
    }

    @Override
    protected boolean deleteDataIfNeeded(Collection<Category> result) throws RemoteException, OperationApplicationException {
        getTable().clear();
        Map<String, Category> categories = ShoppistUtils.getStandardCategories();
        if (result.size() > 0) {
            for (Category itemFromServer : result) {
                if (itemFromServer.isDelete()) continue;
                Category category = categories.get(itemFromServer.getId());
                if (category != null) {
                    categories.remove(category.getId());
                    categories.put(category.getId(), itemFromServer);
                } else {
                    categories.put(itemFromServer.getId(), itemFromServer);
                }
            }
        }
        getTable().put(categories.values());
        return false;
    }

    @Override
    protected Map<String, Category> getCache() {
        return tablesHolder.getCategoriesTable().getAllCategories();
    }

    @Override
    protected BaseTable<Category> getTable() {
        return tablesHolder.getCategoriesTable();
    }

    @Override
    protected NotificationBuilder<Category> getNotificationBuilder() {
        return new CategoriesNotificationBuilder(tablesHolder.getContext());
    }

    @Override
    protected String getTag() {
        return "Category from";
    }
}
