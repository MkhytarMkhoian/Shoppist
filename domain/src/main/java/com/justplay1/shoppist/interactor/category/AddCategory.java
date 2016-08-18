package com.justplay1.shoppist.interactor.category;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.repository.CategoryRepository;

import java.util.UUID;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class AddCategory extends UseCase<Boolean> {

    private final CategoryRepository mRepository;
    private CategoryModel mData;

    @Inject
    public AddCategory(CategoryRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(CategoryModel data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            if (mData == null) throw new NullPointerException("CategoryModel == null");
            mData.setId(UUID.nameUUIDFromBytes((mData.getName() + UUID.randomUUID()).getBytes()).toString());
            mRepository.save(mData);
            return true;
        });
    }
}
