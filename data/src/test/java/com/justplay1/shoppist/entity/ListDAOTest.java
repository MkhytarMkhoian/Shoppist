package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.MatrixCursor;

import com.justplay1.shoppist.ApplicationTestCase;

import org.hamcrest.core.Is;
import org.junit.Test;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_BOUGHT_COUNT;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_COLOR;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_SIZE;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListDAO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListDAOTest extends ApplicationTestCase {

    @Test
    public void listConstructor_HappyCase() {
        ListDAO model = createFakeListDAO();

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getColor(), is(FAKE_COLOR));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getBoughtCount(), is(FAKE_BOUGHT_COUNT));
        assertThat(model.getPriority(), is(FAKE_PRIORITY));
        assertThat(model.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(model.getSize(), is(FAKE_SIZE));
    }

    @Test
    public void listHashCode_HappyCase() {
        ListDAO model = createFakeListDAO();
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void listEquals_HappyCase() {
        ListDAO x = createFakeListDAO();
        ListDAO y = createFakeListDAO();
        ListDAO z = createFakeListDAO();

        // reflection rule
        assertEquals(x, x);

        // symmetry rule
        assertEquals(x, y);
        assertEquals(y, x);

        // transitivity rule
        assertEquals(x, y);
        assertEquals(y, z);
        assertEquals(x, z);

        assertNotEquals(x, null);
    }

    @Test
    public void listContentValuesBuilder_HappyCase() {
        ListDAO.Builder builder = new ListDAO.Builder();
        builder.id(FAKE_ID);
        builder.name(FAKE_NAME);
        builder.color(FAKE_COLOR);
        builder.priority(FAKE_PRIORITY);
        builder.timeCreated(FAKE_TIME_CREATED);
        ContentValues contentValues = builder.build();

        assertThat(contentValues.getAsString(ListDAO.COL_ID), is(FAKE_ID));
        assertThat(contentValues.getAsString(ListDAO.COL_NAME), is(FAKE_NAME));
        assertThat(contentValues.getAsInteger(ListDAO.COL_COLOR), is(FAKE_COLOR));
        assertThat(contentValues.getAsInteger(ListDAO.COL_PRIORITY), is(FAKE_PRIORITY));
        assertThat(contentValues.getAsLong(ListDAO.COL_TIME_CREATED), is(FAKE_TIME_CREATED));
    }

    @Test
    public void listMAPPER_HappyCase() {
        MatrixCursor cursor = new MatrixCursor(new String[]{ListDAO.COL_ID,
                ListDAO.COL_NAME,
                ListDAO.COL_COLOR,
                ListDAO.COL_BOUGHT_COUNT,
                ListDAO.COL_SIZE,
                ListDAO.COL_PRIORITY,
                ListDAO.COL_TIME_CREATED});
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(FAKE_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_COLOR);
        rowBuilder.add(FAKE_BOUGHT_COUNT);
        rowBuilder.add(FAKE_SIZE);
        rowBuilder.add(FAKE_PRIORITY);
        rowBuilder.add(FAKE_TIME_CREATED);

        cursor.moveToFirst();

        ListDAO model = ListDAO.MAPPER.call(cursor);

        assertThat(model.getId(), Is.is(FAKE_ID));
        assertThat(model.getName(), Is.is(FAKE_NAME));
        assertEquals(model.getColor(), FAKE_COLOR);
        assertEquals(model.getBoughtCount(), FAKE_BOUGHT_COUNT);
        assertEquals(model.getSize(), FAKE_SIZE);
        assertEquals(model.getPriority(), FAKE_PRIORITY);
        assertEquals(model.getTimeCreated(), FAKE_TIME_CREATED);
    }
}