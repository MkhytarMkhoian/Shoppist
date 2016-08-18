package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.account.DeleteAccount;
import com.justplay1.shoppist.interactor.account.LogOut;
import com.justplay1.shoppist.interactor.account.SignIn;
import com.justplay1.shoppist.interactor.account.SignUp;
import com.justplay1.shoppist.models.AccountManager;
import com.justplay1.shoppist.repository.AccountRepository;
import com.justplay1.shoppist.repository.account.ParseAccount;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 03.06.2016.
 */
@Module
public class AccountModule {

    @Provides
    @PerActivity
    SignUp provideUserSingUp(AccountRepository repository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        return new SignUp(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    LogOut provideUserLogout(AccountRepository repository, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread) {
        return new LogOut(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SignIn provideUserLogin(AccountRepository repository, ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        return new SignIn(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    DeleteAccount provideDeleteUser(AccountRepository repository, ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        return new DeleteAccount(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    AccountManager provideAccountManager() {
        return new ParseAccount();
    }
}
