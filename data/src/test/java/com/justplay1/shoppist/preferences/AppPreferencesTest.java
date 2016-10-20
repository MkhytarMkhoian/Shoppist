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

package com.justplay1.shoppist.preferences;

import android.graphics.Color;

import com.justplay1.shoppist.ApplicationTestCase;
import com.justplay1.shoppist.entity.SortTypeDAO;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AppPreferencesTest extends ApplicationTestCase{

    private AppPreferences preferences;

    @Before
    public void setUp() throws Exception {
        preferences = new AppPreferences(RuntimeEnvironment.application);
        preferences.initPreferences();
    }

    @Test
    public void clear() throws Exception {

    }

    @Test
    public void setColorPrimaryDark_HappyCase() throws Exception {
        preferences.setColorPrimaryDark(Color.BLACK);

        assertThat(preferences.getColorPrimaryDark(), is(Color.BLACK));
    }

    @Test
    public void setColorPrimary_HappyCase() throws Exception {
        preferences.setColorPrimary(Color.BLUE);

        assertThat(preferences.getColorPrimary(), is(Color.BLUE));
    }

    @Test
    public void setSortForGoods_HappyCase() throws Exception {
        preferences.setSortForGoods(SortTypeDAO.SORT_BY_CATEGORIES);

        assertThat(preferences.getSortForGoods(), is(SortTypeDAO.SORT_BY_CATEGORIES));
    }

    @Test
    public void setSortForShoppingLists_HappyCase() throws Exception {
        preferences.setSortForShoppingLists(SortTypeDAO.SORT_BY_TIME_CREATED);

        assertThat(preferences.getSortForShoppingLists(), is(SortTypeDAO.SORT_BY_TIME_CREATED));
    }

    @Test
    public void setSortForShoppingListItems_HappyCase() throws Exception {
        preferences.setSortForShoppingListItems(SortTypeDAO.SORT_BY_NAME);

        assertThat(preferences.getSortForShoppingListItems(), is(SortTypeDAO.SORT_BY_NAME));
    }

    @Test
    public void setConfirmDeleteDialog_HappyCase() throws Exception {
        preferences.setConfirmDeleteDialog(true);

        assertThat(preferences.isNeedShowConfirmDeleteDialog(), is(true));
    }

    @Test
    public void setLockScreen_HappyCase() throws Exception {
        preferences.setLockScreen(true);

        assertThat(preferences.isLockScreen(), is(true));
    }

    @Test
    public void setLeftShoppingListItemSwipeAction_HappyCase() throws Exception {
        preferences.setLeftShoppingListItemSwipeAction(22);

        assertThat(preferences.getLeftShoppingListItemSwipeAction(), is(22));
    }

    @Test
    public void setRightShoppingListItemSwipeAction_HappyCase() throws Exception {
        preferences.setRightShoppingListItemSwipeAction(22);

        assertThat(preferences.getRightShoppingListItemSwipeAction(), is(22));
    }

    @Test
    public void setAddButtonClickAction_HappyCase() throws Exception {
        preferences.setAddButtonClickAction(2);

        assertThat(preferences.getAddButtonClickAction(), is(2));
    }

    @Test
    public void setMessageDialog_HappyCase() throws Exception {
        preferences.setMessageDialog(true);

        assertThat(preferences.isNeedShowMessageDialog(), is(true));
    }
}