package com.justplay1.shoppist.interactor.category;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.repository.CategoryRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class GetCategory extends UseCase<CategoryModel> {

    private final CategoryRepository mRepository;
    private String mId;

    @Inject
    public GetCategory(CategoryRepository repository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setId(String id) {
        this.mId = id;
    }

    @Override
    protected Observable<CategoryModel> buildUseCaseObservable() {
        return mRepository.getItem(mId);
    }
}
