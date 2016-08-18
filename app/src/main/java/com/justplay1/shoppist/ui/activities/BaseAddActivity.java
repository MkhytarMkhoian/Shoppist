package com.justplay1.shoppist.ui.activities;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.ShoppistUtils;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

/**
 * Created by Mkhitar on 15.05.2015.
 */
public abstract class BaseAddActivity<T extends BaseModel> extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    public static final int SEARCH_REQUEST = 100;

    protected MaterialAutoCompleteTextView mNameEdit;
    protected FloatingActionButton mDoneBtn;
    protected Toolbar mToolbar;
    protected CustomProgressDialog mProgressDialog;
    protected T mItem;
    protected ImageButton mVoiceSearchBtn;

    protected String mOldId;

    protected abstract void addItem(final boolean isLongClick);

    protected abstract void clearUI();

    protected abstract void addItem(T item, final boolean isLongClick);

    protected abstract void updateItem(T newItem, final boolean isLongClick);

    protected abstract void showError();

    protected abstract int getToolbarTitleResId();

    protected abstract void getDataFromIntent();

    protected abstract int getContentViewResId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());

        ActivityUtils.setStatusBarColor(this);
        getDataFromIntent();

        if (mItem != null){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        } else {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        initializeToolbar();
        initializeFrame();
    }

    protected void initializeToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mItem != null) {
            mToolbar.setTitle(mItem.getName());
        } else {
            mToolbar.setTitle(getToolbarTitleResId());
        }
        mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
        mToolbar.setNavigationIcon(R.drawable.ic_back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithResult();
            }
        });
        ViewCompat.setElevation(mToolbar, getResources().getDimensionPixelSize(R.dimen.toolbar_elevation));
    }

    protected void initializeFrame() {
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.please_wait));

        mNameEdit = (MaterialAutoCompleteTextView) findViewById(R.id.name_edit);
        mNameEdit.setFloatingLabelTextSize(getResources().getDimensionPixelSize(R.dimen.edit_label_text_size));
        mNameEdit.setTypeface(App.fontRobotoRegular);
        mNameEdit.setPrimaryColor(ShoppistPreferences.getColorPrimary());

        mVoiceSearchBtn = (ImageButton) findViewById(R.id.menu_voice_search);
        mVoiceSearchBtn.setOnClickListener(this);

        mDoneBtn = (FloatingActionButton) findViewById(R.id.done_button);
        mDoneBtn.setOnLongClickListener(this);
        mDoneBtn.setOnClickListener(this);
        mDoneBtn.setBackgroundTintList(ColorStateList.valueOf(ShoppistPreferences.getColorPrimary()));
    }

    protected void complete(boolean isLongClick, int messageResId) {
        if (!isLongClick) {
            finishWithResult();
        } else {
            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
            clearUI();
            mToolbar.setTitle(getToolbarTitleResId());
            ShoppistUtils.showKeyboard(this, mNameEdit);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doSearch(requestCode, resultCode, data);
    }

    private void doSearch(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST && resultCode == RESULT_OK) {

            String query = data.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            mNameEdit.setText(query);
            mNameEdit.setSelection(query.length());
        }
    }

    protected void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition");

        try {
            startActivityForResult(intent, SEARCH_REQUEST);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), R.string.recognition_not_present, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        addItem(true);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShoppistUtils.setKeepScreenOn(getWindow(), ShoppistPreferences.isLockScreen());
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
    }

    protected void finishWithResult() {
        Intent data = new Intent();
        data.putExtra(ActivityUtils.NEW_DATA, true);

        if (mOldId != null && mItem != null) {
            data.putExtra(ActivityUtils.OLD_ID, mOldId);
            data.putExtra(ActivityUtils.NEW_ID, mItem.getId());
        }
        ActivityUtils.finishActivityWithResult(this, RESULT_OK, data);
    }
}
