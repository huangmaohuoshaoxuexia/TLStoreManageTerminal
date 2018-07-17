package com.whmaster.tl.whmaster.base;

import android.content.Context;
import android.view.View;

/**
 * Created by admin on 2017/10/13.
 */

public class PresentationLayerFuncHelper implements PresentationLayerFunc {
    private Context context;

    public PresentationLayerFuncHelper(Context context) {
        this.context = context;
    }
    @Override
    public void showToast(String msg) {

    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showSoftKeyboard(View focusView) {

    }

    @Override
    public void hideSoftKeyboard() {

    }
}
