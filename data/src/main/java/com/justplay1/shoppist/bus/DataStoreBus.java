package com.justplay1.shoppist.bus;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by Mkhytar on 27.04.2016.
 */
public class DataStoreBus {

    private static DataStoreBus instance;

    private PublishSubject<DataChangedEvent> subject = PublishSubject.create();

    public static DataStoreBus instanceOf() {
        if (instance == null) {
            instance = new DataStoreBus();
        }
        return instance;
    }

    /**
     * Pass any event down to event listeners.
     */
    public void notify(DataChangedEvent object) {
        subject.onNext(object);
    }

    /**
     * Subscribe to this Observable. On event, do something
     * e.g. replace a fragment
     */
    public Observable<DataChangedEvent> getEvents() {
        return subject;
    }
}
