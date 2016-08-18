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
import com.justplay1.shoppist.cursors.UnitsCursorLoader;
import com.justplay1.shoppist.models.Unit;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhytar on 06.02.2016.
 */
public class SelectUnitDialogFragment extends BaseSelectItemDialogFragment<Unit> {

    public SelectUnitDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SelectUnitDialogFragment newInstance() {
        return newInstance(null);
    }

    public static SelectUnitDialogFragment newInstance(Unit unit) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM, unit);
        SelectUnitDialogFragment fragment = new SelectUnitDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_unit_dialog, container);
    }

    @Override
    public void init(View view) {
        super.init(view);
        mPositiveButton.setText(R.string.ok);
        getDialog().setTitle(R.string.change_unit);
        getActivity().getSupportLoaderManager().initLoader(UnitsCursorLoader.ID, null, this);
    }

    private void showUnitDialog(final Unit editUnit) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        UnitsEditorDialogFragment dialog = UnitsEditorDialogFragment.newInstance(editUnit);
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Unit>() {
            @Override
            public void onComplete(Unit item, boolean isUpdate) {
                getLoaderManager().restartLoader(UnitsCursorLoader.ID, null, SelectUnitDialogFragment.this);
            }
        });
        dialog.show(fm, UnitsEditorDialogFragment.class.getName());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new UnitsCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSelectView.swapCursor(data);
        if (mItem != null) {
            ShoppistUtils.setSpinnerItem(mSelectView.getSpinner(), ShoppingListContact.Units.UNIT_ID, mItem.getId());
        } else {
            mSelectView.setSelection(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                showUnitDialog(null);
                break;
            case R.id.edit_button:
                showUnitDialog(mSelectView.getSelectedItem());
                break;
            case R.id.positive_button:
                if (mCompleteListener != null) {
                    mCompleteListener.onComplete(mSelectView.getSelectedItem(), false);
                }
                break;
            case R.id.negative_button:
                dismiss();
                break;
        }
    }
}
