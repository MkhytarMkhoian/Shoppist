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
import com.justplay1.shoppist.interactor.lists.AddList;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.interactor.lists.DeleteLists;
import com.justplay1.shoppist.interactor.lists.UpdateLists;
import com.justplay1.shoppist.repository.ListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class ListsModule {

    @Provides
    @NonConfigurationScope
    DeleteLists provideSoftDeleteShoppingList(ListRepository repository, ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread) {
        return new DeleteLists(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    UpdateLists provideSaveListsPosition(ListRepository repository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new UpdateLists(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    GetLists provideGetShoppingList(ListRepository repository, ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        return new GetLists(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @NonConfigurationScope
    AddList provideAddShoppingList(ListRepository repository, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        return new AddList(repository, threadExecutor, postExecutionThread);
    }
}
