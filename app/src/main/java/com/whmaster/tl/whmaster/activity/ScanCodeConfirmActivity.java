package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.LoadingPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/14.
 * 扫码确认
 */

public class ScanCodeConfirmActivity extends BaseActivity implements IMvpView{

    private ImageView mUpDownImage;
    private LinearLayout mProductNameLayout,mProductSkuLayout;
    private Bundle mBundle;
    private String mDeliveryId,vehicleTripId;
    private int page = 1,x = 1;
    private LoadingPresenter loadingPresenter;
    private XRecyclerView mRecyclerView;
    private ArrayList<ArrayMap<String,Object>> mproductList,mAddList;
    private RecyAdapter mAdapter;
    private TextView mTlCode,mProductName,mProdecuSku;
    private String m_Broadcastname,mCodeStr;
    private Button mScanBtn;
    private EditText mTlCodeEdit;
    private ArrayMap<String,Object> mProductMap,mDeliveryInfo,mDelivery;
    private boolean isExist = false;
    private ArrayList<String> caseCodeList,isEqual;

    @Override
    protected int getLayoutId() {
        return R.layout.scan_code_confirm_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBundle = getIntent().getExtras();
        loadingPresenter = new LoadingPresenter(this,this);
        if(mBundle!=null){
            mDeliveryId = mBundle.getString("deliveryId");
            loadingPresenter.getscanCompletedList(mDeliveryId,page);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = page+1;
                        x = 1;
                        loadingPresenter.getscanCompletedList(mDeliveryId,page);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page++;
                        x = 2;
                        loadingPresenter.getscanCompletedList(mDeliveryId,page);
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.up_down_image:
                if(mProductNameLayout.getVisibility()==View.VISIBLE){
                    mProductNameLayout.setVisibility(View.GONE);
                    mProductSkuLayout.setVisibility(View.GONE);
                    mUpDownImage.setImageResource(R.mipmap.icondown);
                }else{
                    mProductNameLayout.setVisibility(View.VISIBLE);
                    mProductSkuLayout.setVisibility(View.VISIBLE);
                    mUpDownImage.setImageResource(R.mipmap.iconup);
                }
                break;
            case R.id.sub_btn:
                isEqual = new ArrayList<>();
                mDelivery = new ArrayMap<>();
                for(int i=0;i<mproductList.size();i++){
                    if(((ArrayList)mproductList.get(i).get("caseCodeList")).size() == Integer.parseInt(mproductList.get(i).get("actPackageNum").toString())){
                        isEqual.add("true");
                    }
                }
                if(isEqual.size() == mproductList.size()){
                    mDelivery.put("deliveryId",mDeliveryId);
                    mDelivery.put("dcDeliveryDetail",mproductList);
                    mDelivery.put("deliveryStatus",mDeliveryInfo.get("deliveryStatus")+"");
                    mDelivery.put("loadStatus",mDeliveryInfo.get("loadStatus")+"");
                    loadingPresenter.scanCompleted(JSON.toJSONString(mDelivery));
                }
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mTlCodeEdit = findViewById(R.id.scan_code_edit);
        mUpDownImage = findViewById(R.id.up_down_image);
        mUpDownImage.setOnClickListener(this);
        mProductNameLayout = findViewById(R.id.product_name_layout);
        mProductSkuLayout = findViewById(R.id.product_sku_layout);
        mRecyclerView = findViewById(R.id.scan_code_list_view);
        mTlCode = findViewById(R.id.scan_code);
        mProductName = findViewById(R.id.scan_code_name);
        mProdecuSku = findViewById(R.id.scan_code_sku);
        mScanBtn = findViewById(R.id.sub_btn);
        mScanBtn.setOnClickListener(this);
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
        mTitle.setText("扫码确认");
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
            case "list":
                ArrayMap<String,Object> map = (ArrayMap<String, Object>) object;
                mDeliveryInfo = Constants.getJsonObject(map.get("deliveryInfo").toString());
                vehicleTripId = mDeliveryInfo.get("vehicleTripId").toString();
                caseCodeList = new ArrayList<>();
                if(x==1){
                    mproductList = Constants.getJsonArray(Constants.getJsonObject(map.get("deliveryDetailList").toString()).get("records").toString());
                    for(int i=0;i<mproductList.size();i++){
                        mproductList.get(i).put("actEntruckPackageNum","0");
                        mproductList.get(i).put("caseCodeList",caseCodeList);
                    }
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                    mProductName.setText("货品名称："+mproductList.get(0).get("productName")+"");
                    mProdecuSku.setText("货品SKU码："+mproductList.get(0).get("productSku")+"");
                }else if(x==2){
                    mAddList = Constants.getJsonArray(Constants.getJsonObject(map.get("deliveryDetailList").toString()).get("records").toString());
                    for(int i=0;i<mAddList.size();i++){
                        mAddList.get(i).put("actEntruckPackageNum","0");
                        mAddList.get(i).put("caseCodeList",caseCodeList);
                    }
                    mAdapter.notifiList(mAddList);
                }
                break;
            case "scanCompleted":
                finish();
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ScanCodeConfirmActivity.this).inflate(R.layout.scan_code_list_item, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mproductList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.productName.setText("货品名称："+mproductList.get(position).get("productName")+"");
            if(mproductList.get(position).get("actPackageNum")!=null) {
                holder.mPlanPackges.setText("计划装车单位数：" + mproductList.get(position).get("actPackageNum") + "");
            }else{
                holder.mSjNums.setText("计划装车单位数："+"0");
            }
            if(mproductList.get(position).get("actEntruckPackageNum")!=null){
                holder.mSjNums.setText("实际装车单位数："+mproductList.get(position).get("actEntruckPackageNum")+"");
            }else{
                holder.mSjNums.setText("实际装车单位数："+"0");
            }
        }

        @Override
        public int getItemCount() {
            return mproductList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, mPlanPackges, mSjNums;
            public MyViewHolder(View view) {
                super(view);
                productName = view.findViewById(R.id.product_name);
                mPlanPackges = view.findViewById(R.id.plan_packge);
                mSjNums = view.findViewById(R.id.sj_packge);
            }
        }
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
                    logcat("scancode获取扫描条形码20170912300000000000001"+str);
                    mCodeStr = "RKD20171109000004";
                    mTlCodeEdit.setText(mCodeStr);
                    getProductInfo("RKD20171109000004");
                }
            }
        }
    };
    private void getProductInfo(String code){
        String tempString = code.substring(6);
        String productSku = tempString.substring(0,tempString.length()-6);
        for(int i=0;i<mproductList.size();i++){
            if(productSku.equals(mproductList.get(i).get("productSku").toString())){//存在的天露码
                isExist = true;
                mProductMap.put("name",mproductList.get(i).get("productName").toString());
                mProductMap.put("sku",mproductList.get(i).get("productSku").toString());
            }
            for(int j = 0; j<((ArrayList)mproductList.get(i).get("caseCodeList")).size(); j++){//判定是否扫过天露码
                if(code.equals(((ArrayList)mproductList.get(i).get("caseCodeList")).get(j))){
                    Toast.makeText(ScanCodeConfirmActivity.this,"已扫过该天露码",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(Integer.parseInt(mproductList.get(i).get("actEntruckPackageNum").toString()) >= Integer.parseInt(mproductList.get(i).get("actPackageNum").toString())){
                Toast.makeText(ScanCodeConfirmActivity.this,"实际装车单位数与计划装车单位数一致",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ScanCodeConfirmActivity.this,"扫码成功",Toast.LENGTH_SHORT).show();
                mproductList.get(i).put("actEntruckPackageNum",(Integer.parseInt(mproductList.get(i).get("actEntruckPackageNum").toString())+1)+"");
                ((ArrayList)mproductList.get(i).get("caseCodeList")).add(code);
            }
        }
    }
}
