package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.UIThread;
import com.justplay1.shoppist.executor.JobExecutor;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 16.05.2016.
 */
@Module
public class ThreadExecutorModule {

    @Provides
    @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides
    @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }
}
