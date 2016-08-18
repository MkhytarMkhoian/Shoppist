package com.justplay1.shoppist.utils;

import com.justplay1.shoppist.models.BaseViewModel;

import java.util.Collection;

/**
 * Created by Mkhitar on 03.02.2015.
 */
public interface AnimationResultListener<T extends BaseViewModel> {

    void onAnimationEnd(Collection<T> deleteItems);
}
