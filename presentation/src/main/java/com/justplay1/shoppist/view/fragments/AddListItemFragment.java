package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.AddListItemsComponent;
import com.justplay1.shoppist.di.components.DaggerAddListItemsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.di.modules.GoodsModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.di.modules.UnitsModule;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
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

/**
 * Created by Mkhytar on 04.07.2016.
 */
public class AddListItemFragment extends BaseAddElementFragment implements AddListItemView {

    private MaterialEditText mPriceEdit;
    private MaterialEditText mQuantityEdit;
    private MaterialEditText mNote;

    private Spinner mPriorityList;
    private CategorySpinnerView mCategoryList;
    private UnitsSpinnerView mUnitList;
    private CurrencySpinnerView mCurrencyList;
    private AutoCompleteTextAdapter mAutoCompleteTextAdapter;

    private AddListItemsComponent mComponent;

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
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerAddListItemsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .listItemsModule(new ListItemsModule())
                .goodsModule(new GoodsModule())
                .unitsModule(new UnitsModule())
                .categoryModule(new CategoryModule())
                .currencyModule(new CurrencyModule())
                .build();
        mComponent.inject(this);
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
        mNameEdit.setAdapter(mAutoCompleteTextAdapter);
        mNameEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductViewModel product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
                mPresenter.onProductSelected(product);

//                String name = mAutoCompleteTextAdapter.getItem(position);
//                if (name.equals(mAutoCompleteTextAdapter.getCurrentText())) {
//                    showNewGoodsDialog(name);
//                } else {
//                    ProductViewModel product = mAutoCompleteTextAdapter.getProduct(name);
//                    setSpinnerItem(mCategoryList.getSpinner(), product.getCategory().getId());
//                    setSpinnerItem(mUnitList.getSpinner(), product.getUnit().getId());
//                }
            }
        });

        mPriceEdit = (MaterialEditText) view.findViewById(R.id.price_edit);
        mQuantityEdit = (MaterialEditText) view.findViewById(R.id.quantity_edit);
        mNote = (MaterialEditText) view.findViewById(R.id.description_edit);
        mPriceEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mQuantityEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mNote.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mDoneBtn.requestFocus();

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

        currencyLabel.setTypeface(App.fontRobotoRegular);
        categoryLabel.setTypeface(App.fontRobotoRegular);
        priorityLabel.setTypeface(App.fontRobotoRegular);
        unitLabel.setTypeface(App.fontRobotoRegular);
        mPriceEdit.setTypeface(App.fontRobotoRegular);
        mQuantityEdit.setTypeface(App.fontRobotoRegular);
        mNote.setTypeface(App.fontRobotoRegular);

        mPriceEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mQuantityEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mNote.setPrimaryColor(mPreferences.getColorPrimary());

        initializePriorityList(view);
        initializeCategoryList(view);
        initializeUnitList(view);
        initializeCurrencyList(view);
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
    public void setPriority(int priority) {
        mPriorityList.setSelection(priority);
    }

    private void initializePriorityList(View view) {
        String[] data = getResources().getStringArray(R.array.priority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPriorityList = (Spinner) view.findViewById(R.id.priority);
        mPriorityList.setAdapter(adapter);
        mPriorityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mPresenter.onPrioritySelected(Priority.NO_PRIORITY);
                        break;
                    case 1:
                        mPresenter.onPrioritySelected(Priority.LOW);
                        break;
                    case 2:
                        mPresenter.onPrioritySelected(Priority.MEDIUM);
                        break;
                    case 3:
                        mPresenter.onPrioritySelected(Priority.HIGH);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeCategoryList(View view) {
        mCategoryList = (CategorySpinnerView) view.findViewById(R.id.category_spinner_view);
        mCategoryList.setOnAddBtnClickListener(v -> mPresenter.onAddCategoryClick());
        mCategoryList.setOnEditBtnClickListener(v -> mPresenter.onEditCategoryClick());
        mCategoryList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onCategorySelected(mCategoryList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeUnitList(View view) {
        mUnitList = (UnitsSpinnerView) view.findViewById(R.id.units_spinner_view);
        mUnitList.setOnAddBtnClickListener(v -> mPresenter.onAddUnitClick());
        mUnitList.setOnEditBtnClickListener(v -> mPresenter.onEditUnitClick());
        mUnitList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onUnitSelected(mUnitList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeCurrencyList(View view) {
        mCurrencyList = (CurrencySpinnerView) view.findViewById(R.id.currency_spinner_view);
        mCurrencyList.setOnAddBtnClickListener(v -> mPresenter.onAddCurrencyClick());
        mCurrencyList.setOnEditBtnClickListener(v -> mPresenter.onEditCurrencyClick());
        mCurrencyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.onCurrencySelected(mCurrencyList.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
//        ProductViewModel product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
//        mPresenter.onProductSelected(product);
        mPresenter.setNote(ShoppistUtils.filterSpace(mNote.getText().toString()));
        mPresenter.setPrice(mPriceEdit.getText().toString());
        mPresenter.setQuantity(mQuantityEdit.getText().toString());
        mPresenter.onDoneButtonLongClick(ShoppistUtils.filterSpace(mNameEdit.getText().toString()));
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                mPresenter.setNote(ShoppistUtils.filterSpace(mNote.getText().toString()));
                mPresenter.setPrice(mPriceEdit.getText().toString());
                mPresenter.setQuantity(mQuantityEdit.getText().toString());
                mPresenter.onDoneButtonClick(ShoppistUtils.filterSpace(mNameEdit.getText().toString()));
                break;
            case R.id.menu_voice_search:
                mPresenter.startVoiceRecognition();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.getBooleanExtra(Const.NEW_DATA, false)) {
                mPresenter.loadCategories();
                data.removeExtra(Const.NEW_DATA);
            }
        }
    }

    @Override
    public void showCurrencyDialog(final CurrencyViewModel editCurrency) {
        FragmentManager fm = getFragmentManager();
        AddCurrencyDialogFragment dialog = AddCurrencyDialogFragment.newInstance(editCurrency);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadCurrency());
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    @Override
    public void showUnitDialog(final UnitViewModel editUnit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(editUnit);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadUnits());
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void showNewGoodsDialog(final String newProduct) {
        FragmentManager fm = getFragmentManager();
        AddGoodsDialogFragment dialog = AddGoodsDialogFragment.newInstance(newProduct);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadGoods());
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
    public void selectProduct(String id) {

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
    public void showError(String message) {

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
        mCurrencyList.selectItem(mPreferences.getDefaultCurrency());
    }
}
