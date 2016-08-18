package com.justplay1.shoppist.ui.views.recyclerview;

import com.justplay1.shoppist.models.BaseModel;

/**
 * Created by Mkhytar on 12.10.2015.
 */
public interface DeleteSwipeResultListener<T extends BaseModel> {

    void onDelete(T deleteItem);
    void onCancel(T deleteItem);
}
