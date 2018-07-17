package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.view.View;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.view.IMvpView;

/**
 * Created by admin on 2017/11/8.
 */

public class PickingCheckActivity extends BaseActivity implements IMvpView{
    @Override
    protected int getLayoutId() {
        return R.layout.goods_shelves_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void initListeners() {
        super.initListeners();
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void logcat(String msg) {
        super.logcat(msg);
    }

    @Override
    public void setHeader() {
        super.setHeader();
    }

    @Override
    public void onFail(String errorMsg) {

    }

    @Override
    public void onSuccess(String type, Object object) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
