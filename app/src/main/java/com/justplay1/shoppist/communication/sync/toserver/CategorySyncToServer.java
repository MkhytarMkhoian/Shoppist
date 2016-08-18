package com.justplay1.shoppist.communication.sync.toserver;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class CategorySyncToServer extends SyncToServer<Category> {
    @Override
    protected void addItemsToServer(Set<Category> toAdd) throws ParseException {
        if (toAdd.size() > 0) {
            List<ParseObject> object = ServerRequests.addCategories(toAdd);
            toAdd.clear();
            for (ParseObject item : object) {
                Category category = new Category(item);
                toAdd.add(category);
            }
        }
    }

    @Override
    protected void updateItemsToServer(Set<Category> toUpdate) throws ParseException {
        ServerRequests.updateCategories(toUpdate);
    }

    @Override
    protected BaseTable<Category> getTable() {
        return App.get().getTablesHolder().getCategoriesTable();
    }

    @Override
    protected String getTag() {
        return "Category to";
    }
}
