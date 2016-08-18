package com.justplay1.shoppist.ui.fragments.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.activities.SettingsActivity;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.ColorThemeUpdater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mkhitar on 01.03.2015.
 */
public class MainSettingFragment extends PreferenceFragment implements ColorThemeUpdater, AdapterView.OnItemClickListener {

    protected boolean disableAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.setStatusBarColor(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_setting, container, false);
        final String titleId = "title";
        String[] texts = {getString(R.string.category_general),
                getString(R.string.shopping_list),
                getString(R.string.category_notification),
                getString(R.string.category_system)};

        ArrayList<Map<String, Object>> data = new ArrayList<>(texts.length);
        Map<String, Object> m;
        for (String text : texts) {
            m = new HashMap<>();
            m.put(titleId, text);
            data.add(m);
        }

        if (!disableAccount) {
            m = new HashMap<>();
            m.put(titleId, getString(R.string.category_account));
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
        if (ShoppistPreferences.isNeedUpdateTheme(MainSettingFragment.class.getName())) {
            ShoppistPreferences.deleteUpdateItem(MainSettingFragment.class.getName());

            ActivityUtils.setStatusBarColor(getActivity());
            ((SettingsActivity) getActivity()).refreshToolbarColor();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        switch (position) {
            case 0:
                bundle.putInt(SettingsActivity.SETTING_ID, SettingsActivity.GENERAL_SETTING);
                ActivityUtils.startNextActivity(getActivity(), SettingsActivity.class, null, bundle);
                break;
            case 1:
                bundle.putInt(SettingsActivity.SETTING_ID, SettingsActivity.SHOPPING_LISTS_SETTING);
                ActivityUtils.startNextActivity(getActivity(), SettingsActivity.class, null, bundle);
                break;
            case 2:
                bundle.putInt(SettingsActivity.SETTING_ID, SettingsActivity.NOTIFICATION_SETTING);
                ActivityUtils.startNextActivity(getActivity(), SettingsActivity.class, null, bundle);
                break;
            case 3:
                bundle.putInt(SettingsActivity.SETTING_ID, SettingsActivity.SYSTEM_SETTING);
                ActivityUtils.startNextActivity(getActivity(), SettingsActivity.class, null, bundle);
                break;
            case 4:
                bundle.putInt(SettingsActivity.SETTING_ID, SettingsActivity.ACCOUNT_SETTING);
                ActivityUtils.startNextActivity(getActivity(), SettingsActivity.class, null, bundle);
                break;
        }
    }
}
