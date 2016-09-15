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

package com.justplay1.shoppist.view.fragments.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.di.components.CurrencyComponent;
import com.justplay1.shoppist.di.components.DaggerCurrencyComponent;
import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.presenter.ListsSettingPresenter;
import com.justplay1.shoppist.view.ListsSettingView;
import com.justplay1.shoppist.view.component.ColorCheckBoxPreference;
import com.justplay1.shoppist.view.fragments.dialog.AddCurrencyDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectCurrencyDialogFragment;

import javax.inject.Inject;

/**
 * Created by Mkhytar Mkhoian.
 */
public class ListsSettingFragment extends BaseSettingFragment implements ListsSettingView {

    public static final String DEFAULT_CURRENCY_ID = "default_currency";
    public static final String CALCULATE_PRICE_ID = "calculate_price";
    public static final String SHOPPING_LIST_LEFT_SWIPE_ACTION_ID = "shopping_list_left_swipe_action_id";
    public static final String SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID = "shopping_list_right_swipe_action_id";
    public static final String ADD_BUTTON_CLICK_ACTION_ID = "add_button_click_action";

    private CheckBoxPreference mCalculatePriceBtn;

    private Preference mShoppingListLeftSwipeActionBtn;
    private Preference mShoppingListRightSwipeActionBtn;
    private Preference mDefaultCurrencyBtn;
    private Preference mAddButtonClickAction;

    @Inject
    ListsSettingPresenter mPresenter;
    private CurrencyComponent mComponent;

    public static ListsSettingFragment newInstance() {
        return new ListsSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.shopping_lists_setting;
    }

    protected void initFrame() {
        super.initFrame();
        updateShoppingListCheckBox();
        mDefaultCurrencyBtn.setOnPreferenceClickListener(this);
        mShoppingListLeftSwipeActionBtn.setOnPreferenceClickListener(this);
        mShoppingListRightSwipeActionBtn.setOnPreferenceClickListener(this);
        mAddButtonClickAction.setOnPreferenceClickListener(this);

        mCalculatePriceBtn.setChecked(mPreferences.isCalculatePrice());
    }

    @Override
    protected void injectDependencies() {
        super.injectDependencies();
        mComponent = DaggerCurrencyComponent.builder()
                .appComponent(App.get().getAppComponent())
                .activityModule(new ActivityModule(getActivity()))
                .currencyModule(new CurrencyModule())
                .build();
        mComponent.inject(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    public void showDefaultCurrency(CurrencyViewModel data) {
        mDefaultCurrencyBtn.setSummary(data.getName());
    }

    public void onClickPositiveBtn(String id, int[] selectedItem, ArrayAdapter<String> adapter) {
        switch (id) {
            case SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                mPreferences.setLeftShoppingListItemSwipeAction(selectedItem[0]);
                mShoppingListLeftSwipeActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                break;
            case SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                mPreferences.setRightShoppingListItemSwipeAction(selectedItem[0]);
                mShoppingListRightSwipeActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                break;
            case ADD_BUTTON_CLICK_ACTION_ID:
                mPreferences.setAddButtonClickAction(selectedItem[0]);
                mAddButtonClickAction.setSummary(adapter.getItem(selectedItem[0]));
                break;
        }
    }

    @Override
    public boolean onPreferenceClick(android.preference.Preference preference) {
        switch (preference.getKey()) {
            case DEFAULT_CURRENCY_ID:
                showSelectCurrencyDialog();
                break;
            case CALCULATE_PRICE_ID:
                mPreferences.setCalculatePrice(((CheckBoxPreference) preference).isChecked());
                break;
            case SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                showChooseActionDialog(getActivity(), SHOPPING_LIST_LEFT_SWIPE_ACTION_ID);
                break;
            case SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                showChooseActionDialog(getActivity(), SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID);
                break;
            case ADD_BUTTON_CLICK_ACTION_ID:
                showChooseActionDialog(getActivity(), ADD_BUTTON_CLICK_ACTION_ID);
                break;
        }
        return true;
    }

    protected void updateShoppingListCheckBox() {
        if (findPreference(DEFAULT_CURRENCY_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(DEFAULT_CURRENCY_ID));
            getPreferenceScreen().removePreference(findPreference(CALCULATE_PRICE_ID));
        }

        if (mCalculatePriceBtn == null) {
            mCalculatePriceBtn = new ColorCheckBoxPreference(getActivity(), mPreferences.getColorPrimary());
            mCalculatePriceBtn.setKey(CALCULATE_PRICE_ID);
            mCalculatePriceBtn.setTitle(getString(R.string.calculate_price));
            mCalculatePriceBtn.setSummary(getString(R.string.calculate_price_summary));
        }
        getPreferenceScreen().removePreference(mCalculatePriceBtn);
        getPreferenceScreen().addPreference(mCalculatePriceBtn);

        if (mDefaultCurrencyBtn == null) {
            mDefaultCurrencyBtn = new Preference(getActivity());
            mDefaultCurrencyBtn.setKey(DEFAULT_CURRENCY_ID);
            mDefaultCurrencyBtn.setTitle(getString(R.string.currency));
        }
        getPreferenceScreen().removePreference(mDefaultCurrencyBtn);
        getPreferenceScreen().addPreference(mDefaultCurrencyBtn);

        mCalculatePriceBtn.setOnPreferenceClickListener(this);

        if (mShoppingListLeftSwipeActionBtn == null) {
            mShoppingListLeftSwipeActionBtn = new Preference(getActivity());
            mShoppingListLeftSwipeActionBtn.setKey(SHOPPING_LIST_LEFT_SWIPE_ACTION_ID);
            mShoppingListLeftSwipeActionBtn.setTitle(R.string.left_swipe_action);
            switch (mPreferences.getLeftShoppingListItemSwipeAction()) {
                case 0:
                    mShoppingListLeftSwipeActionBtn.setSummary(getString(R.string.move_item_to_cart));
                    break;
                case 1:
                    mShoppingListLeftSwipeActionBtn.setSummary(getString(R.string.delete_item));
                    break;
                case 2:
                    mShoppingListLeftSwipeActionBtn.setSummary(getString(R.string.edit_item));
                    break;
            }
        }
        getPreferenceScreen().removePreference(mShoppingListLeftSwipeActionBtn);
        getPreferenceScreen().addPreference(mShoppingListLeftSwipeActionBtn);

        if (mShoppingListRightSwipeActionBtn == null) {
            mShoppingListRightSwipeActionBtn = new Preference(getActivity());
            mShoppingListRightSwipeActionBtn.setKey(SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID);
            mShoppingListRightSwipeActionBtn.setTitle(R.string.right_swipe_action);
            switch (mPreferences.getRightShoppingListItemSwipeAction()) {
                case 0:
                    mShoppingListRightSwipeActionBtn.setSummary(getString(R.string.move_item_to_cart));
                    break;
                case 1:
                    mShoppingListRightSwipeActionBtn.setSummary(getString(R.string.delete_item));
                    break;
                case 2:
                    mShoppingListRightSwipeActionBtn.setSummary(getString(R.string.edit_item));
                    break;
            }
        }
        getPreferenceScreen().removePreference(mShoppingListRightSwipeActionBtn);
        getPreferenceScreen().addPreference(mShoppingListRightSwipeActionBtn);

        if (mAddButtonClickAction == null) {
            mAddButtonClickAction = new Preference(getActivity());
            mAddButtonClickAction.setKey(ADD_BUTTON_CLICK_ACTION_ID);
            mAddButtonClickAction.setTitle(R.string.add_button_click_action);
            switch (mPreferences.getAddButtonClickAction()) {
                case 0:
                    mAddButtonClickAction.setSummary(R.string.standart_mode);
                    break;
                case 1:
                    mAddButtonClickAction.setSummary(R.string.quick_mode);
                    break;
            }
        }
        getPreferenceScreen().removePreference(mAddButtonClickAction);
        getPreferenceScreen().addPreference(mAddButtonClickAction);
    }

    private void showSelectCurrencyDialog() {
        FragmentManager fm = ((AppCompatActivity) getActivity()).getSupportFragmentManager();
        SelectCurrencyDialogFragment dialog = SelectCurrencyDialogFragment.newInstance(null);
        dialog.setCompleteListener((item, isUpdate) -> {
            mDefaultCurrencyBtn.setSummary(item.getName());
            mPreferences.setDefaultCurrency(item.getId());
        });
        dialog.show(fm, AddCurrencyDialogFragment.class.getName());
    }

    private void showChooseActionDialog(Context context, final String id) {
        String[] actions = {context.getString(R.string.move_item_to_cart),
                context.getString(R.string.delete_item), context.getString(R.string.edit_item)};

        final int[] selectedItem = {-1};
        switch (id) {
            case ListsSettingFragment.SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                selectedItem[0] = mPreferences.getLeftShoppingListItemSwipeAction();
                break;
            case ListsSettingFragment.SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                selectedItem[0] = mPreferences.getRightShoppingListItemSwipeAction();
                break;
            case ListsSettingFragment.ADD_BUTTON_CLICK_ACTION_ID:
                selectedItem[0] = mPreferences.getAddButtonClickAction();
                actions = new String[]{context.getString(R.string.standart_mode), context.getString(R.string.quick_mode)};
                break;
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.dialog_list_item, actions);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setSingleChoiceItems(adapter, selectedItem[0], (dialog, which) -> {
            selectedItem[0] = which;
        });

        DialogInterface.OnClickListener listener = (dialog, which) -> {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    onClickPositiveBtn(id, selectedItem, adapter);
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };

        builder.setTitle(R.string.choose_actions);
        builder.setPositiveButton(R.string.choose, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(mPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(mPreferences.getColorPrimary());
    }
}
