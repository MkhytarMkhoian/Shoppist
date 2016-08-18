package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.units.AddUnits;
import com.justplay1.shoppist.interactor.units.GetUnits;
import com.justplay1.shoppist.interactor.units.SoftDeleteUnits;
import com.justplay1.shoppist.interactor.units.UpdateUnits;
import com.justplay1.shoppist.repository.UnitsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class UnitsModule {

    @Provides
    @PerActivity
    AddUnits provideAddUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        return new AddUnits(repository, threadExecutor, postExecutionThread);
    }


    @Provides
    @PerActivity
    GetUnits provideGetUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        return new GetUnits(repository, threadExecutor, postExecutionThread);
    }


    @Provides
    @PerActivity
    SoftDeleteUnits provideSoftDeleteUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread) {
        return new SoftDeleteUnits(repository, threadExecutor, postExecutionThread);
    }


    @Provides
    @PerActivity
    UpdateUnits provideUpdateUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        return new UpdateUnits(repository, threadExecutor, postExecutionThread);
    }
}
