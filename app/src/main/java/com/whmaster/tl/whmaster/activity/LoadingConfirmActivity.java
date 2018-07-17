package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.view.IMvpView;

/**
 * Created by admin on 2017/10/23.
 * 装车确认
 */

public class LoadingConfirmActivity extends BaseActivity implements IMvpView{

    private String m_Broadcastname;
    private LinearLayout mContentLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.loading_confirm_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.loading_layout:
                Bundle bundle = new Bundle();
                bundle.putString("code","TC20171009000002");
                startActivity(LoadingConfirmListActivity.class,bundle);
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mContentLayout = findViewById(R.id.loading_layout);
        mContentLayout.setOnClickListener(this);
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
        mTitle.setText("装车确认");
    }
    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        m_Broadcastname = "com.barcode.sendBroadcast";// com.barcode.sendBroadcastScan
        intentFilter.addAction(m_Broadcastname);
        registerReceiver(receiver, intentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    logcat("获取扫描条形码"+str);
                    Bundle bundle = new Bundle();
                    bundle.putString("code",str);
                    startActivity(LoadingConfirmListActivity.class,bundle);
                }
            }
        }
    };
}
