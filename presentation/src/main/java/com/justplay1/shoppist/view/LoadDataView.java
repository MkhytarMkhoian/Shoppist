package com.justplay1.shoppist.view;

/**
 * Interface representing a View that will use to load data.
 */
public interface LoadDataView extends BaseMvpView {
    /**
     * Show a view with a progress bar indicating a loading process.
     */
    void showLoading();

    /**
     * Hide a loading view.
     */
    void hideLoading();

    /**
     * Show an error message
     *
     * @param message A string representing an error.
     */
    void showError(String message);
}
