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

import com.justplay1.shoppist.interactor.currency.AddCurrency;
import com.justplay1.shoppist.interactor.currency.UpdateCurrency;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.mappers.CurrencyViewModelMapper;
import com.justplay1.shoppist.view.AddCurrencyView;

import org.junit.After;
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

import static com.justplay1.shoppist.ViewModelUtil.FAKE_CURRENCY_NAME;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeCurrencyViewModel;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AddCurrency.class, UpdateCurrency.class, Bundle.class})
public class AddCurrencyPresenterTest {

    private AddCurrency addCurrency;
    private UpdateCurrency updateCurrency;
    @Mock
    private AddCurrencyView mockView;

    private AddCurrencyPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addCurrency = PowerMockito.mock(AddCurrency.class);
        updateCurrency = PowerMockito.mock(UpdateCurrency.class);

        presenter = new AddCurrencyPresenter(new CurrencyViewModelMapper(), updateCurrency, addCurrency);
    }

    @After
    public void tearDown() throws Exception {
        presenter.detachView();
    }

    @Test
    public void attachView_EditCurrency_HappyCase() throws Exception {
        CurrencyViewModel viewModel = callOnCreateInPresenter();

        presenter.attachView(mockView);

        verify(mockView).setDefaultUpdateTitle();
        verify(mockView).setName(viewModel.getName());
    }

    @Test
    public void attachView_AddNewCurrency_HappyCase() throws Exception {
        presenter.attachView(mockView);

        verify(mockView).setDefaultNewTitle();
    }

    @Test
    public void onPositiveButtonClick_NameIsRequiredError() throws Exception {
        when(addCurrency.init(Collections.singletonList(createFakeCurrencyModel()))).thenReturn(addCurrency);
        when(addCurrency.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onPositiveButtonClick("");

        verify(mockView).showNameIsRequiredError();
    }

    @Test
    public void onPositiveButtonClick_AddCurrency_HappyCase() throws Exception {
        when(addCurrency.init(anyCollectionOf(CurrencyModel.class))).thenReturn(addCurrency);
        when(addCurrency.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(FAKE_CURRENCY_NAME);

        verify(addCurrency).init(anyCollectionOf(CurrencyModel.class));
        verify(addCurrency).get();

        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
        verify(mockView).onComplete(false);

        verifyZeroInteractions(updateCurrency);
    }

    @Test
    public void onPositiveButtonClick_UpdateCurrency_HappyCase() throws Exception {
        when(updateCurrency.init(anyCollectionOf(CurrencyModel.class))).thenReturn(updateCurrency);
        when(updateCurrency.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);

        presenter.onPositiveButtonClick(FAKE_CURRENCY_NAME);

        verify(updateCurrency).init(anyCollectionOf(CurrencyModel.class));
        verify(updateCurrency).get();

        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
        verify(mockView).onComplete(true);

        verifyZeroInteractions(addCurrency);
    }

    @Test
    public void onNegativeButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onNegativeButtonClick();

        verify(mockView).closeDialog();

        verifyZeroInteractions(updateCurrency);
        verifyZeroInteractions(addCurrency);
    }

    private CurrencyViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);
        CurrencyViewModel viewModel = createFakeCurrencyViewModel();

        when(bundle.getParcelable(CurrencyViewModel.class.getName())).thenReturn(viewModel);
        presenter.onCreate(bundle, null);
        return viewModel;
    }
}