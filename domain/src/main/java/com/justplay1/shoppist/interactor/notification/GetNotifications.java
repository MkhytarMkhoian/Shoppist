package com.justplay1.shoppist.interactor.notification;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.NotificationModel;
import com.justplay1.shoppist.repository.NotificationRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetNotifications extends UseCase<List<NotificationModel>> {

    private final NotificationRepository mRepository;

    @Inject
    public GetNotifications(NotificationRepository repository,
                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<List<NotificationModel>> buildUseCaseObservable() {
        return mRepository.getItems();
    }
}
