package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.CategoryModule;
import com.justplay1.shoppist.di.modules.RepositoryModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.AddCategoryFragment;
import com.justplay1.shoppist.view.fragments.CategoryFragment;
import com.justplay1.shoppist.view.fragments.dialog.SelectCategoryDialogFragment;

import dagger.Component;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, CategoryModule.class})
public interface CategoryComponent extends ActivityComponent {

    void inject(CategoryFragment fragment);

    void inject(AddCategoryFragment fragment);

    void inject(SelectCategoryDialogFragment fragment);
}
