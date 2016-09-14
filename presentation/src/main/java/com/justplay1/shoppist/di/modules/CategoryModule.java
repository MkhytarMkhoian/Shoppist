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

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.category.AddCategory;
import com.justplay1.shoppist.interactor.category.DeleteCategory;
import com.justplay1.shoppist.interactor.category.GetCategories;
import com.justplay1.shoppist.interactor.category.UpdateCategory;
import com.justplay1.shoppist.repository.CategoryRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class CategoryModule {

    @Provides
    @PerActivity
    AddCategory provideAddCategory(CategoryRepository repository, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        return new AddCategory(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetCategories provideGetCategories(CategoryRepository repository, ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        return new GetCategories(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UpdateCategory provideUpdateCategory(CategoryRepository repository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new UpdateCategory(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    DeleteCategory provideSoftDeleteCategory(CategoryRepository repository, ThreadExecutor threadExecutor,
                                             PostExecutionThread postExecutionThread) {
        return new DeleteCategory(repository, threadExecutor, postExecutionThread);
    }
}
