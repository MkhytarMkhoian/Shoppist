package com.justplay1.shoppist.ui.activities;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainHelpActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_main_activity);
        ActivityUtils.setStatusBarColor(this);
        initToolbar();

        final String titleId = "title";
        String[] texts = {getString(R.string.help_shopping_lists).split(": ")[1],
                getString(R.string.help_shopping_list_items).split(": ")[1],
                getString(R.string.help_categories).split(": ")[1]};

//        getString(R.string.help_account).split(": ")[1]

        ArrayList<Map<String, Object>> data = new ArrayList<>(texts.length);
        Map<String, Object> m;
        for (String text : texts) {
            m = new HashMap<>();
            m.put(titleId, text);
            data.add(m);
        }

        String[] from = {titleId};
        int[] to = {android.R.id.text1};

        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1,
                from, to);

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_help);
        toolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        toolbar.setNavigationIcon(R.drawable.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewCompat.setElevation(toolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                ActivityUtils.startNextActivity(this, ShoppingListsHelpActivity.class, view, null);
                break;
            case 1:
                ActivityUtils.startNextActivity(this, ShoppingListItemsHelpActivity.class, view, null);
                break;
            case 2:
                ActivityUtils.startNextActivity(this, CategoriesHelpActivity.class, view, null);
                break;
            case 3:
                ActivityUtils.startNextActivity(this, AccountHelpActivity.class, view, null);
                break;
        }
    }
}
