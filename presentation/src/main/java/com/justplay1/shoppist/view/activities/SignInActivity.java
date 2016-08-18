package com.justplay1.shoppist.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.fragments.login.SignInFragment;
import com.justplay1.shoppist.view.fragments.login.SignInHelpFragment;
import com.justplay1.shoppist.view.fragments.login.SignUpFragment;

/**
 * Created by Mkhytar on 13.08.2016.
 */
public class SignInActivity extends BaseActivity
        implements SignInFragment.SignInFragmentInteractionListener, SignInHelpFragment.SignInHelpFragmentInteractionListener {

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, SignInActivity.class);
        return callingIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();

        // Show the singIn form
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, SignInFragment.newInstance())
                    .commit();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_login);
        toolbar.setBackgroundColor(mPreferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public void onSignUpClicked(String username, String password) {
        // Show the signup form, but keep the transaction on the back stack
        // so that if the user clicks the back button, they are brought back
        // to the singIn form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, SignUpFragment.newInstance(username, password));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSignInHelpClicked() {
        // Show the singIn help form for resetting the user's password.
        // Keep the transaction on the back stack so that if the user clicks
        // the back button, they are brought back to the singIn form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, SignInHelpFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSignInHelpSuccess() {
        // Display the singIn form, which is the previous item onto the stack
        getSupportFragmentManager().popBackStackImmediate();
    }
}
