package com.justplay1.shoppist.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.component.search.FloatingSearchView;
import com.justplay1.shoppist.view.fragments.SearchFragment;

/**
 * Created by Mkhytar on 11.02.2016.
 */
public class SearchActivity extends SingleFragmentActivity<SearchFragment> {

    private int mContextType;
    private String mParentListId;

    public static Intent getCallingIntent(Context context, int contextType, String parentListId) {
        Intent callingIntent = new Intent(context, SearchActivity.class);
        callingIntent.putExtra(Const.SEARCH_CONTEXT_TYPE, contextType);
        callingIntent.putExtra(Const.PARENT_LIST_ID, parentListId);
        return callingIntent;
    }

    @Override
    public SearchFragment createFragment() {
        return SearchFragment.newInstance(mParentListId, mContextType);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
