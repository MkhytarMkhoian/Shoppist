package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.CurrencyDAO;
import com.justplay1.shoppist.models.CurrencyModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyDAODataMapperTest {

    private CurrencyDAODataMapper dataMapper;

    @Before
    public void setUp() throws Exception {
        dataMapper = new CurrencyDAODataMapper();
    }

    @Test
    public void transformCurrencyDAO() {
        CurrencyDAO dao = createFakeCurrencyDAO();

        CurrencyModel model = dataMapper.transformFromDAO(dao);

        assertThat(model, is(instanceOf(CurrencyModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
    }

    @Test
    public void transformCurrencyDAOCollection() {
        CurrencyDAO mockDAOOne = mock(CurrencyDAO.class);
        CurrencyDAO mockDAOTwo = mock(CurrencyDAO.class);

        List<CurrencyDAO> list = new ArrayList<>(5);
        list.add(mockDAOOne);
        list.add(mockDAOTwo);

        Collection<CurrencyModel> collection = dataMapper.transformFromDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(CurrencyModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CurrencyModel.class)));
        assertThat(collection.size(), is(2));
    }


    @Test
    public void transformCurrencyModel() {
        CurrencyModel model = createFakeCurrencyModel();

        CurrencyDAO dao = dataMapper.transformToDAO(model);

        assertThat(dao, is(instanceOf(CurrencyDAO.class)));
        assertThat(dao.getId(), is(FAKE_ID));
        assertThat(dao.getName(), is(FAKE_NAME));
    }

    @Test
    public void transformCurrencyModelCollection() {
        CurrencyModel mockModelOne = mock(CurrencyModel.class);
        CurrencyModel mockModelTwo = mock(CurrencyModel.class);

        List<CurrencyModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<CurrencyDAO> collection = dataMapper.transformToDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(CurrencyDAO.class)));
        assertThat(collection.toArray()[1], is(instanceOf(CurrencyDAO.class)));
        assertThat(collection.size(), is(2));
    }
}