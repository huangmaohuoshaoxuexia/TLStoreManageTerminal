package com.whmaster.tl.whmaster.view;

/**
 * Created by admin on 2017/10/13.
 */

public interface IMvpView {
    void onFail(String errorMsg);

    void onSuccess(String type, Object object);

    void showLoading();

    void hideLoading();
}
