package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.GoodsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.GoodsFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddGoodsDialogFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, GoodsModule.class})
public interface GoodsComponent extends ActivityComponent {

    void inject(GoodsFragment fragment);

    void inject(AddGoodsDialogFragment fragment);
}
