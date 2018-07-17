package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.LibraryPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;
import java.util.ArrayList;

/**
 * Created by admin on 2017/11/14.
 */

public class ChooseNewLibraryActivity extends BaseActivity implements IMvpView{

    private Bundle mBundle;
    private ArrayList<ArrayMap<String,Object>> mList;
    private String mOldPositionCode,mNewCode;
    private LibraryPresenter libraryPresenter;
    private Button mTransferIsCompletedBtn;
    private EditText mNewCodeEdit,mRemarks;
    private ArrayMap<String,Object> mEntityMap;
    private String m_Broadcastname;

    @Override
    protected int getLayoutId() {
        return R.layout.choose_new_library_layout;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        libraryPresenter = new LibraryPresenter(this,this);
        if(mBundle!=null){
            mList = (ArrayList<ArrayMap<String, Object>>) mBundle.getSerializable("list");
            mOldPositionCode = mBundle.getString("code");
        }
//        mNewCodeEdit.setText("CK510112A-I01-18");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub_btn:
                mNewCode = mNewCodeEdit.getText().toString();

                if(!mNewCode.equals("")){
                    if(!mNewCode.equals(mOldPositionCode)){
                        mEntityMap = new ArrayMap<>();
                        mEntityMap.put("list",mList);
                        mEntityMap.put("oldPositionCode",mOldPositionCode);
                        mEntityMap.put("newPositionCode",mNewCode);
                        mEntityMap.put("memo",mRemarks.getText().toString());
//                        libraryPresenter.checkIsSameWharehouseByCode(mOldPositionCode,mNewCode);
                    }else{
                        Toast.makeText(this,"新库位码不能和旧库位码相同",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"请扫描库位码或输入库位编码",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mTransferIsCompletedBtn = findViewById(R.id.sub_btn);
        mTransferIsCompletedBtn.setOnClickListener(this);
        mNewCodeEdit = findViewById(R.id.new_code_edit);
        mRemarks = findViewById(R.id.remark_edit);
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
        mTitle.setText("选择新库位 ");
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
            case "isSameSuccess":
                mEntityMap = new ArrayMap<>();
//                mList = new ArrayList<>();
                mEntityMap.put("list",mList);
                mEntityMap.put("oldPositionCode",mOldPositionCode);
                mEntityMap.put("newPositionCode",mNewCode);
                mEntityMap.put("memo",mRemarks.getText().toString());
                break;
            case "complete":
                Toast.makeText(ChooseNewLibraryActivity.this,"移库成功",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {
        loadingDialog.builder().show();
    }

    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
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
                    mNewCode = str;
//                    mNewCode = "CK510112A-J01-200";
                    mNewCodeEdit.setText(mNewCode);
                    logcat("获取获取扫描条形码" + str);
                }else{
                    Toast.makeText(ChooseNewLibraryActivity.this,"请扫描正确的条形码！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
