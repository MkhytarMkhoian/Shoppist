package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.AddListFragment;
import com.justplay1.shoppist.view.fragments.ListFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ListsModule.class})
public interface ListsComponent extends ActivityComponent {

    void inject(ListFragment fragment);

    void inject(AddListFragment fragment);
}
