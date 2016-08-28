package com.justplay1.shoppist.view.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CategoryComponent;
import com.justplay1.shoppist.di.components.DaggerCategoryComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.presenter.AddCategoryPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddCategoryView;
import com.justplay1.shoppist.view.component.ColorPickerDialog;

import javax.inject.Inject;

/**
 * Created by Mkhitar on 21.11.2014.
 */
public class AddCategoryFragment extends BaseAddElementFragment
        implements AddCategoryView {

    @Inject
    AddCategoryPresenter mPresenter;

    private CategoryComponent mComponent;
    private ImageView mColorBtn;

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
        mPresenter.onCreate(getArguments(), savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mPresenter.init();
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
        super.injectDependencies();
        mComponent = DaggerCategoryComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .categoryModule(new CategoryModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected boolean isItemEdit() {
        return mPresenter.isItemEdit();
    }

    @Override
    protected void init(View view) {
        super.init(view);

        TextView colorLabel = (TextView) view.findViewById(R.id.color_label);
        colorLabel.setTypeface(App.fontRobotoRegular);
        colorLabel.setTextColor(mPreferences.getColorPrimary());

        mNameEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return false;
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                ShoppistUtils.hideKeyboard(getContext(), v);
                return true;
            }
            return false;
        });

        mColorBtn = (ImageView) view.findViewById(R.id.color_button);
        mColorBtn.setOnClickListener(this);
    }

    @Override
    public void setName(String name) {
        mNameEdit.setText(name);
        mNameEdit.setSelection(mNameEdit.getText().length());
    }

    @Override
    public void setColorToButton(int color) {
        mColorBtn.setBackgroundColor(color);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_category;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color_button:
                mPresenter.onColorButtonClick();
                break;
            case R.id.done_button:
                mPresenter.onDoneButtonClick(ShoppistUtils.filterSpace(mNameEdit.getText().toString()));
                break;
            case R.id.menu_voice_search:
                mPresenter.startVoiceRecognition();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        mPresenter.onDoneButtonLongClick(ShoppistUtils.filterSpace(mNameEdit.getText().toString()));
        return true;
    }

    @Override
    public void closeScreen() {
        mListener.closeScreen();
    }

    @Override
    public void showNameIsRequiredError() {
        mNameEdit.setError(getString(R.string.category_name_is_required));
    }

    @Override
    public void showKeyboard() {
        ShoppistUtils.showKeyboard(getContext(), mNameEdit);
    }

    @Override
    public void showSelectColorDialog(int color) {
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog(getContext(), mPreferences.getColorPrimary());
        colorPickerDialog.setColor(color);
        colorPickerDialog.setOnClickListener(view -> {
            switch (view.getId()) {
                case R.id.positive_button:
                    mPresenter.onColorSelected(colorPickerDialog.getColor());
                    mColorBtn.setBackgroundColor(colorPickerDialog.getColor());
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
        mProgressDialog.show();
    }

    @Override
    public void hideLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        showToastMessage(message);
    }

    @Override
    public void setDefaultToolbarTitle() {
        mListener.setTitle(getString(R.string.title_activity_add_category));
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
        mListener.setTitle(title);
    }
}
