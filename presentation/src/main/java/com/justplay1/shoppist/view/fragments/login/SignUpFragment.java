package com.justplay1.shoppist.view.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.SignUpPresenter;
import com.justplay1.shoppist.view.SignUpView;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.justplay1.shoppist.view.fragments.BaseFragment;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SignUpFragment extends BaseFragment implements SignUpView, View.OnClickListener {

    @Inject
    SignUpPresenter mPresenter;
    @Inject
    ShoppistPreferences mPreferences;

    private CustomProgressDialog mProgressDialog;
    private MaterialEditText mEmailField;
    private MaterialEditText mPasswordField;
    private MaterialEditText mConfirmPasswordField;
    private Button mCreateAccountBtn;

    public static SignUpFragment newInstance(String username, String password) {
        SignUpFragment signupFragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(USERNAME, username);
        args.putString(PASSWORD, password);
        signupFragment.setArguments(args);
        return signupFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        init(view);
        return view;
    }

    @Override
    protected void init(View view) {
        mProgressDialog = new CustomProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        mEmailField = (MaterialEditText) view.findViewById(R.id.signup_username_input);
        mPasswordField = (MaterialEditText) view.findViewById(R.id.signup_password_input);
        mConfirmPasswordField = (MaterialEditText) view.findViewById(R.id.signup_confirm_password_input);
        mCreateAccountBtn = (Button) view.findViewById(R.id.create_account);
        mCreateAccountBtn.setOnClickListener(this);

        mEmailField.setPrimaryColor(mPreferences.getColorPrimary());
        mPasswordField.setPrimaryColor(mPreferences.getColorPrimary());
        mConfirmPasswordField.setPrimaryColor(mPreferences.getColorPrimary());

        mPasswordField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mEmailField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mConfirmPasswordField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void setUserName(String name) {
        mEmailField.setText(name);
    }

    @Override
    public void setPassword(String password) {
        mPasswordField.setText(password);
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
    public void onClick(View v) {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();
        String passwordAgain = mConfirmPasswordField.getText().toString();
        mPresenter.onSignUpClick(email, password, passwordAgain);
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
    public void showPasswordTooShortMessage() {
        showToastMessage(getResources().getQuantityString(
                R.plurals.password_too_short_toast,
                DEFAULT_MIN_PASSWORD_LENGTH, DEFAULT_MIN_PASSWORD_LENGTH));
    }

    @Override
    public void showReenterPasswordMessage() {
        mConfirmPasswordField.setError(getString(R.string.reenter_password_toast));
    }

    @Override
    public void showConfirmPasswordMessage() {
        showToastMessage(getString(R.string.mismatch_confirm_password_toast));
        mConfirmPasswordField.selectAll();
        mConfirmPasswordField.requestFocus();
    }

    @Override
    public void showInvalidEmailMessage() {
        mEmailField.setError(getString(R.string.invalid_email));
    }

    @Override
    public void showSignUpFailedMessage() {
        showToastMessage(getString(R.string.com_parse_ui_signup_failed_unknown_toast));
    }
}
