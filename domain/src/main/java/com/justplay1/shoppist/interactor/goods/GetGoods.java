package com.justplay1.shoppist.interactor.goods;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.repository.GoodsRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetGoods extends UseCase<List<ProductModel>> {

    private final GoodsRepository mRepository;

    @Inject
    public GetGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    @Override
    protected Observable<List<ProductModel>> buildUseCaseObservable() {
        return mRepository.getItems();
    }
}
