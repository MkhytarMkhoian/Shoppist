package com.justplay1.shoppist.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;

/**
 * Created by Mkhitar on 18.06.2015.
 */
public class AccountHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_account_activity);
        ActivityUtils.setStatusBarColor(this);
        initializeToolbar();

    }

    private void initializeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.help_account);
        toolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }
}
