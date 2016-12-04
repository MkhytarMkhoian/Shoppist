/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.utils;

import android.support.test.rule.ActivityTestRule;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.test.TestListActivity;

import static junit.framework.Assert.fail;

/**
 * Created by Mkhytar Mkhoian.
 */

public class FragmentTestRule<F extends Fragment> extends ActivityTestRule<TestListActivity> {

    private final Class<F> fragmentClass;
    private F fragment;

    public FragmentTestRule(final Class<F> fragmentClass) {
        super(TestListActivity.class, true, false);
        this.fragmentClass = fragmentClass;
    }

    @Override
    protected void afterActivityLaunched() {
        super.afterActivityLaunched();

        getActivity().runOnUiThread(() -> {
            try {
                //Instantiate and insert the fragment into the container layout
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                fragment = fragmentClass.newInstance();
                transaction.replace(R.id.container, fragment);
                transaction.commit();
            } catch (Exception e) {
                fail(String.format("%s: Could not insert %s into TestListActivity: %s",
                        getClass().getSimpleName(),
                        fragmentClass.getSimpleName(),
                        e.getMessage()));
            }
        });
    }

    public F getFragment() {
        return fragment;
    }
}