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
package com.justplay1.shoppist.interactor.goods;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.repository.GoodsRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static com.justplay1.shoppist.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.TestUtil.createFakeProductModel;
import static com.justplay1.shoppist.TestUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class UpdateGoodsTest {

    private UpdateGoods useCase;

    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;
    @Mock private GoodsRepository mockGoodsRepository;

    private List<ProductModel> models;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        useCase = new UpdateGoods(mockGoodsRepository, mockThreadExecutor, mockPostExecutionThread);

        models = Collections.singletonList(createFakeProductModel(createFakeUnitModel(), createFakeCategoryModel()));
        useCase.init(models);
    }

    @Test
    public void updateGoodsUseCase_HappyCase() {
        useCase.buildUseCaseObservable().subscribe();

        verify(mockGoodsRepository).update(models);
        verifyNoMoreInteractions(mockGoodsRepository);
        verifyZeroInteractions(mockThreadExecutor);
        verifyZeroInteractions(mockPostExecutionThread);
    }
}
