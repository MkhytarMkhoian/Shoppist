package com.justplay1.shoppist.interactor.lists;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.repository.ListRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class UpdateLists extends UseCase<Boolean> {

    private final ListRepository mRepository;
    private List<ListModel> mData;

    @Inject
    public UpdateLists(ListRepository repository,
                       ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(List<ListModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mRepository.update(mData);
            return true;
        });
    }
}
