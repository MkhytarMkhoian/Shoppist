package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CategoryViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface CategoryView extends LoadDataView {

    void showData(List<CategoryViewModel> data);

    void openAddCategoryScreen(CategoryViewModel category);

    void setManualSortEnable(boolean manualSortEnable);
}
