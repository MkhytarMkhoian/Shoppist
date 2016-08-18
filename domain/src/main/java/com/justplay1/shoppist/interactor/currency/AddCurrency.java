package com.justplay1.shoppist.interactor.currency;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CurrencyModel;
import com.justplay1.shoppist.repository.CurrencyRepository;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class AddCurrency extends UseCase<Boolean> {

    private final CurrencyRepository mRepository;
    private CurrencyModel mData;

    @Inject
    public AddCurrency(CurrencyRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(CurrencyModel data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            mData.setId(UUID.nameUUIDFromBytes((mData.getName() + UUID.randomUUID()).getBytes()).toString());
            mRepository.save(mData);
            return true;
        });
    }
}
