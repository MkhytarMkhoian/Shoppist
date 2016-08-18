package com.justplay1.shoppist.interactor.lists;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListModel;
import com.justplay1.shoppist.repository.ListRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class SoftDeleteLists extends UseCase<Boolean> {

    private final ListRepository mRepository;
    private Collection<ListModel> mData;

    @Inject
    public SoftDeleteLists(ListRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<ListModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<ListModel> toDelete = new ArrayList<>();
            List<ListModel> toUpdate = new ArrayList<>();
            for (ListModel list : mData) {
                if (list.getServerId() == null) {
                    toDelete.add(list);
                } else {
                    list.setDirty(true);
                    list.setDelete(true);
                    toUpdate.add(list);
                }
            }
            mRepository.delete(toDelete);
            mRepository.update(toUpdate);

            for (ListModel list : toUpdate) {
                mRepository.markListItemsAsDeleted(list.getId());
            }
            for (ListModel list : toDelete) {
                mRepository.deleteListItems(list.getId());
            }
            return true;
        });
    }
}
