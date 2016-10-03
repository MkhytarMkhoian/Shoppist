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

package com.justplay1.shoppist.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.AddListItemsComponent;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.presenter.AddListItemPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddListItemView;
import com.justplay1.shoppist.view.adapters.AutoCompleteTextAdapter;
import com.justplay1.shoppist.view.component.spinner.CategorySpinnerView;
import com.justplay1.shoppist.view.component.spinner.CurrencySpinnerView;
import com.justplay1.shoppist.view.component.spinner.UnitsSpinnerView;
import com.justplay1.shoppist.view.fragments.dialog.AddCurrencyDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddListItemFragment extends BaseAddElementFragment implements AddListItemView {

    @Bind(R.id.price_edit)
    MaterialEditText mPriceEdit;
    @Bind(R.id.quantity_edit)
    MaterialEditText mQuantityEdit;
    @Bind(R.id.description_edit)
    MaterialEditText mNote;

    @Bind(R.id.priority)
    Spinner mPriorityList;
    @Bind(R.id.category_spinner_view)
    CategorySpinnerView mCategoryList;
    @Bind(R.id.units_spinner_view)
    UnitsSpinnerView mUnitList;
    @Bind(R.id.currency_spinner_view)
    CurrencySpinnerView mCurrencyList;

    private AutoCompleteTextAdapter mAutoCompleteTextAdapter;
    private AddListItemListener mListItemListener;

    @Inject
    AddListItemPresenter mPresenter;

    public static AddListItemFragment newInstance(String parentListId, ListItemViewModel item) {
        Bundle args = new Bundle();
        args.putParcelable(ListItemViewModel.class.getName(), item);
        args.putString(Const.PARENT_LIST_ID, parentListId);
        AddListItemFragment fragment = new AddListItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListItemListener = (AddListItemListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(AddListItemsComponent.class).inject(this);
    }

    @Override
    protected boolean isItemEdit() {
        return mPresenter.isItemEdit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_list_item;
    }

    protected void init(View view) {
        super.init(view);

        mAutoCompleteTextAdapter = new AutoCompleteTextAdapter(getContext());
        mAutoCompleteTextAdapter.setListener(this::showNewGoodsDialog);
        mNameEdit.setAdapter(mAutoCompleteTextAdapter);
        mNameEdit.setOnItemClickListener((parent, view1, position, id) -> {
            ProductViewModel product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
            mPresenter.onProductClick(product);
        });
        mNameEdit.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.selectName(ShoppistUtils.filterSpace(s.toString()));
                ProductViewModel product = mAutoCompleteTextAdapter.getProduct(s.toString());
                mPresenter.setProduct(product);
            }
        });

        mPriceEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mQuantityEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mNote.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mNote.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.setNote(ShoppistUtils.filterSpace(s.toString()));
            }
        });
        mPriceEdit.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.setPrice(s.toString());
            }
        });
        mQuantityEdit.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.setQuantity(s.toString());
            }
        });

        ImageButton incrementPriceBtn = (ImageButton) view.findViewById(R.id.increment_price_button);
        ImageButton decrementPriceBtn = (ImageButton) view.findViewById(R.id.decrement_price_button);
        ImageButton incrementUnitBtn = (ImageButton) view.findViewById(R.id.increment_quantity_button);
        ImageButton decrementUnitBtn = (ImageButton) view.findViewById(R.id.decrement_quantity_button);

        incrementPriceBtn.setOnClickListener(this);
        decrementPriceBtn.setOnClickListener(this);
        incrementUnitBtn.setOnClickListener(this);
        decrementUnitBtn.setOnClickListener(this);

        TextView categoryLabel = (TextView) view.findViewById(R.id.category_label);
        TextView priorityLabel = (TextView) view.findViewById(R.id.priority_label);
        TextView unitLabel = (TextView) view.findViewById(R.id.unit_label);
        TextView currencyLabel = (TextView) view.findViewById(R.id.currency_label);

        currencyLabel.setTextColor(mPreferences.getColorPrimary());
        categoryLabel.setTextColor(mPreferences.getColorPrimary());
        priorityLabel.setTextColor(mPreferences.getColorPrimary());
        unitLabel.setTextColor(mPreferences.getColorPrimary());

        mPriceEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mQuantityEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mNote.setPrimaryColor(mPreferences.getColorPrimary());

        initializePriorityList();
        initializeCategoryList();
        initializeUnitList();
        initializeCurrencyList();
    }

    @Override
    public void setQuantity(String quantity) {
        mQuantityEdit.setText(quantity);
    }

    @Override
    public void setNote(String note) {
        mNote.setText(note);
    }

    @Override
    public void setPrice(String price) {
        mPriceEdit.setText(price);
    }

    @Override
    public void selectPriority(int priority) {
        mPriorityList.setSelection(priority);
    }

    private void initializePriorityList() {
        String[] data = getResources().getStringArray(R.array.priority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPriorityList.setAdapter(adapter);
        mPriorityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setPriority(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeCategoryList() {
        mCategoryList.setOnAddBtnClickListener(v -> mListItemListener.openAddCategoryScreen(null));
        mCategoryList.setOnEditBtnClickListener(v -> mListItemListener.openAddCategoryScreen(mCategoryList.getSelectedItem()));
        mCategoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setCategory(mCategoryList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeUnitList() {
        mUnitList.setOnAddBtnClickListener(v -> showUnitDialog(null));
        mUnitList.setOnEditBtnClickListener(v -> showUnitDialog(mUnitList.getSelectedItem()));
        mUnitList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setUnit(mUnitList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeCurrencyList() {
        mCurrencyList.setOnAddBtnClickListener(v -> showCurrencyDialog(null));
        mCurrencyList.setOnEditBtnClickListener(v -> showCurrencyDialog(mCurrencyList.getSelectedItem()));
        mCurrencyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.setCurrency(mCurrencyList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        mPresenter.onDoneButtonLongClick();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                mPresenter.onDoneButtonClick();
                break;
            case R.id.decrement_price_button:
                mPresenter.onDecrementPriceClick(mPriceEdit.getText().toString());
                break;
            case R.id.increment_quantity_button:
                mPresenter.onIncrementQuantityClick(mQuantityEdit.getText().toString());
                break;
            case R.id.decrement_quantity_button:
                mPresenter.onDecrementQuantityClick(mQuantityEdit.getText().toString());
                break;
            case R.id.increment_price_button:
                mPresenter.onIncrementPriceClick(mPriceEdit.getText().toString());
                break;
        }
    }

    private void showCurrencyDialog(final CurrencyViewModel editCurrency) {
        FragmentManager fm = getFragmentManager();
        AddCurrencyDialogFragment dialog = AddCurrencyDialogFragment.newInstance(editCurrency);
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    private void showUnitDialog(final UnitViewModel editUnit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(editUnit);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    private void showNewGoodsDialog(final String newProduct) {
        FragmentManager fm = getFragmentManager();
        AddGoodsDialogFragment dialog = AddGoodsDialogFragment.newInstance(newProduct);
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void setCategory(List<CategoryViewModel> category) {
        mCategoryList.setData(category);
    }

    @Override
    public void setUnits(List<UnitViewModel> unit) {
        mUnitList.setData(unit);
    }

    @Override
    public void setCurrency(List<CurrencyViewModel> currency) {
        mCurrencyList.setData(currency);
    }

    @Override
    public void selectCategory(String id) {
        mCategoryList.selectItem(id);
    }

    @Override
    public void selectUnit(String id) {
        mUnitList.selectItem(id);
    }

    @Override
    public void selectCurrency(String id) {
        mCurrencyList.selectItem(id);
    }

    @Override
    public void setGoods(Map<String, ProductViewModel> goods) {
        mAutoCompleteTextAdapter.setData(goods);
    }

    @Override
    public void setName(String name) {
        mNameEdit.setText(name);
        mNameEdit.setSelection(mNameEdit.getText().length());
        mAutoCompleteTextAdapter.setNotEqualText(name);
    }

    @Override
    public void setDefaultToolbarTitle() {
        mListener.setTitle(getString(R.string.add_list_item));
    }

    @Override
    public void setToolbarTitle(String title) {
        mListener.setTitle(title);
    }

    @Override
    public void showNameIsRequiredError() {
        mNameEdit.setError(getString(R.string.goods_name_is_required));
    }

    @Override
    public void showKeyboard() {
        ShoppistUtils.showKeyboard(getContext(), mNameEdit);
    }

    @Override
    public void closeScreen() {
        mListener.closeScreen();
    }

    @Override
    public void showNewElementAddedMessage() {
        showToastMessage(getString(R.string.goods_added));
    }

    @Override
    public void showElementUpdatedMessage() {
        showToastMessage(getString(R.string.list_item_updated));
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
    public void setDefaultCategory() {
        mCurrencyList.selectItem(CategoryViewModel.NO_CATEGORY_ID);
    }

    @Override
    public void setDefaultUnit() {
        mUnitList.selectItem(UnitViewModel.NO_UNIT_ID);
    }

    @Override
    public void setDefaultCurrency() {
        mCurrencyList.selectItem(CurrencyViewModel.NO_CURRENCY_ID);
    }

    public interface AddListItemListener {

        void openAddCategoryScreen(CategoryViewModel category);
    }
}
