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

import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.view.BaseMvpView;
import com.justplay1.shoppist.view.StringView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by Mkhytar Mkhoian.
 */
public class BaseSortablePresenterTest {

    private BaseSortablePresenter<StringView, BaseViewModel, Router> presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new MockBaseSortablePresenter();
    }

    @Test
    public void sort() throws Exception {

    }
}