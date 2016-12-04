/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.justplay1.shoppist.features.lists.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.shared.base.fragments.BaseAddElementFragment;
import com.justplay1.shoppist.shared.widget.ColorPickerDialog;
import com.justplay1.shoppist.utils.ShoppistUtils;

import com.justplay1.shoppist.utils.AbstractTextWatcher;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mkhytar Mkhoian.
 */
public class AddListFragment extends BaseAddElementFragment implements AddListView {

    @Inject
    AddListPresenter presenter;
    @Bind(R.id.priority)
    Spinner priorityList;
    @Bind(R.id.color_button)
    ImageView colorBtn;

    public static AddListFragment newInstance(ListViewModel list) {
        Bundle args = new Bundle();
        args.putParcelable(ListViewModel.class.getName(), list);

        AddListFragment fragment = new AddListFragment();
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
        getInjector(ListsComponent.class).inject(this);
    }

    @Override
    protected void init(View view) {
        super.init(view);

        TextView colorLabel = (TextView) view.findViewById(R.id.color_label);
        colorLabel.setTextColor(preferences.getColorPrimary());

        TextView priorityLabel = (TextView) view.findViewById(R.id.priority_label);
        priorityLabel.setTextColor(preferences.getColorPrimary());

        nameEdit.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                presenter.selectName(ShoppistUtils.filterSpace(s.toString()));
            }
        });

        String[] data = getResources().getStringArray(R.array.priority);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priorityList.setAdapter(adapter);
        priorityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        presenter.selectPriority(Priority.NO_PRIORITY);
                        break;
                    case 1:
                        presenter.selectPriority(Priority.LOW);
                        break;
                    case 2:
                        presenter.selectPriority(Priority.MEDIUM);
                        break;
                    case 3:
                        presenter.selectPriority(Priority.HIGH);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        colorBtn.setOnClickListener(this);
    }

    @Override
    public void setPriority(@Priority int priority) {
        priorityList.setSelection(priority);
    }

    @Override
    public void closeScreen() {
        listener.closeScreen();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                presenter.onDoneButtonClick();
                break;
            case R.id.color_button:
                presenter.onColorButtonClick();
                break;
        }
    }

    @Override
    public void setRandomColor() {
        int color = getRandomColor();
        setColorToButton(color);
        presenter.selectColor(color);
    }

    @Override
    public void setName(String name) {
        nameEdit.setText(name);
        nameEdit.setSelection(nameEdit.getText().length());
    }

    @Override
    public void showNameIsRequiredError() {
        nameEdit.setError(getString(R.string.list_name_is_required));
    }

    @Override
    public void setDefaultToolbarTitle() {
        listener.setTitle(getString(R.string.title_activity_add_list));
    }

    @Override
    public void setToolbarTitle(String title) {
        listener.setTitle(title);
    }

    @Override
    public void showNewElementAddedMessage() {
        showToastMessage(getString(R.string.new_list_added));
    }

    @Override
    public void showElementUpdatedMessage() {
        showToastMessage(getString(R.string.list_updated));
    }

    @Override
    public void showKeyboard() {
        ShoppistUtils.showKeyboard(getContext(), nameEdit);
    }

    @Override
    public void setColorToButton(int color) {
        colorBtn.setBackgroundColor(color);
    }

    @Override
    protected boolean isItemEdit() {
        return presenter.isItemEdit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_list;
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
    public boolean onLongClick(View v) {
        presenter.onDoneButtonLongClick();
        return true;
    }
}
