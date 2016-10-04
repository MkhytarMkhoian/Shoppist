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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerSearchComponent;
import com.justplay1.shoppist.di.components.SearchComponent;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.component.search.FloatingSearchView;
import com.justplay1.shoppist.view.fragments.SearchFragment;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SearchActivity extends BaseActivity {

    private int mContextType;
    private String mParentListId;

    public static Intent getCallingIntent(Context context, int contextType, String parentListId) {
        Intent callingIntent = new Intent(context, SearchActivity.class);
        callingIntent.putExtra(Const.SEARCH_CONTEXT_TYPE, contextType);
        callingIntent.putExtra(Const.PARENT_LIST_ID, parentListId);
        return callingIntent;
    }

    public SearchFragment createFragment() {
        return SearchFragment.newInstance(mParentListId, mContextType);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNewInjectorIfNeeded();
        setContentView(R.layout.layout_fragment_container);
        setStatusBarColor(FloatingSearchView.DEFAULT_BACKGROUND_COLOR);

        if (getIntent() != null) {
            mContextType = getIntent().getIntExtra(Const.SEARCH_CONTEXT_TYPE, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
            switch (mContextType) {
                case Const.CONTEXT_QUICK_ADD_GOODS_TO_LIST:
                    mParentListId = getIntent().getStringExtra(Const.PARENT_LIST_ID);
                    break;
                case Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST:
                    break;
            }
        }

        SearchFragment fragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag(SearchFragment.class.getName());
        if (fragment == null) {
            fragment = createFragment();
        }
        replaceFragment(R.id.container, fragment, SearchFragment.class.getName());
    }

    private void createNewInjectorIfNeeded() {
        SearchComponent component = getInjector(SearchComponent.class.getName());
        if (component == null) {
            component = DaggerSearchComponent.builder()
                    .appComponent(App.get().getAppComponent())
                    .build();
            putInjector(SearchComponent.class.getName(), component);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
