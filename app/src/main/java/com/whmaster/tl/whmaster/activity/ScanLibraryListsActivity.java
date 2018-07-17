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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.LibraryPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/10/23.
 * 库位库存
 */

public class ScanLibraryListsActivity extends BaseActivity implements IMvpView{

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private Bundle mBundle;
    private String mCode;
    private LibraryPresenter libraryPresenter;
    private ArrayList<ArrayMap<String,Object>> mList;
    private TextView mLibraryText;
    private LinearLayout mEmptyLayout;
    private String m_Broadcastname;
    @Override
    protected int getLayoutId() {
        return R.layout.scan_library_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        libraryPresenter = new LibraryPresenter(this,this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mCode = mBundle.getString("code");
            mLibraryText.setText("库位编码："+mCode);
        }
        RecyclerUtil.init(mRecyclerView,this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.scan_recy_view);
        mLibraryText = findViewById(R.id.library_code);
        mEmptyLayout = findViewById(R.id.empty_layout);
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
        mTitle.setText("库位库存");
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
        mList = (ArrayList<ArrayMap<String, Object>>) object;
        if(mList!=null && mList.size()>0){
            mAdapter = new RecyAdapter();
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyLayout.setVisibility(View.GONE);
        }else{
            mRecyclerView.setVisibility(View.GONE);
            mEmptyLayout.setVisibility(View.VISIBLE);
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

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ScanLibraryListsActivity.this).inflate(R.layout.scan_library_item_layout, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if(mList.get(position).get("productName")!=null){
                holder.productName.setText("货品名称："+mList.get(position).get("productName").toString());
            }
            if(mList.get(position).get("productSku")!=null){
                holder.productSku.setText("货品SKU码："+mList.get(position).get("productSku").toString());
            }
            if(mList.get(position).get("batchNo")!=null){
                holder.productNumber.setText("批次号："+mList.get(position).get("batchNo").toString());
            }
            if(mList.get(position).get("consignorName")!=null){
                holder.userName.setText("货主名称："+mList.get(position).get("consignorName").toString());
            }
//            if(mList.get(position).get("inventoryPackageNum")!=null){
//                holder.packges.setText("库存整数："+mList.get(position).get("inventoryPackageNum").toString());
//            }
            if(mList.get(position).get("inventoryNum")!=null){
                holder.nums.setText("库存数量："+mList.get(position).get("inventoryNum").toString());
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, productSku, productNumber, userName, nums;
            public MyViewHolder(View view) {
                super(view);
                productName = view.findViewById(R.id.product_name);
                productSku = view.findViewById(R.id.product_sku);
                productNumber = view.findViewById(R.id.product_number);
                userName = view.findViewById(R.id.user_name);
                nums = view.findViewById(R.id.nums);
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
            logcat("获取获取扫描条形码" + arg1);
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    mCode = str;
                    mLibraryText.setText("库位编码："+mCode);
                    logcat("获取获取扫描条形码" + str);

                }else{
                    Toast.makeText(ScanLibraryListsActivity.this,"请扫描正确的条形码！",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
