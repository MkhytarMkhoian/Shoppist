package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.MatrixCursor;

import com.justplay1.shoppist.ApplicationTestCase;

import org.junit.Test;

import static com.justplay1.shoppist.entity.TestUtil.FAKE_CATEGORY_ID;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_COLOR;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_CREATE_BY_USER;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_SHORT_NAME;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.TestUtil.FAKE_UNIT_ID;
import static com.justplay1.shoppist.entity.TestUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeProductDAO;
import static com.justplay1.shoppist.entity.TestUtil.createFakeUnitDAO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ProductDAOTest extends ApplicationTestCase {

    @Test
    public void productConstructor_HappyCase() {
        UnitDAO unitModel = createFakeUnitDAO();
        CategoryDAO categoryModel = createFakeCategoryDAO();
        ProductDAO model = createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO());

        String id = model.getId();
        String name = model.getName();
        boolean isCreateByUser = model.isCreateByUser();
        CategoryDAO category = model.getCategory();
        UnitDAO unit = model.getUnit();

        assertThat(id, is(FAKE_ID));
        assertThat(name, is(FAKE_NAME));
        assertThat(isCreateByUser, is(FAKE_CREATE_BY_USER));

        assertEquals(categoryModel, category);
        assertEquals(unitModel, unit);
    }

    @Test
    public void productHashCode_HappyCase() {
        ProductDAO model = createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO());
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode()));
    }

    @Test
    public void productEquals_HappyCase() {
        ProductDAO x = createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO());
        ProductDAO y = createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO());
        ProductDAO z = createFakeProductDAO(createFakeUnitDAO(), createFakeCategoryDAO());

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
    public void productContentValuesBuilder_HappyCase() {
        ProductDAO.Builder builder = new ProductDAO.Builder();
        builder.id(FAKE_ID);
        builder.name(FAKE_NAME);
        builder.unitId(FAKE_UNIT_ID);
        builder.isCreateByUser(FAKE_CREATE_BY_USER);
        builder.timeCreated(FAKE_TIME_CREATED);
        ContentValues contentValues = builder.build();

        assertThat(contentValues.getAsString(ProductDAO.COL_ID), is(FAKE_ID));
        assertThat(contentValues.getAsString(ProductDAO.COL_NAME), is(FAKE_NAME));
        assertThat(contentValues.getAsString(ProductDAO.COL_UNIT_ID), is(FAKE_UNIT_ID));
        assertThat(contentValues.getAsLong(ProductDAO.COL_TIME_CREATED), is(FAKE_TIME_CREATED));
        assertThat(contentValues.getAsBoolean(ProductDAO.COL_IS_CREATE_BY_USER), is(FAKE_CREATE_BY_USER));
    }

    @Test
    public void productMAPPER_HappyCase() {
        MatrixCursor cursor = new MatrixCursor(new String[]{ProductDAO.COL_ID,
                ProductDAO.COL_NAME,
                ProductDAO.COL_CATEGORY_ID,
                ProductDAO.COL_UNIT_ID,
                ProductDAO.COL_IS_CREATE_BY_USER,
                ProductDAO.COL_TIME_CREATED,

                UnitDAO.COL_ID,
                UnitDAO.COL_FULL_NAME,
                UnitDAO.COL_SHORT_NAME,

                CategoryDAO.COL_ID,
                CategoryDAO.COL_NAME,
                CategoryDAO.COL_COLOR,
                CategoryDAO.COL_CREATE_BY_USER});
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(FAKE_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_CATEGORY_ID);
        rowBuilder.add(FAKE_UNIT_ID);
        rowBuilder.add(1);
        rowBuilder.add(FAKE_TIME_CREATED);

        rowBuilder.add(FAKE_UNIT_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_SHORT_NAME);

        rowBuilder.add(FAKE_CATEGORY_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_COLOR);
        rowBuilder.add(1);

        cursor.moveToFirst();

        ProductDAO model = ProductDAO.MAPPER.call(cursor);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getCategory().getId(), is(FAKE_CATEGORY_ID));
        assertThat(model.getUnit().getId(), is(FAKE_UNIT_ID));
        assertEquals(model.isCreateByUser(), FAKE_CREATE_BY_USER);
        assertEquals(model.getTimeCreated(), FAKE_TIME_CREATED);
    }
}