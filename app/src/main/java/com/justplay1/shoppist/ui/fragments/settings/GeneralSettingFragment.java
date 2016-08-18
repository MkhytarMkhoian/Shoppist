package com.justplay1.shoppist.ui.fragments.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.ui.views.ColorCheckBoxPreference;
import com.justplay1.shoppist.ui.activities.SettingsActivity;
import com.justplay1.shoppist.ui.activities.CategoriesActivity;
import com.justplay1.shoppist.ui.activities.MainHelpActivity;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.activities.ShoppingListActivity;
import com.justplay1.shoppist.ui.activities.ShoppingListItemActivity;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.ColorThemeUpdater;
import com.justplay1.shoppist.ui.fragments.dialog.SelectThemeColorDialogFragment;
import com.justplay1.shoppist.ui.views.themedialog.ColorPickerSwatch;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class GeneralSettingFragment extends BaseSettingFragment implements ColorThemeUpdater {

    public static final String COLOR_THEME_BTN_ID = "color_theme";
    public static final String CONFIRM_TO_DELETE_BTN_ID = "confirm_to_delete";
    public static final String SELECT_ANIMATION_BTN_ID = "select_animation";
    public static final String CLOSE_MANUAL_SORT_MODE_WITH_BACK_ID = "close_manual_sort_mode_with_back_press_id";
    public static final String LONG_ITEM_CLICK_ACTION_ID = "long_item_click_action_id";

    protected Preference mColorThemeBtn;
    private Preference mLongItemClickActionBtn;

    protected CheckBoxPreference mConfirmToDeleteBtn;
    protected CheckBoxPreference mSelectAnimationBtn;
    protected CheckBoxPreference mCloseManualSortModeWithBackBtn;

    public static GeneralSettingFragment newInstance() {
        return new GeneralSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.general_setting;
    }

    protected void initializeFrame() {
        super.initializeFrame();
        updateGeneralCheckBox();

        mColorThemeBtn = (Preference) findPreference(COLOR_THEME_BTN_ID);
        mColorThemeBtn.setOnPreferenceClickListener(this);
        mLongItemClickActionBtn.setOnPreferenceClickListener(this);
        mConfirmToDeleteBtn.setChecked(ShoppistPreferences.isNeedShowConfirmDeleteDialog());
        mSelectAnimationBtn.setChecked(ShoppistPreferences.isSelectAnimationEnable());
        mCloseManualSortModeWithBackBtn.setChecked(ShoppistPreferences.isCloseManualSortModeWithBackButton());
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case COLOR_THEME_BTN_ID:
                FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
                SelectThemeColorDialogFragment colorDialog = SelectThemeColorDialogFragment.newInstance();
                colorDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int colorPrimary, int colorPrimaryDark) {
                        addToUpdate();
                        updateTheme();
                    }
                });
                colorDialog.show(fm, SelectThemeColorDialogFragment.class.getName());
                break;
            case SELECT_ANIMATION_BTN_ID:
                ShoppistPreferences.setSelectAnimation(((CheckBoxPreference) preference).isChecked());
                break;
            case CONFIRM_TO_DELETE_BTN_ID:
                ShoppistPreferences.setConfirmDeleteDialog(((CheckBoxPreference) preference).isChecked());
                break;
            case CLOSE_MANUAL_SORT_MODE_WITH_BACK_ID:
                ShoppistPreferences.setCloseManualSortModeWithBackButton(((CheckBoxPreference) preference).isChecked());
                break;
            case LONG_ITEM_CLICK_ACTION_ID:
                showLongItemClickActionDialog();
                break;
        }
        return true;
    }

    protected void updateGeneralCheckBox() {
        if (findPreference(CONFIRM_TO_DELETE_BTN_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(CONFIRM_TO_DELETE_BTN_ID));
            getPreferenceScreen().removePreference(findPreference(SELECT_ANIMATION_BTN_ID));
            getPreferenceScreen().removePreference(findPreference(CLOSE_MANUAL_SORT_MODE_WITH_BACK_ID));
        }

        if (mConfirmToDeleteBtn == null) {
            mConfirmToDeleteBtn = new ColorCheckBoxPreference(getActivity());
            mConfirmToDeleteBtn.setKey(CONFIRM_TO_DELETE_BTN_ID);
            mConfirmToDeleteBtn.setTitle(R.string.confirm_to_delete);
            mConfirmToDeleteBtn.setSummary(R.string.confirm_to_delete_summary);
        }
        getPreferenceScreen().removePreference(mConfirmToDeleteBtn);
        getPreferenceScreen().addPreference(mConfirmToDeleteBtn);

        if (mSelectAnimationBtn == null) {
            mSelectAnimationBtn = new ColorCheckBoxPreference(getActivity());
            mSelectAnimationBtn.setKey(SELECT_ANIMATION_BTN_ID);
            mSelectAnimationBtn.setTitle(getString(R.string.select_with_animation));
        }
        getPreferenceScreen().removePreference(mSelectAnimationBtn);
        getPreferenceScreen().addPreference(mSelectAnimationBtn);

        if (mCloseManualSortModeWithBackBtn == null) {
            mCloseManualSortModeWithBackBtn = new ColorCheckBoxPreference(getActivity());
            mCloseManualSortModeWithBackBtn.setKey(CLOSE_MANUAL_SORT_MODE_WITH_BACK_ID);
            mCloseManualSortModeWithBackBtn.setTitle(R.string.closing_manual_sort_mode);
            mCloseManualSortModeWithBackBtn.setSummary(R.string.close_manual_sort_mode_with_back_btn_symmary);
        }
        getPreferenceScreen().removePreference(mCloseManualSortModeWithBackBtn);
        getPreferenceScreen().addPreference(mCloseManualSortModeWithBackBtn);

        mConfirmToDeleteBtn.setOnPreferenceClickListener(this);
        mSelectAnimationBtn.setOnPreferenceClickListener(this);
        mCloseManualSortModeWithBackBtn.setOnPreferenceClickListener(this);

        if (findPreference(LONG_ITEM_CLICK_ACTION_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(LONG_ITEM_CLICK_ACTION_ID));
        }

        if (mLongItemClickActionBtn == null) {
            mLongItemClickActionBtn = new Preference(getActivity());
            mLongItemClickActionBtn.setKey(LONG_ITEM_CLICK_ACTION_ID);
            mLongItemClickActionBtn.setTitle(R.string.long_item_click_action);
            switch (ShoppistPreferences.getLongItemClickAction()) {
                case 0:
                    mLongItemClickActionBtn.setSummary(R.string.select_item);
                    break;
                case 1:
                    mLongItemClickActionBtn.setSummary(R.string.edit_item);
                    break;
            }
        }
        getPreferenceScreen().removePreference(mLongItemClickActionBtn);
        getPreferenceScreen().addPreference(mLongItemClickActionBtn);
        mLongItemClickActionBtn.setOnPreferenceClickListener(this);
    }

    private void showLongItemClickActionDialog() {
        String[] swipeActions = {getString(R.string.select_item), getString(R.string.edit_item)};

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.dialog_list_item, swipeActions);

        final int[] selectedItem = {ShoppistPreferences.getLongItemClickAction()};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setSingleChoiceItems(adapter, selectedItem[0], new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItem[0] = which;
            }
        });

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ShoppistPreferences.setLongItemClickAction(selectedItem[0]);
                        mLongItemClickActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                        break;
                    case Dialog.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        builder.setTitle(R.string.long_item_click_action);
        builder.setPositiveButton(R.string.choose, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTheme();
    }

    @Override
    public void updateTheme() {
        if (ShoppistPreferences.isNeedUpdateTheme(GeneralSettingFragment.class.getName())) {
            ShoppistPreferences.deleteUpdateItem(GeneralSettingFragment.class.getName());

            ((SettingsActivity) getActivity()).refreshToolbarColor();
            setThemeColor();
        }
    }

    protected void setThemeColor() {
        ActivityUtils.setStatusBarColor(getActivity());
        updateGeneralCheckBox();
    }

    private void addToUpdate() {
        ShoppistPreferences.addUpdateItem(GeneralSettingFragment.class.getName());
        ShoppistPreferences.addUpdateItem(MainSettingFragment.class.getName());
        ShoppistPreferences.addUpdateItem(SettingsActivity.class.getName());
        ShoppistPreferences.addUpdateItem(ShoppingListActivity.class.getName());
        ShoppistPreferences.addUpdateItem(ShoppingListItemActivity.class.getName());
        ShoppistPreferences.addUpdateItem(CategoriesActivity.class.getName());
        ShoppistPreferences.addUpdateItem(MainHelpActivity.class.getName());
    }
}
