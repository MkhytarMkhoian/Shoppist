package com.justplay1.shoppist.ui.fragments.settings;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.jenzz.materialpreference.CheckBoxPreference;
import com.jenzz.materialpreference.Preference;
import com.justplay1.shoppist.R;
import com.justplay1.shoppist.communication.ExecutorListener;
import com.justplay1.shoppist.App;
import com.justplay1.shoppist.models.Currency;
import com.justplay1.shoppist.settings.SwipeActionDialogCallback;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.fragments.dialog.BaseDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.CurrencyEditorDialogFragment;
import com.justplay1.shoppist.ui.fragments.dialog.SelectCurrencyDialogFragment;
import com.justplay1.shoppist.ui.views.ColorCheckBoxPreference;
import com.justplay1.shoppist.utils.SettingUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by Mkhytar on 19.12.2015.
 */
public class ShoppingListsSettingFragment extends BaseSettingFragment implements SwipeActionDialogCallback {

    public static final String DEFAULT_CURRENCY_ID = "default_currency";
    public static final String DISCOLOR_PURCHASED_GOODS_ID = "discolor_purchased_goods";
    public static final String CALCULATE_PRICE_ID = "calculate_price";
    public static final String SHOW_GOODS_HEADER = "show_goods_header";
    public static final String SHOPPING_LIST_LEFT_SWIPE_ACTION_ID = "shopping_list_left_swipe_action_id";
    public static final String SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID = "shopping_list_right_swipe_action_id";
    public static final String ADD_BUTTON_CLICK_ACTION_ID = "add_button_click_action";

    private CheckBoxPreference mDiscolorPurchasedGoodsBtn;
    private CheckBoxPreference mShowGoodsHeaderBtn;
    private CheckBoxPreference mCalculatePriceBtn;

    private Preference mShoppingListLeftSwipeActionBtn;
    private Preference mShoppingListRightSwipeActionBtn;
    private Preference mDefaultCurrencyBtn;
    private Preference mAddButtonClickAction;

    public static ShoppingListsSettingFragment newInstance() {
        return new ShoppingListsSettingFragment();
    }

    @Override
    protected int getPreferencesResId() {
        return R.xml.shopping_lists_setting;
    }

    protected void initializeFrame() {
        super.initializeFrame();
        updateShoppingListCheckBox();
        mDefaultCurrencyBtn.setOnPreferenceClickListener(this);
        mShoppingListLeftSwipeActionBtn.setOnPreferenceClickListener(this);
        mShoppingListRightSwipeActionBtn.setOnPreferenceClickListener(this);
        mAddButtonClickAction.setOnPreferenceClickListener(this);

        mCalculatePriceBtn.setChecked(ShoppistPreferences.isCalculatePrice());
        mShowGoodsHeaderBtn.setChecked(ShoppistPreferences.isShowGoodsHeader());
        mDiscolorPurchasedGoodsBtn.setChecked(ShoppistPreferences.isDiscolorPurchasedGoods());

        if (!ShoppistPreferences.getDefaultCurrency().isEmpty()) {
            App.get().getCurrenciesManager().getCurrency(Collections.singletonList(ShoppistPreferences.getDefaultCurrency() + ""),
                    new ExecutorListener<Collection<Currency>>() {
                        @Override
                        public void start() {
                        }

                        @Override
                        public void complete(Collection<Currency> result) {
                            if (result.size() == 0) return;
                            if (result.size() == 1) {
                                Currency currency = result.iterator().next();
                                mDefaultCurrencyBtn.setSummary(currency.getName());
                            }
                        }

                        @Override
                        public void error(Exception e) {
                        }
                    });
        }
    }

    @Override
    public void onClickPositiveBtn(String id, int[] selectedItem, ArrayAdapter<String> adapter) {
        switch (id) {
            case SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                ShoppistPreferences.setLeftShoppingListItemSwipeAction(selectedItem[0]);
                mShoppingListLeftSwipeActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                break;
            case SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                ShoppistPreferences.setRightShoppingListItemSwipeAction(selectedItem[0]);
                mShoppingListRightSwipeActionBtn.setSummary(adapter.getItem(selectedItem[0]));
                break;
            case ADD_BUTTON_CLICK_ACTION_ID:
                ShoppistPreferences.setAddButtonClickAction(selectedItem[0]);
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
            case DISCOLOR_PURCHASED_GOODS_ID:
                ShoppistPreferences.setDiscolorPurchasedGoods(((CheckBoxPreference) preference).isChecked());
                break;
            case CALCULATE_PRICE_ID:
                ShoppistPreferences.setCalculatePrice(((CheckBoxPreference) preference).isChecked());
                break;
            case SHOW_GOODS_HEADER:
                ShoppistPreferences.setShowGoodsHeader(((CheckBoxPreference) preference).isChecked());
                break;
            case SHOPPING_LIST_LEFT_SWIPE_ACTION_ID:
                SettingUtils.showChooseActionDialog(getActivity(), SHOPPING_LIST_LEFT_SWIPE_ACTION_ID, this);
                break;
            case SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID:
                SettingUtils.showChooseActionDialog(getActivity(), SHOPPING_LIST_RIGHT_SWIPE_ACTION_ID, this);
                break;
            case ADD_BUTTON_CLICK_ACTION_ID:
                SettingUtils.showChooseActionDialog(getActivity(), ADD_BUTTON_CLICK_ACTION_ID, this);
                break;
        }
        return true;
    }

    protected void updateShoppingListCheckBox() {
        if (findPreference(DEFAULT_CURRENCY_ID) != null) {
            getPreferenceScreen().removePreference(findPreference(DEFAULT_CURRENCY_ID));
            getPreferenceScreen().removePreference(findPreference(DISCOLOR_PURCHASED_GOODS_ID));
            getPreferenceScreen().removePreference(findPreference(CALCULATE_PRICE_ID));
        }

        if (mDiscolorPurchasedGoodsBtn == null) {
            mDiscolorPurchasedGoodsBtn = new ColorCheckBoxPreference(getActivity());
            mDiscolorPurchasedGoodsBtn.setKey(DISCOLOR_PURCHASED_GOODS_ID);
            mDiscolorPurchasedGoodsBtn.setTitle(getString(R.string.discolor_purchased_goods));
            mDiscolorPurchasedGoodsBtn.setSummary(getString(R.string.discolor_purchased_goods_summary));
        }
        getPreferenceScreen().removePreference(mDiscolorPurchasedGoodsBtn);
        getPreferenceScreen().addPreference(mDiscolorPurchasedGoodsBtn);

        if (mCalculatePriceBtn == null) {
            mCalculatePriceBtn = new ColorCheckBoxPreference(getActivity());
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

        if (mShowGoodsHeaderBtn == null) {
            mShowGoodsHeaderBtn = new ColorCheckBoxPreference(getActivity());
            mShowGoodsHeaderBtn.setKey(SHOW_GOODS_HEADER);
            mShowGoodsHeaderBtn.setTitle(R.string.show_goods_header);
        }
        getPreferenceScreen().removePreference(mShowGoodsHeaderBtn);
        getPreferenceScreen().addPreference(mShowGoodsHeaderBtn);

        mCalculatePriceBtn.setOnPreferenceClickListener(this);
        mDiscolorPurchasedGoodsBtn.setOnPreferenceClickListener(this);
        mShowGoodsHeaderBtn.setOnPreferenceClickListener(this);

        if (mShoppingListLeftSwipeActionBtn == null) {
            mShoppingListLeftSwipeActionBtn = new Preference(getActivity());
            mShoppingListLeftSwipeActionBtn.setKey(SHOPPING_LIST_LEFT_SWIPE_ACTION_ID);
            mShoppingListLeftSwipeActionBtn.setTitle(R.string.left_swipe_action);
            switch (ShoppistPreferences.getLeftShoppingListItemSwipeAction()) {
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
            switch (ShoppistPreferences.getRightShoppingListItemSwipeAction()) {
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
            switch (ShoppistPreferences.getAddButtonClickAction()) {
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
        dialog.setCompleteListener(new BaseDialogFragment.OnCompleteListener<Currency>() {
            @Override
            public void onComplete(Currency item, boolean isUpdate) {
                mDefaultCurrencyBtn.setSummary(item.getName());
                ShoppistPreferences.setDefaultCurrency(item.getId());
            }
        });
        dialog.show(fm, CurrencyEditorDialogFragment.class.getName());
    }
}
