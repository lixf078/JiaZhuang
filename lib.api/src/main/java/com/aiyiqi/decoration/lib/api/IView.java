package com.aiyiqi.decoration.lib.api;

/**
 * Created by hubing on 15/12/7.
 */
public interface IView {
    public void showLoadingView();
    public void hideLoadingView();
    public void showEmptyView();
    public void hideEmptyView();
    public void showNoNetworkView();
    public void hideNoNetworkView();
}
