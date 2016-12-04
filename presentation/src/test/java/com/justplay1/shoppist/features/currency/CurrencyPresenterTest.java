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

package com.justplay1.shoppist.features.currency;

import com.justplay1.shoppist.features.currency.CurrencyPresenter;
import com.justplay1.shoppist.interactor.currency.DeleteCurrency;
import com.justplay1.shoppist.interactor.currency.GetCurrencyList;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.features.currency.CurrencyRouter;
import com.justplay1.shoppist.features.currency.CurrencyView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import rx.Observable;

import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyModel;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DeleteCurrency.class, GetCurrencyList.class})
public class CurrencyPresenterTest {

    private GetCurrencyList getCurrencyList;
    private DeleteCurrency deleteCurrency;

    @Mock
    private CurrencyView mockView;
    @Mock
    private CurrencyRouter mockRouter;

    private CurrencyPresenter presenter;

    private CurrencyViewModel fakeCurrencyViewModel;
    private CurrencyModel fakeCurrencyModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getCurrencyList = PowerMockito.mock(GetCurrencyList.class);
        deleteCurrency = PowerMockito.mock(DeleteCurrency.class);

        CurrencyViewModelMapper mapper = new CurrencyViewModelMapper();
        fakeCurrencyModel = createFakeCurrencyModel();
        fakeCurrencyViewModel = mapper.transformToViewModel(fakeCurrencyModel);

        when(getCurrencyList.get()).thenReturn(Observable.just(Collections.singletonList(fakeCurrencyModel)));

        presenter = new CurrencyPresenter(getCurrencyList, deleteCurrency, mapper);
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.getView(), isNull());
    }

    @Test
    public void onAddButtonClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onAddButtonClick();

        verify(mockRouter).showCurrencyEditDialog(null);
    }

    @Test
    public void onListItemClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onListItemClick(fakeCurrencyViewModel);

        verify(mockRouter).showCurrencyEditDialog(fakeCurrencyViewModel);
    }

    @Test
    public void deleteItems_HappyCase() throws Exception {
        when(deleteCurrency.init(anyCollectionOf(CurrencyModel.class))).thenReturn(deleteCurrency);
        when(deleteCurrency.get()).thenReturn(Observable.just(true));

        presenter.deleteItems(Collections.singletonList(fakeCurrencyViewModel));

        verify(deleteCurrency).init(anyCollectionOf(CurrencyModel.class));
        verify(deleteCurrency).get();
    }
}