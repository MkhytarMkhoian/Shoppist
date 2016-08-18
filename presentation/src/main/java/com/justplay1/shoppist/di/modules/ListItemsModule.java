package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.listitems.AddListItems;
import com.justplay1.shoppist.interactor.listitems.GetListItems;
import com.justplay1.shoppist.interactor.listitems.MoveProductToCart;
import com.justplay1.shoppist.interactor.listitems.MoveToList;
import com.justplay1.shoppist.interactor.listitems.SoftDeleteListItems;
import com.justplay1.shoppist.interactor.listitems.UpdateListItems;
import com.justplay1.shoppist.repository.ListItemsRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class ListItemsModule {

    @Provides
    @PerActivity
    AddListItems provideAddShoppingListItems(ListItemsRepository repository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new AddListItems(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetListItems provideGetShoppingListItems(ListItemsRepository repository,
                                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetListItems(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UpdateListItems provideUpdateShoppingListItems(ListItemsRepository repository,
                                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new UpdateListItems(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    MoveProductToCart provideMoveProductToCart(ListItemsRepository repository,
                                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new MoveProductToCart(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    MoveToList provideMoveToList(ListItemsRepository repository,
                                 ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new MoveToList(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SoftDeleteListItems provideSoftDeleteShoppingListItems(ListItemsRepository repository,
                                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new SoftDeleteListItems(repository, threadExecutor, postExecutionThread);
    }
}
