package com.justplay1.shoppist.view.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.AddElementType;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.view.fragments.AddCategoryFragment;
import com.justplay1.shoppist.view.fragments.AddListFragment;
import com.justplay1.shoppist.view.fragments.AddListItemFragment;
import com.justplay1.shoppist.view.fragments.BaseAddElementFragment;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public class AddElementActivity extends SingleListFragmentActivity<BaseAddElementFragment>
        implements BaseAddElementFragment.AddElementListener, AddListItemFragment.AddListItemListener {

    private Toolbar mToolbar;
    private @AddElementType int mElementType;

    private CategoryViewModel mCategoryModel;
    private ListViewModel mListModel;
    private ListItemViewModel mListItemModel;

    public static Intent getCallingIntent(Context context, @AddElementType int type,
                                          CategoryViewModel category, ListViewModel list,
                                          ListItemViewModel listItem) {
        Intent callingIntent = new Intent(context, AddElementActivity.class);
        callingIntent.putExtra(AddElementType.class.getName(), type);
        callingIntent.putExtra(CategoryViewModel.class.getName(), category);
        callingIntent.putExtra(ListViewModel.class.getName(), list);
        callingIntent.putExtra(ListItemViewModel.class.getName(), listItem);
        return callingIntent;
    }

    @Override
    public BaseAddElementFragment createFragment() {
        switch (mElementType) {
            case AddElementType.CATEGORY:
                return AddCategoryFragment.newInstance(mCategoryModel);
            case AddElementType.LIST:
                return AddListFragment.newInstance(mListModel);
            case AddElementType.LIST_ITEM:
                return AddListItemFragment.newInstance(mListModel.getId(), mListItemModel);
        }
        return null;
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_fragment);
        initToolbar();
        if (getIntent() != null) {
            mElementType = getIntent().getIntExtra(AddElementType.class.getName(), 0);
            mCategoryModel = getIntent().getParcelableExtra(CategoryViewModel.class.getName());
            mListModel = getIntent().getParcelableExtra(ListViewModel.class.getName());
            mListItemModel = getIntent().getParcelableExtra(ListItemViewModel.class.getName());
        }
    }

    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(mPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(v -> finishWithResult());
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    @Override
    public void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    @Override
    public void closeScreen() {
        finishWithResult();
    }

    @Override
    public void openAddCategoryScreen(CategoryViewModel category) {
        mNavigator.navigateToAddCategoryScreen(this, category);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
    }

    protected void finishWithResult() {
        Intent data = new Intent();
        finishActivityWithResult(this, RESULT_OK, data);
    }
}
