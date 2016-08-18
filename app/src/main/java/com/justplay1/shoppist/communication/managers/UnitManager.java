package com.justplay1.shoppist.communication.managers;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.database.TablesHolder;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.widget.ShoppistWidgetProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Created by Mkhitar on 14.02.2015.
 */
public class UnitManager {

    private TablesHolder mTablesHolder;

    public UnitManager(TablesHolder cacheHolder) {
        this.mTablesHolder = cacheHolder;
    }

    public Collection<Unit> getAllUnits() {
        return mTablesHolder.getUnitTable().getAllUnits().values();
    }

    public Collection<Unit> getAllUnits(long timestamp) {
        return mTablesHolder.getUnitTable().getAllUnits(timestamp).values();
    }

    public Collection<Unit> getDirtyUnits() {
        return mTablesHolder.getUnitTable().getDirtyUnits().values();
    }

    public void add(final Unit unit, ExecutorListener<Unit> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Unit>() {
            @Override
            public Unit call() throws Exception {
                unit.setId(UUID.nameUUIDFromBytes((unit.getName() + unit.getShortName() + UUID.randomUUID()).getBytes()).toString());
                mTablesHolder.getUnitTable().put(unit);

                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return unit;
            }
        }, listener);
    }

    public void deleteAll(final Collection<Unit> units, ExecutorListener<Collection<Unit>> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Collection<Unit>>() {
            @Override
            public Collection<Unit> call() throws Exception {
                List<Unit> toUpdate = new ArrayList<>();
                for (Unit unit : units) {
                    unit.setDirty(true);
                    unit.setDelete(true);
                    toUpdate.add(unit);
                }
                mTablesHolder.getUnitTable().update(toUpdate);
                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
                return units;
            }
        }, listener);
    }

    public void update(final Unit newItem, ExecutorListener<Unit> listener) {
        ThreadExecutor.doNetworkTaskAsync(new Callable<Unit>() {
            @Override
            public Unit call() throws Exception {
                mTablesHolder.getUnitTable().update(newItem);
                App.get().getSyncLimitFilter().requestSync(false);
                ShoppistWidgetProvider.updateAllWidget(mTablesHolder.getContext());
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
}
