/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.justplay1.shoppist.interactor.listitems;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.models.ListItemModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static com.justplay1.shoppist.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.TestUtil.createFakeListItemModelList;
import static com.justplay1.shoppist.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.TestUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.TestUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MoveToListTest {

    private MoveToList useCase;

    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;
    @Mock private DeleteListItems mockDeleteListItems;
    @Mock private AddListItems mockAddListItems;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new MoveToList(mockDeleteListItems, mockAddListItems, mockThreadExecutor, mockPostExecutionThread);

        List<ListItemModel> models = createFakeListItemModelList(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
        useCase.setData(models);
        useCase.setNewParentListId(FAKE_ID);
    }

    @Test
    public void moveToListUseCase_HappyCase() {
        useCase.setCopy(false);
        when(mockAddListItems.buildUseCaseObservable()).thenReturn(Observable.just(true));
        when(mockDeleteListItems.buildUseCaseObservable()).thenReturn(Observable.just(true));
        useCase.buildUseCaseObservable().subscribe();

        verify(mockAddListItems).buildUseCaseObservable();
        verify(mockDeleteListItems).buildUseCaseObservable();
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }

    @Test
    public void copyToListUseCase_HappyCase() {
        useCase.setCopy(true);
        when(mockAddListItems.buildUseCaseObservable()).thenReturn(Observable.just(true));
        useCase.buildUseCaseObservable().subscribe();

        verify(mockAddListItems).buildUseCaseObservable();
        verifyNoMoreInteractions(mockDeleteListItems);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
