package com.justplay1.shoppist.interactor.lists;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.repository.ListRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetLists extends UseCase<List<ListModel>> {

    private final ListRepository mRepository;

    @Inject
    public GetLists(ListRepository repository,
                    ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<List<ListModel>> buildUseCaseObservable() {
        return mRepository.getItems();
    }
}
