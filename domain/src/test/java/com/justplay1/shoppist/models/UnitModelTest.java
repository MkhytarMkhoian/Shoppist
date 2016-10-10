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

package com.justplay1.shoppist.models;

import com.justplay1.shoppist.TestUtil;

import org.junit.Before;
import org.junit.Test;

import static com.justplay1.shoppist.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.TestUtil.FAKE_NAME;
import static com.justplay1.shoppist.TestUtil.FAKE_SHORT_NAME;
import static com.justplay1.shoppist.TestUtil.createFakeUnitModel;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */

public class UnitModelTest {

    private UnitModel model;

    @Before
    public void setUp() {
        model = createFakeUnitModel();
    }

    @Test
    public void unitConstructor_HappyCase() {
        String id = model.getId();
        String name = model.getName();
        String shortName = model.getShortName();

        assertThat(id, is(FAKE_ID));
        assertThat(shortName, is(FAKE_SHORT_NAME));
        assertThat(name, is(FAKE_NAME));
    }
}
