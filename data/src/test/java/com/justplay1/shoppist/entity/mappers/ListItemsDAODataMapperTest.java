package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.CategoryDAO;
import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.entity.ListItemDAO;
import com.justplay1.shoppist.entity.UnitDAO;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.models.UnitModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NOTE;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PRICE;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_STATUS;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListItemDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListItemModel;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemsDAODataMapperTest {

    @Test
    public void transformListItemsDAO() {
        UnitsDAODataMapper unitsDAODataMapper = new UnitsDAODataMapper();
        CategoryDAODataMapper categoryDAODataMapper = new CategoryDAODataMapper();
        CurrencyDAODataMapper currencyDAODataMapper = new CurrencyDAODataMapper();
        ListItemsDAODataMapper dataMapper = new ListItemsDAODataMapper(categoryDAODataMapper, currencyDAODataMapper, unitsDAODataMapper);

        CategoryDAO categoryDAO = createFakeCategoryDAO();
        UnitDAO unitDAO = createFakeUnitDAO();
        CurrencyDAO currencyDAO = createFakeCurrencyDAO();

        CategoryModel categoryModel = categoryDAODataMapper.transformFromDAO(categoryDAO);
        UnitModel unitModel = unitsDAODataMapper.transformFromDAO(unitDAO);
        CurrencyModel currencyModel = currencyDAODataMapper.transformFromDAO(currencyDAO);
        ListItemDAO listItemDAO = createFakeListItemDAO(categoryDAO, unitDAO, currencyDAO);

        ListItemModel listItemModel = dataMapper.transformFromDAO(listItemDAO);

        assertThat(listItemModel, is(instanceOf(ListItemModel.class)));
        assertThat(listItemModel.getId(), is(FAKE_ID));
        assertThat(listItemModel.getName(), is(FAKE_NAME));
        assertThat(listItemModel.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(listItemModel.getNote(), is(FAKE_NOTE));
        assertThat(listItemModel.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(listItemModel.getPrice(), is(FAKE_PRICE));
        assertThat(listItemModel.getPriority(), is(FAKE_PRIORITY));
        assertThat(listItemModel.getQuantity(), is(FAKE_QUANTITY));
        assertThat(listItemModel.getStatus(), is(FAKE_STATUS));
        assertThat(listItemModel.getCategory(), is(categoryModel));
        assertThat(listItemModel.getUnit(), is(unitModel));
        assertThat(listItemModel.getCurrency(), is(currencyModel));
    }

    @Test
    public void transformListItemsDAOCollection() {
        UnitsDAODataMapper unitsDAODataMapper = new UnitsDAODataMapper();
        CategoryDAODataMapper categoryDAODataMapper = new CategoryDAODataMapper();
        CurrencyDAODataMapper currencyDAODataMapper = new CurrencyDAODataMapper();
        ListItemsDAODataMapper dataMapper = new ListItemsDAODataMapper(categoryDAODataMapper, currencyDAODataMapper, unitsDAODataMapper);

        ListItemDAO mockDAOOne = mock(ListItemDAO.class);
        ListItemDAO mockDAOTwo = mock(ListItemDAO.class);

        List<ListItemDAO> list = new ArrayList<>(5);
        list.add(mockDAOOne);
        list.add(mockDAOTwo);

        Collection<ListItemModel> collection = dataMapper.transformFromDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListItemModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListItemModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformListItemModel() {
        UnitsDAODataMapper unitsDAODataMapper = new UnitsDAODataMapper();
        CategoryDAODataMapper categoryDAODataMapper = new CategoryDAODataMapper();
        CurrencyDAODataMapper currencyDAODataMapper = new CurrencyDAODataMapper();
        ListItemsDAODataMapper dataMapper = new ListItemsDAODataMapper(categoryDAODataMapper, currencyDAODataMapper, unitsDAODataMapper);

        CategoryModel categoryModel = createFakeCategoryModel();
        UnitModel unitModel = createFakeUnitModel();
        CurrencyModel currencyModel = createFakeCurrencyModel();

        CategoryDAO categoryDAO = categoryDAODataMapper.transformToDAO(categoryModel);
        UnitDAO unitDAO = unitsDAODataMapper.transformToDAO(unitModel);
        CurrencyDAO currencyDAO = currencyDAODataMapper.transformToDAO(currencyModel);

        ListItemModel listItemModel = createFakeListItemModel(categoryModel, unitModel, currencyModel);

        ListItemDAO listItemDAO = dataMapper.transformToDAO(listItemModel);

        assertThat(listItemDAO, is(instanceOf(ListItemDAO.class)));
        assertThat(listItemDAO.getId(), is(FAKE_ID));
        assertThat(listItemDAO.getName(), is(FAKE_NAME));
        assertThat(listItemDAO.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(listItemDAO.getNote(), is(FAKE_NOTE));
        assertThat(listItemDAO.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(listItemDAO.getPrice(), is(FAKE_PRICE));
        assertThat(listItemDAO.getPriority(), is(FAKE_PRIORITY));
        assertThat(listItemDAO.getQuantity(), is(FAKE_QUANTITY));
        assertThat(listItemDAO.getStatus(), is(FAKE_STATUS));
        assertThat(listItemDAO.getCategory(), is(categoryDAO));
        assertThat(listItemDAO.getUnit(), is(unitDAO));
        assertThat(listItemDAO.getCurrency(), is(currencyDAO));
    }

    @Test
    public void transformListItemModelCollection() {
        UnitsDAODataMapper unitsDAODataMapper = new UnitsDAODataMapper();
        CategoryDAODataMapper categoryDAODataMapper = new CategoryDAODataMapper();
        CurrencyDAODataMapper currencyDAODataMapper = new CurrencyDAODataMapper();
        ListItemsDAODataMapper dataMapper = new ListItemsDAODataMapper(categoryDAODataMapper, currencyDAODataMapper, unitsDAODataMapper);

        ListItemModel mockModelOne = mock(ListItemModel.class);
        ListItemModel mockModelTwo = mock(ListItemModel.class);

        List<ListItemModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<ListItemDAO> collection = dataMapper.transformToDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListItemDAO.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListItemDAO.class)));
        assertThat(collection.size(), is(2));
    }
}