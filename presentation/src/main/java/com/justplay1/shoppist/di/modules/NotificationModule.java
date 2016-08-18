package com.justplay1.shoppist.di.modules;

import com.justplay1.shoppist.di.scope.PerActivity;
import com.justplay1.shoppist.executor.PostExecutionThread;
import com.justplay1.shoppist.executor.ThreadExecutor;
import com.justplay1.shoppist.interactor.notification.ClearNotification;
import com.justplay1.shoppist.interactor.notification.DeleteNotifications;
import com.justplay1.shoppist.interactor.notification.GetNewNotificationCount;
import com.justplay1.shoppist.interactor.notification.GetNewNotifications;
import com.justplay1.shoppist.interactor.notification.GetNotifications;
import com.justplay1.shoppist.repository.NotificationRepository;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Mkhytar on 14.05.2016.
 */
@Module
public class NotificationModule {

    @Provides
    @PerActivity
    ClearNotification provideClearNotification(NotificationRepository repository,
                                               ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new ClearNotification(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    DeleteNotifications provideDeleteNotifications(NotificationRepository repository,
                                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new DeleteNotifications(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetNewNotificationCount provideGetNewNotificationCount(NotificationRepository repository,
                                                           ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetNewNotificationCount(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetNewNotifications provideGetNewNotifications(NotificationRepository repository,
                                                   ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetNewNotifications(repository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetNotifications provideGetNotification(NotificationRepository repository,
                                            ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetNotifications(repository, threadExecutor, postExecutionThread);
    }

}
