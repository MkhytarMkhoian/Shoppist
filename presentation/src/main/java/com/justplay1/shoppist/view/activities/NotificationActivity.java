package com.justplay1.shoppist.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.fragments.NotificationFragment;

/**
 * Created by Mkhytar on 11.11.2015.
 */
public class NotificationActivity extends SingleListFragmentActivity<NotificationFragment>
        implements Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, NotificationActivity.class);
        return callingIntent;
    }

    @Override
    public NotificationFragment createFragment() {
        return NotificationFragment.newInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.notifications);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishActivity());
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.inflateMenu(R.menu.notifications_toolbar);
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_all:
                mFragment.onClearAllClick();
                break;
            case R.id.action_mark_as_viewed:
                mFragment.onMarkAsViewedClick();
                break;
        }
        return true;
    }
}
