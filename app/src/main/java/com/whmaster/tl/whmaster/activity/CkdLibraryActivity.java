package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.view.View;

import com.whmaster.tl.whmaster.R;

/**
 * Created by admin on 2017/12/26.
 */

public class CkdLibraryActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.ckd_library_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
        mTitle.setText("货品库位选择");
    }
}
