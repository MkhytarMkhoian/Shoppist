package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.MatrixCursor;

import com.justplay1.shoppist.ApplicationTestCase;

import org.junit.Test;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CurrencyDAOTest extends ApplicationTestCase {

    @Test
    public void currencyConstructor_HappyCase() {
        CurrencyDAO model = createFakeCurrencyDAO();

        String id = model.getId();
        String name = model.getName();

        assertThat(id, is(FAKE_ID));
        assertThat(name, is(FAKE_NAME));
    }

    @Test
    public void currencyHashCode_HappyCase() {
        CurrencyDAO model = createFakeCurrencyDAO();
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void currencyEquals_HappyCase() {
        CurrencyDAO x = createFakeCurrencyDAO();
        CurrencyDAO y = createFakeCurrencyDAO();
        CurrencyDAO z = createFakeCurrencyDAO();

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
    public void currencyContentValuesBuilder_HappyCase() {
        CurrencyDAO.Builder builder = new CurrencyDAO.Builder();
        builder.id(FAKE_ID);
        builder.name(FAKE_NAME);
        ContentValues contentValues = builder.build();

        assertThat(contentValues.getAsString(CurrencyDAO.COL_ID), is(FAKE_ID));
        assertThat(contentValues.getAsString(CurrencyDAO.COL_NAME), is(FAKE_NAME));
    }

    @Test
    public void currencyMAPPER_HappyCase() {
        MatrixCursor cursor = new MatrixCursor(new String[]{CurrencyDAO.COL_ID, CurrencyDAO.COL_NAME});
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(FAKE_ID);
        rowBuilder.add(FAKE_NAME);

        cursor.moveToFirst();

        CurrencyDAO model = CurrencyDAO.MAPPER.call(cursor);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
    }
}