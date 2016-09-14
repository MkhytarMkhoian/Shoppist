/*
 * Copyright (C) 2016 Mkhytar Mkhoian
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.justplay1.shoppist.di.components;

import com.justplay1.shoppist.di.modules.ActivityModule;
import com.justplay1.shoppist.di.modules.ListsModule;
import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.view.fragments.AddListFragment;
import com.justplay1.shoppist.view.fragments.ListFragment;

import dagger.Component;

/**
 * Created by Mkhytar Mkhoian.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, ListsModule.class})
public interface ListsComponent extends ActivityComponent {

    void inject(ListFragment fragment);

    void inject(AddListFragment fragment);
}
