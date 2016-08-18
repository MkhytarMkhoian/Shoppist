package com.justplay1.shoppist.interactor.units;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.repository.UnitsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class SoftDeleteUnits extends UseCase<Boolean> {

    private final UnitsRepository mRepository;
    private Collection<UnitModel> mData;

    @Inject
    public SoftDeleteUnits(UnitsRepository repository,
                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<UnitModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<UnitModel> toUpdate = new ArrayList<>();
            for (UnitModel unit : mData) {
                unit.setDirty(true);
                unit.setDelete(true);
                toUpdate.add(unit);
            }
            mRepository.update(toUpdate);
            return true;
        });
    }
}
