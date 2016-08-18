package com.justplay1.shoppist.ui.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.communication.network.ServerRequests;
import com.justplay1.shoppist.communication.sync.ShoppistAuthenticator;
import com.justplay1.shoppist.database.ShoppingListContact;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.fragments.login.LoginFragment;
import com.justplay1.shoppist.ui.fragments.login.LoginHelpFragment;
import com.justplay1.shoppist.ui.fragments.login.OnLoadingListener;
import com.justplay1.shoppist.ui.fragments.login.OnLoginSuccessListener;
import com.justplay1.shoppist.ui.fragments.login.SignupFragment;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;

/**
 * Created by Mkhitar on 07.11.2014.
 */
public class LoginActivity extends AppCompatActivity implements
        LoginFragment.LoginFragmentListener,
        LoginHelpFragment.OnLoginHelpSuccessListener,
        OnLoginSuccessListener, OnLoadingListener {

    private final String TAG = this.getClass().getSimpleName();

    public final static String PARAM_USER_PASS = "USER_PASS";

    private CustomProgressDialog mProgressDialog;
    private boolean destroyed = false;

    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    private Bundle mResultBundle = null;
    private String mAuthTokenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActivityUtils.setStatusBarColor(this);

        mAccountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }

        mAccountManager = AccountManager.get(getBaseContext());
        mAuthTokenType = getIntent().getStringExtra(ShoppistAuthenticator.ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = ShoppistAuthenticator.AUTHTOKEN_TYPE_FULL_ACCESS;

        initializeToolbar();

        // Show the login form
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, LoginFragment.newInstance()).commit();
        }
    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_login);
        toolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.super.onBackPressed();
            }
        });
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        destroyed = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onSignUpClicked(String username, String password) {
        // Show the signup form, but keep the transaction on the back stack
        // so that if the user clicks the back button, they are brought back
        // to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, SignupFragment.newInstance(username, password));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoginHelpClicked() {
        // Show the login help form for resetting the user's password.
        // Keep the transaction on the back stack so that if the user clicks
        // the back button, they are brought back to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, LoginHelpFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLoginSuccess(Intent intent, boolean isSingUp) {
        finishLogin(intent);
        loadData(isSingUp);
    }

    private void loadData(boolean isSingUp) {
        if (isSingUp) {
            ServerRequests.singUpSync(new ExecutorListener<Boolean>() {
                @Override
                public void start() {
                    onLoadingStart(true, R.string.sync_data);
                }

                @Override
                public void complete(Boolean result) {
                    onLoadingFinish();
                    setResult(RESULT_OK);
                    finishLoginActivity();
                }

                @Override
                public void error(Exception e) {
                    e.printStackTrace();
                    onLoadingFinish();
                }
            });
        } else {
            ServerRequests.loadAllData();
            setResult(RESULT_OK);
            finishLoginActivity();
        }
    }

    private void finishLoginActivity() {
        finish();
    }

    @Override
    public void onLoadingStart(boolean showSpinner) {
        onLoadingStart(showSpinner, R.string.parse_ui_progress_dialog_text);
    }

    private void onLoadingStart(boolean showSpinner, int titleResId) {
        if (showSpinner) {
            mProgressDialog = new CustomProgressDialog(this);
            mProgressDialog.setMessage(getString(titleResId));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    @Override
    public void onLoadingFinish() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onLoginHelpSuccess() {
        // Display the login form, which is the previous item onto the stack
        getSupportFragmentManager().popBackStackImmediate();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        }
        return destroyed;
    }

    /**
     * Set the result that is to be sent as the result of the request that caused this
     * Activity to be launched. If result is null or this method is never called then
     * the request will be canceled.
     *
     * @param result this is returned as the result of the AbstractAccountAuthenticator request
     */
    public final void setAccountAuthenticatorResult(Bundle result) {
        mResultBundle = result;
    }

    /**
     * Sends the result or a Constants.ERROR_CODE_CANCELED error if a result isn't present.
     */
    public void finish() {
        if (mAccountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (mResultBundle != null) {
                mAccountAuthenticatorResponse.onResult(mResultBundle);
            } else {
                mAccountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

    private void finishLogin(Intent intent) {
        Log.d(ShoppistUtils.MAIN_TAG, TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        App.get().mAccount = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        boolean isAccountEnableOnDevice = true;
        Account[] accounts = mAccountManager.getAccountsByType(ShoppistAuthenticator.ACCOUNT_TYPE);
        for (Account account : accounts) {
            if (account.name.equals(accountName)) {
                App.get().mAccount = account;
                isAccountEnableOnDevice = false;
            }
        }

        if (isAccountEnableOnDevice || intent.getBooleanExtra(ShoppistAuthenticator.ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d(ShoppistUtils.MAIN_TAG, TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            if (mAccountManager.addAccountExplicitly(App.get().mAccount, accountPassword, intent.getBundleExtra(AccountManager.KEY_USERDATA))) {
                ContentResolver.setSyncAutomatically(App.get().mAccount, ShoppingListContact.AUTHORITY, true);
            }
            mAccountManager.setAuthToken(App.get().mAccount, authtokenType, authtoken);
            mAccountManager.setPassword(App.get().mAccount, accountPassword);
        } else {
            Log.d(ShoppistUtils.MAIN_TAG, TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(App.get().mAccount, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
    }
}
