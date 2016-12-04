/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.shared.base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.shared.widget.actionmode.ActionModeInteractionListener;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseListActivity extends BaseActivity
        implements ActionModeInteractionListener, ActionMode.Callback {

    private static final String IS_ACTION_MODE_SHOWING = "is_action_mode_showing";
    private static final String ACTION_MODE_TITLE = "action_mode_title";

    protected ActionMode actionMode;
    protected boolean isActionModeShowing;

    @Override
    public boolean isActionModeShowing() {
        return isActionModeShowing;
    }

    @Override
    public void closeActionMode() {
        if (actionMode != null) {
            actionMode.finish();
            actionMode = null;
        }
    }

    @Override
    public void openActionMode(int count) {
        actionMode = startSupportActionMode(this);
        updateActionMode(count);
    }

    @Override
    public void updateActionMode(int count) {
        if (actionMode != null) {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_ACTION_MODE_SHOWING, isActionModeShowing);
        if (isActionModeShowing) {
            outState.putInt(ACTION_MODE_TITLE, Integer.valueOf(actionMode.getTitle().toString()));
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            isActionModeShowing = savedInstanceState.getBoolean(IS_ACTION_MODE_SHOWING);
            if (isActionModeShowing) {
                openActionMode(savedInstanceState.getInt(ACTION_MODE_TITLE, 0));
            }
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
        isActionModeShowing = true;
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        setStatusBarColor();
        isActionModeShowing = false;
    }
}
