package com.justplay1.shoppist.bus;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mkhytar on 29.07.2016.
 */
@Singleton
public class UiEventBus {

    private final PublishSubject<Object> mBusSubject;

    private static UiEventBus instance;

    public static UiEventBus instanceOf() {
        if (instance == null) {
            instance = new UiEventBus();
        }
        return instance;
    }

    @Inject
    public UiEventBus() {
        mBusSubject = PublishSubject.create();
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        mBusSubject.onNext(event);
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Observable<Object> observable() {
        return mBusSubject;
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return mBusSubject.filter(eventClass::isInstance)
                .map(event -> (T) event);
    }
}
