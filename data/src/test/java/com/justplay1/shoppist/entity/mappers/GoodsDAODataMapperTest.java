package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.UnitModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.entity.TestUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.entity.TestUtil.createFakeProductDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeProductModel;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsDAODataMapperTest {

    @Test
    public void transformGoodsDAO() {
        UnitsDAODataMapper unitsDAODataMapper = new UnitsDAODataMapper();
        CategoryDAODataMapper categoryDAODataMapper = new CategoryDAODataMapper();
        GoodsDAODataMapper dataMapper = new GoodsDAODataMapper(unitsDAODataMapper, categoryDAODataMapper);

        CategoryDAO categoryDAO = createFakeCategoryDAO();
        CategoryModel categoryModel = categoryDAODataMapper.transformFromDAO(categoryDAO);

        UnitDAO unitDAO = createFakeUnitDAO();
        UnitModel unitModel = unitsDAODataMapper.transformFromDAO(unitDAO);

        ProductDAO productDAO = createFakeProductDAO(unitDAO, categoryDAO);

        ProductModel productModel = dataMapper.transformFromDAO(productDAO);

        assertThat(productModel, is(instanceOf(ProductModel.class)));
        assertThat(productModel.getId(), is(FAKE_ID));
        assertThat(productModel.getName(), is(FAKE_NAME));
        assertThat(productModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(productModel.isCreateByUser(), is(FAKE_CREATE_BY_USER));
        assertThat(productModel.getCategory(), is(categoryModel));
        assertThat(productModel.getUnit(), is(unitModel));
    }

    @Test
    public void transformGoodsDAOCollection() {
        GoodsDAODataMapper dataMapper = new GoodsDAODataMapper(new UnitsDAODataMapper(), new CategoryDAODataMapper());

        ProductDAO mockDAOOne = mock(ProductDAO.class);
        ProductDAO mockDAOTwo = mock(ProductDAO.class);

        List<ProductDAO> list = new ArrayList<>(5);
        list.add(mockDAOOne);
        list.add(mockDAOTwo);

        Collection<ProductModel> collection = dataMapper.transformFromDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(ProductModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ProductModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformGoodsModel() {
        UnitsDAODataMapper unitsDAODataMapper = new UnitsDAODataMapper();
        CategoryDAODataMapper categoryDAODataMapper = new CategoryDAODataMapper();
        GoodsDAODataMapper dataMapper = new GoodsDAODataMapper(unitsDAODataMapper, categoryDAODataMapper);

        CategoryModel categoryModel = createFakeCategoryModel();
        CategoryDAO categoryDAO = categoryDAODataMapper.transformToDAO(categoryModel);

        UnitModel unitModel = createFakeUnitModel();
        UnitDAO unitDAO = unitsDAODataMapper.transformToDAO(unitModel);

        ProductModel productModel = createFakeProductModel(unitModel, categoryModel);

        ProductDAO dao = dataMapper.transformToDAO(productModel);

        assertThat(dao, is(instanceOf(ProductDAO.class)));
        assertThat(dao.getId(), is(FAKE_ID));
        assertThat(dao.getName(), is(FAKE_NAME));
        assertThat(dao.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(dao.isCreateByUser(), is(FAKE_CREATE_BY_USER));
        assertThat(dao.getCategory(), is(categoryDAO));
        assertThat(dao.getUnit(), is(unitDAO));
    }

    @Test
    public void transformGoodsModelCollection() {
        GoodsDAODataMapper dataMapper = new GoodsDAODataMapper(new UnitsDAODataMapper(), new CategoryDAODataMapper());

        ProductModel mockModelOne = mock(ProductModel.class);
        ProductModel mockModelTwo = mock(ProductModel.class);

        List<ProductModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<ProductDAO> collection = dataMapper.transformToDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(ProductDAO.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ProductDAO.class)));
        assertThat(collection.size(), is(2));
    }
}