package com.justplay1.shoppist.utils;

import com.justplay1.shoppist.models.BaseModel;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mkhitar on 03.02.2015.
 */
public interface AnimationResultListener<T extends BaseModel> {

    public void onAnimationEnd(Collection<T> deleteItems);
}
