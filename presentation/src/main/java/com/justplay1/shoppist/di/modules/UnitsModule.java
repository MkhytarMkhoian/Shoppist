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
import com.justplay1.shoppist.interactor.units.AddUnits;
import com.justplay1.shoppist.interactor.units.GetUnitsList;
import com.justplay1.shoppist.interactor.units.DeleteUnits;
import com.justplay1.shoppist.interactor.units.UpdateUnits;
import com.justplay1.shoppist.repository.UnitsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar Mkhoian.
 */
@Module
public class UnitsModule {

    @Provides
    @NonConfigurationScope
    AddUnits provideAddUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        return new AddUnits(repository, threadExecutor, postExecutionThread);
    }


    @Provides
    @NonConfigurationScope
    GetUnitsList provideGetUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                                 PostExecutionThread postExecutionThread) {
        return new GetUnitsList(repository, threadExecutor, postExecutionThread);
    }


    @Provides
    @NonConfigurationScope
    DeleteUnits provideSoftDeleteUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        return new DeleteUnits(repository, threadExecutor, postExecutionThread);
    }


    @Provides
    @NonConfigurationScope
    UpdateUnits provideUpdateUnits(UnitsRepository repository, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        return new UpdateUnits(repository, threadExecutor, postExecutionThread);
    }
}
