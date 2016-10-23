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

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.SearchComponent;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.presenter.SearchPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.utils.ViewUtils;
import com.justplay1.shoppist.view.SearchView;
import com.justplay1.shoppist.view.activities.BaseActivity;
import com.justplay1.shoppist.view.adapters.SearchAdapter;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.view.component.recyclerview.holders.BaseViewHolder;
import com.justplay1.shoppist.view.component.search.FloatingSearchView;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SearchFragment extends BaseFragment
        implements FloatingSearchView.OnIconClickListener,
        FloatingSearchView.OnSearchListener, ActionMenuView.OnMenuItemClickListener,
        FloatingSearchView.OnSearchFocusChangedListener, FloatingSearchView.OnContainerTouchClickListener,
        ShoppistRecyclerView.OnItemClickListener<BaseViewHolder>, SearchView {

    private static final int REQ_CODE_SPEECH_INPUT = 111;

    @Inject
    SearchPresenter presenter;

    @Bind(R.id.search_view)
    FloatingSearchView searchView;

    private int contextType;
    private SearchAdapter adapter;

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
        presenter.onCreate(getArguments(), savedInstanceState);
        if (getArguments() != null) {
            contextType = getArguments().getInt(Const.SEARCH_CONTEXT_TYPE, Const.CONTEXT_QUICK_SEARCH_IN_GOODS_LIST);
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
        ButterKnife.bind(this, view);
        init(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        getInjector(SearchComponent.class).inject(this);
    }

    @Override
    protected void init(View view) {
        adapter = new SearchAdapter(getContext(), contextType);
        adapter.setClickListener(this);

        searchView.setAdapter(adapter);
        updateNavigationIcon();
        searchView.showIcon(true);
        searchView.setOnIconClickListener(this);
        searchView.setOnSearchListener(this);
        searchView.setOnMenuItemClickListener(this);
        searchView.setOnSearchFocusChangedListener(this);
        searchView.setOnContainerTouchClickListener(this);
        searchView.setText(null);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                showVoiceButton(query.length() == 0 && searchView.isActivated());
                showClearButton(query.length() > 0 && searchView.isActivated());
                search(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                searchView.removeOnLayoutChangeListener(this);
                searchView.setActivated(true);
                presenter.attachView(SearchFragment.this);
            }
        });
        searchView.setHint(getString(R.string.search));
    }

    private void updateNavigationIcon() {
        Context context = searchView.getContext();
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_back);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ViewUtils.getThemeAttrColor(context, R.attr.colorControlNormal));
        searchView.setIcon(drawable);
    }

    private void search(String query) {
        adapter.getFilter().filter(query);
    }

    private void showProgressBar(boolean show) {
        searchView.getMenu().findItem(R.id.menu_progress_bar).setVisible(show);
    }

    private void showClearButton(boolean show) {
        searchView.getMenu().findItem(R.id.menu_clear).setVisible(show);
    }

    private void showVoiceButton(boolean show) {
        searchView.getMenu().findItem(R.id.menu_voice_search).setVisible(show);
    }

    @Override
    public void onNavigationClick() {
        presenter.onNavigationClick();
    }

    @Override
    public void onSearchAction(CharSequence charSequence) {
        searchView.setActivated(false);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                clearSearch();
                break;
            case R.id.menu_voice_search:
                openVoiceSearch();
                break;
        }
        return true;
    }

    @Override
    public void onFocusChanged(boolean focused) {
        if (searchView == null) return;
        boolean textEmpty = searchView.getText().length() == 0;
        showClearButton(focused && !textEmpty);
        showVoiceButton(focused && textEmpty);
        if (!focused) showProgressBar(false);
        searchView.showLogo(!focused && textEmpty);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        presenter.onTouch();
        return true;
    }

    @Override
    public void onItemClick(BaseViewHolder holder, int position, long id) {
        presenter.onListItemClick(adapter.getItem(position));
    }

    @Override
    public boolean onItemLongClick(BaseViewHolder holder, int position, long id) {
        return false;
    }

    public void clearSearch() {
        searchView.setText(null);
        searchView.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
    }

    @Override
    public void showData(Map<String, ProductViewModel> data) {
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void closeSearch() {
        getActivity().finish();
    }

    @Override
    public void fadeInSignal(@ColorInt int color) {
        searchView.fadeInSignal(color, animator -> {
            if (searchView == null) return;
            int value = (int) animator.getAnimatedValue();
            searchView.setBackgroundColor(value);
            ((BaseActivity) getActivity()).setStatusBarColor(value);
        });
    }

    public void openVoiceSearch() {
        startTextToSpeech(null, REQ_CODE_SPEECH_INPUT);
    }

    private void startTextToSpeech(String prompt, int requestCode) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(), getString(R.string.recognition_not_present), Toast.LENGTH_SHORT).show();
        }
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
        dialog.show(fm, AddUnitsDialogFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                searchView.setActivated(true);
                searchView.setText(result.get(0));
            }
        }
    }
}
