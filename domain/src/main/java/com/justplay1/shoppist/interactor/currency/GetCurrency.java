package com.justplay1.shoppist.interactor.currency;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.repository.CurrencyRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetCurrency extends UseCase<CurrencyModel> {

    private final CurrencyRepository mRepository;
    private String mId;

    @Inject
    public GetCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Override
    protected Observable<CurrencyModel> buildUseCaseObservable() {
        return mRepository.getItem(mId);
    }
}
