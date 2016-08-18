package com.justplay1.shoppist.ui.views.spinner;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;

import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.models.Unit;

/**
 * Created by Mkhytar on 02.02.2016.
 */
public class UnitsSpinnerView extends BaseSpinnerView<Unit> {

    public UnitsSpinnerView(Context context) {
        super(context);
    }

    public UnitsSpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public UnitsSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public UnitsSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected String getColumnName() {
        return ShoppingListContact.Units.FULL_NAME;
    }

    @Override
    protected Unit getItemInstance(Cursor cursor) {
        return new Unit(cursor);
    }
}
