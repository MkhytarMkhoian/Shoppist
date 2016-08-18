package com.justplay1.shoppist.view.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.SingInHelpPresenter;
import com.justplay1.shoppist.view.SingInHelpView;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.justplay1.shoppist.view.fragments.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SignInHelpFragment extends BaseFragment implements View.OnClickListener, SingInHelpView {

    @Inject
    SingInHelpPresenter mPresenter;
    @Inject
    ShoppistPreferences mPreferences;

    private SignInHelpFragmentInteractionListener mListener;
    private CustomProgressDialog mProgressDialog;
    private MaterialEditText mEmailField;
    private Button mSubmitButton;

    public static SignInHelpFragment newInstance() {
        Bundle args = new Bundle();
        SignInHelpFragment fragment = new SignInHelpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SignInHelpFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_help, parent, false);
        init(view);
        return view;
    }

    @Override
    protected void init(View view) {
        mProgressDialog = new CustomProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        mEmailField = (MaterialEditText) view.findViewById(R.id.login_help_email_input);
        mEmailField.setPrimaryColor(mPreferences.getColorPrimary());
        mEmailField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mSubmitButton = (Button) view.findViewById(R.id.login_help_submit);
        mSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String email = mEmailField.getText().toString();
        mPresenter.onSubmitClick(email);
    }

    @Override
    public void showNoEmailMessage() {
        mEmailField.setError(getString(R.string.no_email));
    }

    @Override
    public void showInvalidEmailMessage() {
        mEmailField.setError(getString(R.string.invalid_email));
    }

    @Override
    public void showInstructions() {
        showToastMessage(getString(R.string.com_parse_ui_login_help_email_sent));
        mListener.onSignInHelpSuccess();
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

    public interface SignInHelpFragmentInteractionListener {

        void onSignInHelpSuccess();
    }
}
