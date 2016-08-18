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
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.ThreadExecutor;
import com.justplay1.shoppist.communication.network.ParseErrorHandler;
import com.justplay1.shoppist.communication.sync.ShoppistAuthenticator;
import com.justplay1.shoppist.communication.sync.ShoppistServerAuthenticate;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.Callable;

/**
 * Fragment for the user login screen.
 */
public class LoginFragment extends LoginFragmentBase implements OnClickListener {

    public interface LoginFragmentListener {
        void onSignUpClicked(String username, String password);

        void onLoginHelpClicked();
    }

    private static final String LOG_TAG = "LoginFragment";

    private View mRoot;
    private MaterialEditText mEmailField;
    private MaterialEditText mPasswordField;
    private TextView mLoginHelpButton;
    private Button mLoginButton;
    private Button mSignupButton;
    private LoginFragmentListener mLoginFragmentListener;
    private OnLoginSuccessListener mOnLoginSuccessListener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.login_fragment, parent, false);
        mEmailField = (MaterialEditText) mRoot.findViewById(R.id.user_name_edit);
        mPasswordField = (MaterialEditText) mRoot.findViewById(R.id.password_edit);
        mLoginHelpButton = (Button) mRoot.findViewById(R.id.parse_login_help);
        mLoginButton = (Button) mRoot.findViewById(R.id.login_btn);
        mSignupButton = (Button) mRoot.findViewById(R.id.sign_up_btn);

        mEmailField.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mEmailField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mPasswordField.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mPasswordField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mLoginHelpButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);

        return mRoot;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof LoginFragmentListener) {
            mLoginFragmentListener = (LoginFragmentListener) context;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement LoginFragmentListener");
        }

        if (context instanceof OnLoginSuccessListener) {
            mOnLoginSuccessListener = (OnLoginSuccessListener) context;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement OnLoginSuccessListener");
        }

        if (context instanceof OnLoadingListener) {
            onLoadingListener = (OnLoadingListener) context;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement OnLoadingListener");
        }
    }

    @Override
    public void onClick(View view) {
        final String email;
        final String password;

        switch (view.getId()) {
            case R.id.login_btn:
                email = mEmailField.getText().toString();
                password = mPasswordField.getText().toString();

                if (email.length() == 0) {
                    mEmailField.setError(getString(R.string.com_parse_ui_no_email_toast));
                } else if (password.length() == 0) {
                    mPasswordField.setError(getString(R.string.no_password_toast));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmailField.setError(getString(R.string.com_parse_ui_invalid_email_toast));
                } else {

                    ThreadExecutor.doNetworkTaskAsync(new Callable<ParseUser>() {
                        @Override
                        public ParseUser call() throws Exception {
                            return ShoppistServerAuthenticate.userSignIn(email, password);
                        }
                    }, new ExecutorListener<ParseUser>() {
                        @Override
                        public void start() {
                            loadingStart();
                        }

                        @Override
                        public void complete(ParseUser user) {
                            Bundle data = new Bundle();
                            data.putString(AccountManager.KEY_ACCOUNT_NAME, email);
                            data.putString(AccountManager.KEY_ACCOUNT_TYPE, ShoppistAuthenticator.ACCOUNT_TYPE);
                            data.putString(AccountManager.KEY_AUTHTOKEN, user.getSessionToken());
                            data.putString(ShoppistAuthenticator.PARAM_USER_PASS, password);

                            // We keep the user's object id as an extra data on the account.
                            // It's used later for determine ACL for the data we send to the Parse.com service
                            Bundle userData = new Bundle();
                            userData.putString(ShoppistAuthenticator.USERDATA_USER_OBJ_ID, user.getObjectId());
                            data.putBundle(AccountManager.KEY_USERDATA, userData);

                            final Intent res = new Intent();
                            res.putExtras(data);
                            loginSuccess(res);
                            loadingFinish();
                        }

                        @Override
                        public void error(Exception e) {
                            loadingFinish();
                            if (e != null) {
                                Log.e(LOG_TAG, getString(R.string.com_parse_ui_login_warning_parse_login_failed) +
                                        e.toString());
                                if (e instanceof ParseException && ((ParseException) e).getCode() == ParseException.OBJECT_NOT_FOUND) {
                                    ParseErrorHandler.showToast(getActivity(), R.string.parse_login_invalid_credentials_toast);

                                    mPasswordField.selectAll();
                                    mPasswordField.requestFocus();
                                } else {
                                    ParseErrorHandler.showToast(getActivity(),
                                            R.string.parse_login_failed_unknown_toast);
                                }
                            }
                        }
                    });
                }
                break;
            case R.id.sign_up_btn:
                email = mEmailField.getText().toString();
                password = mPasswordField.getText().toString();

                mLoginFragmentListener.onSignUpClicked(email, password);
                break;
            case R.id.parse_login_help:
                mLoginFragmentListener.onLoginHelpClicked();

                break;
        }
    }

    private void loginSuccess(Intent intent) {
        mOnLoginSuccessListener.onLoginSuccess(intent, false);
    }

}
