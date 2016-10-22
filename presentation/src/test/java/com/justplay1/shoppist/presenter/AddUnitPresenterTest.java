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

import com.justplay1.shoppist.interactor.units.AddUnits;
import com.justplay1.shoppist.interactor.units.UpdateUnits;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.view.AddUnitView;

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

import static com.justplay1.shoppist.ViewModelUtil.FAKE_NAME;
import static com.justplay1.shoppist.ViewModelUtil.FAKE_SHORT_NAME;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitViewModel;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AddUnits.class, UpdateUnits.class, Bundle.class})
public class AddUnitPresenterTest {

    private AddUnits addUnits;
    private UpdateUnits updateUnits;
    @Mock
    private AddUnitView mockView;

    private AddUnitPresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addUnits = PowerMockito.mock(AddUnits.class);
        updateUnits = PowerMockito.mock(UpdateUnits.class);

        presenter = new AddUnitPresenter(new UnitsViewModelMapper(), updateUnits, addUnits);
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.getView(), isNull());
    }

    @Test
    public void attachView_EditCurrency_HappyCase() throws Exception {
        UnitViewModel viewModel = callOnCreateInPresenter();

        presenter.attachView(mockView);

        verify(mockView).setDefaultUpdateTitle();
        verify(mockView).setFullName(viewModel.getName());
        verify(mockView).setShortName(viewModel.getShortName());
    }

    @Test
    public void attachView_AddNewCurrency_HappyCase() throws Exception {
        presenter.attachView(mockView);

        verify(mockView).setDefaultNewTitle();
    }

    @Test
    public void onPositiveButtonClick_FullNameIsRequiredError() throws Exception {
        when(addUnits.init(Collections.singletonList(createFakeUnitModel()))).thenReturn(addUnits);
        when(addUnits.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onPositiveButtonClick("", FAKE_SHORT_NAME);

        verify(mockView).showFullNameIsRequiredError();
    }

    @Test
    public void onPositiveButtonClick_ShortNameIsRequiredError() throws Exception {
        when(addUnits.init(Collections.singletonList(createFakeUnitModel()))).thenReturn(addUnits);
        when(addUnits.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(FAKE_NAME, "");

        verify(mockView).showShortNameIsRequiredError();
    }

    @Test
    public void onPositiveButtonClick_AddCurrency_HappyCase() throws Exception {
        when(addUnits.init(anyCollectionOf(UnitModel.class))).thenReturn(addUnits);
        when(addUnits.get()).thenReturn(Observable.just(true));

        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(FAKE_NAME, FAKE_SHORT_NAME);

        verify(addUnits).init(anyCollectionOf(UnitModel.class));
        verify(addUnits).get();

        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
        verify(mockView).onComplete(false);

        verifyZeroInteractions(updateUnits);
    }

    @Test
    public void onPositiveButtonClick_UpdateCurrency_HappyCase() throws Exception {
        when(updateUnits.init(anyCollectionOf(UnitModel.class))).thenReturn(updateUnits);
        when(updateUnits.get()).thenReturn(Observable.just(true));

        callOnCreateInPresenter();
        presenter.attachView(mockView);

        presenter.onPositiveButtonClick(FAKE_NAME, FAKE_SHORT_NAME);

        verify(updateUnits).init(anyCollectionOf(UnitModel.class));
        verify(updateUnits).get();

        verify(mockView).hideLoading();
        verify(mockView).closeDialog();
        verify(mockView).onComplete(true);

        verifyZeroInteractions(addUnits);
    }

    @Test
    public void onNegativeButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onNegativeButtonClick();

        verify(mockView).closeDialog();

        verifyZeroInteractions(updateUnits);
        verifyZeroInteractions(addUnits);
    }

    private UnitViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);
        UnitViewModel viewModel = createFakeUnitViewModel();

        when(bundle.getParcelable(UnitViewModel.class.getName())).thenReturn(viewModel);
        presenter.onCreate(bundle, null);
        return viewModel;
    }
}