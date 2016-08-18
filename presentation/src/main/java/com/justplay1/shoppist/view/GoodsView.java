package com.justplay1.shoppist.view;

import android.support.v4.util.Pair;

import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.HeaderViewModel;
import com.justplay1.shoppist.models.ProductViewModel;
import com.justplay1.shoppist.models.UnitViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface GoodsView extends LoadDataView, ContextView {

    void showData(List<Pair<HeaderViewModel, List<ProductViewModel>>> data);

    void showChangeUnitDialog(List<ProductViewModel> editProducts);

    void showChangeCategoryDialog(List<ProductViewModel> editProducts);

    void showEditGoodsDialog(ProductViewModel editProduct);

    void showDeleteDialog();

    void openSearchScreen();
}
