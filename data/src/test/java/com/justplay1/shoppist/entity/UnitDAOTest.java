package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.MatrixCursor;

import com.justplay1.shoppist.ApplicationTestCase;

import org.junit.Test;

import static com.justplay1.shoppist.entity.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_SHORT_NAME;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitDAO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class UnitDAOTest extends ApplicationTestCase {

    @Test
    public void unitConstructor_HappyCase() {
        UnitDAO model = createFakeUnitDAO();

        String id = model.getId();
        String name = model.getName();
        String shortName = model.getShortName();

        assertThat(id, is(FAKE_ID));
        assertThat(shortName, is(FAKE_SHORT_NAME));
        assertThat(name, is(FAKE_NAME));
    }

    @Test
    public void unitHashCode_HappyCase() {
        UnitDAO model = createFakeUnitDAO();
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void unitEquals_HappyCase() {
        UnitDAO x = createFakeUnitDAO();
        UnitDAO y = createFakeUnitDAO();
        UnitDAO z = createFakeUnitDAO();

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
    public void unitContentValuesBuilder_HappyCase() {
        UnitDAO.Builder builder = new UnitDAO.Builder();
        builder.id(FAKE_ID);
        builder.fullName(FAKE_NAME);
        builder.shortName(FAKE_SHORT_NAME);
        ContentValues contentValues = builder.build();

        assertThat(contentValues.getAsString(UnitDAO.COL_ID), is(FAKE_ID));
        assertThat(contentValues.getAsString(UnitDAO.COL_FULL_NAME), is(FAKE_NAME));
        assertThat(contentValues.getAsString(UnitDAO.COL_SHORT_NAME), is(FAKE_SHORT_NAME));
    }

    @Test
    public void unitMAPPER_HappyCase() {
        MatrixCursor cursor = new MatrixCursor(new String[]{UnitDAO.COL_ID,
                UnitDAO.COL_FULL_NAME,
                UnitDAO.COL_SHORT_NAME});
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(FAKE_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_SHORT_NAME);

        cursor.moveToFirst();

        UnitDAO model = UnitDAO.MAPPER.call(cursor);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getShortName(), is(FAKE_SHORT_NAME));
    }
}