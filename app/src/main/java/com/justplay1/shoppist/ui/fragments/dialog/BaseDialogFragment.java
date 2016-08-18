package com.justplay1.shoppist.ui.fragments.dialog;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhytar on 31.01.2016.
 */
public abstract class BaseDialogFragment<T extends BaseModel> extends AppCompatDialogFragment implements View.OnClickListener {

    protected static final String ITEM = "item";

    protected T mItem;
    protected OnCompleteListener<T> mCompleteListener;
    protected Button mPositiveButton;
    protected Button mNegativeButton;

    public abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    public void init(View view) {
        mPositiveButton = (Button) view.findViewById(R.id.positive_button);
        mNegativeButton = (Button) view.findViewById(R.id.negative_button);
        mPositiveButton.setTextColor(ShoppistPreferences.getColorPrimary());
        mNegativeButton.setTextColor(ShoppistPreferences.getColorPrimary());
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
        mItem = getArguments().getParcelable(ITEM);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners_dialog);
        View view = createView(inflater, container, savedInstanceState);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public void setCompleteListener(OnCompleteListener<T> listener) {
        mCompleteListener = listener;
    }

    protected void showError(Exception e, EditText editText) {
        if (e instanceof SQLiteConstraintException) {
            DialogUtils.showErrorDialog(getActivity(),
                    ShoppistUtils.getParseMessageFromException(getActivity(), e.getMessage()));
        } else {
            ShoppistUtils.hideKeyboard(getActivity(), editText);
            dismiss();
        }
    }

    public interface OnCompleteListener<T> {

        void onComplete(T item, boolean isUpdate);
    }
}
