package com.justplay1.shoppist.presenter;

import android.os.Bundle;
import android.support.v4.util.Pair;

import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.interactor.lists.SoftDeleteLists;
import com.justplay1.shoppist.interactor.lists.UpdateLists;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.mappers.ListModelDataMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseSortablePresenter;
import com.justplay1.shoppist.view.ListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 01.07.2016.
 */
public class ListPresenter extends BaseSortablePresenter<ListView, ListViewModel> {

    private final ListModelDataMapper mListModelDataMapper;
    private final GetLists mGetLists;
    private final SoftDeleteLists mSoftDeleteLists;
    private final UpdateLists mUpdateLists;

    @Inject
    public ListPresenter(ShoppistPreferences preferences,
                         GetLists getLists,
                         SoftDeleteLists softDeleteLists,
                         UpdateLists updateLists,
                         ListModelDataMapper listModelDataMapper) {
        super(preferences);
        this.mGetLists = getLists;
        this.mSoftDeleteLists = softDeleteLists;
        this.mUpdateLists = updateLists;
        this.mListModelDataMapper = listModelDataMapper;
    }

    @Override
    public boolean isManualSortEnable() {
        return false;
    }

    public void init() {
        if (mPreferences.isNeedShowRateDialog()) {
            showRateDialog();
        }
    }

    private void showRateDialog() {
        if (isViewAttached()) {
            getView().showRateDialog();
        }
    }

    public void savePosition(List<ListViewModel> lists) {
        if (mPreferences.isManualSortEnableForShoppingLists()) {
            final int size = lists.size();
            for (int i = 0; i < size; i++) {
                lists.get(i).setPosition(i);
            }
//            mUpdateLists.setData(lists);
        }
    }

    public void sortByName(final List<ListViewModel> data) {
        mPreferences.setManualSortEnableForShoppingLists(false);
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_NAME);
        sort(data, SortType.SORT_BY_NAME, false);
    }

    public void sortByPriority(final List<ListViewModel> data) {
        mPreferences.setManualSortEnableForShoppingLists(false);
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_PRIORITY);
        sort(data, SortType.SORT_BY_PRIORITY, false);
    }

    public void sortByTimeCreated(final List<ListViewModel> data) {
        mPreferences.setManualSortEnableForShoppingLists(false);
        mPreferences.setSortForShoppingLists(SortType.SORT_BY_TIME_CREATED);
        sort(data, SortType.SORT_BY_TIME_CREATED, false);
    }

    public void onSortByManualClick() {
        if (isManualSortEnable()) {
            setManualSortModeEnable(false);
        } else {
            setManualSortModeEnable(true);
        }
    }

    public void onListItemLongClick(ListViewModel item) {

    }

    public void onListItemClick(ListViewModel item) {

    }

    public void onEmailShareClick() {

    }

    public void onAddButtonClick() {
        openEditListScreen(null);
    }

    public void onEditItemClick(ListViewModel list) {

    }

    public void onDeleteCheckedItemsClick() {

    }

    public void deleteItems(Collection<ListViewModel> data) {
        mSubscriptions.add(Observable.fromCallable(() -> mListModelDataMapper.transform(data))
                .flatMap(list -> {
                    mSoftDeleteLists.setData(list);
                    return mSoftDeleteLists.get();
                }).subscribe(new DefaultSubscriber<Boolean>()));
    }

    private void showData(List<Pair<HeaderViewModel, List<ListViewModel>>> data) {
        if (isViewAttached()) {
            getView().showData(data);
        }
    }

    private void showEmailShareDialog(String listName) {
        if (isViewAttached()) {
            getView().showEmailShareDialog(listName);
        }
    }

    private void setManualSortModeEnable(boolean enable) {
        if (isViewAttached()) {
            getView().setManualSortModeEnable(enable);
        }
    }

    private void openEditListScreen(ListViewModel list) {
        if (isViewAttached()) {
            getView().openEditListScreen(list);
        }
    }
}
