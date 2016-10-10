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

import com.justplay1.shoppist.navigation.Router;
import com.justplay1.shoppist.view.BaseMvpView;

import java.lang.ref.WeakReference;

import dagger.internal.Preconditions;


public abstract class BaseRouterPresenter<V extends BaseMvpView, R extends Router> extends BasePresenter<V> {

    private WeakReference<R> routerRef;

    /**
     * Called to surrender control of taken router.
     * <p>
     * It is expected that this method will be called with the same argument as {@link #attachRouter}. Mismatched routers
     * are ignored. This is to provide protection in the not uncommon case that {@code detachRouter} and {@code attachRouter}
     * are called out of order.
     * <p>
     */
    public final void detachRouter() {
        releaseRouter();
    }

    /**
     * Checks if a router is attached to this presenter. You should always call this method before calling {@link
     * #getRouter} to get the router instance.
     *
     * @return {@code true} if presenter has attached router
     */
    public final boolean hasRouter() {
        return routerRef != null && routerRef.get() != null;
    }

    /**
     * Called to give this presenter control of a router.
     *
     * @param router router that will be returned from {@link #getRouter()}.
     */
    public final void attachRouter(R router) {
        Preconditions.checkNotNull(router, "router");

        final R currentRouter = getRouter();
        if (currentRouter != router) {
            if (currentRouter != null) {
                detachRouter();
            }
            assignRouter(router);
        }
    }

    /**
     * Returns the router managed by this presenter, or {@code null} if {@link #attachRouter} has never been called, or
     * after {@link #detachRouter}.
     * <p>
     * You should always call {@link #hasRouter} to check if the router is taken to avoid NullPointerExceptions.
     *
     * @return {@code null}, if router is not taken, otherwise the concrete router instance.
     */
    protected final R getRouter() {
        return routerRef == null ? null : routerRef.get();
    }

    private void assignRouter(R router) {
        routerRef = new WeakReference<>(router);
    }

    private void releaseRouter() {
        if (routerRef != null) {
            routerRef.clear();
            routerRef = null;
        }
    }
}
