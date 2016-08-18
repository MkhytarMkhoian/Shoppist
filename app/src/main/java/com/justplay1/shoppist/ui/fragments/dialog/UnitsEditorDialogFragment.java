package com.justplay1.shoppist.ui.fragments.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public class UnitsEditorDialogFragment extends BaseDialogFragment<Unit> {

    private MaterialEditText mFullNameEdit;
    private MaterialEditText mShortNameEdit;

    public UnitsEditorDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static UnitsEditorDialogFragment newInstance(Unit unit) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM, unit);
        UnitsEditorDialogFragment fragment = new UnitsEditorDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFullNameEdit.setSelection(mFullNameEdit.getText().length());
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_units_editor_dialog, container);
    }

    @Override
    public void init(View view) {
        super.init(view);
        mFullNameEdit = (MaterialEditText) view.findViewById(R.id.full_name_edit);
        mShortNameEdit = (MaterialEditText) view.findViewById(R.id.short_name_edit);
        mShortNameEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mFullNameEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mFullNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mShortNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        if (mItem != null) {
            mPositiveButton.setText(R.string.update);
            mFullNameEdit.setText(mItem.getName());
            mShortNameEdit.setText(mItem.getShortName());
            getDialog().setTitle(R.string.edit_unit);
        } else {
            getDialog().setTitle(R.string.new_unit);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                final String fullName = ShoppistUtils.filterSpace(mFullNameEdit.getText().toString());
                final String shortName = ShoppistUtils.filterSpace(mShortNameEdit.getText().toString());

                if (mItem != null) {
                    update(fullName, shortName);
                } else {
                    add(fullName, shortName);
                }
                break;
            case R.id.negative_button:
                ShoppistUtils.hideKeyboard(getActivity(), mFullNameEdit);
                dismiss();
                break;
        }
    }

    private void add(String fullName, String shortName) {
        Unit unit = new Unit();
        unit.setName(fullName);
        unit.setShortName(shortName);
        unit.setDirty(true);

        if (!fullName.isEmpty() && !shortName.isEmpty()
                && fullName.length() <= mFullNameEdit.getMaxCharacters()
                && shortName.length() <= mShortNameEdit.getMaxCharacters()) {

            App.get().getUnitManager().add(unit, new ExecutorListener<Unit>() {
                @Override
                public void start() {

                }

                @Override
                public void complete(Unit result) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onComplete(result, false);
                    }
                    ShoppistUtils.hideKeyboard(getActivity(), mFullNameEdit);
                    dismiss();
                }

                @Override
                public void error(Exception e) {
                    showError(e, mFullNameEdit);
                }
            });

        } else {
            setErrorMessage(fullName, shortName);
        }
    }

    private void update(String fullName, String shortName) {
        Unit unit = new Unit();
        unit.setId(mItem.getId());
        unit.setServerId(mItem.getServerId());
        unit.setName(fullName);
        unit.setShortName(shortName);
        unit.setTimestamp(mItem.getTimestamp());
        if (!mItem.getShortName().equals(unit.getShortName()) || !mItem.getName().equals(unit.getName())) {
            unit.setDirty(true);
        }

        if (!fullName.isEmpty() && !shortName.isEmpty()
                && fullName.length() <= mFullNameEdit.getMaxCharacters()
                && shortName.length() <= mShortNameEdit.getMaxCharacters()) {

            App.get().getUnitManager().update(unit, new ExecutorListener<Unit>() {
                @Override
                public void start() {

                }

                @Override
                public void complete(Unit result) {
                    if (mCompleteListener != null) {
                        mCompleteListener.onComplete(result, true);
                    }
                    ShoppistUtils.hideKeyboard(getActivity(), mFullNameEdit);
                    dismiss();
                }

                @Override
                public void error(Exception e) {
                    showError(e, mFullNameEdit);
                }
            });

        } else {
            setErrorMessage(fullName, shortName);
        }
    }

    private void setErrorMessage(String fullName, String shortName) {
        if (fullName.isEmpty() && fullName.length() <= mFullNameEdit.getMaxCharacters()) {
            mFullNameEdit.setError(getString(R.string.full_unit_name_is_required));
        }
        if (shortName.isEmpty() && shortName.length() <= mShortNameEdit.getMaxCharacters()) {
            mShortNameEdit.setError(getString(R.string.short_unit_name_is_required));
        }
    }
}
