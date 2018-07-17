package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.LoadingPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/14.
 * 装车确认
 */

public class LoadingConfirmListActivity extends BaseActivity implements IMvpView{

    private ImageView mUpImage;
    private LinearLayout mItemLayout;
    private int page = 1,x = 1;
    private Bundle mBundle;
    private String mVehicleTripCode;
    private LoadingPresenter loadingPresenter;
    private ArrayList<ArrayMap<String,Object>> mDeliverList,mAddList;
    private ArrayMap<String,Object> mDistributionTripInfoMap;
    private XRecyclerView mRecyclerView;
    private TextView mPlateNumber,mDriverName,mDriverPhone,mNumbers,mFailText;
    private RecyAdapter mAdapter;
    private boolean isComplete = true;
    private Button mSubBtn;
    private LinearLayout mEmptyLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.loading_confirm_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingPresenter = new LoadingPresenter(this,this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mVehicleTripCode = mBundle.getString("code");
            loadingPresenter.getLoadingList(mVehicleTripCode,page);
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
                        x = 1;
                        page = 1;
                        loadingPresenter.getLoadingList(mVehicleTripCode,page);
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
                        loadingPresenter.getLoadingList(mVehicleTripCode,page);
                    }
                }, 1000);
            }
        });
    }
    @Override
    public void initViews() {
        super.initViews();
        mPlateNumber = findViewById(R.id.license_plate_number);
        mDriverName = findViewById(R.id.driver_name);
        mDriverPhone = findViewById(R.id.driver_phone);
        mNumbers = findViewById(R.id.text_number);
        mUpImage = findViewById(R.id.up_down_image);
        mUpImage.setOnClickListener(this);
        mItemLayout = findViewById(R.id.item_layout);
        mRecyclerView = findViewById(R.id.loading_confirm_list_view);
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mFailText = findViewById(R.id.empty_text);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.up_down_image:
                if(mItemLayout.getVisibility()==View.VISIBLE){
                    mItemLayout.setVisibility(View.GONE);
                    mUpImage.setImageResource(R.mipmap.icondown);
                }else{
                    mItemLayout.setVisibility(View.VISIBLE);
                    mUpImage.setImageResource(R.mipmap.iconup);
                }
                break;
            case R.id.sub_btn:
                if(isComplete){
                    showLoading();
                    loadingPresenter.loadCompleted(mDistributionTripInfoMap.get("vehicleTripId")+"");
                }else{
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("仍有未扫码配送单！")
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
        mTitle.setText("装车确认");
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
        mSubBtn.setVisibility(View.GONE);
    }

    @Override
    public void onSuccess(String type, Object object) {
        mSubBtn.setVisibility(View.VISIBLE);
        switch (type){
            case "list":
                mEmptyLayout.setVisibility(View.GONE);
                ArrayMap<String,Object> map = (ArrayMap<String, Object>) object;
                ArrayMap<String,Object> mDeliverMap;
                mDeliverMap = Constants.getJsonObject(map.get("deliveryList").toString());
                mDistributionTripInfoMap = Constants.getJsonObject(Constants.getJsonObject(map.get("distributionTripInfo").toString()).get("result").toString());
                mPlateNumber.setText("车牌号："+mDistributionTripInfoMap.get("plateNo")+"");
                mDriverName.setText("司机："+mDistributionTripInfoMap.get("driverName")+"");
                mDriverPhone.setText("联系方式："+mDistributionTripInfoMap.get("driverPhone")+"");
                mNumbers.setText(mDeliverMap.get("total")+"");
                if(x==1){
                    mDeliverList = Constants.getJsonArray(mDeliverMap.get("records").toString());
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }else if(x==2){
                    mAddList = Constants.getJsonArray(Constants.getJsonObject(map.get("deliveryList").toString()).get("records").toString());
                    mAdapter.notifiList(mAddList);
                }

                break;
            case "success":
                Toast.makeText(LoadingConfirmListActivity.this,"装车成功",Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                isComplete = true;
                x = 1;
                page = 1;
                loadingPresenter.getLoadingList(mVehicleTripCode,page);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
                break;
        }
    }
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    LoadingConfirmListActivity.this).inflate(R.layout.loading_confirm_list_item, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mDeliverList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (mDeliverList.get(position).get("deliveryCode") != null) {
                holder.orderName.setText("配送单编号：" + mDeliverList.get(position).get("deliveryCode")+"");
            }
            if (mDeliverList.get(position).get("loadStatus") != null) {
                if(mDeliverList.get(position).get("loadStatus").toString().equals("10")){
                    holder.mStates.setText("未扫码");
                    holder.mStates.setBackgroundResource(R.mipmap.bg_red);
                    isComplete = false;
                }else if(mDeliverList.get(position).get("loadStatus").toString().equals("20")){
                    holder.mStates.setText("已扫码");
                    holder.mStates.setBackgroundResource(R.mipmap.bg_green);
                }
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("deliveryId",mDeliverList.get(position).get("deliveryId")+"");
                    openActivityForResult(LoadingListActivity.class,0,bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDeliverList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView orderName;
            private Button mStates;
            private LinearLayout mContentLayout;
            public MyViewHolder(View view) {
                super(view);
                orderName = view.findViewById(R.id.loading_item_orderid);
                mStates = view.findViewById(R.id.loading_item_states);
                mContentLayout = view.findViewById(R.id.content_layout);
            }
        }
    }
}
