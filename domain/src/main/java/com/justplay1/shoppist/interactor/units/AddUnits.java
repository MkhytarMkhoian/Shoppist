package com.justplay1.shoppist.interactor.units;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.repository.UnitsRepository;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class AddUnits extends UseCase<Boolean> {

    private final UnitsRepository mRepository;
    private UnitModel mData;

    @Inject
    public AddUnits(UnitsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(UnitModel data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mData.setId(UUID.nameUUIDFromBytes((mData.getName() + mData.getShortName() + UUID.randomUUID()).getBytes()).toString());
            mRepository.save(mData);
            return true;
        });
    }


}
