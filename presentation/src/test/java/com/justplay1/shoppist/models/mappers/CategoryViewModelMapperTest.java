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

package com.justplay1.shoppist.models.mappers;

import com.justplay1.shoppist.models.CategoryModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryViewModelMapperTest {

    @Before
    public void setUp() throws Exception {
        CategoryViewModelMapper modelDataMapper = new CategoryViewModelMapper();
        CategoryDAO dao = createFakeCategoryDAO();
    }

    @Test
    public void transformCategoryDAO() {
        CategoryDAODataMapper dataMapper = new CategoryDAODataMapper();
        CategoryDAO dao = createFakeCategoryDAO();

        CategoryModel model = dataMapper.transformFromDAO(dao);

        assertThat(model, is(instanceOf(CategoryModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getColor(), is(FAKE_COLOR));
        assertThat(model.isCreateByUser(), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void transformCategoryDAOCollection() {
        CategoryDAODataMapper dataMapper = new CategoryDAODataMapper();

        CategoryDAO mockDAOOne = mock(CategoryDAO.class);
        CategoryDAO mockDAOTwo = mock(CategoryDAO.class);

        List<CategoryDAO> list = new ArrayList<>(5);
        list.add(mockDAOOne);
        list.add(mockDAOTwo);

        Collection<CategoryModel> collection = dataMapper.transformFromDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(CategoryModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CategoryModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformCategoryModel() {
        CategoryDAODataMapper dataMapper = new CategoryDAODataMapper();
        CategoryModel model = createFakeCategoryModel();

        CategoryDAO dao = dataMapper.transformToDAO(model);

        assertThat(dao, is(instanceOf(CategoryDAO.class)));
        assertThat(dao.getId(), is(FAKE_ID));
        assertThat(dao.getName(), is(FAKE_NAME));
        assertThat(dao.getColor(), is(FAKE_COLOR));
        assertThat(dao.isCreateByUser(), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void transformCategoryModelCollection() {
        CategoryDAODataMapper dataMapper = new CategoryDAODataMapper();

        CategoryModel mockModelOne = mock(CategoryModel.class);
        CategoryModel mockModelTwo = mock(CategoryModel.class);

        List<CategoryModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<CategoryDAO> collection = dataMapper.transformToDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(CategoryDAO.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CategoryDAO.class)));
        assertThat(collection.size(), is(2));
    }
}