package com.justplay1.shoppist.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.DaggerListsComponent;
import com.justplay1.shoppist.di.components.ListsComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListsModule;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.presenter.AddListPresenter;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.view.AddListView;
import com.justplay1.shoppist.view.component.ColorPickerDialog;

import javax.inject.Inject;

/**
 * Created by Mkhytar on 04.07.2016.
 */
public class AddListFragment extends BaseAddElementFragment implements AddListView {

    @Inject
    AddListPresenter mPresenter;

    private ListsComponent mComponent;
    private Spinner mPriorityList;
    private ImageView mColorBtn;

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
        mComponent = DaggerListsComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .listsModule(new ListsModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    protected void init(View view) {
        super.init(view);

        TextView colorLabel = (TextView) view.findViewById(R.id.color_label);
        colorLabel.setTextColor(mPreferences.getColorPrimary());

        TextView priorityLabel = (TextView) view.findViewById(R.id.priority_label);
        priorityLabel.setTextColor(mPreferences.getColorPrimary());

        mNameEdit.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() != KeyEvent.ACTION_DOWN)
                return false;
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                ShoppistUtils.hideKeyboard(getContext(), mNameEdit);
                return true;
            }
            return false;
        });

        String[] data = getResources().getStringArray(R.array.priority);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPriorityList = (Spinner) view.findViewById(R.id.priority);
        mPriorityList.setAdapter(adapter);
        mPriorityList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mPresenter.selectPriority(Priority.NO_PRIORITY);
                        break;
                    case 1:
                        mPresenter.selectPriority(Priority.LOW);
                        break;
                    case 2:
                        mPresenter.selectPriority(Priority.MEDIUM);
                        break;
                    case 3:
                        mPresenter.selectPriority(Priority.HIGH);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mColorBtn = (ImageView) view.findViewById(R.id.color_button);
        mColorBtn.setOnClickListener(this);
    }

    @Override
    public void setPriority(@Priority int priority) {
        mPriorityList.setSelection(priority);
    }

    @Override
    public void closeScreen() {
        mListener.closeScreen();
    }

    protected void showError() {
        mNameEdit.setError(getString(R.string.list_name_is_required));
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                mPresenter.onDoneButtonClick(ShoppistUtils.filterSpace(mNameEdit.getText().toString()));
                break;
            case R.id.menu_voice_search:
                mPresenter.startVoiceRecognition();
                break;
            case R.id.color_button:
                mPresenter.onColorButtonClick();
                break;
        }
    }

    @Override
    public void setRandomColor() {
        mPresenter.onColorSelected(getRandomColor());
    }

    @Override
    public void setName(String name) {
        mNameEdit.setText(name);
        mNameEdit.setSelection(mNameEdit.getText().length());
    }

    @Override
    public void showNameIsRequiredError() {
        mNameEdit.setError(getString(R.string.list_name_is_required));
    }

    @Override
    public void setDefaultToolbarTitle() {
        mListener.setTitle(getString(R.string.title_activity_add_list));
    }

    @Override
    public void setToolbarTitle(String title) {
        mListener.setTitle(title);
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
        ShoppistUtils.showKeyboard(getContext(), mNameEdit);
    }

    @Override
    public void setColorToButton(int color) {
        mColorBtn.setBackgroundColor(color);
    }

    @Override
    protected boolean isItemEdit() {
        return mPresenter.isItemEdit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_list;
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
    public boolean onLongClick(View v) {
        mPresenter.onDoneButtonLongClick(ShoppistUtils.filterSpace(mNameEdit.getText().toString()));
        return true;
    }
}
