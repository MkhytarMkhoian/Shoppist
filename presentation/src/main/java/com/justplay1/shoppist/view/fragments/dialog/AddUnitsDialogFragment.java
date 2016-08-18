package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.presenter.AddUnitPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddUnitView;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class AddUnitsDialogFragment extends BaseDialogFragment
        implements AddUnitView {

    @Inject
    AddUnitPresenter mPresenter;

    private OnCompleteListener mCompleteListener;
    private MaterialEditText mFullNameEdit;
    private MaterialEditText mShortNameEdit;

    public AddUnitsDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static AddUnitsDialogFragment newInstance(UnitViewModel unit) {
        Bundle args = new Bundle();
        args.putParcelable(UnitViewModel.class.getName(), unit);
        AddUnitsDialogFragment fragment = new AddUnitsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFullNameEdit.setSelection(mFullNameEdit.getText().length());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.onDestroy();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_units_editor_dialog, container);
    }

    @Override
    public void init(View view) {
        super.init(view);
        mFullNameEdit = (MaterialEditText) view.findViewById(R.id.full_name_edit);
        mShortNameEdit = (MaterialEditText) view.findViewById(R.id.short_name_edit);
        mShortNameEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mFullNameEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mFullNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mShortNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                final String fullName = ShoppistUtils.filterSpace(mFullNameEdit.getText().toString());
                final String shortName = ShoppistUtils.filterSpace(mShortNameEdit.getText().toString());
                mPresenter.onPositiveButtonClick(fullName, shortName);
                break;
            case R.id.negative_button:
                mPresenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void setShortName(String name) {
        mShortNameEdit.setText(name);
    }

    @Override
    public void setFullName(String name) {
        mFullNameEdit.setText(name);
    }

    @Override
    public void setDefaultUpdateTitle() {
        mPositiveButton.setText(R.string.update);
        getDialog().setTitle(R.string.edit_unit);
    }

    @Override
    public void setDefaultNewTitle() {
        getDialog().setTitle(R.string.new_unit);
    }

    @Override
    public void closeDialog() {
        ShoppistUtils.hideKeyboard(getActivity(), mFullNameEdit);
        dismiss();
    }

    @Override
    public void showFullNameIsRequiredError() {
        mFullNameEdit.setError(getString(R.string.full_unit_name_is_required));
    }

    @Override
    public void showShortNameIsRequiredError() {
        mShortNameEdit.setError(getString(R.string.short_unit_name_is_required));
    }

    @Override
    public void onComplete(boolean isUpdate) {
        if (mCompleteListener != null) {
            mCompleteListener.onComplete(true);
        }
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }


    @Override
    public void showError(String message) {

    }

    public void setCompleteListener(OnCompleteListener listener) {
        mCompleteListener = listener;
    }

    public interface OnCompleteListener {

        void onComplete(boolean isUpdate);
    }
}