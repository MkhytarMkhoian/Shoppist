package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.CategoryViewModel;

import java.util.List;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface SelectCategoryView extends LoadDataView {

    void setCategory(List<CategoryViewModel> category);

    void selectCategory(String id);

    void onComplete(CategoryViewModel category, boolean isUpdate);

    void closeDialog();
}
