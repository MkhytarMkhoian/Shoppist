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
import com.justplay1.shoppist.interactor.goods.AddGoods;
import com.justplay1.shoppist.interactor.goods.DeleteGoods;
import com.justplay1.shoppist.interactor.goods.GetGoods;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.repository.GoodsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class GoodsModule {

    @Provides
    @PerActivity
    AddGoods provideAddGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddGoods(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetGoods provideGetGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetGoods(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    DeleteGoods provideSoftDeleteGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new DeleteGoods(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UpdateGoods provideUpdateGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateGoods(repository, threadExecutor, postExecutionThread);
    }
}
