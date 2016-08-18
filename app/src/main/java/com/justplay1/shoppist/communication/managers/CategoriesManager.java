package com.justplay1.shoppist.communication.managers;

import android.content.Context;
import android.database.Cursor;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.communication.network.ParseErrorHandler;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.widget.ShoppistWidgetProvider;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Mkhitar on 20.01.2015.
 */
public class CategoriesManager {

    private TablesHolder mTablesHolder;

    public CategoriesManager(TablesHolder cacheHolder) {
        this.mTablesHolder = cacheHolder;
    }

    public Collection<Category> getAllCategories() {
        return mTablesHolder.getCategoriesTable().getAllCategories().values();
    }

    public Collection<Category> getAllCategories(long timestamp) {
        return mTablesHolder.getCategoriesTable().getAllCategories(timestamp).values();
    }

    public Collection<Category> getDirtyCategories() {
        return mTablesHolder.getCategoriesTable().getDirtyCategories().values();
    }

    public Cursor getAllCategoriesCursor() {
        return mTablesHolder.getCategoriesTable().getAllCategoriesCursor();
    }

    public Collection<Category> getCategory(List<String> ids) {
        return mTablesHolder.getCategoriesTable().getCategory(ids).values();
    }

    public void add(final Category category, ExecutorListener<Category> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Category>() {
            @Override
            public Category call() throws Exception {
                category.setId(UUID.nameUUIDFromBytes((category.getName() + UUID.randomUUID()).getBytes()).toString());
                mTablesHolder.getCategoriesTable().put(category);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return category;
            }
        }, listener);
    }

    public void update(final Category newCategory, ExecutorListener<Category> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Category>() {
            @Override
            public Category call() throws Exception {
                mTablesHolder.getCategoriesTable().update(newCategory);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newCategory;
            }
        }, listener);
    }

    public void updateAll(final Collection<Category> newCategories, ExecutorListener<Collection<Category>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Category>>() {
            @Override
            public Collection<Category> call() throws Exception {
                mTablesHolder.getCategoriesTable().update(newCategories);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newCategories;
            }
        }, listener);
    }

    public void getCategory(final Category category, ExecutorListener<Collection<Category>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Category>>() {
            @Override
            public Collection<Category> call() throws Exception {

                return mTablesHolder.getCategoriesTable().getCategory(Collections.singletonList(category.getId() + "")).values();
            }
        }, listener);
    }

    public void deleteAll(final Collection<Category> categories, ExecutorListener<Collection<Category>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Category>>() {
            @Override
            public Collection<Category> call() throws Exception {
                List<Category> toUpdate = new ArrayList<>();
                for (Category category : categories) {
                    category.setDirty(true);
                    category.setDelete(true);
                    toUpdate.add(category);
                }
                mTablesHolder.getCategoriesTable().update(toUpdate);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return categories;
            }
        }, listener);
    }

    public void updateLanguage(final Context context, ExecutorListener<List<Category>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<Category>>() {
            @Override
            public List<Category> call() throws Exception {

                String[] categories = context.getResources().getStringArray(R.array.categories);

                List<Category> oldCategories = new ArrayList<>(categories.length);
                List<Category> newCategories = new ArrayList<>(categories.length);

                for (Category category : getAllCategories()) {
                    if (!category.isCreateByUser()) {
                        oldCategories.add(category);
                    }
                }

                for (int i = 0; i < oldCategories.size(); i++) {
                    Category category = new Category();

                    category.setEnable(oldCategories.get(i).isEnable());
                    category.setColor(oldCategories.get(i).getColor());
                    category.setCreateByUser(oldCategories.get(i).isCreateByUser());
                    category.setName(categories[i]);

                    newCategories.add(category);
                }

                mTablesHolder.getCategoriesTable().update(newCategories);

                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newCategories;
            }
        }, listener);
    }

    public void reset(final Context context, ExecutorListener<List<Category>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<List<Category>>() {
            @Override
            public List<Category> call() throws Exception {

                mTablesHolder.getCategoriesTable().clear();
                String[] categories = context.getResources().getStringArray(R.array.categories);
                int[] colors = context.getResources().getIntArray(R.array.categories_colors);

                List<Category> newCategories = new ArrayList<>(categories.length);

                for (int i = 0; i < categories.length; i++) {
                    Category category = new Category();
                    String[] categoriesName = categories[i].split(" ! ");

                    category.setId(categoriesName[1]);
                    category.setEnable(true);
                    category.setColor(colors[i]);
                    category.setCreateByUser(false);
                    category.setName(categoriesName[0]);

                    newCategories.add(category);
                }

                mTablesHolder.getCategoriesTable().put(newCategories);

                if (ParseUser.getCurrentUser() != null) {
                    try {
                        ServerRequests.deleteCategories();
                        ServerRequests.addCategories(newCategories);
                    } catch (Exception e) {
                        ParseErrorHandler.handleParseError(e);
                    }
                }

                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return newCategories;
            }
        }, listener);
    }
}
