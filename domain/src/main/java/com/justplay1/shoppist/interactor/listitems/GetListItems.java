package com.justplay1.shoppist.interactor.listitems;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.repository.ListItemsRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetListItems extends UseCase<List<ListItemModel>> {

    private final ListItemsRepository mRepository;
    private String mParentId;

    @Inject
    public GetListItems(ListItemsRepository repository,
                        ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setParentId(String parentId) {
        this.mParentId = parentId;
    }

    @Override
    protected Observable<List<ListItemModel>> buildUseCaseObservable() {
        return mRepository.getItems(mParentId);
    }
}
