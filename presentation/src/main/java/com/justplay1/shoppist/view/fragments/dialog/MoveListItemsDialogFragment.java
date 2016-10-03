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

package com.justplay1.shoppist.view.fragments.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerMoveListItemsComponent;
import com.justplay1.shoppist.di.components.MoveListItemsComponent;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.presenter.MoveListItemsPresenter;
import com.justplay1.shoppist.view.MoveListItemsView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class MoveListItemsDialogFragment extends BaseDialogFragment implements MoveListItemsView {

    @Inject
    MoveListItemsPresenter mPresenter;

    private OnCompleteListener mCompleteListener;
    private MoveListItemsComponent mComponent;
    private ListView listView;
    private SimpleAdapter adapter;

    public static MoveListItemsDialogFragment newInstance(ListViewModel current, List<ListItemViewModel> items, boolean isCopy) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ListItemViewModel.class.getName(), new ArrayList<>(items));
        args.putParcelable(ListViewModel.class.getName(), current);
        args.putBoolean("isCopy", isCopy);
        MoveListItemsDialogFragment fragment = new MoveListItemsDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerMoveListItemsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .build();
        mComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
    }

    @Override
    public void init(View view) {
        super.init(view);
        listView = (ListView) view.findViewById(R.id.list_view);

        mPositiveButton.setText(R.string.choose);
        mNegativeButton.setText(R.string.cancel);
        getDialog().setTitle(R.string.title_activity_shopping_lists);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_move_list_items_dialog;
    }

    @Override
    public void closeDialog() {
        dismiss();
        if (mCompleteListener != null) {
            mCompleteListener.onComplete();
        }
    }

    @Override
    public void showData(ArrayList<Map<String, Object>> data) {
        String[] from = {"name"};
        int[] to = {android.R.id.text1};

        adapter = new SimpleAdapter(getContext(), data, R.layout.dialog_list_item, from, to);
        adapter.setDropDownViewResource(R.layout.dialog_list_item);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                ListViewModel newList = (ListViewModel) (((Map<String, Object>) adapter.getItem(listView.getCheckedItemPosition())).get("object"));
                mPresenter.onPositiveButtonClick(newList);
                break;
            case R.id.negative_button:
                dismiss();
                break;
        }
    }

    @Override
    public void showLoading() {
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    public void setCompleteListener(OnCompleteListener listener) {
        mCompleteListener = listener;
    }

    public interface OnCompleteListener {

        void onComplete();
    }
}
