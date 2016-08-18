package com.justplay1.shoppist.view;

import com.justplay1.shoppist.models.Priority;

/**
 * Created by Mkhytar on 03.07.2016.
 */
public interface AddListView extends AddElementView {

    void setPriority(@Priority int priority);

    void showSelectColorDialog(int color);

    void setColorToButton(int color);

    void setRandomColor();
}
