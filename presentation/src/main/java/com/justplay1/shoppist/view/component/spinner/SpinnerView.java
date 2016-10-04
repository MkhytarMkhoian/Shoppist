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

package com.justplay1.shoppist.view.component.spinner;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class SpinnerView<T extends BaseViewModel> extends FrameLayout {

    @Bind(R.id.spinner)
    protected Spinner spinner;
    @Bind(R.id.add_button)
    protected ImageButton addBtn;
    @Bind(R.id.edit_button)
    protected ImageButton editBtn;

    protected SpinnerAdapter<T> adapter;

    public SpinnerView(Context context) {
        super(context);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.view_custom_spinner, this);
        ButterKnife.bind(this);
    }

    public void setData(List<T> data) {
        adapter = new SpinnerAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        spinner.setAdapter(adapter);
    }

    public void setOnAddBtnClickListener(View.OnClickListener listener) {
        addBtn.setOnClickListener(listener);
    }

    public void setOnEditBtnClickListener(View.OnClickListener listener) {
        editBtn.setOnClickListener(listener);
    }

    public void setAddBtnVisibility(int visibility) {
        addBtn.setVisibility(visibility);
    }

    public void setEditBtnVisibility(int visibility) {
        editBtn.setVisibility(visibility);
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener listener) {
        spinner.setOnItemSelectedListener(listener);
    }

    public void setSelection(int position) {
        spinner.setSelection(position);
    }

    public T getSelectedItem() {
        return (T) spinner.getSelectedItem();
    }

    public void selectItem(String currentId) {
        int spinnerCount = spinner.getCount();
        for (int i = 0; i < spinnerCount; i++) {
            T value = (T) spinner.getItemAtPosition(i);
            String id = value.getId();
            if (id.equals(currentId)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    protected static class SpinnerAdapter<T extends BaseViewModel> extends ArrayAdapter<T> {

        private LayoutInflater mInflater;

        SpinnerAdapter(Context context, int textViewResourceId, List<T> objects) {
            super(context, textViewResourceId, objects);
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent, true);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getView(position, convertView, parent, false);
        }

        private View getView(int position, View convertView, ViewGroup parent, boolean isDropDownView) {
            T item = getItem(position);

            if (convertView == null) {
                if (isDropDownView) {
                    convertView = mInflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                } else {
                    convertView = mInflater.inflate(android.R.layout.simple_spinner_item, parent, false);
                }
            }
            TextView text = (TextView) convertView.findViewById(android.R.id.text1);
            text.setText(item.getName());
            return convertView;
        }
    }
}
