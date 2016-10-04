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
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerGoodsComponent;
import com.justplay1.shoppist.di.components.GoodsComponent;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.presenter.AddGoodsPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddGoodsView;
import com.justplay1.shoppist.view.component.spinner.CategorySpinnerView;
import com.justplay1.shoppist.view.component.spinner.UnitsSpinnerView;
import com.justplay1.shoppist.view.fragments.AbstractTextWatcher;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddGoodsDialogFragment extends BaseDialogFragment implements AddGoodsView {

    @Inject
    AddGoodsPresenter presenter;

    private OnCompleteListener completeListener;
    private MaterialEditText nameEdit;
    private CategorySpinnerView categoryList;
    private UnitsSpinnerView unitList;

    private static AddGoodsDialogFragment newInstance(ProductViewModel product, String newName) {
        Bundle args = new Bundle();
        args.putParcelable(ProductViewModel.class.getName(), product);
        args.putString(Const.NEW_NAME, newName);
        AddGoodsDialogFragment fragment = new AddGoodsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddGoodsDialogFragment newInstance(String newName) {
        return newInstance(null, newName);
    }

    public static AddGoodsDialogFragment newInstance(ProductViewModel product) {
        return newInstance(product, null);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        GoodsComponent component = getInjector(GoodsComponent.class);
        if (component == null) {
            component = DaggerGoodsComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(GoodsComponent.class.getName(), component);
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_editor_dialog;
    }

    @Override
    public void init(View view) {
        super.init(view);
        TextView categoryLabel = (TextView) view.findViewById(R.id.category_label);
        categoryLabel.setTextColor(preferences.getColorPrimary());
        TextView unitsLabel = (TextView) view.findViewById(R.id.units_label);
        unitsLabel.setTextColor(preferences.getColorPrimary());

        nameEdit = (MaterialEditText) view.findViewById(R.id.goods_name);
        nameEdit.setPrimaryColor(preferences.getColorPrimary());
        nameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        nameEdit.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.setName(ShoppistUtils.filterSpace(s.toString()));
            }
        });

        initCategoryList(view);
        initUnitList(view);
    }

    private void initUnitList(View view) {
        unitList = (UnitsSpinnerView) view.findViewById(R.id.units_spinner_view);
        unitList.setOnAddBtnClickListener(this);
        unitList.setOnEditBtnClickListener(this);
        unitList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setUnit(unitList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initCategoryList(View parent) {
        categoryList = (CategorySpinnerView) parent.findViewById(R.id.category_spinner_view);
        categoryList.setEditBtnVisibility(View.GONE);
        categoryList.setAddBtnVisibility(View.GONE);
        categoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.setCategory(categoryList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nameEdit.setSelection(nameEdit.getText().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                presenter.onPositiveButtonClick();
                break;
            case R.id.negative_button:
                presenter.onNegativeButtonClick();
                break;
            case R.id.add_button:
                presenter.onAddUnitClick();
                break;
            case R.id.edit_button:
                presenter.onEditUnitClick(unitList.getSelectedItem());
                break;
        }
    }

    @Override
    public void showUnitDialog(final UnitViewModel editUnit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(editUnit);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void selectCategory(String id) {
        categoryList.selectItem(id);
    }

    @Override
    public void selectUnit(String id) {
        unitList.selectItem(id);
    }

    @Override
    public void setCategory(List<CategoryViewModel> category) {
        categoryList.setData(category);
    }

    @Override
    public void setUnits(List<UnitViewModel> unit) {
        unitList.setData(unit);
    }

    @Override
    public void setViewName(String name) {
        nameEdit.setText(name);
    }

    @Override
    public void setDefaultUpdateTitle() {
        positiveButton.setText(R.string.update);
        getDialog().setTitle(R.string.edit_goods);
    }

    @Override
    public void setDefaultNewTitle() {
        getDialog().setTitle(R.string.new_goods);
    }

    @Override
    public void closeDialog() {
        ShoppistUtils.hideKeyboard(getActivity(), nameEdit);
        dismiss();
    }

    @Override
    public void showNameIsRequiredError() {
        nameEdit.setError(getString(R.string.goods_name_is_required));
    }

    @Override
    public void onComplete(boolean isUpdate) {
        if (completeListener != null) {
            completeListener.onComplete(true);
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
        completeListener = listener;
    }

    public interface OnCompleteListener {

        void onComplete(boolean isUpdate);
    }
}
