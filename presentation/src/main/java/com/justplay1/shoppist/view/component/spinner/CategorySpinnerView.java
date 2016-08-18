package com.justplay1.shoppist.view.component.spinner;

import android.content.Context;
import android.util.AttributeSet;

import com.justplay1.shoppist.models.CategoryViewModel;

/**
 * Created by Mkhytar on 02.02.2016.
 */
public class CategorySpinnerView extends SpinnerView<CategoryViewModel> {
    public CategorySpinnerView(Context context) {
        super(context);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CategorySpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
