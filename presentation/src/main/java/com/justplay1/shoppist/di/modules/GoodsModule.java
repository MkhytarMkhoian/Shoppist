package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.goods.AddGoods;
import com.justplay1.shoppist.interactor.goods.GetGoods;
import com.justplay1.shoppist.interactor.goods.SoftDeleteGoods;
import com.justplay1.shoppist.interactor.goods.UpdateGoods;
import com.justplay1.shoppist.repository.GoodsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
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
    SoftDeleteGoods provideSoftDeleteGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new SoftDeleteGoods(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UpdateGoods provideUpdateGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateGoods(repository, threadExecutor, postExecutionThread);
    }
}
