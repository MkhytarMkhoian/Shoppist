package com.justplay1.shoppist.ui.views.spinner;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;

import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.models.Category;

/**
 * Created by Mkhytar on 02.02.2016.
 */
public class CategorySpinnerView extends BaseSpinnerView<Category> {
    public CategorySpinnerView(Context context) {
        super(context);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected String getColumnName() {
        return ShoppingListContact.Categories.NAME;
    }

    @Override
    protected Category getItemInstance(Cursor cursor) {
        return new Category(cursor, ShoppingListContact.Categories.CATEGORY_ID);
    }
}
