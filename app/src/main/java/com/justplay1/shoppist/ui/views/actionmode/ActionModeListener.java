package com.justplay1.shoppist.ui.views.actionmode;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Mkhitar on 02.12.2014.
 */
public interface ActionModeListener {

    void onItemClick(ActionMode actionMode, MenuItem menuItem);
    void onDestroyActionMode(ActionMode actionMode);
    void onPrepareActionMode(ActionMode actionMode, Menu menu);
    void onCreateActionMode(ActionMode actionMode, Menu menu);
}
