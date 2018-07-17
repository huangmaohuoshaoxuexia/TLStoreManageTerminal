package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.LibraryPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

/**
 * Created by admin on 2017/11/16.
 * 扫描库位码
 */

public class ScanLibraryActivity extends BaseActivity implements IMvpView{

    private LinearLayout mScanLayout;
    private LibraryPresenter libraryPresenter;
    private String mPositionCode = "";
    private String m_Broadcastname;
    @Override
    protected int getLayoutId() {
        return R.layout.scan_library_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libraryPresenter = new LibraryPresenter(this,this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.scan_library_layout:
//                Bundle bundle = new Bundle();
//                bundle.putString("code","KW-jhx1-5544-5-5-4-4");
//                startActivity(ScanLibraryListsActivity.class,bundle);
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mScanLayout = findViewById(R.id.scan_library_layout);
        mScanLayout.setOnClickListener(this);
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
        mTitle.setText("扫描库位码");
    }

    @Override
    public void onFail(String errorMsg) {
        mAlertDialog.builder().setTitle("提示")
                .setMsg(errorMsg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type){
            case "success":
                Bundle bundle = new Bundle();
                bundle.putString("code", mPositionCode);
                startActivity(ScanLibraryListsActivity.class, bundle);
                break;
        }
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
                    mPositionCode = str;
                    logcat("获取获取扫描条形码" + str);
//                    libraryPresenter.isPermission(mPositionCode);

                    Bundle bundle = new Bundle();
                    bundle.putString("code", mPositionCode);
                    startActivity(ScanLibraryListsActivity.class, bundle);

                }else{
                    Toast.makeText(ScanLibraryActivity.this,"请扫描正确的条形码！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
