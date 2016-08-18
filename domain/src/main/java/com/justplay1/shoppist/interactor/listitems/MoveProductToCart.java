package com.justplay1.shoppist.interactor.listitems;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.repository.ListItemsRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class MoveProductToCart extends UseCase<Boolean> {

    private final ListItemsRepository mRepository;
    private ListItemModel mData;

    @Inject
    public MoveProductToCart(ListItemsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(ListItemModel data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mData.setDirty(true);
            mData.setTimestamp(System.currentTimeMillis());
            mData.setStatus(!mData.getStatus());
            mRepository.update(mData);
            return true;
        });
    }
}
