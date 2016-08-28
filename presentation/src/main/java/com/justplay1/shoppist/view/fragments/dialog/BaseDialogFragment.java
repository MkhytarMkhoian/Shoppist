package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.component.CustomProgressDialog;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public abstract class BaseDialogFragment extends AppCompatDialogFragment
        implements View.OnClickListener {

    @Inject
    protected ShoppistPreferences mPreferences;
    protected Button mPositiveButton;
    protected Button mNegativeButton;
    protected CustomProgressDialog mProgressDialog;

    protected abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    /**
     * Inject the dependencies
     */
    protected void injectDependencies() {
        App.get().getAppComponent().inject(this);
    }

    public void init(View view) {
        mPositiveButton = (Button) view.findViewById(R.id.positive_button);
        mNegativeButton = (Button) view.findViewById(R.id.negative_button);
        mPositiveButton.setTextColor(mPreferences.getColorPrimary());
        mNegativeButton.setTextColor(mPreferences.getColorPrimary());
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);

        mProgressDialog = new CustomProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.please_wait));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog);
        View view = inflater.inflate(getLayoutId(), container, false);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    protected void showError(Exception e, EditText editText) {
        ShoppistUtils.hideKeyboard(getContext(), editText);
        dismiss();
    }
}