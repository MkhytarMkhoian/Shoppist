package com.justplay1.shoppist.interactor.category;

import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.UseCase;
import com.justplay1.shoppist.models.CategoryModel;
import com.justplay1.shoppist.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Mkhytar on 13.05.2016.
 */
public class SoftDeleteCategory extends UseCase<Boolean> {

    private final CategoryRepository mRepository;
    private Collection<CategoryModel> mData;

    @Inject
    public SoftDeleteCategory(CategoryRepository repository,
                              ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mRepository = repository;
    }

    public void setData(Collection<CategoryModel> data) {
        this.mData = data;
    }

    @Override
    protected Observable<Boolean> buildUseCaseObservable() {
        return Observable.fromCallable(() -> {
            List<CategoryModel> toUpdate = new ArrayList<>();
            for (CategoryModel category : mData) {
                category.setDirty(true);
                category.setDelete(true);
                toUpdate.add(category);
            }
            mRepository.update(toUpdate);
            return true;
        });
    }
}
