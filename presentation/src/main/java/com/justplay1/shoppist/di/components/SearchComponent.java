package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.di.modules.GoodsModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.CategoryFragment;
import com.justplay1.shoppist.view.fragments.SearchFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class,
        modules = {ActivityModule.class, CategoryModule.class, GoodsModule.class, ListItemsModule.class})
public interface SearchComponent extends ActivityComponent {

    void inject(SearchFragment fragment);
}
