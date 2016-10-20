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

import static com.justplay1.shoppist.ModelUtil.FAKE_ID;
import static com.justplay1.shoppist.ModelUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.ModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ModelUtil.createFakeListItemModelList;
import static com.justplay1.shoppist.ModelUtil.createFakeUnitModel;
import static org.mockito.Matchers.anyObject;
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

    private List<ListItemModel> models;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new MoveToList(mockDeleteListItems, mockAddListItems, mockThreadExecutor, mockPostExecutionThread);

        models = createFakeListItemModelList(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
    }

    @Test
    public void moveToListUseCase_HappyCase() {
        when(mockAddListItems.init(anyObject())).thenReturn(mockAddListItems);
        when(mockDeleteListItems.init(anyObject())).thenReturn(mockDeleteListItems);

        when(mockAddListItems.buildUseCaseObservable()).thenReturn(Observable.just(true));
        when(mockDeleteListItems.buildUseCaseObservable()).thenReturn(Observable.just(true));
        useCase.init(models, FAKE_ID, false).buildUseCaseObservable().subscribe();

        verify(mockAddListItems).buildUseCaseObservable();
        verify(mockDeleteListItems).buildUseCaseObservable();
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }

    @Test
    public void copyToListUseCase_HappyCase() {
        when(mockAddListItems.init(anyObject())).thenReturn(mockAddListItems);
        when(mockAddListItems.buildUseCaseObservable()).thenReturn(Observable.just(true));
        useCase.init(models, FAKE_ID, true).buildUseCaseObservable().subscribe();

        verify(mockAddListItems).buildUseCaseObservable();
        verifyNoMoreInteractions(mockDeleteListItems);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
