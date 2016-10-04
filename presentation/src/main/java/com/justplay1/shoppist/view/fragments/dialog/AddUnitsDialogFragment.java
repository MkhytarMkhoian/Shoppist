/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerUnitsComponent;
import com.justplay1.shoppist.di.components.UnitsComponent;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.presenter.AddUnitPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddUnitView;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddUnitsDialogFragment extends BaseDialogFragment
        implements AddUnitView {

    @Inject
    AddUnitPresenter presenter;

    private OnCompleteListener onCompleteListener;
    private MaterialEditText fullNameEdit;
    private MaterialEditText shortNameEdit;

    public static AddUnitsDialogFragment newInstance(UnitViewModel unit) {
        Bundle args = new Bundle();
        args.putParcelable(UnitViewModel.class.getName(), unit);
        AddUnitsDialogFragment fragment = new AddUnitsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        UnitsComponent component = getInjector(UnitsComponent.class);
        if (component == null) {
            component = DaggerUnitsComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(UnitsComponent.class.getName(), component);
        }
        component.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fullNameEdit.setSelection(fullNameEdit.getText().length());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_units_editor_dialog;
    }

    @Override
    public void init(View view) {
        super.init(view);
        fullNameEdit = (MaterialEditText) view.findViewById(R.id.full_name_edit);
        shortNameEdit = (MaterialEditText) view.findViewById(R.id.short_name_edit);
        shortNameEdit.setPrimaryColor(preferences.getColorPrimary());
        fullNameEdit.setPrimaryColor(preferences.getColorPrimary());
        fullNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        shortNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                final String fullName = ShoppistUtils.filterSpace(fullNameEdit.getText().toString());
                final String shortName = ShoppistUtils.filterSpace(shortNameEdit.getText().toString());
                presenter.onPositiveButtonClick(fullName, shortName);
                break;
            case R.id.negative_button:
                presenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void setShortName(String name) {
        shortNameEdit.setText(name);
    }

    @Override
    public void setFullName(String name) {
        fullNameEdit.setText(name);
    }

    @Override
    public void setDefaultUpdateTitle() {
        positiveButton.setText(R.string.update);
        getDialog().setTitle(R.string.edit_unit);
    }

    @Override
    public void setDefaultNewTitle() {
        getDialog().setTitle(R.string.new_unit);
    }

    @Override
    public void closeDialog() {
        ShoppistUtils.hideKeyboard(getActivity(), fullNameEdit);
        dismiss();
    }

    @Override
    public void showFullNameIsRequiredError() {
        fullNameEdit.setError(getString(R.string.full_unit_name_is_required));
    }

    @Override
    public void showShortNameIsRequiredError() {
        shortNameEdit.setError(getString(R.string.short_unit_name_is_required));
    }

    @Override
    public void onComplete(boolean isUpdate) {
        if (onCompleteListener != null) {
            onCompleteListener.onComplete(true);
        }
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    public void setCompleteListener(OnCompleteListener listener) {
        onCompleteListener = listener;
    }

    public interface OnCompleteListener {

        void onComplete(boolean isUpdate);
    }
}
