package com.justplay1.shoppist.interactor.notification;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.repository.NotificationRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class ClearNotification extends UseCase<Integer> {

    private final NotificationRepository mRepository;

    @Inject
    public ClearNotification(NotificationRepository repository,
                             ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<Integer> buildUseCaseObservable() {
        return Observable.fromCallable(mRepository::clear);
    }
}
