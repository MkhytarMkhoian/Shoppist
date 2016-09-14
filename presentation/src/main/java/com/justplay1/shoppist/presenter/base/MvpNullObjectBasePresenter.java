/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.presenter.base;

import android.support.annotation.NonNull;
import android.support.annotation.UiThread;

import com.justplay1.shoppist.view.BaseMvpView;

import java.lang.ref.WeakReference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Mkhytar Mkhoian.
 */
public abstract class MvpNullObjectBasePresenter<V extends BaseMvpView> implements Presenter<V> {

    private WeakReference<V> view;
    private final V nullView;

    public MvpNullObjectBasePresenter() {
        try {

            // Scan the inheritance hierarchy until we reached MvpNullObjectBasePresenter
            Class<V> viewClass = null;
            Class<?> currentClass = getClass();

            while (viewClass == null) {

                Type genericSuperType = currentClass.getGenericSuperclass();

                while (!(genericSuperType instanceof ParameterizedType)) {
                    // Scan inheritance tree until we find ParameterizedType which is probably a MvpSubclass
                    currentClass = currentClass.getSuperclass();
                    genericSuperType = currentClass.getGenericSuperclass();
                }

                Type[] types = ((ParameterizedType) genericSuperType).getActualTypeArguments();

                for (int i = 0; i < types.length; i++) {
                    Class<?> genericType = (Class<?>) types[i];
                    if (genericType.isInterface() && isSubTypeOfMvpView(genericType)) {
                        viewClass = (Class<V>) genericType;
                        break;
                    }
                }

                // Continue with next class in inheritance hierachy (see genericSuperType assignment at start of while loop)
                currentClass = currentClass.getSuperclass();
            }

            nullView = NoOp.of(viewClass);
        } catch (Throwable t) {
            throw new IllegalArgumentException(
                    "The generic type <V extends MvpView> must be the first generic type argument of class "
                            + getClass().getSimpleName()
                            + " (per convention). Otherwise we can't determine which type of View this"
                            + " Presenter coordinates.", t);
        }
    }

    /**
     * Scans the interface inheritnace hierarchy and checks if on the root is MvpView.class
     *
     * @param klass The leaf interface where to begin to scan
     * @return true if subtype of MvpView, otherwise false
     */
    private boolean isSubTypeOfMvpView(Class<?> klass) {
        if (klass.equals(BaseMvpView.class)) {
            return true;
        }
        Class[] superInterfaces = klass.getInterfaces();
        for (int i = 0; i < superInterfaces.length; i++) {
            if (isSubTypeOfMvpView(superInterfaces[0])) {
                return true;
            }
        }
        return false;
    }

    @UiThread
    @Override
    public void attachView(@NonNull V view) {
        this.view = new WeakReference<V>(view);
    }

    @UiThread
    @NonNull
    protected V getView() {
        if (view != null) {
            V realView = view.get();
            if (realView != null) {
                return realView;
            }
        }

        return nullView;
    }

    @UiThread
    @Override
    public void detachView() {
        if (view != null) {
            view.clear();
            view = null;
        }
    }
}