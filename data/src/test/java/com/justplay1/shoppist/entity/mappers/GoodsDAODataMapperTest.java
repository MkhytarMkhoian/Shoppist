package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.ProductDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.models.UnitModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeProductDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeProductModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class GoodsDAODataMapperTest {

    private UnitsDAODataMapper unitsDAODataMapper;
    private CategoryDAODataMapper categoryDAODataMapper;
    private GoodsDAODataMapper dataMapper;

    @Before
    public void setUp() throws Exception {
        unitsDAODataMapper = new UnitsDAODataMapper();
        categoryDAODataMapper = new CategoryDAODataMapper();
        dataMapper = new GoodsDAODataMapper(unitsDAODataMapper, categoryDAODataMapper);
    }

    @Test
    public void transformGoodsDAO() {
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