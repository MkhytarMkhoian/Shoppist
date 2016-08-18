package com.justplay1.shoppist.ui.fragments.dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.cursors.CategoriesCursorLoader;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhytar on 01.02.2016.
 */
public class SelectCategoryDialogFragment extends BaseSelectItemDialogFragment<Category> {

    public SelectCategoryDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static SelectCategoryDialogFragment newInstance() {
        return newInstance(null);
    }

    public static SelectCategoryDialogFragment newInstance(Category category) {
        Bundle args = new Bundle();
        args.putParcelable(ITEM, category);
        SelectCategoryDialogFragment fragment = new SelectCategoryDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_category_dialog, container);
    }

    @Override
    public void init(View view) {
        super.init(view);
        mPositiveButton.setText(R.string.ok);
        getDialog().setTitle(R.string.change_category);

        mSelectView.setAddBtnVisibility(View.GONE);
        mSelectView.setEditBtnVisibility(View.GONE);
        getActivity().getSupportLoaderManager().initLoader(CategoriesCursorLoader.ID, null, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_button:
                showCategoryDialog(null);
                break;
            case R.id.edit_button:
                showCategoryDialog(mSelectView.getSelectedItem());
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

    private void showCategoryDialog(Category category) {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        CurrencyEditorDialogFragment dialog = CurrencyEditorDialogFragment.newInstance(category);
//        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Currency>() {
//            @Override
//            public void onComplete(Currency item, boolean isUpdate) {
//                getLoaderManager().restartLoader(CategoriesCursorLoader.ID, null, ChangeCategoryDialogFragment.this);
//            }
//        });
//        dialog.show(fm, CurrencyEditorDialogFragment.class.getName());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CategoriesCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mSelectView.swapCursor(data);
        if (mItem != null) {
            ShoppistUtils.setSpinnerItem(mSelectView.getSpinner(), ShoppingListContact.Categories.CATEGORY_ID, mItem.getId());
        } else {
            mSelectView.setSelection(0);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
