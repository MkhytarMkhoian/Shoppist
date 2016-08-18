package com.justplay1.shoppist.view.component.recyclerview;

import com.justplay1.shoppist.models.BaseViewModel;

/**
 * Created by Mkhytar on 12.10.2015.
 */
public interface DeleteSwipeResultListener<T extends BaseViewModel> {

    void onDelete(T deleteItem);
    void onCancel(T deleteItem);
}
