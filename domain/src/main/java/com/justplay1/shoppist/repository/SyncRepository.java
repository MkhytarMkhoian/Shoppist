package com.justplay1.shoppist.repository;

/**
 * Created by Mkhytar on 30.05.2016.
 */
public interface SyncRepository {

    void doSync(boolean deleteDataBefore, boolean notifyUser);
}
