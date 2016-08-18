package com.justplay1.shoppist.communication.sync.toserver;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class GoodsSyncToServer extends SyncToServer<Product> {

    @Override
    protected void filter(Collection<Product> data, Set<Product> toUpdate, Set<Product> toAdd) {
        for (Product product : data) {
            if (product.getServerId() == null) {
                toAdd.add(product);
            } else {
                toUpdate.add(product);
            }
        }
    }

    @Override
    protected void addItemsToServer(Set<Product> toAdd) throws ParseException {
        if (toAdd.size() > 0) {
            List<ParseObject> object = ServerRequests.addProducts(toAdd);
            for (ParseObject item : object) {
                for (Product product : toAdd) {
                    if (product.getId().equals(item.getString(ShoppingListContact.Products.PRODUCT_ID))) {
                        product.setServerId(item.getObjectId());
                        product.setTimestamp(item.getLong(ServerRequests.TIMESTAMP));
                        product.setDirty(false);
                    }
                }
            }
        }
    }

    @Override
    protected void updateItemsToServer(Set<Product> toUpdate) throws ParseException {
        ServerRequests.updateProducts(toUpdate);
    }

    @Override
    protected BaseTable<Product> getTable() {
        return App.get().getTablesHolder().getProductsTable();
    }

    @Override
    protected String getTag() {
        return "Goods to";
    }
}
