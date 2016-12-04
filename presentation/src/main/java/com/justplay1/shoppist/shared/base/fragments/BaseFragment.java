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

package com.justplay1.shoppist.shared.base.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.HasInjector;
import com.justplay1.shoppist.preferences.AppPreferences;

import java.util.Random;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseFragment extends Fragment {

    @Inject
    protected AppPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    protected abstract void init(View view);

    /**
     * Inject the dependencies
     */
    protected void injectDependencies() {
        App.get().getAppComponent().inject(this);
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getInjector(Class<C> injectorType) {
        return injectorType.cast(((HasInjector) getActivity()).getInjector(injectorType.getName()));
    }

    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    protected void share(String textToSend, String header) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, header);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.send_to)));
    }

    protected int getRandomColor() {
        Random random = new Random();
        int[] color1 = getResources().getIntArray(R.array.categories_colors);
        int[] color2 = getResources().getIntArray(R.array.color_theme);
        int position = random.nextInt(color1.length + color2.length);
        if (position < color1.length) {
            return color1[position];
        } else {
            return color2[position - color1.length];
        }
    }
}
