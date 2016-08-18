package com.justplay1.shoppist.view.activities;

import android.content.Intent;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;

/**
 * Created by Mkhytar on 28.07.2016.
 */
public abstract class BaseListActivity extends BaseActivity
        implements ActionModeOpenCloseListener, ActionMode.Callback {

    protected Toolbar mToolbar;
    protected ActionMode mActionMode;
    protected boolean isActionModeShowing;

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
        // Set action mode title
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
        setStatusBarColor((getResources().getColor(R.color.grey_700)));
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        setStatusBarColor();
    }
}
