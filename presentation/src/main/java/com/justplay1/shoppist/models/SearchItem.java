package com.justplay1.shoppist.models;

/**
 * Created by Mkhytar on 14.02.2016.
 */
public interface SearchItem<T extends BaseViewModel> {

    T getItem();
    SearchItemType getItemType();
}
