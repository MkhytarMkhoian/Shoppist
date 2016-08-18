package com.justplay1.shoppist.view;

import android.support.annotation.ColorInt;

import com.justplay1.shoppist.models.ProductViewModel;

import java.util.Map;

/**
 * Created by Mkhytar on 07.08.2016.
 */
public interface SearchView extends BaseMvpView {

    void fadeInSignal(@ColorInt int color);

    void clearSearch();

    void closeSearch();

    void openVoiceSearch();

    void showEditGoodsDialog(String name);

    void showEditGoodsDialog(ProductViewModel product);

    void showData(Map<String, ProductViewModel> data);
}
