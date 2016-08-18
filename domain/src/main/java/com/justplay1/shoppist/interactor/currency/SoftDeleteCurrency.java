package com.justplay1.shoppist.interactor.currency;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.repository.CurrencyRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class SoftDeleteCurrency extends UseCase<Boolean> {

    private final CurrencyRepository mRepository;
    private Collection<CurrencyModel> mData;

    @Inject
    public SoftDeleteCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<CurrencyModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<CurrencyModel> toUpdate = new ArrayList<>();
            for (CurrencyModel currency : mData) {
                currency.setDirty(true);
                currency.setDelete(true);
                toUpdate.add(currency);
            }
            mRepository.update(toUpdate);
            return true;
        });
    }
}
