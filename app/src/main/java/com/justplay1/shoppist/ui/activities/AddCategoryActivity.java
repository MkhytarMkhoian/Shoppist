package com.justplay1.shoppist.ui.activities;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Category;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.ColorPickerDialog;

/**
 * Created by Mkhitar on 21.11.2014.
 */
public class AddCategoryActivity extends BaseAddActivity<Category> {

    private ImageView mColorBtn;
    private int mColor = Color.DKGRAY;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_category;
    }

    @Override
    protected void getDataFromIntent() {
        if (getIntent() != null) {
            Bundle data = getIntent().getBundleExtra(ActivityUtils.DATA);
            if (data != null) {
                mItem = data.getParcelable(Category.class.getName());
            }
        }
    }

    protected void initializeFrame() {
        super.initializeFrame();

        TextView colorLabel = (TextView) findViewById(R.id.color_label);
        colorLabel.setTypeface(App.fontRobotoRegular);
        colorLabel.setTextColor(ShoppistPreferences.getColorPrimary());

        mNameEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return false;
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mNameEdit.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        mColorBtn = (ImageView) findViewById(R.id.color_button);
        mColorBtn.setOnClickListener(this);

        if (mItem != null) {
            mNameEdit.setText(mItem.getName());
            mNameEdit.setSelection(mNameEdit.getText().length());
            mColor = mItem.getColor();
        }
        mColorBtn.setBackgroundColor(mColor);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.color_button:
                showColorDialog();
                break;
            case R.id.done_button:
                addItem(false);
                break;
            case R.id.menu_voice_search:
                startVoiceRecognition();
                break;
        }
    }

    protected void clearUI() {
        mColor = Color.DKGRAY;
        mNameEdit.setText("");
        mColorBtn.setBackgroundColor(mColor);
        mItem = null;
    }

    @Override
    protected int getToolbarTitleResId() {
        return R.string.title_activity_add_category;
    }

    protected void addItem(boolean isLongClick) {
        String name = ShoppistUtils.filterSpace(mNameEdit.getText().toString());
        boolean flag = true;

        if (name.isEmpty()) {
            mNameEdit.setError(getString(R.string.category_name_is_required));
            flag = false;
        } else if (name.length() > mNameEdit.getMaxCharacters()) {
            flag = false;
        }

        Category category = new Category();
        category.setName(name);
        category.setColor(mColor);
        category.setDirty(true);

        if (flag) {
            if (mItem != null) {
                category.setId(mItem.getId());
                category.setServerId(mItem.getServerId());
                category.setCreateByUser(mItem.isCreateByUser());
                updateItem(category, isLongClick);
            } else {
                category.setCreateByUser(true);
                addItem(category, isLongClick);
            }
        }
    }

    protected void addItem(final Category category, final boolean isLongClick) {
        App.get().getCategoriesManager().add(category, new ExecutorListener<Category>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(Category result) {
                mProgressDialog.dismiss();
                AddCategoryActivity.this.complete(isLongClick, R.string.new_category_added);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof SQLiteConstraintException) {
                    DialogUtils.showErrorDialog(AddCategoryActivity.this,
                            ShoppistUtils.getParseMessageFromException(AddCategoryActivity.this, e.getMessage()));
                }
                mProgressDialog.dismiss();
            }
        });
    }

    protected void updateItem(final Category newCategory, final boolean isLongClick) {
        App.get().getCategoriesManager().update(newCategory, new ExecutorListener<Category>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(Category result) {
                mProgressDialog.dismiss();
                AddCategoryActivity.this.complete(isLongClick, R.string.category_is_updated);
            }

            @Override
            public void error(Exception e) {
                if (e instanceof SQLiteConstraintException) {
                    DialogUtils.showErrorDialog(AddCategoryActivity.this,
                            ShoppistUtils.getParseMessageFromException(AddCategoryActivity.this, e.getMessage()));
                }
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    protected void showError() {

    }

    private void showColorDialog() {
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this);
        colorPickerDialog.setColor(mColor);
        colorPickerDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.positive_button:
                        mColor = colorPickerDialog.getColor();
                        mColorBtn.setBackgroundColor(colorPickerDialog.getColor());
                        colorPickerDialog.dismiss();
                        break;
                    case R.id.negative_button:
                        colorPickerDialog.dismiss();
                        break;
                }
            }
        });
        colorPickerDialog.show();
    }
}
