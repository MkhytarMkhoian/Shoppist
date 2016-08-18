package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.di.modules.GoodsModule;
import com.justplay1.shoppist.di.modules.ListItemsModule;
import com.justplay1.shoppist.di.modules.UnitsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.AddListItemFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class,
        modules = {ActivityModule.class,
                ListItemsModule.class,
                GoodsModule.class,
                CategoryModule.class,
                UnitsModule.class,
                CurrencyModule.class})
public interface AddListItemsComponent extends ActivityComponent {

    void inject(AddListItemFragment fragment);
}
