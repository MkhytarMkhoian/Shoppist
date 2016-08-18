package com.justplay1.shoppist.ui.fragments.dialog;

import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.ui.views.spinner.BaseSpinnerView;

/**
 * Created by Mkhytar on 01.02.2016.
 */
public abstract class BaseSelectItemDialogFragment<T extends BaseModel> extends BaseDialogFragment<T> implements LoaderManager.LoaderCallbacks<Cursor> {

    protected BaseSpinnerView<T> mSelectView;

    @Override
    public void init(View view) {
        super.init(view);
        mSelectView = (BaseSpinnerView<T>) view.findViewById(R.id.custom_spinner);
        mSelectView.setOnAddBtnClickListener(this);
        mSelectView.setOnEditBtnClickListener(this);
    }
}
