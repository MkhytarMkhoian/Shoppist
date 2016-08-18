package com.justplay1.shoppist.view;

import android.support.v4.util.Pair;

import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface ListView extends LoadDataView, ContextView{

    void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data);

    void showRateDialog();

    void openEditListScreen(ListViewModel list);

    void setManualSortModeEnable(boolean enable);

    void showEmailShareDialog(String listName);
}
