package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.NotificationModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.NotificationFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, NotificationModule.class})
public interface NotificationComponent extends ActivityComponent {

    void inject(NotificationFragment fragment);
}
