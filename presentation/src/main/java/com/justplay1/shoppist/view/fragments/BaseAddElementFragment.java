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

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class BaseAddElementFragment extends BaseFragment
        implements View.OnLongClickListener, View.OnClickListener {

    public static final int SEARCH_REQUEST = 100;

    protected AddElementListener mListener;
    protected MaterialAutoCompleteTextView mNameEdit;
    protected FloatingActionButton mActionButton;
    protected CustomProgressDialog mProgressDialog;

    protected abstract boolean isItemEdit();

    protected abstract
    @LayoutRes
    int getLayoutId();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (AddElementListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isItemEdit()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                    | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        init(view);
    }

    @Override
    protected void init(View view) {
        mProgressDialog = new CustomProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setCancelable(false);

        mNameEdit = (MaterialAutoCompleteTextView) view.findViewById(R.id.name_edit);
        mNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mNameEdit.setPrimaryColor(mPreferences.getColorPrimary());

        mActionButton = (FloatingActionButton) view.findViewById(R.id.done_button);
        mActionButton.setOnLongClickListener(this);
        mActionButton.setOnClickListener(this);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
    }

    public interface AddElementListener {

        void setTitle(String title);

        void closeScreen();
    }
}
