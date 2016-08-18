package com.justplay1.shoppist.communication.sync.fromserver;

import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.justplay1.shoppist.communication.alarm.builders.GoodsNotificationBuilder;
import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.network.ServerConstants;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;
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
public class GoodsSyncFromServer extends SyncFromServer<Product> {
    @Override
    protected Collection<Product> getDataFromServer(long refreshTime) throws ParseException {
        List<Product> result = new ArrayList<>();
        List<ParseObject> objects = ServerRequests.getAllProducts(refreshTime);
        Map<String, Category> categoryMap = tablesHolder.getCategoriesTable().getAllCategories();
        Map<String, Unit> unitMap = tablesHolder.getUnitTable().getAllUnits();
        for (ParseObject object : objects) {
            Category category = categoryMap.get(object.getString(ShoppingListContact.Products.CATEGORY_ID));
            Unit unit = unitMap.get(object.getString(ShoppingListContact.Products.UNIT_ID));
            result.add(new Product(object, category, unit));
        }
        return result;
    }

    @Override
    protected boolean deleteDataIfNeeded(Collection<Product> result) throws RemoteException, OperationApplicationException {
        getTable().clear();
        Map<String, Product> standardProducts = ShoppistUtils.getStandardProductList();
        if (result.size() > 0) {
            for (Product itemFromServer : result) {
                if (itemFromServer.isDelete()) continue;
                Product product = standardProducts.get(itemFromServer.getId());
                if (product != null) {
                    standardProducts.remove(product.getId());
                    standardProducts.put(product.getId(), itemFromServer);
                } else {
                    standardProducts.put(itemFromServer.getId(), itemFromServer);
                }
            }
        }
        getTable().put(standardProducts.values());
        return false;
    }

    @Override
    protected Map<String, Product> getCache() {
        return tablesHolder.getProductsTable().getAllProducts();
    }

    @Override
    protected NotificationBuilder<Product> getNotificationBuilder() {
        return new GoodsNotificationBuilder(tablesHolder.getContext());
    }

    @Override
    protected BaseTable<Product> getTable() {
        return tablesHolder.getProductsTable();
    }

    @Override
    protected String getTag() {
        return "Goods from";
    }
}
