package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.ActionMenuView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerSearchComponent;
import com.justplay1.shoppist.di.components.SearchComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.di.modules.GoodsModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.presenter.SearchPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ViewUtils;
import com.justplay1.shoppist.view.SearchView;
import com.justplay1.shoppist.view.activities.BaseActivity;
import com.justplay1.shoppist.view.adapters.SearchAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.view.component.search.FloatingSearchView;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;

import java.util.ArrayList;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 07.08.2016.
 */
public class SearchFragment extends BaseFragment
        implements FloatingSearchView.OnIconClickListener,
        FloatingSearchView.OnSearchListener, ActionMenuView.OnMenuItemClickListener,
        FloatingSearchView.OnSearchFocusChangedListener, FloatingSearchView.OnContainerTouchClickListener,
        ShoppistRecyclerView.OnItemClickListener, SearchView {

    @Inject
    SearchPresenter mPresenter;

    private SearchComponent mComponent;
    private int mContextType;
    private FloatingSearchView mSearchView;
    private SearchAdapter mAdapter;

    public static SearchFragment newInstance(String parentListId, int contextType) {
        Bundle args = new Bundle();
        args.putString(Const.PARENT_LIST_ID, parentListId);
        args.putInt(Const.SEARCH_CONTEXT_TYPE, contextType);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
        if (getArguments() != null) {
            mContextType = getArguments().getInt(Const.SEARCH_CONTEXT_TYPE, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seach, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        init(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    protected void injectDependencies() {
        mComponent = DaggerSearchComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .categoryModule(new CategoryModule())
                .listItemsModule(new ListItemsModule())
                .goodsModule(new GoodsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void init(View view) {
        mAdapter = new SearchAdapter(getContext(), mContextType);
        mAdapter.setClickListener(this);

        mSearchView = (FloatingSearchView) view.findViewById(R.id.search_view);
        mSearchView.setAdapter(mAdapter);
        updateNavigationIcon();
        mSearchView.showIcon(true);
        mSearchView.setOnIconClickListener(this);
        mSearchView.setOnSearchListener(this);
        mSearchView.setOnMenuItemClickListener(this);
        mSearchView.setOnSearchFocusChangedListener(this);
        mSearchView.setOnContainerTouchClickListener(this);
        mSearchView.setText(null);
        mSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                showVoiceButton(query.length() == 0 && mSearchView.isActivated());
                showClearButton(query.length() > 0 && mSearchView.isActivated());
                search(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mSearchView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mSearchView.removeOnLayoutChangeListener(this);
                mSearchView.setActivated(true);
                mPresenter.init();
            }
        });
        mSearchView.setHint(getString(R.string.search));
    }

    private void updateNavigationIcon() {
        Context context = mSearchView.getContext();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_arrow_back);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ViewUtils.getThemeAttrColor(context, R.attr.colorControlNormal));
        mSearchView.setIcon(drawable);
    }

    private void search(String query) {
        //   showProgressBar(mSearchView.isActivated());
        mAdapter.getFilter().filter(query);
    }

    private void showProgressBar(boolean show) {
        mSearchView.getMenu().findItem(R.id.menu_progress_bar).setVisible(show);
    }

    private void showClearButton(boolean show) {
        mSearchView.getMenu().findItem(R.id.menu_clear).setVisible(show);
    }

    private void showVoiceButton(boolean show) {
        mSearchView.getMenu().findItem(R.id.menu_voice_search).setVisible(show);
    }

    @Override
    public void onNavigationClick() {
        mPresenter.onNavigationClick();
    }

    @Override
    public void onSearchAction(CharSequence charSequence) {
        mSearchView.setActivated(false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                mPresenter.onClearClick();
                break;
            case R.id.menu_voice_search:
                mPresenter.onVoiceSearchClick();
                break;
        }
        return true;
    }

    @Override
    public void onFocusChanged(boolean focused) {
        boolean textEmpty = mSearchView.getText().length() == 0;
        showClearButton(focused && !textEmpty);
        showVoiceButton(focused && textEmpty);
        if (!focused) showProgressBar(false);
        mSearchView.showLogo(!focused && textEmpty);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mPresenter.onTouch();
        return true;
    }

    @Override
    public void onItemClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemClick(mAdapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        mPresenter.onListItemLongClick(mAdapter.getItem(position));
        return true;
    }

    @Override
    public void clearSearch() {
        mSearchView.setText(null);
        mSearchView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    }

    @Override
    public void showData(Map<String, ProductViewModel> data) {
        mAdapter.setData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void closeSearch() {
        getActivity().finish();
    }

    @Override
    public void fadeInSignal(@ColorInt int color) {
        mSearchView.fadeInSignal(color, animator -> {
            int value = (int) animator.getAnimatedValue();
            mSearchView.setBackgroundColor(value);
            ((BaseActivity) getActivity()).setStatusBarColor(value);
        });
    }

    @Override
    public void openVoiceSearch() {
        startTextToSpeech(getActivity(), null, Const.REQ_CODE_SPEECH_INPUT);
    }

    @Override
    public void showEditGoodsDialog(String name) {
        showEditGoodsDialog(AddGoodsDialogFragment.newInstance(name));
    }

    @Override
    public void showEditGoodsDialog(ProductViewModel product) {
        showEditGoodsDialog(AddGoodsDialogFragment.newInstance(product));
    }

    private void showEditGoodsDialog(AddGoodsDialogFragment dialog) {
        FragmentManager fm = getFragmentManager();
        dialog.setCompleteListener(isUpdate -> mPresenter.loadData());
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Const.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchView.setActivated(true);
                    mSearchView.setText(result.get(0));
                }
                break;
            }
        }
    }
}