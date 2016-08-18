package com.justplay1.shoppist.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.justplay1.shoppist.view.component.EmptyView;
import com.justplay1.shoppist.view.component.actionmode.ActionModeOpenCloseListener;
import com.justplay1.shoppist.view.component.recyclerview.ShoppistRecyclerView;

/**
 * Created by Mkhytar on 04.07.2016.
 */
public abstract class BaseListFragment extends BaseFragment {

    protected EmptyView mEmptyView;
    protected CustomProgressDialog mProgressDialog;
    protected ShoppistRecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected ActionModeOpenCloseListener mActionModeOpenCloseListener;

    protected abstract
    @LayoutRes
    int getLayoutId();

    protected abstract void initAdapter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActionModeOpenCloseListener = (ActionModeOpenCloseListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ActionModeOpenCloseListener");
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
}
