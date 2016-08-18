package com.justplay1.shoppist.interactor.listitems;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.repository.ListItemsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class SoftDeleteListItems extends UseCase<Boolean> {

    private final ListItemsRepository mRepository;
    private Collection<ListItemModel> mData;

    @Inject
    public SoftDeleteListItems(ListItemsRepository repository,
                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<ListItemModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<ListItemModel> toDelete = new ArrayList<>();
            List<ListItemModel> toUpdate = new ArrayList<>();
            for (ListItemModel item : mData) {
                if (item.getServerId() == null) {
                    toDelete.add(item);
                } else {
                    item.setDirty(true);
                    item.setDelete(true);
                    toUpdate.add(item);
                }
            }
            mRepository.update(toUpdate);
            mRepository.delete(toDelete);
            return true;
        });
    }
}
