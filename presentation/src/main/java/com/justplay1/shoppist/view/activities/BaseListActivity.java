package com.justplay1.shoppist.view.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;

/**
 * Created by Mkhytar on 28.07.2016.
 */
public abstract class BaseListActivity extends BaseActivity
        implements ActionModeInteractionListener, ActionMode.Callback {

    protected Toolbar mToolbar;
    protected ActionMode mActionMode;
    protected boolean isActionModeShowing;

    @Override
    public boolean isActionModeShowing() {
        return isActionModeShowing;
    }

    @Override
    public void closeActionMode() {
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
            isActionModeShowing = false;
        }
    }

    @Override
    public void openActionMode(int count) {
        mActionMode = startSupportActionMode(this);
        isActionModeShowing = true;
        updateActionMode(count);
    }

    @Override
    public void updateActionMode(int count) {
        if (mActionMode != null) {
            mActionMode.setTitle(String.valueOf(count));
            mActionMode.invalidate();
        }
    }

    @Override
    public void onBackPressed() {
        if (isActionModeShowing()) {
            closeActionMode();
        } else {
            finishActivity(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        setStatusBarColor(ContextCompat.getColor(this, R.color.grey_700));
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        setStatusBarColor();
    }
}
