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

package com.justplay1.shoppist.presenter.base;

import com.justplay1.shoppist.view.BaseMvpView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.Assert.assertEquals;

/**
 * Created by Mkhytar Mkhoian.
 */
public class BasePresenterTest {

    @Mock
    private BaseMvpView mockView;
    private BasePresenter<BaseMvpView> presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new MockBasePresenter();
    }

    @Test
    public void attachView_HappyCase() throws Exception {
        presenter.attachView(mockView);

        assertEquals(presenter.isViewAttached(), true);
        assertEquals(presenter.getView(), mockView);
    }

    @Test
    public void detachView_HappyCase() throws Exception {
        presenter.attachView(mockView);
        presenter.detachView();

        assertEquals(presenter.isViewAttached(), false);
        assertEquals(presenter.getView(), null);
    }

    @Test
    public void isViewAttached_Attached_HappyCase() throws Exception {
        presenter.attachView(mockView);

        assertEquals(presenter.isViewAttached(), true);
    }

    @Test
    public void isViewAttached_NotAttached_HappyCase() throws Exception {
        assertEquals(presenter.isViewAttached(), false);
    }

    @Test
    public void getView_NotNull_HappyCase() throws Exception {
        presenter.attachView(mockView);

        assertEquals(presenter.getView(), mockView);
    }

    @Test
    public void getView_Null_HappyCase() throws Exception {
        assertEquals(presenter.getView(), null);
    }
}