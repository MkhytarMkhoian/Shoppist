package com.justplay1.shoppist.presenter.base;

import android.os.Bundle;
import android.support.annotation.UiThread;

public interface Presenter<V> {

    @UiThread
    void attachView(V view);

    @UiThread
    void detachView();

    @UiThread
    void onCreate(Bundle arguments, Bundle savedInstanceState);

    @UiThread
    void onSaveInstanceState(Bundle bundle);

    @UiThread
    void onDestroy();
}
