package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.lists.AddList;
import com.justplay1.shoppist.interactor.lists.GetLists;
import com.justplay1.shoppist.interactor.lists.SoftDeleteLists;
import com.justplay1.shoppist.interactor.lists.UpdateLists;
import com.justplay1.shoppist.repository.ListRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class ListsModule {

    @Provides
    @PerActivity
    SoftDeleteLists provideSoftDeleteShoppingList(ListRepository repository, ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new SoftDeleteLists(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UpdateLists provideSaveListsPosition(ListRepository repository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new UpdateLists(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetLists provideGetShoppingList(ListRepository repository, ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        return new GetLists(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    AddList provideAddShoppingList(ListRepository repository, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        return new AddList(repository, threadExecutor, postExecutionThread);
    }
}
