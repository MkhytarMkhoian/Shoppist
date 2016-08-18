package com.justplay1.shoppist.view.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.SignInPresenter;
import com.justplay1.shoppist.view.SignInView;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.justplay1.shoppist.view.fragments.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SignInFragment extends BaseFragment implements SignInView, View.OnClickListener {

    @Inject
    SignInPresenter mPresenter;
    @Inject
    ShoppistPreferences mPreferences;

    private SignInFragmentInteractionListener mListener;
    private CustomProgressDialog mProgressDialog;
    private MaterialEditText mEmailField;
    private MaterialEditText mPasswordField;
    private TextView mLoginHelpButton;
    private Button mLoginButton;
    private Button mSignUpButton;

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SignInFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void init(View view) {
        mProgressDialog = new CustomProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        mEmailField = (MaterialEditText) view.findViewById(R.id.user_name_edit);
        mPasswordField = (MaterialEditText) view.findViewById(R.id.password_edit);
        mLoginHelpButton = (Button) view.findViewById(R.id.parse_login_help);
        mLoginButton = (Button) view.findViewById(R.id.login_btn);
        mSignUpButton = (Button) view.findViewById(R.id.sign_up_btn);

        mEmailField.setPrimaryColor(mPreferences.getColorPrimary());
        mEmailField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mPasswordField.setPrimaryColor(mPreferences.getColorPrimary());
        mPasswordField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mLoginHelpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
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

    @Override
    public void showNoEmailMessage() {
        mEmailField.setError(getString(R.string.no_email));
    }

    @Override
    public void showNoPasswordMessage() {
        mPasswordField.setError(getString(R.string.no_password));
    }

    @Override
    public void showInvalidEmailMessage() {
        mEmailField.setError(getString(R.string.invalid_email));
    }

    @Override
    public void showSignInFailedMessage() {
        showToastMessage(getString(R.string.parse_login_failed_unknown_toast));
    }

    @Override
    public void onClick(View v) {
        final String email;
        final String password;

        switch (v.getId()) {
            case R.id.login_btn:
                email = mEmailField.getText().toString();
                password = mPasswordField.getText().toString();
                mPresenter.onSignInClick(email, password);
                break;
            case R.id.sign_up_btn:
                email = mEmailField.getText().toString();
                password = mPasswordField.getText().toString();
                mListener.onSignUpClicked(email, password);
                break;
            case R.id.parse_login_help:
                mListener.onSignInHelpClicked();
                break;
        }
    }

    public interface SignInFragmentInteractionListener {
        void onSignUpClicked(String username, String password);

        void onSignInHelpClicked();
    }
}
