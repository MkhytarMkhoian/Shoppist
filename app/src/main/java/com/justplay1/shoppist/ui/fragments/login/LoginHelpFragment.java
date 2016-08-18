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

import android.content.Context;
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
import com.justplay1.shoppist.communication.network.ParseErrorHandler;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Fragment for the login help screen for resetting the user's password.
 */
public class LoginHelpFragment extends LoginFragmentBase implements OnClickListener {

    public interface OnLoginHelpSuccessListener {
        void onLoginHelpSuccess();
    }

    private TextView mInstructionsTextView;
    private MaterialEditText mEmailField;
    private Button mSubmitButton;
    private boolean emailSent = false;
    private OnLoginHelpSuccessListener onLoginHelpSuccessListener;

    private static final String LOG_TAG = "ParseLoginHelpFragment";

    public static LoginHelpFragment newInstance() {
        return new LoginHelpFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.login_help_fragment, parent, false);
        mInstructionsTextView = (TextView) v.findViewById(R.id.login_help_instructions);
        mEmailField = (MaterialEditText) v.findViewById(R.id.login_help_email_input);
        mEmailField.setPrimaryColor(ShoppistPreferences.getColorPrimary());
        mEmailField.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));

        mSubmitButton = (Button) v.findViewById(R.id.login_help_submit);
        mSubmitButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnLoadingListener) {
            onLoadingListener = (OnLoadingListener) context;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoadingListener");
        }

        if (context instanceof OnLoginHelpSuccessListener) {
            onLoginHelpSuccessListener = (OnLoginHelpSuccessListener) context;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implemement ParseOnLoginHelpSuccessListener");
        }
    }

    @Override
    public void onClick(View v) {
        if (!emailSent) {
            String email = mEmailField.getText().toString();
            if (email.length() == 0) {
                mEmailField.setError(getString(R.string.com_parse_ui_no_email_toast));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mEmailField.setError(getString(R.string.com_parse_ui_invalid_email_toast));
            }  else {
                loadingStart();
                ParseUser.requestPasswordResetInBackground(email,
                        new RequestPasswordResetCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (isActivityDestroyed()) {
                                    return;
                                }

                                loadingFinish();
                                if (e == null) {
                                    mInstructionsTextView
                                            .setText(R.string.com_parse_ui_login_help_email_sent);
                                    mEmailField.setVisibility(View.INVISIBLE);
                                    mSubmitButton
                                            .setText(R.string.com_parse_ui_login_help_login_again_button_label);
                                    emailSent = true;
                                } else {
                                    Log.e(LOG_TAG, getString(R.string.com_parse_ui_login_warning_password_reset_failed) +
                                            e.toString());
                                    if (e.getCode() == ParseException.INVALID_EMAIL_ADDRESS ||
                                            e.getCode() == ParseException.EMAIL_NOT_FOUND) {
                                        ParseErrorHandler.handleParseError(e);
                                    } else {
                                        ParseErrorHandler.showToast(getActivity(), R.string.com_parse_ui_login_help_submit_failed_unknown);
                                    }
                                }
                            }
                        });
            }
        } else {
            onLoginHelpSuccessListener.onLoginHelpSuccess();
        }
    }
}
