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

package com.justplay1.shoppist.features.units;

import com.justplay1.shoppist.features.units.UnitsPresenter;
import com.justplay1.shoppist.interactor.units.DeleteUnits;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.UnitsViewModelMapper;
import com.justplay1.shoppist.features.units.UnitRouter;
import com.justplay1.shoppist.features.units.UnitsView;

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
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mkhytar Mkhoian.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DeleteUnits.class, GetUnitsList.class})
public class UnitsPresenterTest {

    private GetUnitsList getUnitsList;
    private DeleteUnits deleteUnits;

    @Mock
    private UnitsView mockView;
    @Mock
    private UnitRouter mockRouter;

    private UnitsPresenter presenter;

    private UnitViewModel fakeUnitViewModel;
    private UnitModel fakeUnitModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getUnitsList = PowerMockito.mock(GetUnitsList.class);
        deleteUnits = PowerMockito.mock(DeleteUnits.class);

        UnitsViewModelMapper mapper = new UnitsViewModelMapper();
        fakeUnitModel = createFakeUnitModel();
        fakeUnitViewModel = mapper.transformToViewModel(fakeUnitModel);

        when(getUnitsList.get()).thenReturn(Observable.just(Collections.singletonList(fakeUnitModel)));

        presenter = new UnitsPresenter(mapper, getUnitsList, deleteUnits);
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

        verify(mockRouter).openUnitEditDialog(null);
    }

    @Test
    public void onListItemClick_HappyCase() throws Exception {
        presenter.attachRouter(mockRouter);
        presenter.onListItemClick(fakeUnitViewModel);

        verify(mockRouter).openUnitEditDialog(fakeUnitViewModel);
    }

    @Test
    public void deleteItems_HappyCase() throws Exception {
        when(deleteUnits.init(anyCollectionOf(UnitModel.class))).thenReturn(deleteUnits);
        when(deleteUnits.get()).thenReturn(Observable.just(true));

        presenter.deleteItems(Collections.singletonList(fakeUnitViewModel));

        verify(deleteUnits).init(anyCollectionOf(UnitModel.class));
        verify(deleteUnits).get();
    }
}