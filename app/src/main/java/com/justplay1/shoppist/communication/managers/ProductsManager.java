package com.justplay1.shoppist.communication.managers;

import android.content.Context;
import android.database.Cursor;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Product;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Mkhitar on 14.02.2015.
 */
public class ProductsManager {

    private TablesHolder mTablesHolder;

    public ProductsManager(TablesHolder cacheHolder) {
        this.mTablesHolder = cacheHolder;
    }

    public Collection<Product> getAllProducts() {
        return mTablesHolder.getProductsTable().getAllProducts().values();
    }

    public Collection<Product> getAllProducts(long timestamp) {
        return mTablesHolder.getProductsTable().getAllProducts(timestamp).values();
    }

    public Collection<Product> getDirtyProducts() {
        return mTablesHolder.getProductsTable().getDirtyProducts().values();
    }

    public Cursor getAllProductsCursor() {
        return mTablesHolder.getProductsTable().getAllProductsCursor();
    }

    public void add(final Product product, ExecutorListener<Product> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                product.setId(UUID.nameUUIDFromBytes((product.getName() + UUID.randomUUID()).getBytes()).toString());
                mTablesHolder.getProductsTable().put(product);

                App.get().getSyncLimitFilter().requestSync(false);
                return product;
            }
        }, listener);
    }

    public void deleteAll(final Collection<Product> products, ExecutorListener<Collection<Product>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Product>>() {
            @Override
            public Collection<Product> call() throws Exception {
                List<Product> toUpdate = new ArrayList<>();
                for (Product product : products) {
                    product.setDirty(true);
                    product.setDelete(true);
                    toUpdate.add(product);
                }
                mTablesHolder.getProductsTable().update(toUpdate);
                App.get().getSyncLimitFilter().requestSync(false);
                return products;
            }
        }, listener);
    }

    public void updateAll(final List<Product> newItem, ExecutorListener<List<Product>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<Product>>() {
            @Override
            public List<Product> call() throws Exception {
                mTablesHolder.getProductsTable().update(newItem);
                App.get().getSyncLimitFilter().requestSync(false);
                return newItem;
            }
        }, listener);
    }

    public void update(final Product newItem, ExecutorListener<Product> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                mTablesHolder.getProductsTable().update(newItem);
                App.get().getSyncLimitFilter().requestSync(false);
                return newItem;
            }
        }, listener);
    }

    public void updateLanguage(ExecutorListener<List<String>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                // TODO updateLanguage
                return null;
            }
        }, listener);
    }

    public void reset(final Context context, ExecutorListener<List<Product>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<Product>>() {
            @Override
            public List<Product> call() throws Exception {
                String[] products = context.getResources().getStringArray(R.array.products);
                List<Product> newProducts = new ArrayList<>(products.length);

                for (String item : products) {
                    Product product = new Product();
                    String[] productsName = item.split(" ! ");

                    Category category = new Category();
                    category.setId(productsName[1]);
                    product.setCategory(category);
                    product.setCreateByUser(false);
                    product.setName(productsName[0]);

                    newProducts.add(product);
                }

                mTablesHolder.getProductsTable().clear();
                mTablesHolder.getProductsTable().put(newProducts);
                return newProducts;
            }
        }, listener);
    }
}
