package com.justplay1.shoppist.communication.sync.toserver;

import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Unit;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.List;
import java.util.Set;

/**
 * Created by Mkhytar on 27.03.2016.
 */
public class UnitsSyncToServer extends SyncToServer<Unit> {
    @Override
    protected void addItemsToServer(Set<Unit> toAdd) throws ParseException {
        if (toAdd.size() > 0) {
            List<ParseObject> object = ServerRequests.addUnits(toAdd);
            toAdd.clear();
            for (ParseObject item : object) {
                Unit unit = new Unit(item);
                toAdd.add(unit);
            }
        }
    }

    @Override
    protected void updateItemsToServer(Set<Unit> toUpdate) throws ParseException {
        ServerRequests.updateUnits(toUpdate);
    }

    @Override
    protected BaseTable<Unit> getTable() {
        return App.get().getTablesHolder().getUnitTable();
    }

    @Override
    protected String getTag() {
        return "Units to";
    }
}
