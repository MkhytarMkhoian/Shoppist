package com.justplay1.shoppist.ui.activities;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.cursors.CategoriesCursorLoader;
import com.justplay1.shoppist.cursors.CurrenciesCursorLoader;
import com.justplay1.shoppist.cursors.ProductsCursorLoader;
import com.justplay1.shoppist.cursors.UnitsCursorLoader;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.ui.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.CurrencyEditorDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.GoodsEditorDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.UnitsEditorDialogFragment;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.models.ShoppingListItem;
import com.justplay1.shoppist.models.Status;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.adapters.AutoCompleteTextAdapter;
import com.justplay1.shoppist.ui.views.spinner.CategorySpinnerView;
import com.justplay1.shoppist.ui.views.spinner.CurrencySpinnerView;
import com.justplay1.shoppist.ui.views.spinner.UnitsSpinnerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * Created by Mkhitar on 21.11.2014.
 */
public class AddListItemActivity extends BaseAddActivity<ShoppingListItem> implements LoaderManager.LoaderCallbacks<Cursor> {

    private MaterialEditText mPriceEdit;
    private MaterialEditText mQuantityEdit;
    private MaterialEditText mNote;

    private Spinner mPriorityList;
    private CategorySpinnerView mCategoryList;
    private UnitsSpinnerView mUnitList;
    private CurrencySpinnerView mCurrencyList;
    private AutoCompleteTextAdapter mAutoCompleteTextAdapter;
    private ShoppingList mParentList;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_list_item;
    }

    @Override
    protected void getDataFromIntent() {
        if (getIntent() != null) {
            Bundle data = getIntent().getBundleExtra(ActivityUtils.DATA);
            if (data != null) {
                mParentList = data.getParcelable(ShoppingList.class.getName());
                mItem = data.getParcelable(ShoppingListItem.class.getName());
            }
        }
    }

    protected void initializeFrame() {
        super.initializeFrame();
        mAutoCompleteTextAdapter = new AutoCompleteTextAdapter(this, new ChangeObserver());
        getSupportLoaderManager().initLoader(ProductsCursorLoader.ID, null, this);
        mNameEdit.setAdapter(mAutoCompleteTextAdapter);
        mNameEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mAutoCompleteTextAdapter.getItem(position);
                if (name.equals(mAutoCompleteTextAdapter.getCurrentText())) {
                    showNewGoodsDialog(name);
                } else {
                    Product product = mAutoCompleteTextAdapter.getProduct(name);
                    ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, product.getCategory().getId());
                    ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, product.getUnit().getId());
                }
            }
        });

        mPriceEdit = (MaterialEditText) findViewById(R.id.price_edit);
        mQuantityEdit = (MaterialEditText) findViewById(R.id.quantity_edit);
        mNote = (MaterialEditText) findViewById(R.id.description_edit);
        mPriceEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mQuantityEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mNote.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mDoneBtn.requestFocus();

        ImageButton incrementPriceBtn = (ImageButton) findViewById(R.id.increment_price_button);
        ImageButton decrementPriceBtn = (ImageButton) findViewById(R.id.decrement_price_button);
        ImageButton incrementUnitBtn = (ImageButton) findViewById(R.id.increment_quantity_button);
        ImageButton decrementUnitBtn = (ImageButton) findViewById(R.id.decrement_quantity_button);

        incrementPriceBtn.setOnClickListener(this);
        decrementPriceBtn.setOnClickListener(this);
        incrementUnitBtn.setOnClickListener(this);
        decrementUnitBtn.setOnClickListener(this);

        TextView categoryLabel = (TextView) findViewById(R.id.category_label);
        TextView priorityLabel = (TextView) findViewById(R.id.priority_label);
        TextView unitLabel = (TextView) findViewById(R.id.unit_label);
        TextView currencyLabel = (TextView) findViewById(R.id.currency_label);

        currencyLabel.setTextColor(ShoppistPreferences.getColorPrimary());
        categoryLabel.setTextColor(ShoppistPreferences.getColorPrimary());
        priorityLabel.setTextColor(ShoppistPreferences.getColorPrimary());
        unitLabel.setTextColor(ShoppistPreferences.getColorPrimary());

        currencyLabel.setTypeface(App.fontRobotoRegular);
        categoryLabel.setTypeface(App.fontRobotoRegular);
        priorityLabel.setTypeface(App.fontRobotoRegular);
        unitLabel.setTypeface(App.fontRobotoRegular);
        mPriceEdit.setTypeface(App.fontRobotoRegular);
        mQuantityEdit.setTypeface(App.fontRobotoRegular);
        mNote.setTypeface(App.fontRobotoRegular);

        mPriceEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mQuantityEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mNote.setPrimaryColor(ShoppistPreferences.getColorPrimary());

        initializePriorityList();
        initializeCategoryList();
        initializeUnitList();
        initializeCurrencyList();

        if (mItem != null) {
            mPriceEdit.setText(String.valueOf(mItem.getPrice()));
            mQuantityEdit.setText(String.valueOf(mItem.getQuantity()));
            mNote.setText(mItem.getNote());
            mAutoCompleteTextAdapter.setNotEqualText(mItem.getName());
            mNameEdit.setText(mItem.getName());
            mNameEdit.setSelection(mNameEdit.getText().length());
            mPriorityList.setSelection(mItem.getPriority().ordinal());
        } else {
            mPriceEdit.setText("0");
            mQuantityEdit.setText("0");
        }
    }

    private void initializePriorityList() {
        String[] data = getResources().getStringArray(R.array.priority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPriorityList = (Spinner) findViewById(R.id.priority);
        mPriorityList.setAdapter(adapter);
        mPriorityList.setSelection(0);
    }

    private void initializeCategoryList() {
        mCategoryList = (CategorySpinnerView) findViewById(R.id.category_spinner_view);
        mCategoryList.setOnAddBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.startNextActivityForResult(AddListItemActivity.this, AddCategoryActivity.class, 0, mCategoryList, null);
            }
        });
        mCategoryList.setOnEditBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Category.class.getName(), mCategoryList.getSelectedItem());
                ActivityUtils.startNextActivityForResult(AddListItemActivity.this, AddCategoryActivity.class, 0, mCategoryList, bundle);
            }
        });
        getSupportLoaderManager().initLoader(CategoriesCursorLoader.ID, null, this);
    }

    private void initializeUnitList() {
        mUnitList = (UnitsSpinnerView) findViewById(R.id.units_spinner_view);
        mUnitList.setOnAddBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitDialog(null);
            }
        });
        mUnitList.setOnEditBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUnitDialog(mUnitList.getSelectedItem());
            }
        });
        getSupportLoaderManager().initLoader(UnitsCursorLoader.ID, null, this);
    }

    private void initializeCurrencyList() {
        mCurrencyList = (CurrencySpinnerView) findViewById(R.id.currency_spinner_view);
        mCurrencyList.setOnAddBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyDialog(null);
            }
        });
        mCurrencyList.setOnEditBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrencyDialog(mCurrencyList.getSelectedItem());
            }
        });
        getSupportLoaderManager().initLoader(CurrenciesCursorLoader.ID, null, this);
    }

    protected void clearUI() {
        mNameEdit.setText("");
        mPriorityList.setSelection(0);
        mNote.setText("");
        ShoppistUtils.setSpinnerItem(mCurrencyList.getSpinner(), ShoppingListContact.Currencies.CURRENCY_ID, ShoppistPreferences.getDefaultCurrency());
        ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, Unit.NO_UNIT_ID);
        ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, Category.NO_CATEGORY_ID);
        mPriceEdit.setText("0");
        mQuantityEdit.setText("0");
        mItem = null;
    }

    @Override
    protected int getToolbarTitleResId() {
        return R.string.title_activity_add_list_item;
    }

    protected void addItem(final boolean isLongClick) {
        String name = ShoppistUtils.filterSpace(mNameEdit.getText().toString());
        double price = 0;
        double quantity = 0;
        boolean flag = true;

        if (name.isEmpty()) {
            mNameEdit.setError(getString(R.string.list_name_is_required));
            flag = false;
        } else if (name.length() > mNameEdit.getMaxCharacters()) {
            flag = false;
        }

        String priceText = mPriceEdit.getText().toString();
        if (!priceText.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(mPriceEdit.getText().toString());
            BigDecimal decimal = smallNumber.setScale(2, RoundingMode.HALF_UP);
            price = decimal.doubleValue();
        } else if (priceText.length() > mPriceEdit.getMaxCharacters()) {
            flag = false;
        }

        String quantityText = mQuantityEdit.getText().toString();
        if (!quantityText.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(mQuantityEdit.getText().toString());
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            quantity = decimal.doubleValue();
        } else if (quantityText.length() > mQuantityEdit.getMaxCharacters()) {
            flag = false;
        }

        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setParentListId(mParentList.getId());

        if (mNote.getText().length() > mNote.getMaxCharacters()) {
            flag = false;
        } else {
            shoppingListItem.setNote(ShoppistUtils.filterSpace(mNote.getText().toString()));
        }
        shoppingListItem.setDirty(true);
        shoppingListItem.setName(name);

        if (mItem == null) {
            shoppingListItem.setTimeCreated(System.currentTimeMillis());
            shoppingListItem.setStatus(Status.NOT_DONE);
            shoppingListItem.setId(UUID.nameUUIDFromBytes((name + UUID.randomUUID()).getBytes()).toString());
        } else {
            shoppingListItem.setId(mItem.getId());
            shoppingListItem.setStatus(mItem.getStatus());
            shoppingListItem.setServerId(mItem.getServerId());
            shoppingListItem.setTimeCreated(mItem.getTimeCreated());
            shoppingListItem.setTimestamp(mItem.getTimestamp());
        }
        shoppingListItem.setUnit(mUnitList.getSelectedItem());
        shoppingListItem.setCurrency(mCurrencyList.getSelectedItem());
        shoppingListItem.setPrice(price);
        shoppingListItem.setQuantity(quantity);
        shoppingListItem.setCategory(mCategoryList.getSelectedItem());

        switch (mPriorityList.getSelectedItemPosition()) {
            case 0:
                shoppingListItem.setPriority(Priority.NO_PRIORITY);
                break;
            case 1:
                shoppingListItem.setPriority(Priority.LOW);
                break;
            case 2:
                shoppingListItem.setPriority(Priority.MEDIUM);
                break;
            case 3:
                shoppingListItem.setPriority(Priority.HIGH);
                break;
        }
        if (flag) {
            if (mItem != null) {
                shoppingListItem.setChecked(mItem.isChecked());
                shoppingListItem.setServerId(mItem.getServerId());
                updateItem(shoppingListItem, isLongClick);
            } else {
                addItem(shoppingListItem, isLongClick);
            }
        }
    }

    protected void addItem(final ShoppingListItem item, final boolean isLongClick) {
        App.get().getShoppingListItemsManager().add(item, new ExecutorListener<ShoppingListItem>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(ShoppingListItem result) {
                saveProductUnit();
                mProgressDialog.dismiss();
                AddListItemActivity.this.complete(isLongClick, R.string.goods_added);
            }

            @Override
            public void error(Exception e) {
                mProgressDialog.dismiss();
                DialogUtils.showErrorDialog(AddListItemActivity.this,
                        ShoppistUtils.getParseMessageFromException(AddListItemActivity.this, e.getMessage()));
            }
        });
    }

    protected void updateItem(final ShoppingListItem newItem, final boolean isLongClick) {
        App.get().getShoppingListItemsManager().update(newItem, new ExecutorListener<ShoppingListItem>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(ShoppingListItem result) {
                saveProductUnit();
                mItem = newItem;
                mOldId = result.getId();

                mProgressDialog.dismiss();
                AddListItemActivity.this.complete(isLongClick, R.string.list_item_updated);
            }

            @Override
            public void error(Exception e) {
                mProgressDialog.dismiss();
                DialogUtils.showErrorDialog(AddListItemActivity.this,
                        ShoppistUtils.getParseMessageFromException(AddListItemActivity.this, e.getMessage()));
            }
        });
    }

    private void saveProductUnit(){
        Product product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
        if (product == null) return;
        product.setUnit(mUnitList.getSelectedItem());
        App.get().getProductsManager().update(product, new ExecutorListener<Product>() {
            @Override
            public void start() {

            }

            @Override
            public void complete(Product result) {

            }

            @Override
            public void error(Exception e) {

            }
        });
    }

    @Override
    protected void showError() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                addItem(false);
                break;
            case R.id.menu_voice_search:
                startVoiceRecognition();
                break;
            case R.id.decrement_price_button:
                mPriceEdit.setText(String.format("%s", decrementValue(mPriceEdit.getText().toString())));
                break;
            case R.id.increment_quantity_button:
                mQuantityEdit.setText(String.format("%s", incrementValue(mQuantityEdit.getText().toString())));
                break;
            case R.id.decrement_quantity_button:
                mQuantityEdit.setText(String.format("%s", decrementValue(mQuantityEdit.getText().toString())));
                break;
            case R.id.increment_price_button:
                mPriceEdit.setText(String.format("%s", incrementValue(mPriceEdit.getText().toString())));
                break;
        }
    }

    private double incrementValue(String valueTxt) {
        try {
            double value = Double.valueOf(valueTxt);
            value++;
            BigDecimal smallNumber = new BigDecimal(value);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }

    private double decrementValue(String valueTxt) {
        try {
            double value = Double.valueOf(valueTxt);
            if (value < 1) return value;
            value--;
            BigDecimal smallNumber = new BigDecimal(value);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra(ActivityUtils.NEW_DATA, false)) {
                getSupportLoaderManager().restartLoader(CategoriesCursorLoader.ID, null, this);
                data.removeExtra(ActivityUtils.NEW_DATA);
            }
        }
    }

    private void showCurrencyDialog(final Currency editCurrency) {
        FragmentManager fm = getSupportFragmentManager();
        CurrencyEditorDialogFragment dialog = CurrencyEditorDialogFragment.newInstance(editCurrency);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Currency>() {
            @Override
            public void onComplete(Currency item, boolean isUpdate) {
                getSupportLoaderManager().restartLoader(CurrenciesCursorLoader.ID, null, AddListItemActivity.this);
            }
        });
        dialog.show(fm, CurrencyEditorDialogFragment.class.getName());
    }

    private void showUnitDialog(final Unit editUnit) {
        FragmentManager fm = getSupportFragmentManager();
        UnitsEditorDialogFragment dialog = UnitsEditorDialogFragment.newInstance(editUnit);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Unit>() {
            @Override
            public void onComplete(Unit item, boolean isUpdate) {
                getSupportLoaderManager().restartLoader(UnitsCursorLoader.ID, null, AddListItemActivity.this);
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    private void showNewGoodsDialog(final String newProduct) {
        FragmentManager fm = getSupportFragmentManager();
        GoodsEditorDialogFragment dialog = GoodsEditorDialogFragment.newInstance(newProduct);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Product>() {
            @Override
            public void onComplete(Product item, boolean isUpdate) {
                getSupportLoaderManager().restartLoader(ProductsCursorLoader.ID, null, AddListItemActivity.this);
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case UnitsCursorLoader.ID:
                return new UnitsCursorLoader(this);
            case CurrenciesCursorLoader.ID:
                return new CurrenciesCursorLoader(this);
            case CategoriesCursorLoader.ID:
                return new CategoriesCursorLoader(this);
            case ProductsCursorLoader.ID:
                return new ProductsCursorLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case UnitsCursorLoader.ID:
                mUnitList.swapCursor(data);
                if (mItem != null) {
                    ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, mItem.getUnit().getId());
                } else if (mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString()) != null) {
                    Product product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
                    ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, product.getUnit().getId());
                } else {
                    ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, Unit.NO_UNIT_ID);
                }
                break;
            case CurrenciesCursorLoader.ID:
                mCurrencyList.swapCursor(data);
                if (mItem != null) {
                    ShoppistUtils.setSpinnerItem(mCurrencyList.getSpinner(), ShoppingListContact.Currencies.CURRENCY_ID, mItem.getCurrency().getId());
                } else {
                    ShoppistUtils.setSpinnerItem(mCurrencyList.getSpinner(), ShoppingListContact.Currencies.CURRENCY_ID, ShoppistPreferences.getDefaultCurrency());
                }
                break;
            case CategoriesCursorLoader.ID:
                mCategoryList.swapCursor(data);
                if (mItem != null) {
                    ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, mItem.getCategory().getId());
                } else if (mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString()) != null) {
                    Product product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
                    ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, product.getCategory().getId());
                } else {
                    ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, Category.NO_CATEGORY_ID);
                }
                break;
            case ProductsCursorLoader.ID:
                mAutoCompleteTextAdapter.changeCursor(data);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            getSupportLoaderManager().restartLoader(ProductsCursorLoader.ID, null, AddListItemActivity.this);
        }
    }
}
