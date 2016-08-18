package com.justplay1.shoppist.view;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface MenuView extends ContextView {

    void setNotificationBadge(int count);

    void setAccount(String username);

    void setDefaultAccountTitle();

    void openLogin();

    void openAccountSetting();
}
