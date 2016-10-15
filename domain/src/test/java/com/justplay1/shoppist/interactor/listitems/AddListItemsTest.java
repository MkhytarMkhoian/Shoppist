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
import com.justplay1.shoppist.repository.ListItemsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.justplay1.shoppist.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.TestUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.TestUtil.createFakeListItemModelList;
import static com.justplay1.shoppist.TestUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class AddListItemsTest {

    private AddListItems useCase;

    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;
    @Mock private ListItemsRepository mockListItemsRepository;

    private List<ListItemModel> models;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new AddListItems(mockListItemsRepository, mockThreadExecutor, mockPostExecutionThread);

        models = createFakeListItemModelList(createFakeCategoryModel(), createFakeUnitModel(), createFakeCurrencyModel());
        useCase.setData(models);
    }

    @Test
    public void addListItemsUseCase_HappyCase() {
        useCase.buildUseCaseObservable().subscribe();

        verify(mockListItemsRepository).save(models);
        verifyNoMoreInteractions(mockListItemsRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
