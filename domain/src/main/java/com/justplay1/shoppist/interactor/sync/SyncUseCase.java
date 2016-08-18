package com.justplay1.shoppist.interactor.sync;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.repository.SyncRepository;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 30.05.2016.
 */
public class SyncUseCase extends UseCase<Void> {

    private final SyncRepository mRepository;
    private boolean mDeleteDataBefore;
    private boolean mNotifyUser;

    @Inject
    public SyncUseCase(SyncRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setDeleteDataBefore(boolean deleteDataBefore) {
        this.mDeleteDataBefore = deleteDataBefore;
    }

    public void setNotifyUser(boolean notifyUser) {
        this.mNotifyUser = notifyUser;
    }

    @Override
    protected Observable<Void> buildUseCaseObservable() {
        return Observable.fromCallable((Callable<Void>) () -> {
            mRepository.doSync(mDeleteDataBefore, mNotifyUser);
            return null;
        });
    }
}
