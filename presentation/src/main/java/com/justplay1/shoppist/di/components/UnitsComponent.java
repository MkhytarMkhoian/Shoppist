package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.UnitsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.UnitFragment;
import com.justplay1.shoppist.view.fragments.dialog.AddUnitsDialogFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, UnitsModule.class})
public interface UnitsComponent extends ActivityComponent {

    void inject(UnitFragment fragment);

    void inject(AddUnitsDialogFragment fragment);
}
