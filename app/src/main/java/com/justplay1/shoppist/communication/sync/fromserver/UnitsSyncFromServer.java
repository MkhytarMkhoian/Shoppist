package com.justplay1.shoppist.communication.sync.fromserver;

import android.content.OperationApplicationException;
import android.os.RemoteException;

import com.justplay1.shoppist.communication.alarm.builders.NotificationBuilder;
import com.justplay1.shoppist.communication.alarm.builders.UnitsNotificationBuilder;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.database.BaseTable;
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
public class UnitsSyncFromServer extends SyncFromServer<Unit> {
    @Override
    protected Collection<Unit> getDataFromServer(long refreshTime) throws ParseException {
        List<Unit> result = new ArrayList<>();
        List<ParseObject> objects = ServerRequests.getAllUnits(refreshTime);
        for (ParseObject object : objects) {
            result.add(new Unit(object));
        }
        return result;
    }

    @Override
    protected boolean deleteDataIfNeeded(Collection<Unit> result) throws RemoteException, OperationApplicationException {
        getTable().clear();
        Map<String, Unit> units = ShoppistUtils.getStandardUnits();
        if (result.size() > 0) {
            for (Unit itemFromServer : result) {
                if (itemFromServer.isDelete()) continue;
                Unit unit = units.get(itemFromServer.getId());
                if (unit != null) {
                    units.remove(unit.getId());
                    units.put(unit.getId(), itemFromServer);
                } else {
                    units.put(itemFromServer.getId(), itemFromServer);
                }
            }
        }
        getTable().put(units.values());
        return false;
    }

    @Override
    protected Map<String, Unit> getCache() {
        return tablesHolder.getUnitTable().getAllUnits();
    }

    @Override
    protected BaseTable<Unit> getTable() {
        return tablesHolder.getUnitTable();
    }

    @Override
    protected NotificationBuilder<Unit> getNotificationBuilder() {
        return new UnitsNotificationBuilder(tablesHolder.getContext());
    }

    @Override
    protected String getTag() {
        return "Units from";
    }
}
