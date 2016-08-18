package com.justplay1.shoppist.exception;

/**
 * Created by Mkhytar on 29.04.2016.
 */
public class DataNotFoundException extends Exception {

    public DataNotFoundException() {
    }

    public DataNotFoundException(String detailMessage) {
        super(detailMessage);
    }

    public DataNotFoundException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DataNotFoundException(Throwable throwable) {
        super(throwable);
    }
}
