package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.adapter.StorageListApter;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/10/23.
 * 入库单
 */

public class StorageActivity extends BaseActivity implements IMvpView {
    private RecyAdapter mAdapter;
    private XRecyclerView mRecyclerView;
    private StoragePresenter storagePresenter;
    private int page = 1, pageSize = 6, x = 1;
    private ArrayList<HashMap<String, Object>> mList, mAddList;
    private ImageView mSearchImage;
    private EditText mSearchEdit;
    private LinearLayout mEmptyLayout;
    private String m_Broadcastname;

    @Override
    protected int getLayoutId() {
        return R.layout.storage_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storagePresenter = new StoragePresenter(this, this);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        x = 1;
                        page = 1;
                        storagePresenter.getStorageList("", page, pageSize);
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
                        storagePresenter.getStorageList("", page, pageSize);
                    }
                }, 1000);
            }
        });
        storagePresenter.getStorageList("", page, pageSize);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.storage_search_image:
                x = 1;
                page = 1;
                storagePresenter.getStorageList(mSearchEdit.getText().toString(), page, pageSize);
                mRecyclerView.setLoadingMoreEnabled(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchImage.getWindowToken(), 0);
                break;
            case R.id.back:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast,null);
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.storage_recyview);
        mSearchImage = findViewById(R.id.storage_search_image);
        mSearchImage.setOnClickListener(this);
        mSearchEdit = findViewById(R.id.search_edit);
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
        mTitle.setText("入库单");
    }

    @Override
    public void onFail(String errorMsg) {
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type) {
            case "list":
                if (x == 1) {
                    mList = (ArrayList) object;
                    if(mList!=null && mList.size()>0){
                        mAdapter = new RecyAdapter();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                } else if (x == 2){
                    mAddList = (ArrayList) object;
                    mAdapter.notifiList(mAddList);
                    mRecyclerView.loadMoreComplete();
                    if (mAddList.size() < pageSize) {
                        mRecyclerView.setLoadingMoreEnabled(false);
                    }else{
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                x = 1;
                page = 1;
                storagePresenter.getStorageList("", page, pageSize);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast,null);
                finish();
                break;
        }
        return true;
    }
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    StorageActivity.this).inflate(R.layout.storage_list_item, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<HashMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (mList.get(position).get("stockInCode") != null) {
                holder.orderName.setText("入库单编号：" + mList.get(position).get("stockInCode").toString());
            }
            if (mList.get(position).get("executeStatus") != null) {
                holder.mStates.setText(mList.get(position).get("executeStatus").toString());
                if (mList.get(position).get("executeStatus").toString().equals("未执行")) {
                    holder.mStates.setBackgroundResource(R.mipmap.bg_red);
                } else if (mList.get(position).get("executeStatus").toString().equals("执行中")) {
                    holder.mStates.setBackgroundResource(R.mipmap.bg_orange);
                } else {
                    holder.mStates.setBackgroundResource(R.mipmap.bg_green);
                }
            }
            if (mList.get(position).get("noShelfNum") != null) {
                holder.mPendingUpText.setText("待上架零散数：" + mList.get(position).get("noShelfNum").toString());
            }
            if (mList.get(position).get("noShelfPackageNum") != null) {
                holder.mAllPendingUpText.setText("待上架整数：" + mList.get(position).get("noShelfPackageNum").toString());
            }
            if (mList.get(position).get("shelfAlreadyNum") != null) {
                holder.mRackupText.setText("已上架零散数：" + mList.get(position).get("shelfAlreadyNum").toString());
            }
            if (mList.get(position).get("shelfAlreadyPackageNum") != null) {
                holder.mAllRackpUpText.setText("已上架整数：" + mList.get(position).get("shelfAlreadyPackageNum").toString());
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("stockInId", mList.get(position).get("stockInId")+"");
                    bundle.putString("orderInId", mList.get(position).get("orderInId")+"");
                    openActivityForResult(StorageGoodsActivity.class,0,bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView orderName, mAllRackpUpText, mAllPendingUpText, mRackupText, mPendingUpText;
            private Button mStates;
            LinearLayout mContentLayout;

            public MyViewHolder(View view) {
                super(view);
                orderName = view.findViewById(R.id.order_name);
                mContentLayout = view.findViewById(R.id.content_layout);
                mStates = view.findViewById(R.id.item_state);
                mAllRackpUpText = view.findViewById(R.id.already_packge_text);
                mAllPendingUpText = view.findViewById(R.id.no_packge_text);
                mRackupText = view.findViewById(R.id.already_num_text);
                mPendingUpText = view.findViewById(R.id.no_num_text);
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
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    logcat("获取扫描条形码" + str);
                    x = 1;
                    page = 1;
                    storagePresenter.getStorageList(str, page, pageSize);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
            }
        }
    };
}
