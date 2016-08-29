package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.CurrencyViewModel;
import com.justplay1.shoppist.models.Priority;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;
import java.util.Map;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AddListItemView extends AddElementView {

    void setPriority(@Priority int priority);

    void setPrice(String price);

    void setNote(String note);

    void setQuantity(String quantity);

    void selectCategory(String id);

    void setCategory(List<CategoryViewModel> category);

    void selectUnit(String id);

    void setUnits(List<UnitViewModel> unit);

    void selectCurrency(String id);

    void setCurrency(List<CurrencyViewModel> currency);

    void selectProduct(String id);

    void setGoods(Map<String, ProductViewModel> goods);

    void setDefaultCategory();

    void setDefaultUnit();

    void setDefaultCurrency();
}
