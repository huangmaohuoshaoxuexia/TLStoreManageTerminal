package com.whmaster.tl.whmaster.base;

import android.view.View;

/**
 * Created by admin on 2017/10/13.
 */

public interface PresentationLayerFunc {
    /**
     * 弹出消息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 网络请求加载框
     */
    public void showProgressDialog();

    /**
     * 隐藏网络请求加载框
     */
    public void hideProgressDialog();

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    public void showSoftKeyboard(View focusView);

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard();
}
