package com.justplay1.shoppist.interactor.units;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.UnitModel;
import com.justplay1.shoppist.repository.UnitsRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetUnit extends UseCase<UnitModel> {

    private final UnitsRepository mRepository;
    private String mId;

    @Inject
    public GetUnit(UnitsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Override
    protected Observable<UnitModel> buildUseCaseObservable() {
        return mRepository.getItem(mId);
    }
}
