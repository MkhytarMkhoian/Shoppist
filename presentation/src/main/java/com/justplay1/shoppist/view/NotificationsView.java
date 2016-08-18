package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.NotificationViewModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface NotificationsView extends LoadDataView {

    void showData(List<NotificationViewModel> data);

    void notifyDataSetChanged();
}
