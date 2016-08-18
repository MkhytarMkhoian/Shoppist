package com.justplay1.shoppist.ui.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ShoppingList;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.DialogUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.ColorPickerDialog;

/**
 * Created by Mkhitar on 21.11.2014.
 */
public class AddListActivity extends BaseAddActivity<ShoppingList> {

    private Spinner mPriorityList;
    private ImageView mColorBtn;
    private int mColor = Color.DKGRAY;

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_add_list;
    }

    @Override
    protected void getDataFromIntent() {
        if (getIntent() != null) {
            Bundle data = getIntent().getBundleExtra(ActivityUtils.DATA);
            if (data != null) {
                mItem = data.getParcelable(ShoppingList.class.getName());
            }
        }
    }

    protected void initializeFrame() {
        super.initializeFrame();

        TextView colorLabel = (TextView) findViewById(R.id.color_label);
        colorLabel.setTypeface(App.fontRobotoRegular);
        colorLabel.setTextColor(ShoppistPreferences.getColorPrimary());

        TextView priorityLabel = (TextView) findViewById(R.id.priority_label);
        priorityLabel.setTypeface(App.fontRobotoRegular);
        priorityLabel.setTextColor(ShoppistPreferences.getColorPrimary());

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

        String[] data = getResources().getStringArray(R.array.priority);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPriorityList = (Spinner) findViewById(R.id.priority);
        mPriorityList.setAdapter(adapter);

        mColorBtn = (ImageView) findViewById(R.id.color_button);
        mColorBtn.setOnClickListener(this);

        if (mItem != null) {
            mNameEdit.setText(mItem.getName());
            mNameEdit.setSelection(mNameEdit.getText().length());
            mPriorityList.setSelection(mItem.getPriority().ordinal());
            mColor = mItem.getColor();
        } else {
            mPriorityList.setSelection(0);
            mColor = ShoppistUtils.getRandomColor(this);
        }
        mColorBtn.setBackgroundColor(mColor);
    }

    protected void clearUI() {
        mNameEdit.setText("");
        mPriorityList.setSelection(0);
        mItem = null;
    }

    protected void addItem(final boolean isLongClick) {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setColor(mColor);
        shoppingList.setDirty(true);

        boolean flag = true;

        String name = ShoppistUtils.filterSpace(mNameEdit.getText().toString());
        if (name.isEmpty()) {
            showError();
            flag = false;
        } else if (name.length() > mNameEdit.getMaxCharacters()) {
            flag = false;
        }
        shoppingList.setName(name);

        if (mItem == null){
            shoppingList.setTimeCreated(System.currentTimeMillis());
            shoppingList.setId(ShoppistUtils.generateId(name));
        } else {
            shoppingList.setId(mItem.getId());
            shoppingList.setTimestamp(mItem.getTimestamp());
            shoppingList.setTimeCreated(mItem.getTimeCreated());
        }

        switch (mPriorityList.getSelectedItemPosition()) {
            case 0:
                shoppingList.setPriority(Priority.NO_PRIORITY);
                break;
            case 1:
                shoppingList.setPriority(Priority.LOW);
                break;
            case 2:
                shoppingList.setPriority(Priority.MEDIUM);
                break;
            case 3:
                shoppingList.setPriority(Priority.HIGH);
                break;
        }

        if (flag) {
            if (mItem != null) {
                shoppingList.setChecked(mItem.isChecked());
                shoppingList.setServerId(mItem.getServerId());
                updateItem(shoppingList, isLongClick);
            } else {
                addItem(shoppingList, isLongClick);
            }
        }

    }

    protected void addItem(final ShoppingList list, final boolean isLongClick) {
        App.get().getModelHolder().getShoppingListsManager().add(list, new ExecutorListener<ShoppingList>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(ShoppingList result) {
                mProgressDialog.dismiss();
                AddListActivity.this.complete(isLongClick, R.string.new_list_added);
            }

            @Override
            public void error(Exception e) {
                mProgressDialog.dismiss();
                DialogUtils.showErrorDialog(AddListActivity.this,
                        ShoppistUtils.getParseMessageFromException(AddListActivity.this, e.getMessage()));
            }
        });
    }

    protected void updateItem(final ShoppingList newItem, final boolean isLongClick) {
        App.get().getModelHolder().getShoppingListsManager().update(newItem, new ExecutorListener<ShoppingList>() {
            @Override
            public void start() {
                mProgressDialog.show();
            }

            @Override
            public void complete(ShoppingList result) {
                mOldId = result.getId();
                mItem = newItem;

                mProgressDialog.dismiss();
                AddListActivity.this.complete(isLongClick, R.string.list_updated);
            }

            @Override
            public void error(Exception e) {
                mProgressDialog.dismiss();
                DialogUtils.showErrorDialog(AddListActivity.this,
                        ShoppistUtils.getParseMessageFromException(AddListActivity.this, e.getMessage()));
            }
        });
    }

    protected void showError() {
        mNameEdit.setError(getString(R.string.list_name_is_required));
    }

    @Override
    protected int getToolbarTitleResId() {
        return R.string.title_activity_add_list;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done_button:
                addItem(false);
                break;
            case R.id.menu_voice_search:
                startVoiceRecognition();
                break;
            case R.id.color_button:
                showColorDialog();
                break;
        }
    }
}
