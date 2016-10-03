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

package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.NonConfigurationScope;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.interactor.listitems.DeleteListItems;
import com.justplay1.shoppist.interactor.listitems.GetListItems;
import com.justplay1.shoppist.interactor.listitems.MoveProductToCart;
import com.justplay1.shoppist.interactor.listitems.MoveToList;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.repository.ListItemsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class ListItemsModule {

    @Provides
    @NonConfigurationScope
    AddListItems provideAddShoppingListItems(ListItemsRepository repository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddListItems(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    GetListItems provideGetShoppingListItems(ListItemsRepository repository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetListItems(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    UpdateListItems provideUpdateShoppingListItems(ListItemsRepository repository,
                                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateListItems(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    MoveProductToCart provideMoveProductToCart(ListItemsRepository repository,
                                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new MoveProductToCart(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    MoveToList provideMoveToList(ListItemsRepository repository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new MoveToList(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    DeleteListItems provideSoftDeleteShoppingListItems(ListItemsRepository repository,
                                                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new DeleteListItems(repository, threadExecutor, postExecutionThread);
    }
}
