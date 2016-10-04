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

package com.justplay1.shoppist.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;

/**
 * Created by Mkhytar Mkhoian.
 */
public class MenuFragment extends BaseFragment implements NavigationView.OnNavigationItemSelectedListener {

    private MenuFragmentInteraction listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MenuFragmentInteraction) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    @Override
    protected void init(View view) {
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.drawer_menu);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(getContext()).inflate(R.layout.menu_header, navigationView, false);
        navigationView.addHeaderView(header);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.goods_button:
                listener.onGoodsClick();
                break;
            case R.id.category_button:
                listener.onCategoryClick();
                break;
            case R.id.currencies_button:
                listener.onCurrencyClick();
                break;
            case R.id.units_button:
                listener.onUnitsClick();
                break;
            case R.id.settings_button:
                listener.onSettingClick(0);
                break;
            case R.id.feedback_button:
                listener.onFeedbackClick();
                break;
        }
        return true;
    }

    public interface MenuFragmentInteraction {

        void onCategoryClick();

        void onCurrencyClick();

        void onSettingClick(int settingId);

        void onFeedbackClick();

        void onGoodsClick();

        void onUnitsClick();
    }
}
