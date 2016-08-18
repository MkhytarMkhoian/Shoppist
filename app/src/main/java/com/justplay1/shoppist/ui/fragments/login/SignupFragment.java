/*
 *  Copyright (c) 2014, Parse, LLC. All rights reserved.
 *
 *  You are hereby granted a non-exclusive, worldwide, royalty-free license to use,
 *  copy, modify, and distribute this software in source code or binary form for use
 *  in connection with the web services and APIs provided by Parse.
 *
 *  As with any software that integrates with the Parse platform, your use of
 *  this software is subject to the Parse Terms of Service
 *  [https://www.parse.com/about/terms]. This copyright notice shall be
 *  included in all copies or substantial portions of the software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 *  FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 *  IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.justplay1.shoppist.ui.fragments.login;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.communication.network.ParseErrorHandler;
import com.justplay1.shoppist.communication.sync.ShoppistAuthenticator;
import com.justplay1.shoppist.communication.sync.ShoppistServerAuthenticate;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.Callable;

/**
 * Fragment for the user signup screen.
 */
public class SignupFragment extends LoginFragmentBase implements OnClickListener {
    public static final String USERNAME = "SignupFragment.USERNAME";
    public static final String PASSWORD = "SignupFragment.PASSWORD";

    private MaterialEditText mEmailField;
    private MaterialEditText mPasswordField;
    private MaterialEditText mConfirmPasswordField;
    private Button mCreateAccountBtn;
    private OnLoginSuccessListener onLoginSuccessListener;

    private int minPasswordLength;

    private static final String LOG_TAG = "SignupFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;

    public static SignupFragment newInstance(String username, String password) {
        SignupFragment signupFragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString(SignupFragment.USERNAME, username);
        args.putString(SignupFragment.PASSWORD, password);
        signupFragment.setArguments(args);
        return signupFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Bundle args = getArguments();
        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;

        String username = args.getString(USERNAME);
        String password = args.getString(PASSWORD);

        View v = inflater.inflate(R.layout.parse_signup_fragment, parent, false);
        mEmailField = (MaterialEditText) v.findViewById(R.id.signup_username_input);
        mPasswordField = (MaterialEditText) v.findViewById(R.id.signup_password_input);
        mConfirmPasswordField = (MaterialEditText) v.findViewById(R.id.signup_confirm_password_input);
        mCreateAccountBtn = (Button) v.findViewById(R.id.create_account);
        mCreateAccountBtn.setOnClickListener(this);

        mEmailField.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mPasswordField.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mConfirmPasswordField.setPrimaryColor(ShoppistPreferences.getColorPrimary());

        mPasswordField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mEmailField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mConfirmPasswordField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mEmailField.setText(username);
        mPasswordField.setText(password);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginSuccessListener) {
            onLoginSuccessListener = (OnLoginSuccessListener) context;
        } else {
            throw new IllegalArgumentException("Activity must implemement ParseOnLoginSuccessListener");
        }
        if (context instanceof OnLoadingListener) {
            onLoadingListener = (OnLoadingListener) context;
        } else {
            throw new IllegalArgumentException("Activity must implemement ParseOnLoadingListener");
        }
    }

    @Override
    public void onClick(View v) {
        final String email = mEmailField.getText().toString();
        final String password = mPasswordField.getText().toString();
        String passwordAgain = mConfirmPasswordField.getText().toString();

        if (email.length() == 0) {
            mEmailField.setError(getString(R.string.com_parse_ui_no_email_toast));
        } else if (password.length() == 0) {
            mPasswordField.setError(getString(R.string.no_password_toast));
        } else if (password.length() < minPasswordLength) {
            ParseErrorHandler.showToast(getActivity(), getResources().getQuantityString(
                    R.plurals.password_too_short_toast,
                    minPasswordLength, minPasswordLength));
        } else if (passwordAgain.length() == 0) {
            mConfirmPasswordField.setError(getString(R.string.reenter_password_toast));
        } else if (!password.equals(passwordAgain)) {
            ParseErrorHandler.showToast(getActivity(), R.string.mismatch_confirm_password_toast);
            mConfirmPasswordField.selectAll();
            mConfirmPasswordField.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailField.setError(getString(R.string.com_parse_ui_invalid_email_toast));
        } else {

            ThreadExecutor.doNetworkTaskAsync(new Callable<ParseUser>() {
                @Override
                public ParseUser call() throws Exception {
                    return ShoppistServerAuthenticate.userSignUp(email, password);
                }
            }, new ExecutorListener<ParseUser>() {
                @Override
                public void start() {
                    loadingStart();
                }

                @Override
                public void complete(ParseUser user) {
                    Bundle data = new Bundle();
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, user.getUsername());
                    data.putString(AccountManager.KEY_AUTHTOKEN, user.getSessionToken());
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, ShoppistAuthenticator.ACCOUNT_TYPE);
                    data.putBoolean(ShoppistAuthenticator.ARG_IS_ADDING_NEW_ACCOUNT, true);
                    data.putString(ShoppistAuthenticator.PARAM_USER_PASS, password);

                    Bundle userData = new Bundle();
                    userData.putString(ShoppistAuthenticator.USERDATA_USER_OBJ_ID, user.getObjectId());
                    data.putBundle(AccountManager.KEY_USERDATA, userData);

                    final Intent res = new Intent();
                    res.putExtras(data);

                    signupSuccess(res, true);
                    loadingFinish();
                }

                @Override
                public void error(Exception e) {
                    loadingFinish();
                    Log.e(LOG_TAG, getString(R.string.com_parse_ui_login_warning_parse_signup_failed) +
                            e.toString());
                    ParseErrorHandler.handleParseError(e);
                }
            });
        }
    }

    private void signupSuccess(Intent intent, boolean isSingUp) {
        onLoginSuccessListener.onLoginSuccess(intent, isSingUp);
    }
}
