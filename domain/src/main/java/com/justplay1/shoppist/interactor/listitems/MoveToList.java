package com.justplay1.shoppist.interactor.listitems;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ListItemModel;
import com.justplay1.shoppist.repository.ListItemsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class MoveToList extends UseCase<Boolean> {

    private final ListItemsRepository mRepository;
    private Collection<ListItemModel> mData;
    private String mNewParentListId;
    private boolean mCopy;

    @Inject
    public MoveToList(ListItemsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<ListItemModel> data) {
        this.mData = data;
    }

    public void setNewParentListId(String newParentListId) {
        this.mNewParentListId = newParentListId;
    }

    public void setCopy(boolean copy) {
        this.mCopy = copy;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<ListItemModel> needAdd = new ArrayList<>();

            for (ListItemModel item : mData) {
                ListItemModel newItem = new ListItemModel(item);
                newItem.setId(UUID.nameUUIDFromBytes((newItem.getName() + UUID.randomUUID()).getBytes()).toString());
                newItem.setParentListId(mNewParentListId);
                newItem.setServerId(null);
                newItem.setDirty(true);
                needAdd.add(newItem);
            }
            if (needAdd.size() > 0) {
                mRepository.save(needAdd);
            }
            if (!mCopy) {
                List<ListItemModel> toDelete = new ArrayList<>();
                List<ListItemModel> toUpdate = new ArrayList<>();
                for (ListItemModel item : mData) {
                    item.setDirty(true);
                    item.setDelete(true);
                    if (item.getServerId() == null) {
                        toDelete.add(item);
                    } else {
                        toUpdate.add(item);
                    }
                }
                mRepository.delete(toDelete);
                mRepository.update(toUpdate);
            }
            return true;
        });
    }
}
