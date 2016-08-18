package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.category.AddCategory;
import com.justplay1.shoppist.interactor.category.GetCategories;
import com.justplay1.shoppist.interactor.category.SoftDeleteCategory;
import com.justplay1.shoppist.interactor.category.UpdateCategory;
import com.justplay1.shoppist.repository.CategoryRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class CategoryModule {

    @Provides
    @PerActivity
    AddCategory provideAddCategory(CategoryRepository repository, ThreadExecutor threadExecutor,
                                   PostExecutionThread postExecutionThread) {
        return new AddCategory(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetCategories provideGetCategories(CategoryRepository repository, ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        return new GetCategories(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UpdateCategory provideUpdateCategory(CategoryRepository repository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new UpdateCategory(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SoftDeleteCategory provideSoftDeleteCategory(CategoryRepository repository, ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread) {
        return new SoftDeleteCategory(repository, threadExecutor, postExecutionThread);
    }
}
