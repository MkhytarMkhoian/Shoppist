package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.presenter.AddGoodsPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddGoodsView;
import com.justplay1.shoppist.view.component.spinner.CategorySpinnerView;
import com.justplay1.shoppist.view.component.spinner.UnitsSpinnerView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class AddGoodsDialogFragment extends BaseDialogFragment implements AddGoodsView {

    @Inject
    AddGoodsPresenter mPresenter;

    private OnCompleteListener mCompleteListener;
    private MaterialEditText mNameEdit;
    private CategorySpinnerView mCategoryList;
    private UnitsSpinnerView mUnitList;
    private String mNewName;

    public AddGoodsDialogFragment() {
        // Empty constructor required for DialogFragment
    }

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
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
        mPresenter.onDestroy();
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_goods_editor_dialog, container);
    }

    @Override
    public void init(View view) {
        super.init(view);
        TextView categoryLabel = (TextView) view.findViewById(R.id.category_label);
        categoryLabel.setTextColor(mPreferences.getColorPrimary());
        categoryLabel.setTypeface(App.fontRobotoRegular);
        TextView unitsLabel = (TextView) view.findViewById(R.id.units_label);
        unitsLabel.setTextColor(mPreferences.getColorPrimary());
        unitsLabel.setTypeface(App.fontRobotoRegular);

        mNameEdit = (MaterialEditText) view.findViewById(R.id.goods_name);
        mNameEdit.setPrimaryColor(mPreferences.getColorPrimary());
        mNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        initCategoryList(view);
        initUnitList(view);
    }

    private void initUnitList(View view) {
        mUnitList = (UnitsSpinnerView) view.findViewById(R.id.units_spinner_view);
        mUnitList.setOnAddBtnClickListener(this);
        mUnitList.setOnEditBtnClickListener(this);
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

    private void initCategoryList(View parent) {
        mCategoryList = (CategorySpinnerView) parent.findViewById(R.id.category_spinner_view);
        mCategoryList.setEditBtnVisibility(View.GONE);
        mCategoryList.setAddBtnVisibility(View.GONE);
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
                mPresenter.onPositiveButtonClick(name);
                break;
            case R.id.negative_button:
                mPresenter.onNegativeButtonClick();
                break;
            case R.id.add_button:
                mPresenter.onAddUnitClick();
                break;
            case R.id.edit_button:
                mPresenter.onEditUnitClick(mUnitList.getSelectedItem());
                break;
        }
    }

    @Override
    public void showUnitDialog(final UnitViewModel editUnit) {
        FragmentManager fm = getFragmentManager();
        AddUnitsDialogFragment dialog = AddUnitsDialogFragment.newInstance(editUnit);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadUnits());
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
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
    public void setCategory(List<CategoryViewModel> category) {
        mCategoryList.setData(category);
    }

    @Override
    public void setUnits(List<UnitViewModel> unit) {
        mUnitList.setData(unit);
    }

    @Override
    public void setName(String name) {
        mNameEdit.setText(name);
    }

    @Override
    public void setDefaultUpdateTitle() {
        mPositiveButton.setText(R.string.update);
        getDialog().setTitle(R.string.edit_goods);
    }

    @Override
    public void setDefaultNewTitle() {
        getDialog().setTitle(R.string.new_goods);
    }

    @Override
    public void closeDialog() {
        ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
        dismiss();
    }

    @Override
    public void showNameIsRequiredError() {
        mNameEdit.setError(getString(R.string.goods_name_is_required));
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