package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CurrencyModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.CurrencyFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddCurrencyDialogFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectCurrencyDialogFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, CurrencyModule.class})
public interface CurrencyComponent extends ActivityComponent {

    void inject(CurrencyFragment fragment);

    void inject(AddCurrencyDialogFragment fragment);

    void inject(SelectCurrencyDialogFragment fragment);

}
