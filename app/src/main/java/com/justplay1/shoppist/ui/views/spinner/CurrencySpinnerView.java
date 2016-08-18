package com.justplay1.shoppist.ui.views.spinner;

import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;

import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.models.Currency;

/**
 * Created by Mkhytar on 02.02.2016.
 */
public class CurrencySpinnerView extends BaseSpinnerView<Currency> {

    public CurrencySpinnerView(Context context) {
        super(context);
    }

    public CurrencySpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CurrencySpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CurrencySpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected String getColumnName() {
        return ShoppingListContact.Currencies.NAME;
    }

    @Override
    protected Currency getItemInstance(Cursor cursor) {
        return new Currency(cursor);
    }
}
