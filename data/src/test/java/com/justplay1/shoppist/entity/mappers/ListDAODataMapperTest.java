package com.justplay1.shoppist.entity.mappers;

import com.justplay1.shoppist.entity.ListDAO;
import com.justplay1.shoppist.models.ListModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_BOUGHT_COUNT;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_COLOR;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_SIZE;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListModel;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListDAODataMapperTest {

    private  ListDAODataMapper dataMapper;

    @Before
    public void setUp() throws Exception {
        dataMapper = new ListDAODataMapper();
    }

    @Test
    public void transformListDAO() {
        ListDAO dao = createFakeListDAO();

        ListModel model = dataMapper.transformFromDAO(dao);

        assertThat(model, is(instanceOf(ListModel.class)));
        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(model.getBoughtCount(), is(FAKE_BOUGHT_COUNT));
        assertThat(model.getSize(), is(FAKE_SIZE));
        assertThat(model.getColor(), is(FAKE_COLOR));
        assertThat(model.getPriority(), is(FAKE_PRIORITY));
    }

    @Test
    public void transformListDAOCollection() {
        ListDAO mockDAOOne = mock(ListDAO.class);
        ListDAO mockDAOTwo = mock(ListDAO.class);

        List<ListDAO> list = new ArrayList<>(5);
        list.add(mockDAOOne);
        list.add(mockDAOTwo);

        Collection<ListModel> collection = dataMapper.transformFromDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListModel.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListModel.class)));
        assertThat(collection.size(), is(2));
    }

    @Test
    public void transformListModel() {
        ListModel model = createFakeListModel();

        ListDAO dao = dataMapper.transformToDAO(model);

        assertThat(dao, is(instanceOf(ListDAO.class)));
        assertThat(dao.getId(), is(FAKE_ID));
        assertThat(dao.getName(), is(FAKE_NAME));
        assertThat(dao.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(dao.getBoughtCount(), is(FAKE_BOUGHT_COUNT));
        assertThat(dao.getSize(), is(FAKE_SIZE));
        assertThat(dao.getColor(), is(FAKE_COLOR));
        assertThat(dao.getPriority(), is(FAKE_PRIORITY));
    }

    @Test
    public void transformListModelCollection() {
        ListModel mockModelOne = mock(ListModel.class);
        ListModel mockModelTwo = mock(ListModel.class);

        List<ListModel> list = new ArrayList<>(5);
        list.add(mockModelOne);
        list.add(mockModelTwo);

        Collection<ListDAO> collection = dataMapper.transformToDAO(list);

        assertThat(collection.toArray()[0], is(instanceOf(ListDAO.class)));
        assertThat(collection.toArray()[1], is(instanceOf(ListDAO.class)));
        assertThat(collection.size(), is(2));
    }
}