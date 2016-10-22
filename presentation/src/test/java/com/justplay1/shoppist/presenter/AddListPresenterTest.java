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

import android.graphics.Color;
import android.os.Bundle;

import com.justplay1.shoppist.interactor.lists.AddList;
import com.justplay1.shoppist.interactor.lists.UpdateLists;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.mappers.ListViewModelMapper;
import com.justplay1.shoppist.view.AddListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import rx.Observable;

import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.createFakeListViewModel;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AddList.class, UpdateLists.class, Bundle.class})
public class AddListPresenterTest {

    private AddList addList;
    private UpdateLists updateLists;

    @Mock
    private AddListView mockView;

    private AddListPresenter presenter;
    private ListViewModel fakeListViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addList = PowerMockito.mock(AddList.class);
        updateLists = PowerMockito.mock(UpdateLists.class);

        fakeListViewModel = createFakeListViewModel();
        ListViewModelMapper dataMapper = new ListViewModelMapper();
        presenter = new AddListPresenter(dataMapper, addList, updateLists);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void attachView_EditList_HappyCase() throws Exception {
        callOnCreateInPresenter();
        presenter.selectColor(Color.BLUE);
        presenter.attachView(mockView);

        verify(mockView).setPriority(fakeListViewModel.getPriority());
        verify(mockView).setToolbarTitle(fakeListViewModel.getName());
        verify(mockView).setColorToButton(Color.BLUE);
        verify(mockView).setName(fakeListViewModel.getName());
    }

    @Test
    public void attachView_AddNewList_HappyCase() throws Exception {
        presenter.attachView(mockView);

        verify(mockView).setPriority(Priority.NO_PRIORITY);
        verify(mockView).setDefaultToolbarTitle();
        verify(mockView).setName("");
        verify(mockView).setRandomColor();
    }

    @Test
    public void onColorButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onColorButtonClick();

        verify(mockView).showSelectColorDialog(Color.DKGRAY);
    }

    @Test
    public void onDoneButtonClick_NameIsRequiredError() throws Exception {
        when(addList.init(anyCollectionOf(ListModel.class))).thenReturn(addList);
        when(addList.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onDoneButtonClick();

        verify(mockView).showNameIsRequiredError();
    }

    @Test
    public void onDoneButtonClick_AddCategory_HappyCase() throws Exception {
        when(addList.init(anyCollectionOf(ListModel.class))).thenReturn(addList);
        when(addList.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.selectName(FAKE_NAME);
        presenter.onDoneButtonClick();

        verify(addList).init(anyCollectionOf(ListModel.class));
        verify(addList).get();
        verify(mockView).closeScreen();
        verifyZeroInteractions(updateLists);
    }

    @Test
    public void onDoneButtonClick_UpdateCategory_HappyCase() throws Exception {
        when(updateLists.init(anyCollectionOf(ListModel.class))).thenReturn(updateLists);
        when(updateLists.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);

        presenter.onDoneButtonClick();

        verify(updateLists).init(anyCollectionOf(ListModel.class));
        verify(updateLists).get();
        verify(mockView).closeScreen();
        verifyZeroInteractions(addList);
    }

    @Test
    public void onDoneButtonLongClick_UpdateCategory_HappyCase() throws Exception {
        when(updateLists.init(anyCollectionOf(ListModel.class))).thenReturn(updateLists);
        when(updateLists.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);

        presenter.onDoneButtonLongClick();

        verify(updateLists).init(anyCollectionOf(ListModel.class));
        verify(updateLists).get();

        verify(mockView).showKeyboard();
        verify(mockView).showElementUpdatedMessage();
        verify(mockView).setDefaultToolbarTitle();
        verify(mockView).setName("");
        verify(mockView).setColorToButton(Color.DKGRAY);

        verifyZeroInteractions(addList);
    }

    @Test
    public void onDoneButtonLongClick_AddCategory_HappyCase() throws Exception {
        when(addList.init(anyCollectionOf(ListModel.class))).thenReturn(addList);
        when(addList.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.selectName(FAKE_NAME);
        presenter.onDoneButtonLongClick();

        verify(addList).init(anyCollectionOf(ListModel.class));
        verify(addList).get();

        verify(mockView).showKeyboard();
        verify(mockView).showNewElementAddedMessage();
        verify(mockView, times(2)).setDefaultToolbarTitle();
        verify(mockView, times(2)).setName("");
        verify(mockView).setColorToButton(Color.DKGRAY);

        verifyZeroInteractions(updateLists);
    }

    private ListViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(ListViewModel.class.getName())).thenReturn(fakeListViewModel);
        presenter.onCreate(bundle, null);
        return fakeListViewModel;
    }
}