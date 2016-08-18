package com.justplay1.shoppist.view.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.justplay1.shoppist.R;

public abstract class SingleListFragmentActivity<F extends Fragment> extends BaseListActivity {

    protected static final String FRAGMENT_TAG = "single_fragment";

    protected F mFragment;

    public abstract F createFragment();

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mFragment = (F) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_TAG);
        }
        if (mFragment == null) {
            mFragment = createFragment();
            if (mFragment != null) {
                replaceFragment(R.id.container, mFragment, FRAGMENT_TAG);
            }
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, mFragment);
    }
}
