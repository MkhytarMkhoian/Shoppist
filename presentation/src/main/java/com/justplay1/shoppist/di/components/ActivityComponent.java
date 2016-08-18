package com.justplay1.shoppist.di.components;

import android.app.Activity;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.scope.PerActivity;

import dagger.Component;

/**
 * A base component upon which mFragment's components may depend.
 * Activity-level components should extend this component.
 * <p>
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link com.justplay1.shoppist.di.scope.PerActivity}
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent{

    Activity activity();
}
