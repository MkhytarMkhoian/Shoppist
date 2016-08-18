package com.justplay1.shoppist.view.fragments.dialog;

import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.view.component.spinner.SpinnerView;

/**
 * Created by Mkhytar on 01.02.2016.
 */
public abstract class BaseSelectItemDialogFragment<T extends BaseViewModel>
        extends BaseDialogFragment {

    protected SpinnerView<T> mSelectView;
    protected OnCompleteListener<T> mCompleteListener;

    @Override
    public void init(View view) {
        super.init(view);
        mSelectView = (SpinnerView<T>) view.findViewById(R.id.custom_spinner);
        mSelectView.setOnAddBtnClickListener(this);
        mSelectView.setOnEditBtnClickListener(this);
    }

    public void setCompleteListener(OnCompleteListener<T> listener) {
        mCompleteListener = listener;
    }

    public interface OnCompleteListener<T> {

        void onComplete(T item, boolean isUpdate);
    }
}
