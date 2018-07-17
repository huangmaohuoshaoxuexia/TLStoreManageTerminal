package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.LoadingPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/13.
 * 装车货品
 */

public class LoadingListActivity extends BaseActivity implements IMvpView{

    private XRecyclerView mRecyclerView;
    private ArrayList<HashMap<String,Object>> mList,mAddList;
    private RecyAdapter mAdapter;
    private Bundle mBundle;
    private String mDeliveryId;
    private int page = 1,x = 1;
    private LoadingPresenter loadingPresenter;
    private Button mSubBtn;
    @Override
    protected int getLayoutId() {
        return R.layout.loading_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingPresenter = new LoadingPresenter(this,this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mDeliveryId = mBundle.getString("deliveryId");
            loadingPresenter.getLoadingGoodsList(mDeliveryId,page);
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
                        page = 1;
                        x = 1;
                        loadingPresenter.getLoadingGoodsList(mDeliveryId,page);
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
                        loadingPresenter.getLoadingGoodsList(mDeliveryId,page);
                    }
                }, 1000);
            }
        });

    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub_btn:
                Bundle bundle = new Bundle();
                bundle.putString("deliveryId",mDeliveryId);
                openActivityForResult(ScanCodeConfirmActivity.class,0,bundle);
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.loading_list_rc);
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                mSubBtn.setVisibility(View.GONE);
                page = 1;
                x = 1;
                loadingPresenter.getLoadingGoodsList(mDeliveryId,page);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
                break;
        }
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
        mTitle.setText("装车货品");
    }

    @Override
    public void onFail(String errorMsg) {
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type){
            case "list":
                if(x==1){
                    mList = (ArrayList<HashMap<String, Object>>) object;
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }else if(x==2){
                    mAddList = (ArrayList<HashMap<String, Object>>) object;
                    mAdapter.notifiList(mAddList);
                }
                break;
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    logcat("是否显示按钮");
                    mSubBtn.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    LoadingListActivity.this).inflate(R.layout.loading_list_item_layout, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<HashMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.productName.setText("货品名称："+mList.get(position).get("productName")+"");
            holder.mPackges.setText("装车单位数："+mList.get(position).get("planPackageNum")+"");
            holder.mNums.setText("装车个数："+mList.get(position).get("planNum")+"");
            if(mList.get(position).get("loadStatus").toString().equals("10")){
                holder.mStates.setText("未扫码");
                handler.sendEmptyMessageDelayed(0,0);

                holder.mStates.setBackgroundResource(R.mipmap.bg_red);
            }else if(mList.get(position).get("loadStatus").toString().equals("20")){
                holder.mStates.setText("已扫码");
                holder.mStates.setBackgroundResource(R.mipmap.bg_green);
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, mPackges, mNums;
            Button mStates;

            public MyViewHolder(View view) {
                super(view);
                productName = view.findViewById(R.id.loading_item_name);
                mPackges = view.findViewById(R.id.loading_item_packge);
                mNums = view.findViewById(R.id.loading_item_num);
                mStates = view.findViewById(R.id.states);
            }
        }
    }
}
