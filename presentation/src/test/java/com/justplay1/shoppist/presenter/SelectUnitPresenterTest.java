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

import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.view.SelectUnitView;

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

import static com.justplay1.shoppist.ViewModelUtil.createFakeUnitModel;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetUnitsList.class, Bundle.class})
public class SelectUnitPresenterTest {

    private GetUnitsList getUnitsList;

    @Mock
    private SelectUnitView mockView;

    private SelectUnitPresenter presenter;

    private UnitViewModel fakeUnitViewModel;
    private UnitModel fakeUnitModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getUnitsList = PowerMockito.mock(GetUnitsList.class);

        UnitsViewModelMapper mapper = new UnitsViewModelMapper();
        fakeUnitModel = createFakeUnitModel();
        fakeUnitViewModel = mapper.transformToViewModel(fakeUnitModel);

        when(getUnitsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeUnitModel)));

        presenter = new SelectUnitPresenter(mapper, getUnitsList);
    }

    @Test
    public void onPositiveButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onPositiveButtonClick(fakeUnitViewModel);

        verify(mockView).closeDialog();
        verify(mockView).onComplete(fakeUnitViewModel, false);
    }

    @Test
    public void onNegativeButtonClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onNegativeButtonClick();

        verify(mockView).closeDialog();
    }

    @Test
    public void onAddUnitClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onAddUnitClick();

        verify(mockView).showUnitDialog(null);
    }

    @Test
    public void onEditUnitClick_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.onEditUnitClick(fakeUnitViewModel);

        verify(mockView).showUnitDialog(fakeUnitViewModel);
    }

    private UnitViewModel callOnCreateInPresenter() {
        Bundle bundle = PowerMockito.mock(Bundle.class);

        when(bundle.getParcelable(UnitViewModel.class.getName())).thenReturn(fakeUnitViewModel);
        presenter.onCreate(bundle, null);
        return fakeUnitViewModel;
    }
}