/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.interactor.listitems.GetListItems;
import com.justplay1.shoppist.interactor.lists.DeleteLists;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.SortType;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.navigation.ListRouter;
import com.justplay1.shoppist.preferences.AppPreferences;
import com.justplay1.shoppist.view.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListItemModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static java.util.Collections.singletonList;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DeleteLists.class,
        GetListItems.class,
        GetLists.class,
        Bundle.class})
public class ListPresenterTest {

    private DeleteLists deleteLists;
    private GetListItems getListItems;

    @Mock
    private AppPreferences mockPreferences;
    @Mock
    private ListRouter mockRouter;
    @Mock
    private ListView mockView;

    private ListPresenter presenter;
    private ListViewModel fakeListViewModel;
    private ListModel fakeListModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        GetLists getLists = PowerMockito.mock(GetLists.class);
        deleteLists = PowerMockito.mock(DeleteLists.class);
        getListItems = PowerMockito.mock(GetListItems.class);

        ListViewModelMapper listMapper = new ListViewModelMapper();
        ListItemsViewModelMapper listItemsMapper = new ListItemsViewModelMapper(new CategoryViewModelMapper(),
                new CurrencyViewModelMapper(), new UnitsViewModelMapper());

        fakeListModel = createFakeListModel();
        fakeListViewModel = createFakeListViewModel();

        when(getLists.get()).thenReturn(Observable.just(singletonList(fakeListModel)));

        presenter = new ListPresenter(mockPreferences,
                getLists,
                deleteLists,
                getListItems,
                listMapper,
                listItemsMapper);

        verify(getLists).get();
    }

    @Test
    public void sortByName_HappyCase() throws Exception {
        List<ListViewModel> data = singletonList(fakeListViewModel);

        presenter.attachView(mockView);
        presenter.sortByName(data);

        verify(mockPreferences).setSortForShoppingLists(SortType.SORT_BY_NAME);
        verify(mockView).showData(anyList());
    }

    @Test
    public void sortByName_ClickTwoTime_HappyCase() throws Exception {
        List<ListViewModel> data = singletonList(fakeListViewModel);
        when(mockPreferences.getSortForShoppingLists()).thenReturn(SortType.SORT_BY_CATEGORIES);

        presenter.attachView(mockView);
        presenter.sortByName(data);

        when(mockPreferences.getSortForShoppingLists()).thenReturn(SortType.SORT_BY_NAME);
        presenter.sortByName(data);

        verify(mockPreferences, times(3)).getSortForShoppingLists();
        verify(mockPreferences, times(1)).setSortForShoppingLists(SortType.SORT_BY_NAME);
        verify(mockView, times(1)).showData(anyList());
    }

    @Test
    public void sortByPriority_HappyCase() throws Exception {
        List<ListViewModel> data = singletonList(fakeListViewModel);

        presenter.attachView(mockView);
        presenter.sortByPriority(data);

        verify(mockPreferences).setSortForShoppingLists(SortType.SORT_BY_PRIORITY);
        verify(mockView).showData(anyList());
    }

    @Test
    public void sortByPriority_ClickTwoTime_HappyCase() throws Exception {
        List<ListViewModel> data = singletonList(fakeListViewModel);
        when(mockPreferences.getSortForShoppingLists()).thenReturn(SortType.SORT_BY_CATEGORIES);

        presenter.attachView(mockView);
        presenter.sortByPriority(data);

        when(mockPreferences.getSortForShoppingLists()).thenReturn(SortType.SORT_BY_PRIORITY);
        presenter.sortByPriority(data);

        verify(mockPreferences, times(3)).getSortForShoppingLists();
        verify(mockPreferences, times(1)).setSortForShoppingLists(SortType.SORT_BY_PRIORITY);
        verify(mockView, times(1)).showData(anyList());
    }

    @Test
    public void sortByTimeCreated_HappyCase() throws Exception {
        List<ListViewModel> data = singletonList(fakeListViewModel);

        presenter.attachView(mockView);
        presenter.sortByTimeCreated(data);

        verify(mockPreferences).setSortForShoppingLists(SortType.SORT_BY_TIME_CREATED);
        verify(mockView).showData(anyList());
    }

    @Test
    public void sortByTimeCreated_ClickTwoTime_HappyCase() throws Exception {
        List<ListViewModel> data = singletonList(fakeListViewModel);
        when(mockPreferences.getSortForShoppingLists()).thenReturn(SortType.SORT_BY_CATEGORIES);

        presenter.attachView(mockView);
        presenter.sortByTimeCreated(data);

        when(mockPreferences.getSortForShoppingLists()).thenReturn(SortType.SORT_BY_TIME_CREATED);
        presenter.sortByTimeCreated(data);

        verify(mockPreferences, times(3)).getSortForShoppingLists();
        verify(mockPreferences, times(1)).setSortForShoppingLists(SortType.SORT_BY_TIME_CREATED);
        verify(mockView, times(1)).showData(anyList());
    }

    @Test
    public void onAddButtonClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onAddButtonClick();

        verify(mockRouter).openEditScreen(null);
    }

    @Test
    public void onEditItemClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onEditItemClick(fakeListViewModel);

        verify(mockRouter).openEditScreen(fakeListViewModel);
    }

    @Test
    public void onItemClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onItemClick(fakeListViewModel);

        verify(mockRouter).openListDetailScreen(fakeListViewModel);
    }

    @Test
    public void deleteItems_HappyCase() throws Exception {
        when(deleteLists.init(anyCollectionOf(ListModel.class))).thenReturn(deleteLists);
        when(deleteLists.get()).thenReturn(Observable.just(true));

        presenter.deleteItems(singletonList(fakeListViewModel));

        verify(deleteLists).init(anyCollectionOf(ListModel.class));
        verify(deleteLists).get();
    }

    @Test
    public void emailShare_HappyCase() throws Exception {
        List<ListItemModel> data = singletonList(createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel()));
        when(getListItems.init(fakeListModel.getId())).thenReturn(getListItems);
        when(getListItems.get()).thenReturn(Observable.just(data));

        presenter.attachView(mockView);
        presenter.emailShare(singletonList(fakeListViewModel));

        verify(getListItems).init(fakeListModel.getId());
        verify(getListItems).get();

        verify(mockView).showLoadingDialog();
        verify(mockView).hideLoadingDialog();
        verify(mockView).share(anyString());
    }

}