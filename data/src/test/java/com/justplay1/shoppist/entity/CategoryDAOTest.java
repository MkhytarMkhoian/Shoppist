package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.MatrixCursor;

import com.justplay1.shoppist.ApplicationTestCase;

import org.junit.Test;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_COLOR;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class CategoryDAOTest extends ApplicationTestCase {

    @Test
    public void categoryConstructor_HappyCase() {
        CategoryDAO model = createFakeCategoryDAO();

        String id = model.getId();
        String name = model.getName();
        int color = model.getColor();
        boolean isCreateByUser = model.isCreateByUser();

        assertThat(id, is(FAKE_ID));
        assertThat(color, is(FAKE_COLOR));
        assertThat(name, is(FAKE_NAME));
        assertThat(isCreateByUser, is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void categoryHashCode_HappyCase() {
        CategoryDAO model = createFakeCategoryDAO();
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void categoryEquals_HappyCase() {
        CategoryDAO x = createFakeCategoryDAO();
        CategoryDAO y = createFakeCategoryDAO();
        CategoryDAO z = createFakeCategoryDAO();

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
    public void categoryContentValuesBuilder_HappyCase() {
        CategoryDAO.Builder builder = new CategoryDAO.Builder();
        builder.id(FAKE_ID);
        builder.name(FAKE_NAME);
        builder.color(FAKE_COLOR);
        builder.createByUser(FAKE_CREATE_BY_USER);
        ContentValues contentValues = builder.build();

        assertThat(contentValues.getAsString(CategoryDAO.COL_ID), is(FAKE_ID));
        assertThat(contentValues.getAsString(CategoryDAO.COL_NAME), is(FAKE_NAME));
        assertThat(contentValues.getAsInteger(CategoryDAO.COL_COLOR), is(FAKE_COLOR));
        assertThat(contentValues.getAsBoolean(CategoryDAO.COL_CREATE_BY_USER), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void categoryMAPPER_HappyCase() {
        MatrixCursor cursor = new MatrixCursor(new String[]{CategoryDAO.COL_ID,
                CategoryDAO.COL_NAME,
                CategoryDAO.COL_COLOR,
                CategoryDAO.COL_CREATE_BY_USER});
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(FAKE_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_COLOR);
        rowBuilder.add(1);

        cursor.moveToFirst();

        CategoryDAO model = CategoryDAO.MAPPER.call(cursor);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertEquals(model.getColor(), FAKE_COLOR);
        assertEquals(model.isCreateByUser(), FAKE_CREATE_BY_USER);
    }
}