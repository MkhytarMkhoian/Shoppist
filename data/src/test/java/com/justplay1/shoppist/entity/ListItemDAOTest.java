package com.justplay1.shoppist.entity;

import android.content.ContentValues;
import android.database.MatrixCursor;

import com.justplay1.shoppist.ApplicationTestCase;

import org.junit.Test;

import static com.justplay1.shoppist.entity.DAOUtil.FAKE_CATEGORY_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_COLOR;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_CURRENCY_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_NOTE;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PARENT_LIST_ID;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PRICE;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_PRIORITY;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_QUANTITY;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_SHORT_NAME;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_STATUS;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_TIME_CREATED;
import static com.justplay1.shoppist.entity.DAOUtil.FAKE_UNIT_ID;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCategoryDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeCurrencyDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeListItemDAO;
import static com.justplay1.shoppist.entity.DAOUtil.createFakeUnitDAO;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListItemDAOTest extends ApplicationTestCase {

    @Test
    public void listItemConstructor_HappyCase() {
        CurrencyDAO currencyModel = createFakeCurrencyDAO();
        UnitDAO unitModel = createFakeUnitDAO();
        CategoryDAO categoryModel = createFakeCategoryDAO();
        ListItemDAO model = createFakeListItemDAO(categoryModel, unitModel, currencyModel);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getNote(), is(FAKE_NOTE));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(model.getPriority(), is(FAKE_PRIORITY));
        assertThat(model.getTimeCreated(), is(FAKE_TIME_CREATED));
        assertThat(model.getStatus(), is(FAKE_STATUS));
        assertThat(model.getPrice(), is(FAKE_PRICE));
        assertThat(model.getQuantity(), is(FAKE_QUANTITY));

        assertEquals(categoryModel, model.getCategory());
        assertEquals(currencyModel, model.getCurrency());
        assertEquals(unitModel, model.getUnit());
    }

    @Test
    public void listItemHashCode_HappyCase() {
        ListItemDAO model = createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO());
        int hashCode = model.hashCode();

        assertThat(hashCode, is(FAKE_ID.hashCode() + FAKE_PARENT_LIST_ID.hashCode()));
    }

    @Test
    public void listItemEquals_HappyCase() {
        ListItemDAO x = createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO());
        ListItemDAO y = createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO());
        ListItemDAO z = createFakeListItemDAO(createFakeCategoryDAO(), createFakeUnitDAO(), createFakeCurrencyDAO());

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
    public void listItemContentValuesBuilder_HappyCase() {
        ListItemDAO.Builder builder = new ListItemDAO.Builder();
        builder.id(FAKE_ID);
        builder.name(FAKE_NAME);
        builder.categoryId(FAKE_CATEGORY_ID);
        builder.currencyId(FAKE_CURRENCY_ID);
        builder.unitId(FAKE_UNIT_ID);
        builder.note(FAKE_NOTE);
        builder.price(FAKE_PRICE);
        builder.quantity(FAKE_QUANTITY);
        builder.priority(FAKE_PRIORITY);
        builder.status(FAKE_STATUS);
        builder.timeCreated(FAKE_TIME_CREATED);
        builder.parentId(FAKE_PARENT_LIST_ID);
        ContentValues contentValues = builder.build();

        assertThat(contentValues.getAsString(ListItemDAO.COL_ID), is(FAKE_ID));
        assertThat(contentValues.getAsString(ListItemDAO.COL_NAME), is(FAKE_NAME));
        assertThat(contentValues.getAsString(ListItemDAO.COL_PARENT_LIST_ID), is(FAKE_PARENT_LIST_ID));
        assertThat(contentValues.getAsInteger(ListItemDAO.COL_PRIORITY), is(FAKE_PRIORITY));
        assertThat(contentValues.getAsLong(ListItemDAO.COL_TIME_CREATED), is(FAKE_TIME_CREATED));
        assertThat(contentValues.getAsDouble(ListItemDAO.COL_PRICE), is(FAKE_PRICE));
        assertThat(contentValues.getAsString(ListItemDAO.COL_CATEGORY_ID), is(FAKE_CATEGORY_ID));
        assertThat(contentValues.getAsString(ListItemDAO.COL_CURRENCY_ID), is(FAKE_CURRENCY_ID));
        assertThat(contentValues.getAsString(ListItemDAO.COL_UNIT_ID), is(FAKE_UNIT_ID));
        assertThat(contentValues.getAsInteger(ListItemDAO.COL_STATUS), is(1)); //FAKE_STATUS
        assertThat(contentValues.getAsString(ListItemDAO.COL_NOTE), is(FAKE_NOTE));
        assertThat(contentValues.getAsDouble(ListItemDAO.COL_QUANTITY), is(FAKE_QUANTITY));
    }

    @Test
    public void listItemMAPPER_HappyCase() {
        MatrixCursor cursor = new MatrixCursor(new String[]{ListItemDAO.COL_ID,
                ListItemDAO.COL_NAME,
                ListItemDAO.COL_CATEGORY_ID,
                ListItemDAO.COL_CURRENCY_ID,
                ListItemDAO.COL_UNIT_ID,
                ListItemDAO.COL_NOTE,
                ListItemDAO.COL_PRICE,
                ListItemDAO.COL_QUANTITY,
                ListItemDAO.COL_PRIORITY,
                ListItemDAO.COL_STATUS,
                ListItemDAO.COL_TIME_CREATED,
                ListItemDAO.COL_PARENT_LIST_ID,

                UnitDAO.COL_ID,
                UnitDAO.COL_FULL_NAME,
                UnitDAO.COL_SHORT_NAME,

                CategoryDAO.COL_ID,
                CategoryDAO.COL_NAME,
                CategoryDAO.COL_COLOR,
                CategoryDAO.COL_CREATE_BY_USER,

                CurrencyDAO.COL_ID,
                CurrencyDAO.COL_NAME});
        MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
        rowBuilder.add(FAKE_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_CATEGORY_ID);
        rowBuilder.add(FAKE_CURRENCY_ID);
        rowBuilder.add(FAKE_UNIT_ID);
        rowBuilder.add(FAKE_NOTE);
        rowBuilder.add(FAKE_PRICE);
        rowBuilder.add(FAKE_QUANTITY);
        rowBuilder.add(FAKE_PRIORITY);
        rowBuilder.add(1);
        rowBuilder.add(FAKE_TIME_CREATED);
        rowBuilder.add(FAKE_PARENT_LIST_ID);

        rowBuilder.add(FAKE_UNIT_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_SHORT_NAME);

        rowBuilder.add(FAKE_CATEGORY_ID);
        rowBuilder.add(FAKE_NAME);
        rowBuilder.add(FAKE_COLOR);
        rowBuilder.add(1);

        rowBuilder.add(FAKE_CURRENCY_ID);
        rowBuilder.add(FAKE_NAME);

        cursor.moveToFirst();

        ListItemDAO model = ListItemDAO.MAPPER.call(cursor);

        assertThat(model.getId(), is(FAKE_ID));
        assertThat(model.getName(), is(FAKE_NAME));
        assertThat(model.getCategory().getId(), is(FAKE_CATEGORY_ID));
        assertThat(model.getUnit().getId(), is(FAKE_UNIT_ID));
        assertThat(model.getCurrency().getId(), is(FAKE_CURRENCY_ID));
        assertThat(model.getNote(), is(FAKE_NOTE));
        assertThat(model.getParentListId(), is(FAKE_PARENT_LIST_ID));
        assertThat(model.getPrice(), is(FAKE_PRICE));
        assertThat(model.getQuantity(), is(FAKE_QUANTITY));
        assertThat(model.getNote(), is(FAKE_NOTE));
        assertEquals(model.getPriority(), FAKE_PRIORITY);
        assertEquals(model.getTimeCreated(), FAKE_TIME_CREATED);
    }
}