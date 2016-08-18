package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.AddListItemFragment;
import com.justplay1.shoppist.view.fragments.ListItemsFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ListItemsModule.class})
public interface ListItemsComponent extends ActivityComponent {

    void inject(ListItemsFragment fragment);

    void inject(AddListItemFragment fragment);
}
