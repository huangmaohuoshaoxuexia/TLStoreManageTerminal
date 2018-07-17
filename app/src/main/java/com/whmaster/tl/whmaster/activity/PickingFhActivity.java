package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.view.View;

import com.whmaster.tl.whmaster.R;

/**
 * Created by admin on 2017/10/23.
 */

public class PickingFhActivity extends BaseActivity{
    @Override
    protected int getLayoutId() {
        return R.layout.picking_layout;
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
    public void setHeader() {
        super.setHeader();
        mTitle.setText("拣货单复核");
    }
}
