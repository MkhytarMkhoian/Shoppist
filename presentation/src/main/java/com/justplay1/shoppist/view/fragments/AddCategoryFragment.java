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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.presenter.AddCategoryPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddCategoryView;
import com.justplay1.shoppist.view.component.ColorPickerDialog;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddCategoryFragment extends BaseAddElementFragment
        implements AddCategoryView {

    @Inject
    AddCategoryPresenter presenter;

    @Bind(R.id.color_button)
    ImageView colorBtn;

    public static AddCategoryFragment newInstance(CategoryViewModel category) {
        Bundle args = new Bundle();
        args.putParcelable(CategoryViewModel.class.getName(), category);

        AddCategoryFragment fragment = new AddCategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        presenter.attachView(this);
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
        getInjector(CategoryComponent.class).inject(this);
    }

    @Override
    protected boolean isItemEdit() {
        return presenter.isItemEdit();
    }

    @Override
    protected void init(View view) {
        super.init(view);
        TextView colorLabel = (TextView) view.findViewById(R.id.color_label);
        colorLabel.setTextColor(preferences.getColorPrimary());

        nameEdit.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.selectName(ShoppistUtils.filterSpace(nameEdit.getText().toString()));
            }
        });
        colorBtn.setOnClickListener(this);
    }

    @Override
    public void setName(String name) {
        nameEdit.setText(name);
        nameEdit.setSelection(nameEdit.getText().length());
    }

    @Override
    public void setColorToButton(int color) {
        colorBtn.setBackgroundColor(color);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_category;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color_button:
                presenter.onColorButtonClick();
                break;
            case R.id.done_button:
                presenter.onDoneButtonClick();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        presenter.onDoneButtonLongClick();
        return true;
    }

    @Override
    public void closeScreen() {
        listener.closeScreen();
    }

    @Override
    public void showNameIsRequiredError() {
        nameEdit.setError(getString(R.string.category_name_is_required));
    }

    @Override
    public void showKeyboard() {
        ShoppistUtils.showKeyboard(getContext(), nameEdit);
    }

    @Override
    public void showSelectColorDialog(int color) {
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(), preferences.getColorPrimary());
        colorPickerDialog.setColor(color);
        colorPickerDialog.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.positive_button:
                    presenter.selectColor(colorPickerDialog.getColor());
                    colorBtn.setBackgroundColor(colorPickerDialog.getColor());
                    colorPickerDialog.dismiss();
                    break;
                case R.id.negative_button:
                    colorPickerDialog.dismiss();
                    break;
            }
        });
        colorPickerDialog.show();
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void hideLoading() {
        progressDialog.dismiss();
    }

    @Override
    public void setDefaultToolbarTitle() {
        listener.setTitle(getString(R.string.title_activity_add_category));
    }

    @Override
    public void showNewElementAddedMessage() {
        showToastMessage(getString(R.string.new_category_added));
    }

    @Override
    public void showElementUpdatedMessage() {
        showToastMessage(getString(R.string.category_is_updated));
    }

    @Override
    public void setToolbarTitle(String title) {
        listener.setTitle(title);
    }
}
