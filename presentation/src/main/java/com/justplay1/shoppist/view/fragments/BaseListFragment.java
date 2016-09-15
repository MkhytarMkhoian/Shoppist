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

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.justplay1.shoppist.view.component.EmptyView;
import com.justplay1.shoppist.view.component.actionmode.ActionModeInteractionListener;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseListFragment extends BaseFragment implements View.OnClickListener{

    protected EmptyView mEmptyView;
    protected CustomProgressDialog mProgressDialog;
    protected ShoppistRecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected ActionModeInteractionListener mActionModeInteractionListener;
    protected FloatingActionButton mActionButton;

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActionModeInteractionListener = (ActionModeInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ActionModeInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        init(view);
        initRecyclerView(view, savedInstanceState);
        return view;
    }

    @Override
    protected void init(View view) {
        mProgressDialog = new CustomProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setColor(mPreferences.getColorPrimary());
        mEmptyView = (EmptyView) view.findViewById(android.R.id.empty);

        mActionButton = (FloatingActionButton) view.findViewById(R.id.add_button);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
        mActionButton.setOnClickListener(this);
    }

    protected void initRecyclerView(View view, Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView = (ShoppistRecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(mEmptyView);

        initAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }
        mLayoutManager = null;
    }

    protected void deleteItems(String message, @NonNull DeleteListener listener) {
        if (mPreferences.isNeedShowConfirmDeleteDialog()) {
            showConfirmDeleteDialog(message, (dialog, which) -> {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        listener.onDelete();
                        dialog.dismiss();
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            });
        } else {
            listener.onDelete();
        }
    }

    protected void showConfirmDeleteDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message);
        builder.setPositiveButton(R.string.action_delete, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }

    public interface DeleteListener {

        void onDelete();
    }
}
