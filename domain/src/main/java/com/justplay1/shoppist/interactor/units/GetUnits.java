package com.justplay1.shoppist.interactor.units;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.repository.UnitsRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetUnits extends UseCase<List<UnitModel>> {

    private final UnitsRepository mRepository;

    @Inject
    public GetUnits(UnitsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<List<UnitModel>> buildUseCaseObservable() {
        return mRepository.getItems();
    }
}
