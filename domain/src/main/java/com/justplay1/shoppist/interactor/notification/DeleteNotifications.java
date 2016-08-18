package com.justplay1.shoppist.interactor.notification;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.NotificationModel;
import com.justplay1.shoppist.repository.NotificationRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class DeleteNotifications extends UseCase<Boolean> {

    private final NotificationRepository mRepository;
    private NotificationModel mData;

    @Inject
    public DeleteNotifications(NotificationRepository repository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(NotificationModel data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mRepository.delete(mData);
            return true;
        });
    }
}
