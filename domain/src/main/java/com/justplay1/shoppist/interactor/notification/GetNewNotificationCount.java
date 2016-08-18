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
public class GetNewNotificationCount extends UseCase<Integer> {

    private final NotificationRepository mRepository;
    private long mTimestamp;

    @Inject
    public GetNewNotificationCount(NotificationRepository repository,
                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setTimestamp(long time) {
        this.mTimestamp = time;
    }

    @Override
    protected Observable<Integer> buildUseCaseObservable() {
        return mRepository.getNewNotificationCount(mTimestamp);
    }
}
