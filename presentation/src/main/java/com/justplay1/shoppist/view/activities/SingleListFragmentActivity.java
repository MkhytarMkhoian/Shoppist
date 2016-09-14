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
