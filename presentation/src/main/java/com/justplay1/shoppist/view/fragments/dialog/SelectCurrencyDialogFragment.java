package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.presenter.SelectCurrencyPresenter;
import com.justplay1.shoppist.view.SelectCurrencyView;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class SelectCurrencyDialogFragment extends BaseSelectItemDialogFragment<CurrencyViewModel>
        implements SelectCurrencyView {

    @Inject
    SelectCurrencyPresenter mPresenter;

    public SelectCurrencyDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SelectCurrencyDialogFragment newInstance(CurrencyViewModel currency) {
        Bundle args = new Bundle();
        args.putParcelable(CurrencyViewModel.class.getName(), currency);
        SelectCurrencyDialogFragment fragment = new SelectCurrencyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);
        mPositiveButton.setText(R.string.select);
        getDialog().setTitle(R.string.currency);
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
        return inflater.inflate(R.layout.fragment_select_currency_dialog, container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                mPresenter.onAddCurrencyClick();
                break;
            case R.id.edit_button:
                mPresenter.onEditCurrencyClick(mSelectView.getSelectedItem());
                break;
            case R.id.positive_button:
                mPresenter.onPositiveButtonClick(mSelectView.getSelectedItem());
                break;
            case R.id.negative_button:
                mPresenter.onNegativeButtonClick();
                break;
        }
    }

    @Override
    public void showCurrencyDialog(CurrencyViewModel editCurrency) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        AddCurrencyDialogFragment dialog = AddCurrencyDialogFragment.newInstance(editCurrency);
        dialog.setCompleteListener(isUpdate -> mPresenter.loadCurrencies());
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    @Override
    public void setCurrencies(List<CurrencyViewModel> data) {
        mSelectView.setData(data);
    }

    @Override
    public void selectCurrency(String id) {
        mSelectView.selectItem(id);
    }

    @Override
    public void onComplete(CurrencyViewModel currency, boolean isUpdate) {
        if (mCompleteListener != null) {
            mCompleteListener.onComplete(currency, isUpdate);
        }
    }

    @Override
    public void closeDialog() {
        dismiss();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String message) {

    }
}
