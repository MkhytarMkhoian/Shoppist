package com.justplay1.shoppist.view.component.actionmode;

/**
 * Created by Mkhytar on 28.07.2016.
 */
public interface ActionModeInteractionListener {

    boolean isActionModeShowing();

    void closeActionMode();

    void openActionMode(int count);

    void updateActionMode(int count);
}
