package com.justplay1.shoppist.ui.fragments.dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.cursors.CurrenciesCursorLoader;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class SelectCurrencyDialogFragment extends BaseSelectItemDialogFragment<Currency> {

    public SelectCurrencyDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SelectCurrencyDialogFragment newInstance(Currency currency) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM, currency);
        SelectCurrencyDialogFragment fragment = new SelectCurrencyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);
        mPositiveButton.setText(R.string.select);
        getDialog().setTitle(R.string.currency);
        getActivity().getSupportLoaderManager().initLoader(CurrenciesCursorLoader.ID, null, this);
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_currency_dialog, container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                showCurrencyDialog(null);
                break;
            case R.id.edit_button:
                showCurrencyDialog(mSelectView.getSelectedItem());
                break;
            case R.id.positive_button:
                if (mCompleteListener != null) {
                    mCompleteListener.onComplete(mSelectView.getSelectedItem(), false);
                }
                dismiss();
                break;
            case R.id.negative_button:
                dismiss();
                break;
        }
    }

    private void showCurrencyDialog(Currency editCurrency) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        CurrencyEditorDialogFragment dialog = CurrencyEditorDialogFragment.newInstance(editCurrency);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Currency>() {
            @Override
            public void onComplete(Currency item, boolean isUpdate) {
                getLoaderManager().restartLoader(CurrenciesCursorLoader.ID, null, SelectCurrencyDialogFragment.this);
            }
        });
        dialog.show(fm, CurrencyEditorDialogFragment.class.getName());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CurrenciesCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSelectView.swapCursor(data);
        if (!ShoppistPreferences.getDefaultCurrency().isEmpty()) {
            ShoppistUtils.setSpinnerItem(mSelectView.getSpinner(), ShoppingListContact.Currencies.CURRENCY_ID, ShoppistPreferences.getDefaultCurrency());
        } else {
            mSelectView.setSelection(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
