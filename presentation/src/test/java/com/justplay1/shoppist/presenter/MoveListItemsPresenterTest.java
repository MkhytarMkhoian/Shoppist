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

import com.justplay1.shoppist.interactor.listitems.MoveToList;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.mappers.CategoryViewModelMapper;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListItemsViewModelMapper;
import com.justplay1.shoppist.models.mappers.ListViewModelMapper;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.MoveListItemsView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collections;

import rx.Observable;

import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCategoryViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListItemModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListItemViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListViewModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetLists.class, MoveToList.class, Bundle.class})
public class MoveListItemsPresenterTest {

    @Mock
    private MoveListItemsView mockView;

    private MoveListItemsPresenter presenter;
    private MoveToList moveToList;
    private ListViewModel fakeListViewModel;
    private ListItemViewModel fakeListItemViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        GetLists getLists = PowerMockito.mock(GetLists.class);
        moveToList = PowerMockito.mock(MoveToList.class);

        ListViewModelMapper listMapper = new ListViewModelMapper();
        ListItemsViewModelMapper listItemsMapper = new ListItemsViewModelMapper(new CategoryViewModelMapper(),
                new CurrencyViewModelMapper(), new UnitsViewModelMapper());

        ListModel fakeListModel = createFakeListModel();
        ListItemModel fakeListItemModel = createFakeListItemModel(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
        fakeListViewModel = createFakeListViewModel();
        fakeListItemViewModel = createFakeListItemViewModel(createFakeCategoryViewModel(), createFakeUnitViewModel(), createFakeCurrencyViewModel());

        when(getLists.get()).thenReturn(Observable.just(Collections.singletonList(fakeListModel)));

        presenter = new MoveListItemsPresenter(listMapper, listItemsMapper, getLists, moveToList);

        verify(getLists).get();
    }

    @Test
    public void onPositiveButtonClick() throws Exception {
        when(moveToList.init(anyCollectionOf(ListItemModel.class), eq(fakeListViewModel.getId()), eq(false))).thenReturn(moveToList);
        when(moveToList.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter(false);
        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(fakeListViewModel);

        verify(moveToList).get();
        verify(moveToList).init(anyCollectionOf(ListItemModel.class), eq(fakeListViewModel.getId()), eq(false));

        verify(mockView).showLoading();
//        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
    }

    private void callOnCreateInPresenter(boolean isCopy) {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(ListViewModel.class.getName())).thenReturn(fakeListViewModel);
        when(bundle.getParcelableArrayList(ListItemViewModel.class.getName()))
                .thenReturn(new ArrayList<>(Collections.singletonList(fakeListItemViewModel)));
        when(bundle.getBoolean(Const.IS_COPY)).thenReturn(isCopy);
        presenter.onCreate(bundle, null);
    }
}