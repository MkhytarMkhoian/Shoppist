package com.justplay1.shoppist.view;

import android.support.v4.util.Pair;

import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface ListItemsView extends LoadDataView, ContextView {

    void showData(List<Pair<HeaderViewModel, List<ListItemViewModel>>> data);

    void openStandardMode(ListViewModel list, ListItemViewModel item);

    void openQuickMode(String parentListId);

    void openEditScreen(ListViewModel list, ListItemViewModel item);

    void setManualSortModeEnable(boolean enable);

    void showDeletingAnimation();

    void showEmailShareDialog(String listName);
}
