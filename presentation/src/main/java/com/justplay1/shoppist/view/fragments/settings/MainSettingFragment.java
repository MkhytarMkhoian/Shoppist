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

package com.justplay1.shoppist.view.fragments.settings;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.ColorThemeUpdater;
import com.justplay1.shoppist.view.activities.SettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhytar Mkhoian.
 */
public class MainSettingFragment extends PreferenceFragment implements ColorThemeUpdater, AdapterView.OnItemClickListener {

    private MainSettingFragmentInteractionListener listener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            listener = (MainSettingFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);
        final String titleId = "title";
        String[] texts = {getString(R.string.category_general),
                getString(R.string.shopping_list),
                getString(R.string.category_system)};

        ArrayList<Map<String, Object>> data = new ArrayList<>(texts.length);
        Map<String, Object> m;
        for (String text : texts) {
            m = new HashMap<>();
            m.put(titleId, text);
            data.add(m);
        }

        String[] from = {titleId};
        int[] to = {android.R.id.text1};

        SimpleAdapter adapter = new SimpleAdapter(getActivity(), data, android.R.layout.simple_list_item_1, from, to);
        ListView listView = (ListView) view.findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
    }

    @Override
    public void updateTheme() {
        ((SettingsActivity) getActivity()).setStatusBarColor();
        ((SettingsActivity) getActivity()).refreshToolbarColor();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                listener.openGeneralSetting();
                break;
            case 1:
                listener.openListsSetting();
                break;
            case 2:
                listener.openSystemSetting();
                break;
        }
    }

    public interface MainSettingFragmentInteractionListener {

        void openGeneralSetting();

        void openListsSetting();

        void openSystemSetting();
    }
}
