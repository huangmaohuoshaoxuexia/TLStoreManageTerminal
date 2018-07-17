package com.whmaster.tl.whmaster.activity.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.activity.BaseActivity;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.InventoryPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;
import com.whmaster.tl.whmaster.widget.SlideView2;

import java.util.ArrayList;

/**
 * Created by admin on 2018/4/3.
 * 盘点详情列表
 */

public class ChoseInvertoryDetailListActivity extends BaseActivity implements IMvpView, SlideView2.onSuccessInterface {

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private LinearLayout mTitleLayout;
    private ImageView mBackImage;
    private SlideView2 slideView2;
    private Bundle mBundle;
    private String mStocktakingId, mStocktakingType;
    private InventoryPresenter inventoryPresenter;
    private ArrayMap<String, Object> mDataMap;
    protected ArrayList<ArrayMap<String, Object>> mList;
    private TextView mOrderId, mOrderStatus, mInventoryTotalNum, mInventoryProduct, mTitleText;

    @Override
    protected int getLayoutId() {
        return R.layout.chose_invertory_detail_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        inventoryPresenter.detailList(mStocktakingId);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 1000);
            }
        });
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mStocktakingId = mBundle.getString("id");
            mStocktakingType = mBundle.getString("type");
        }
        mTitleText.setText(mStocktakingType + "详情");
        inventoryPresenter = new InventoryPresenter(this, this);
        inventoryPresenter.detailList(mStocktakingId);

    }

    @Override
    public void initViews() {
        super.initViews();
        mTitleText = findViewById(R.id.title_text);
        mOrderId = findViewById(R.id.order_name);
        mOrderStatus = findViewById(R.id.status_text);
        mInventoryTotalNum = findViewById(R.id.current_pandian);
        mInventoryProduct = findViewById(R.id.daipandian_text);
        slideView2 = findViewById(R.id.detail_list_btn);
        slideView2.setOnSuccessListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mRecyclerView = findViewById(R.id.invertory_detail_list_recyview);
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_image:
                finish();
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
    }

    @Override
    public void onExcute() {
        try {
            int sum = 0;
            for (int i = 0; i < mList.size(); i++) {
                sum = sum + Integer.parseInt(mList.get(i).get("stocktakingNum") + "");
            }
            if (sum > 0) {
                inventoryPresenter.end(mStocktakingId, sum);
            }else{
                handler.sendEmptyMessageDelayed(0,500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        logcat(resultCode+"是否回去刷新状态===" + requestCode);
        inventoryPresenter.detailList(mStocktakingId);
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
        handler.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type) {
            case "data":
                mDataMap = (ArrayMap<String, Object>) object;
                mList = Constants.getJsonArray(mDataMap.get("productList").toString());
                if (mList != null && mList.size() > 0) {
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }
                if (mDataMap.get("stocktakingCode") != null) {
                    mOrderId.setText("盘点单号：" + mDataMap.get("stocktakingCode"));
                }
                if (mDataMap.get("inventoryTotalNum") != null) {
                    if(mDataMap.get("inventoryTotalNum").toString().length()>6){
                        mInventoryTotalNum.setTextSize(12);
                    }else{
                        mInventoryTotalNum.setTextSize(14);
                    }
                    mInventoryTotalNum.setText(mDataMap.get("inventoryTotalNum") + "/");
                }
                if (mDataMap.get("pendNum") != null) {
                    if(mDataMap.get("pendNum").toString().length()>6){
                        mInventoryProduct.setTextSize(12);
                    }else{
                        mInventoryProduct.setTextSize(14);
                    }
                    mInventoryProduct.setText(mDataMap.get("pendNum") + "/");
                }

                if (mDataMap.get("stocktakingStatus") != null) {
                    if (mDataMap.get("stocktakingStatus").toString().equals("20")) {
                        mOrderStatus.setText("未盘点");
                        mOrderStatus.setBackgroundResource(R.drawable.bg_inventory_item_style1);
                        mOrderStatus.setTextColor(getResources().getColor(R.color.red));
                    } else if (mDataMap.get("stocktakingStatus").toString().equals("30")) {
                        mOrderStatus.setText("盘点中");
                        mOrderStatus.setBackgroundResource(R.drawable.bg_inventory_item_style2);
                        mOrderStatus.setTextColor(getResources().getColor(R.color.color31));
                    }
                }
                break;
            case "updateSuccess":
                String msg = (String) object;
                if(msg.equals("0")){
                    Toast.makeText(this,"盘点完成",Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    setResultOk(bundle);
                }else{
                    handler.sendEmptyMessageDelayed(0, 500);
                    onFail(msg);
                }
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    logcat("重置当前坐标");
                    slideView2.resetXy();
                    break;
            }
        }
    };
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
        public RecyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyAdapter.MyViewHolder holder = new RecyAdapter.MyViewHolder(LayoutInflater.from(
                    ChoseInvertoryDetailListActivity.this).inflate(R.layout.chose_invertory_detail_list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {

            if (mList.get(position).get("productName") != null) {
                holder.mProductName.setText(mList.get(position).get("productName").toString());
            }
            if (mList.get(position).get("stocktakingDetailStatus") != null) {
                if (mList.get(position).get("stocktakingDetailStatus").toString().equals("已完成")) {
                    holder.mOrderStatus.setTextColor(getResources().getColor(R.color.color35));
                } else if (mList.get(position).get("stocktakingDetailStatus").toString().equals("未盘点")) {
                    holder.mOrderStatus.setTextColor(getResources().getColor(R.color.red));
                }
                holder.mOrderStatus.setText(mList.get(position).get("stocktakingDetailStatus").toString());
            }

            if (mList.get(position).get("productSkuCode") != null) {
                holder.mSkuText.setText(mList.get(position).get("productSkuCode").toString());
            }
            if (mList.get(position).get("batchNo") != null) {
                holder.mNoText.setText(mList.get(position).get("batchNo").toString());
            }
            if (mList.get(position).get("consignorName") != null) {
                holder.mCargoOwnerText.setText(mList.get(position).get("consignorName").toString());
            }
            if (mList.get(position).get("regionName") != null) {
                holder.mKuquText.setText(mList.get(position).get("regionName").toString());
            }
            if (mList.get(position).get("positionName") != null) {
                holder.mKuweiText.setText(mList.get(position).get("positionName").toString());
            }

            if (mList.get(position).get("inventoryNum") != null) {
                holder.mNumbersText.setText(mList.get(position).get("inventoryNum").toString());
            }
            if (mList.get(position).get("stocktakingNum") != null) {
                holder.mInventoryNums.setText(mList.get(position).get("stocktakingNum").toString());
            }

            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("mStocktakingType", mStocktakingType);
                    bundle.putString("productName", mList.get(position).get("productName") + "");
                    bundle.putString("productSkuCode", mList.get(position).get("productSkuCode") + "");
                    bundle.putString("batchNo", mList.get(position).get("batchNo") + "");
                    bundle.putString("packageSpec", mList.get(position).get("packageSpec") + "");
                    bundle.putString("consignorName", mList.get(position).get("consignorName") + "");
                    bundle.putString("regionName", mList.get(position).get("regionName") + "");
                    bundle.putString("positionName", mList.get(position).get("positionName") + "");
                    bundle.putString("packageCount", mList.get(position).get("packageCount") + "");
                    bundle.putString("inventoryNum", mList.get(position).get("inventoryNum") + "");
                    bundle.putString("stocktakingNum", mList.get(position).get("stocktakingNum") + "");

                    bundle.putString("memo", mList.get(position).get("memo") + "");
                    bundle.putString("stocktakingDetailId", mList.get(position).get("stocktakingDetailId") + "");
                    bundle.putString("stocktakingDetailStatus", mList.get(position).get("stocktakingDetailStatus") + "");
                    openActivityForResult(ChoseInvertoryDetailActivity.class, 0, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mContentLayout;
            TextView mProductName, mOrderStatus, mSkuText, mNoText, mCargoOwnerText, mKuquText, mKuweiText, mNumbersText, mInventoryNums;

            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                mProductName = view.findViewById(R.id.product_name);
                mOrderStatus = view.findViewById(R.id.order_status);
                mSkuText = view.findViewById(R.id.sku_text);
                mNoText = view.findViewById(R.id.no_text);
                mCargoOwnerText = view.findViewById(R.id.cargo_owner_text);
                mKuquText = view.findViewById(R.id.kuqu_text);
                mKuweiText = view.findViewById(R.id.kuwei_text);
                mNumbersText = view.findViewById(R.id.current_kucun);
                mInventoryNums = view.findViewById(R.id.invertory_kucun);
            }
        }
    }
}
