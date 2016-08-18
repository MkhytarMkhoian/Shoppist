package com.justplay1.shoppist.interactor.account;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.repository.AccountRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 14.05.2016.
 */
public class DeleteAccount extends UseCase<Boolean> {

    private final AccountRepository mRepository;

    @Inject
    public DeleteAccount(AccountRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mRepository.deleteAccount();
            return true;
        });
    }
}
