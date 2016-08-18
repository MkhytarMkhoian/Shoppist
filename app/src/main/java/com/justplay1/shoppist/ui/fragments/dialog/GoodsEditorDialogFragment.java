package com.justplay1.shoppist.ui.fragments.dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.cursors.CategoriesCursorLoader;
import com.justplay1.shoppist.cursors.UnitsCursorLoader;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.models.Product;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.spinner.CategorySpinnerView;
import com.justplay1.shoppist.ui.views.spinner.UnitsSpinnerView;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class GoodsEditorDialogFragment extends BaseDialogFragment<Product> implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String NEW_NAME = "new_name";

    private MaterialEditText mNameEdit;
    private CategorySpinnerView mCategoryList;
    private UnitsSpinnerView mUnitList;
    private String mNewName;

    public GoodsEditorDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    private static GoodsEditorDialogFragment newInstance(Product product, String newName) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM, product);
        args.putString(NEW_NAME, newName);
        GoodsEditorDialogFragment fragment = new GoodsEditorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static GoodsEditorDialogFragment newInstance(String newName) {
        return newInstance(null, newName);
    }

    public static GoodsEditorDialogFragment newInstance(Product product) {
        return newInstance(product, null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewName = getArguments().getString(NEW_NAME);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_editor_dialog, container);
    }

    @Override
    public void init(View view) {
        super.init(view);

        TextView categoryLabel = (TextView) view.findViewById(R.id.category_label);
        categoryLabel.setTextColor(ShoppistPreferences.getColorPrimary());
        categoryLabel.setTypeface(App.fontRobotoRegular);
        TextView unitsLabel = (TextView) view.findViewById(R.id.units_label);
        unitsLabel.setTextColor(ShoppistPreferences.getColorPrimary());
        unitsLabel.setTypeface(App.fontRobotoRegular);

        mNameEdit = (MaterialEditText) view.findViewById(R.id.goods_name);
        mNameEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        initCategoryList(view);
        initUnitList(view);

        if (mItem != null) {
            mPositiveButton.setText(R.string.update);
            mNameEdit.setText(mItem.getName());
            getDialog().setTitle(R.string.edit_goods);
        } else {
            mNameEdit.setText(mNewName);
            getDialog().setTitle(R.string.new_goods);
        }
    }

    private void initUnitList(View view) {
        mUnitList = (UnitsSpinnerView) view.findViewById(R.id.units_spinner_view);
        mUnitList.setOnAddBtnClickListener(this);
        mUnitList.setOnEditBtnClickListener(this);
        getLoaderManager().initLoader(UnitsCursorLoader.ID, null, this);
    }

    private void initCategoryList(View parent) {
        mCategoryList = (CategorySpinnerView) parent.findViewById(R.id.category_spinner_view);
        mCategoryList.setEditBtnVisibility(View.GONE);
        mCategoryList.setAddBtnVisibility(View.GONE);
        getLoaderManager().initLoader(CategoriesCursorLoader.ID, null, this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNameEdit.setSelection(mNameEdit.getText().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                final String name = ShoppistUtils.filterSpace(mNameEdit.getText().toString());
                add(name);
                break;
            case R.id.negative_button:
                ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
                dismiss();
                break;
            case R.id.add_button:
                showUnitDialog(null);
                break;
            case R.id.edit_button:
                showUnitDialog(mUnitList.getSelectedItem());
                break;
        }
    }

    private void showUnitDialog(final Unit editUnit) {
        FragmentManager fm = getFragmentManager();
        UnitsEditorDialogFragment dialog = UnitsEditorDialogFragment.newInstance(editUnit);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Unit>() {
            @Override
            public void onComplete(Unit item, boolean isUpdate) {
                getLoaderManager().restartLoader(UnitsCursorLoader.ID, null, GoodsEditorDialogFragment.this);
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    private void add(String name) {
        Product product = new Product();
        product.setName(name);
        product.setCategory(mCategoryList.getSelectedItem());
        product.setUnit(mUnitList.getSelectedItem());

        if (mItem != null) {
            product.setTimestamp(mItem.getTimestamp());
            product.setId(mItem.getId());
            product.setServerId(mItem.getServerId());
            product.setTimeCreated(mItem.getTimeCreated());
            product.setCreateByUser(mItem.isCreateByUser());
            product.setDirty(mItem.isDirty());
            if (!mItem.getCategory().getId().equals(product.getCategory().getId())
                    || !mItem.getUnit().getId().equals(product.getUnit().getId())
                    || !mItem.getName().equals(product.getName())) {
                product.setCreateByUser(true);
                product.setDirty(true);
            }
        } else {
            product.setTimeCreated(System.currentTimeMillis());
            product.setDirty(true);
            product.setCreateByUser(true);
        }

        if (!name.isEmpty() && name.length() <= mNameEdit.getMaxCharacters()) {
            if (mItem != null) {
                App.get().getProductsManager().update(product, new ExecutorListener<Product>() {
                    @Override
                    public void start() {
                    }

                    @Override
                    public void complete(Product result) {
                        if (mCompleteListener != null) {
                            mCompleteListener.onComplete(result, true);
                        }
                        ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
                        dismiss();
                    }

                    @Override
                    public void error(Exception e) {
                        showError(e);
                    }
                });
            } else {
                App.get().getProductsManager().add(product, new ExecutorListener<Product>() {
                    @Override
                    public void start() {
                    }

                    @Override
                    public void complete(Product result) {
                        if (mCompleteListener != null) {
                            mCompleteListener.onComplete(result, true);
                        }
                        ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
                        dismiss();
                    }

                    @Override
                    public void error(Exception e) {
                        showError(e);
                    }
                });
            }
        } else if (name.isEmpty() && name.length() <= mNameEdit.getMaxCharacters()) {
            String error = getString(R.string.goods_name_is_required);
            mNameEdit.setError(error);
        }
    }

    private void showError(Exception e) {
        DialogUtils.showErrorDialog(getActivity(),
                ShoppistUtils.getParseMessageFromException(getActivity(), e.getMessage()));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case CategoriesCursorLoader.ID:
                return new CategoriesCursorLoader(getActivity());
            case UnitsCursorLoader.ID:
                return new UnitsCursorLoader(getActivity());
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case CategoriesCursorLoader.ID:
                mCategoryList.swapCursor(data);
                if (mItem != null) {
                    ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, mItem.getCategory().getId());
                } else {
                    ShoppistUtils.setSpinnerItem(mCategoryList.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, Category.NO_CATEGORY_ID);
                }
                break;
            case UnitsCursorLoader.ID:
                mUnitList.swapCursor(data);
                if (mItem != null && !mItem.isUnitEmpty()) {
                    ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, mItem.getUnit().getId());
                } else {
                    ShoppistUtils.setSpinnerItem(mUnitList.getSpinner(), ShoppingListContact.Units.UNIT_ID, Unit.NO_UNIT_ID);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
