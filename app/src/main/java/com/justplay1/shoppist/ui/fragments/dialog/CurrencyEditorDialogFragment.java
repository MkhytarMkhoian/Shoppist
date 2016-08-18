package com.justplay1.shoppist.ui.fragments.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class CurrencyEditorDialogFragment extends BaseDialogFragment<Currency> {

    private MaterialEditText mNameEdit;

    public CurrencyEditorDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static CurrencyEditorDialogFragment newInstance(Currency currency) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM, currency);
        CurrencyEditorDialogFragment fragment = new CurrencyEditorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);

        mNameEdit = (MaterialEditText) view.findViewById(R.id.name_edit);
        mNameEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        if (mItem != null) {
            mPositiveButton.setText(R.string.update);
            mNameEdit.setText(mItem.getName());
            getDialog().setTitle(R.string.edit_currency);
        } else {
            getDialog().setTitle(R.string.new_currency);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNameEdit.setSelection(mNameEdit.getText().length());
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_currency_editor_dialog, container);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                final String name = ShoppistUtils.filterSpace(mNameEdit.getText().toString());
                if (mItem != null) {
                    update(name);
                } else {
                    add(name);
                }
                break;
            case R.id.negative_button:
                ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
                dismiss();
                break;
        }
    }

    private void add(String name) {
        Currency currency = new Currency();
        currency.setName(name);
        currency.setDirty(true);

        if (!name.isEmpty() && name.length() <= mNameEdit.getMaxCharacters()) {
            App.get().getCurrenciesManager().add(currency, new ExecutorListener<Currency>() {
                @Override
                public void start() {
                }

                @Override
                public void complete(Currency result) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onComplete(result, false);
                    }
                    ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
                    dismiss();
                }

                @Override
                public void error(Exception e) {
                    showError(e, mNameEdit);
                }
            });

        } else if (name.isEmpty() && name.length() <= mNameEdit.getMaxCharacters()) {
            setErrorMessage();
        }
    }

    private void update(String name) {
        Currency currency = new Currency();
        currency.setId(mItem.getId());
        currency.setServerId(mItem.getServerId());
        currency.setName(name);
        currency.setTimestamp(mItem.getTimestamp());
        if (!mItem.getName().equals(currency.getName())) {
            currency.setDirty(true);
        }

        if (!name.isEmpty() && name.length() <= mNameEdit.getMaxCharacters()) {
            App.get().getCurrenciesManager().update(currency, new ExecutorListener<Currency>() {
                @Override
                public void start() {
                }

                @Override
                public void complete(Currency result) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onComplete(result, true);
                    }
                    ShoppistUtils.hideKeyboard(getActivity(), mNameEdit);
                    dismiss();
                }

                @Override
                public void error(Exception e) {
                    showError(e, mNameEdit);
                }
            });

        } else if (name.isEmpty() && name.length() <= mNameEdit.getMaxCharacters()) {
            setErrorMessage();
        }
    }

    private void setErrorMessage() {
        String error = getString(R.string.currency_name_is_required);
        mNameEdit.setError(error);
    }
}
