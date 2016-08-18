package com.justplay1.shoppist.ui.views.spinner;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseModel;

/**
 * Created by Mkhytar on 02.02.2016.
 */
public abstract class BaseSpinnerView<T extends BaseModel> extends FrameLayout {

    protected Spinner mSpinner;
    protected ImageButton mAddBtn;
    protected ImageButton mEditBtn;

    public BaseSpinnerView(Context context) {
        super(context);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseSpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public BaseSpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseSpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    protected abstract String getColumnName();

    public void init() {
        inflate(getContext(), R.layout.view_custom_spinner, this);

        mAddBtn = (ImageButton) findViewById(R.id.add_button);
        mEditBtn = (ImageButton) findViewById(R.id.edit_button);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        String[] from = new String[]{getColumnName()};
        int[] to = new int[]{android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_spinner_item, null, from, to, 0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    public void setOnAddBtnClickListener(View.OnClickListener listener) {
        mAddBtn.setOnClickListener(listener);
    }

    public void setOnEditBtnClickListener(View.OnClickListener listener) {
        mEditBtn.setOnClickListener(listener);
    }

    public void setAddBtnVisibility(int visibility) {
        mAddBtn.setVisibility(visibility);
    }

    public void setEditBtnVisibility(int visibility) {
        mEditBtn.setVisibility(visibility);
    }

    public Spinner getSpinner() {
        return mSpinner;
    }

    public void swapCursor(Cursor data) {
        ((SimpleCursorAdapter) mSpinner.getAdapter()).swapCursor(data);
    }

    public void setSelection(int position){
        mSpinner.setSelection(position);
    }

    public T getSelectedItem() {
        return getItemInstance((Cursor) mSpinner.getSelectedItem());
    }

    protected abstract T getItemInstance(Cursor cursor);
}
