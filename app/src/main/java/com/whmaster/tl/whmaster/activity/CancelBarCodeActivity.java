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

import java.util.regex.Pattern;

/**
 * Created by admin on 2017/10/23.
 * 取消条码
 */

public class CancelBarCodeActivity extends BaseActivity implements IMvpView {
    private LinearLayout mContentLayout;
    private String mCode = "TL20170706C1T24387";
    private String m_Broadcastname;
    private String regEx = "^(([1-9]\\d{3})|(0\\d{2}[1-9]))(0[1-9]|1[0-2])$";
    private Pattern pattern;
    @Override
    protected int getLayoutId() {
        return R.layout.cancel_bar_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pattern = Pattern.compile(regEx);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.content_layout:
//                Bundle bundle = new Bundle();
//                bundle.putString("code", mCode);
//                startActivity(CancelBarCodeListActivity.class, bundle);
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mContentLayout = findViewById(R.id.content_layout);
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
        mTitle.setText("取消条码");
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
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    logcat("获取扫描条形码" + str);
                    if(pattern.matcher(str.substring(0,6)).matches()){
                        Bundle bundle = new Bundle();
                        bundle.putString("code", str);
                        startActivity(CancelBarCodeListActivity.class, bundle);
                    }else{
                        mAlertDialog.builder().setTitle("提示")
                                .setMsg("请扫描天露码")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                }
            }
        }
    };
}
