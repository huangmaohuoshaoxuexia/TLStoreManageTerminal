package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.CancelCodePresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/11/16.
 */

public class CancelBarCodeListActivity extends BaseActivity implements IMvpView{

    private Bundle mBundle;
    private String mCode;
    private TextView mCodeText,mCancelNums;
    private Button mScanBtn,mCancelBtn;
    private LinearLayout mAlertLayout;
    private String m_Broadcastname;
    private String regEx = "^(([1-9]\\d{3})|(0\\d{2}[1-9]))(0[1-9]|1[0-2])$";
    private Pattern pattern;
    private CancelCodePresenter cancelCodePresenter;
    private ArrayList<ArrayMap<String,Object>> mCodeList = new ArrayList<>();
    private ArrayMap<String,Object> mCodeMap = new ArrayMap<>();
//    private LoginLoadingDialog loadingDialog;
    private int mCancelNum = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.cancel_code_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
//        loadingDialog = new LoginLoadingDialog(this);
        cancelCodePresenter = new CancelCodePresenter(this,this);
        if(mBundle!=null){
            mCode = mBundle.getString("code");
            mCodeText.setText("天露码："+mCode);
            mCodeMap.put("caseCode",mCode);
            mCodeList.add(mCodeMap);
            mCancelNum = 1;
        }
        pattern = Pattern.compile(regEx);
//        pattern.matcher(mCode.substring(0,6)).matches();
//        logcat(pattern.matcher(mCode.substring(0,6)).matches()+"=================");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.scan_code_btn:
                mCancelNums.setText("0");
                mCodeText.setVisibility(View.GONE);
                mAlertLayout.setVisibility(View.VISIBLE);
                mScanBtn.setVisibility(View.GONE);
                final IntentFilter intentFilter = new IntentFilter();
                m_Broadcastname = "com.barcode.sendBroadcast";// com.barcode.sendBroadcastScan
                intentFilter.addAction(m_Broadcastname);
                registerReceiver(receiver, intentFilter);
                mCodeList.clear();
                mCancelNum = 0;
                break;
            case R.id.cancel_btn:
                if(mCancelNum>0 && mCodeList.size()>0){
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("本次操作共取消"+mCancelNum+"个天露码，确认是否提交？")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showLoading();
                                    cancelCodePresenter.updateBatchByCaseCode(JSON.toJSONString(mCodeList));
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {}
                            }).show();
                }else{
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("取消单位数不能为0，请扫描天露码！")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mCodeText = findViewById(R.id.tl_code_text);
        mScanBtn = findViewById(R.id.scan_code_btn);
        mScanBtn.setOnClickListener(this);
        mCancelNums = findViewById(R.id.cancel_numbers);
        mAlertLayout = findViewById(R.id.alert_layout);
        mCancelBtn = findViewById(R.id.cancel_btn);
        mCancelBtn.setOnClickListener(this);
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
        mTitle.setText("取消条码");
    }

    @Override
    public void onFail(String errorMsg) {
    }

    @Override
    public void onSuccess(String type, Object object) {
        Toast.makeText(CancelBarCodeListActivity.this,"取消成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void showLoading() {
        msgLoadingDialog.builder().setMsg("正在取消").show();

    }

    @Override
    public void hideLoading() {
        msgLoadingDialog.dismiss();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                logcat("scancode获取扫描条形码"+str);
                if (!"".equals(str)) {
                    if(pattern.matcher(str.substring(0,6)).matches()){
                        mAlertLayout.setVisibility(View.GONE);
                        mCancelNums.setText("1");
                        mCodeText.setVisibility(View.VISIBLE);
                        mCodeText.setText("天露码："+str);
                        mScanBtn.setVisibility(View.VISIBLE);
                        mCodeMap.put("caseCode",str);
                        mCodeList.add(mCodeMap);
                        mCancelNum = 1;
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
                if(receiver!=null){
                    unregisterReceiver(receiver);
                }
            }
        }
    };
}
