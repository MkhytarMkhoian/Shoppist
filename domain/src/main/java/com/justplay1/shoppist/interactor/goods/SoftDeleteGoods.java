package com.justplay1.shoppist.interactor.goods;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.repository.GoodsRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class SoftDeleteGoods extends UseCase<Boolean> {

    private final GoodsRepository mRepository;
    private Collection<ProductModel> mData;

    @Inject
    public SoftDeleteGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<ProductModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<ProductModel> toUpdate = new ArrayList<>();
            for (ProductModel product : mData) {
                product.setDirty(true);
                product.setDelete(true);
                toUpdate.add(product);
            }
            mRepository.update(toUpdate);
            return true;
        });
    }
}
