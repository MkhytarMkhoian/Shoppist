package com.justplay1.shoppist.view.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.view.component.CustomProgressDialog;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;

/**
 * Created by Mkhytar on 03.06.2016.
 */
public abstract class BaseAddElementFragment extends BaseFragment
        implements View.OnLongClickListener, View.OnClickListener {

    public static final int SEARCH_REQUEST = 100;

    protected AddElementListener mListener;
    protected MaterialAutoCompleteTextView mNameEdit;
    protected FloatingActionButton mActionButton;
    protected CustomProgressDialog mProgressDialog;
    protected ImageButton mVoiceSearchBtn;

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
        mNameEdit.setTypeface(App.fontRobotoRegular);
        mNameEdit.setPrimaryColor(mPreferences.getColorPrimary());

        mVoiceSearchBtn = (ImageButton) view.findViewById(R.id.menu_voice_search);
        mVoiceSearchBtn.setOnClickListener(this);

        mActionButton = (FloatingActionButton) view.findViewById(R.id.done_button);
        mActionButton.setOnLongClickListener(this);
        mActionButton.setOnClickListener(this);
        mActionButton.setBackgroundTintList(ColorStateList.valueOf(mPreferences.getColorPrimary()));
    }

    private void doSearch(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST && resultCode == Activity.RESULT_OK) {

            String query = data.getStringExtra(SearchManager.QUERY);
            Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), R.string.recognition_not_present, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doSearch(requestCode, resultCode, data);
    }

    public interface AddElementListener {

        void setTitle(String title);

        void closeScreen();
    }
}
