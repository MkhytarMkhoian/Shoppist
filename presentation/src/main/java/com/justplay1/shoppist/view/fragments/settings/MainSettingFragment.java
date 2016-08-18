package com.justplay1.shoppist.view.fragments.settings;

import android.content.Context;
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
 * Created by Mkhitar on 01.03.2015.
 */
public class MainSettingFragment extends PreferenceFragment implements ColorThemeUpdater, AdapterView.OnItemClickListener {

    private boolean disableAccount;
    private MainSettingFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MainSettingFragmentInteractionListener) context;
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
        ((SettingsActivity) getActivity()).setStatusBarColor();
        ((SettingsActivity) getActivity()).refreshToolbarColor();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                mListener.openGeneralSetting();
                break;
            case 1:
                mListener.openListsSetting();
                break;
            case 2:
                mListener.openNotificationSetting();
                break;
            case 3:
                mListener.openSystemSetting();
                break;
            case 4:
                mListener.openAccountSetting();
                break;
        }
    }

    public interface MainSettingFragmentInteractionListener {

        void openGeneralSetting();

        void openListsSetting();

        void openNotificationSetting();

        void openSystemSetting();

        void openAccountSetting();
    }
}
