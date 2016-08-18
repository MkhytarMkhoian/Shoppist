package com.justplay1.shoppist.presenter;

import android.os.Bundle;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.interactor.DefaultSubscriber;
import com.justplay1.shoppist.interactor.category.GetCategories;
import com.justplay1.shoppist.interactor.currency.GetCurrencies;
import com.justplay1.shoppist.interactor.goods.GetGoods;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.interactor.units.GetUnits;
import com.justplay1.shoppist.models.BaseViewModel;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;
import com.justplay1.shoppist.models.mappers.CategoryModelDataMapper;
import com.justplay1.shoppist.models.mappers.CurrencyModelDataMapper;
import com.justplay1.shoppist.models.mappers.GoodsModelDataMapper;
import com.justplay1.shoppist.models.mappers.ListItemsModelDataMapper;
import com.justplay1.shoppist.models.mappers.UnitsDataModelMapper;
import com.justplay1.shoppist.preferences.ShoppistPreferences;
import com.justplay1.shoppist.presenter.base.BaseAddElementPresenter;
import com.justplay1.shoppist.utils.Const;
import com.justplay1.shoppist.view.AddListItemView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 04.07.2016.
 */
@PerActivity
public class AddListItemPresenter extends BaseAddElementPresenter<AddListItemView> {

    private final CategoryModelDataMapper mCategoryModelDataMapper;
    private final UnitsDataModelMapper mUnitsDataModelMapper;
    private final CurrencyModelDataMapper mCurrencyModelDataMapper;
    private final GoodsModelDataMapper mGoodsModelDataMapper;
    private final ListItemsModelDataMapper mListItemsModelDataMapper;

    private final AddListItems mAddListItems;
    private final UpdateListItems mUpdateListItems;
    private final GetCategories mGetCategories;
    private final GetCurrencies mGetCurrencies;
    private final GetGoods mGetGoods;
    private final GetUnits mGetUnits;
    private final UpdateGoods mUpdateGoods;

    private final ShoppistPreferences mPreferences;

    private ListItemViewModel mItem;
    private String mParentListId;
    private int mPriority;
    private CategoryViewModel mCategoryModel;
    private UnitViewModel mUnitModel;
    private CurrencyViewModel mCurrencyModel;
    private ProductViewModel mProductModel;
    private double mPrice;
    private double mQuantity;
    private String mNote;

    @Inject
    public AddListItemPresenter(CategoryModelDataMapper categoryModelDataMapper,
                                UnitsDataModelMapper unitsDataModelMapper,
                                CurrencyModelDataMapper currencyModelDataMapper,
                                GoodsModelDataMapper goodsModelDataMapper,
                                ListItemsModelDataMapper listItemsModelDataMapper,
                                AddListItems addListItems,
                                UpdateListItems updateListItems,
                                GetCategories getCategories,
                                GetCurrencies getCurrencies,
                                GetGoods getGoods,
                                GetUnits getUnits,
                                UpdateGoods updateGoods,
                                ShoppistPreferences preferences) {
        this.mCategoryModelDataMapper = categoryModelDataMapper;
        this.mUnitsDataModelMapper = unitsDataModelMapper;
        this.mCurrencyModelDataMapper = currencyModelDataMapper;
        this.mGoodsModelDataMapper = goodsModelDataMapper;
        this.mListItemsModelDataMapper = listItemsModelDataMapper;
        this.mAddListItems = addListItems;
        this.mUpdateListItems = updateListItems;
        this.mGetCategories = getCategories;
        this.mGetCurrencies = getCurrencies;
        this.mGetGoods = getGoods;
        this.mGetUnits = getUnits;
        this.mUpdateGoods = updateGoods;
        this.mPreferences = preferences;
    }

    @Override
    public void onCreate(Bundle arguments, Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
        if (arguments != null) {
            mItem = arguments.getParcelable(ListItemViewModel.class.getName());
            mParentListId = arguments.getString(Const.PARENT_LIST_ID);
        } else if (savedInstanceState != null) {
            mItem = savedInstanceState.getParcelable(ListItemViewModel.class.getName());
            mParentListId = savedInstanceState.getString(Const.PARENT_LIST_ID);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(ListItemViewModel.class.getName(), mItem);
        bundle.putString(Const.PARENT_LIST_ID, mParentListId);
    }

    public void init() {
        if (mItem != null) {
            setViewPrice(String.valueOf(mItem.getPrice()));
            setViewQuantity(String.valueOf(mItem.getQuantity()));
            setViewNote(String.valueOf(mItem.getNote()));
            setToolbarTitle(mItem.getName());
            setName(mItem.getName());
            setPriority(mItem.getPriority());
        } else {
            setViewPrice("0");
            setViewQuantity("0");
            setPriority(Priority.NO_PRIORITY);
            setDefaultToolbarTitle();
        }
        loadCategories();
        loadUnits();
        loadCurrency();
        loadGoods();
    }


    public void onPrioritySelected(@Priority int priority) {
        mPriority = priority;
    }

    public void onCategorySelected(CategoryViewModel category) {
        mCategoryModel = category;
    }

    public void onUnitSelected(UnitViewModel unit) {
        mUnitModel = unit;
    }

    public void onProductSelected(ProductViewModel product) {
        mProductModel = product;
    }

    public void onCurrencySelected(CurrencyViewModel currency) {
        mCurrencyModel = currency;
    }

    private void setPriority(@Priority int priority) {
        if (isViewAttached()) {
            getView().setPriority(priority);
        }
    }

    private void setViewNote(String note) {
        if (isViewAttached()) {
            getView().setNote(note);
        }
    }

    private void setViewPrice(String price) {
        if (isViewAttached()) {
            getView().setPrice(price);
        }
    }

    private void setViewQuantity(String quantity) {
        if (isViewAttached()) {
            getView().setQuantity(quantity);
        }
    }

    private void setCategory(List<CategoryViewModel> category) {
        if (isViewAttached()) {
            getView().setCategory(category);
        }
    }

    private void setCurrency(List<CurrencyViewModel> currency) {
        if (isViewAttached()) {
            getView().setCurrency(currency);
        }
    }

    private void setUnits(List<UnitViewModel> units) {
        if (isViewAttached()) {
            getView().setUnits(units);
        }
    }

    private void setGoods(Map<String, ProductViewModel> goods) {
        if (isViewAttached()) {
            getView().setGoods(goods);
        }
    }

    private void selectCategory(String id) {
        if (isViewAttached()) {
            getView().selectCategory(id);
        }
    }

    private void selectCurrency(String id) {
        if (isViewAttached()) {
            getView().selectCurrency(id);
        }
    }

    private void selectProduct(String id) {
        if (isViewAttached()) {
            getView().selectProduct(id);
        }
    }

    private void selectUnit(String id) {
        if (isViewAttached()) {
            getView().selectUnit(id);
        }
    }

    private void setDefaultCategory() {
        if (isViewAttached()) {
            getView().setDefaultCategory();
        }
    }

    private void setDefaultCurrency() {
        if (isViewAttached()) {
            getView().setDefaultCurrency();
        }
    }

    private void setDefaultUnit() {
        if (isViewAttached()) {
            getView().setDefaultUnit();
        }
    }

    public boolean isItemEdit() {
        return mItem != null;
    }

    public void startVoiceRecognition() {

    }

    public void onEditCurrencyClick() {

    }

    public void onAddCurrencyClick() {

    }

    public void onEditUnitClick() {

    }

    public void onAddUnitClick() {

    }

    public void onEditCategoryClick() {

    }

    public void onAddCategoryClick() {

    }

    public void onIncrementPriceClick(String price) {
        setViewPrice(String.format("%s", decrementValue(price)));
    }

    public void onDecrementPriceClick(String price) {
        setViewPrice(String.format("%s", incrementValue(price)));
    }

    public void onIncrementQuantityClick(String quantity) {
        setViewQuantity(String.format("%s", incrementValue(quantity)));
    }

    public void onDecrementQuantityClick(String quantity) {
        setViewQuantity(String.format("%s", decrementValue(quantity)));
    }

    public void onDoneButtonClick(String name) {
        saveListItem(name, false);
    }

    public void onDoneButtonLongClick(String name) {
        saveListItem(name, true);
    }

    public void setNote(String note) {
        mNote = note;
    }

    public void setPrice(String price) {
        if (!price.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(price);
            BigDecimal decimal = smallNumber.setScale(2, RoundingMode.HALF_UP);
            mPrice = decimal.doubleValue();
        } else {
            mPrice = 0;
        }
    }

    public void setQuantity(String quantity) {
        if (!quantity.isEmpty()) {
            BigDecimal smallNumber = new BigDecimal(quantity);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            mQuantity = decimal.doubleValue();
        } else {
            mQuantity = 0;
        }
    }

    public void loadCategories() {
        mSubscriptions.add(mGetCategories.get()
                .map(mCategoryModelDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<CategoryViewModel>>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CategoryViewModel> category) {
                        setCategory(category);

                        if (mItem != null) {
                            selectCategory(mItem.getCategory().getId());
//                        } else if (mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString()) != null) {
//                            ProductViewModel product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
//                            setSpinnerItem(mCategoryList.getSpinner(), product.getCategory().getId());
                        } else {
                            selectCategory(CategoryViewModel.NO_CATEGORY_ID);
                        }
                    }
                }));
    }

    public void loadUnits() {
        mSubscriptions.add(mGetUnits.get()
                .map(mUnitsDataModelMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<UnitViewModel>>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UnitViewModel> unitViewModels) {
                        setUnits(unitViewModels);
                        if (mItem != null) {
                            selectUnit(mItem.getUnit().getId());
//                        } else if (mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString()) != null) {
//                            ProductViewModel product = mAutoCompleteTextAdapter.getProduct(mNameEdit.getText().toString());
//                            selectUnit(product.getUnit().getId());
                        } else {
                            selectUnit(UnitViewModel.NO_UNIT_ID);
                        }
                    }
                }));
    }

    public void loadCurrency() {
        mSubscriptions.add(mGetCurrencies.get()
                .map(mCurrencyModelDataMapper::transformToViewModel)
                .subscribe(new DefaultSubscriber<List<CurrencyViewModel>>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<CurrencyViewModel> currency) {
                        setCurrency(currency);
                        if (mItem != null) {
                            selectCurrency(mItem.getCurrency().getId());
                        } else {
                            selectCurrency(mPreferences.getDefaultCurrency());
                        }
                    }
                }));
    }

    public void loadGoods() {
        mSubscriptions.add(mGetGoods.get()
                .map(mGoodsModelDataMapper::transformToViewModel)
                .flatMap(Observable::from)
                .toMap(BaseViewModel::getName)
                .subscribe(new DefaultSubscriber<Map<String, ProductViewModel>>() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<String, ProductViewModel> map) {
                        setGoods(map);
                    }
                }));
    }

    private void addListItem(ListItemViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mListItemsModelDataMapper.transform(data))
                        .flatMap(list -> {
                            mAddListItems.setData(Collections.singletonList(list));
                            return mAddListItems.get();
                        }).subscribe(new SaveListItemSubscriber(isLongClick, true)));
    }

    private void updateListItem(ListItemViewModel data, boolean isLongClick) {
        mSubscriptions.add(
                Observable.fromCallable(() -> mListItemsModelDataMapper.transform(data))
                        .flatMap(list -> {
                            mUpdateListItems.setData(Collections.singletonList(list));
                            return mUpdateListItems.get();
                        })
                        .filter(result -> result && mProductModel != null)
                        .flatMap(result -> Observable.fromCallable(() -> {
                            mProductModel.setUnit(mUnitModel);
                            return mGoodsModelDataMapper.transform(mProductModel);
                        }).flatMap(product -> {
                            mUpdateGoods.setData(Collections.singletonList(product));
                            return mUpdateListItems.get();
                        }))
                        .subscribe(new SaveListItemSubscriber(isLongClick, false)));
    }

    private void clearUI() {
        setName("");
        setPriority(Priority.NO_PRIORITY);
        setViewNote("");
        setDefaultUnit();
        setDefaultCurrency();
        setDefaultCategory();
        setViewPrice("0");
        setViewQuantity("0");
        mItem = null;
    }

    private boolean checkDataForErrors(String name) {
        if (name.isEmpty()) {
            showNameIsRequiredError();
            return false;
        } else if (name.length() > 60) {
            return false;
        }
        if (mNote.length() > 200) {
            return false;
        }
        if (mPrice > 10) {
            return false;
        }
        if (mQuantity > 10) {
            return false;
        }
        return true;
    }

    private ListItemViewModel buildList(String name) {
        ListItemViewModel listItem = new ListItemViewModel();
        listItem.setName(name);
        listItem.setDirty(true);
        listItem.setPriority(mPriority);
        listItem.setParentListId(mParentListId);
        listItem.setUnit(mUnitModel);
        listItem.setCurrency(mCurrencyModel);
        listItem.setPrice(mPrice);
        listItem.setQuantity(mQuantity);
        listItem.setCategory(mCategoryModel);
        listItem.setNote(mNote);

        if (mItem != null) {
            listItem.setId(mItem.getId());
            listItem.setServerId(mItem.getServerId());
            listItem.setChecked(mItem.isChecked());
            listItem.setTimestamp(mItem.getTimestamp());
            listItem.setTimeCreated(mItem.getTimeCreated());
            listItem.setStatus(mItem.getStatus());
        } else {
            listItem.setStatus(false);
            listItem.setTimeCreated(System.currentTimeMillis());
            listItem.setId(generateId(name));
        }
        return listItem;
    }

    private void saveListItem(String name, boolean isLongClick) {
        if (checkDataForErrors(name)) {
            ListItemViewModel item = buildList(name);
            if (mItem != null) {
                updateListItem(item, isLongClick);
            } else {
                addListItem(item, isLongClick);
            }
        }
    }

    private double incrementValue(String valueTxt) {
        try {
            double value = Double.valueOf(valueTxt);
            value++;
            BigDecimal smallNumber = new BigDecimal(value);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }

    private double decrementValue(String valueTxt) {
        try {
            double value = Double.valueOf(valueTxt);
            if (value < 1) return value;
            value--;
            BigDecimal smallNumber = new BigDecimal(value);
            BigDecimal decimal = smallNumber.setScale(3, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        } catch (NumberFormatException ignored) {

        }
        return 0;
    }

    protected final class SaveListItemSubscriber extends DefaultSubscriber<Boolean> {

        private boolean isLongClick;
        private boolean isAddAction;

        public SaveListItemSubscriber(boolean isLongClick, boolean isAddAction) {
            this.isLongClick = isLongClick;
            this.isAddAction = isAddAction;
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
//            showError(new DefaultErrorBundle((Exception) e));
        }

        @Override
        public void onNext(Boolean result) {
            if (result) {
                if (!isLongClick) {
                    closeScreen();
                } else {
                    if (isAddAction) {
                        showNewElementAddedMessage();
                    } else {
                        showElementUpdatedMessage();
                    }
                    setDefaultToolbarTitle();
                    clearUI();
                    showKeyboard();
                }
            }
        }
    }
}
