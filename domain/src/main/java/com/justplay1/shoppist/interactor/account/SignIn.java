package com.justplay1.shoppist.interactor.account;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.Account;
import com.justplay1.shoppist.models.AuthCredentials;
import com.justplay1.shoppist.repository.AccountRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public class SignIn extends UseCase<Account> {

    private final AccountRepository mRepository;
    private AuthCredentials mAuthCredentials;

    @Inject
    public SignIn(AccountRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setAuthCredentials(AuthCredentials authCredentials) {
        this.mAuthCredentials = authCredentials;
    }

    @Override
    protected Observable<Account> buildUseCaseObservable() {
        return mRepository.singIn(mAuthCredentials);
    }
}
