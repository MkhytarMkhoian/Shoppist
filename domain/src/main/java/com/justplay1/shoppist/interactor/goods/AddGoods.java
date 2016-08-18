package com.justplay1.shoppist.interactor.goods;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.ProductModel;
import com.justplay1.shoppist.repository.GoodsRepository;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class AddGoods extends UseCase<Boolean> {

    private final GoodsRepository mRepository;
    private ProductModel mData;

    @Inject
    public AddGoods(GoodsRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(ProductModel data) {
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
