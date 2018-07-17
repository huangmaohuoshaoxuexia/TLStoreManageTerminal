package com.whmaster.tl.whmaster.activity.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.activity.BaseActivity;
import com.whmaster.tl.whmaster.presenter.InventoryPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;


/**
 * Created by admin on 2018/4/3.
 * 盘点选择
 */

public class InventoryChoseActivity extends BaseActivity implements IMvpView{

    private XRecyclerView mRecyclerView;
    private TextView mTitleText;
    private RecyAdapter mAdapter;
    private LinearLayout mTitleLayout;
    private ImageView mBackImage;
    private InventoryPresenter inventoryPresenter;
    private String mStocktakingType="";
    private ArrayList<ArrayMap<String,Object>> mList,mAddList;
    private int x = 1,page = 1;
    private RelativeLayout mAllLayout,mOverallLayout,mChoseLayout;
    private TextView mAllText,mOverallText,mChoseText;
    private View mAllView,mOverallView,mChoseView;
    private LinearLayout mEmptyLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.inventory_chose_layout;
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
                        x = 1;
                        page = 1;
                        inventoryPresenter.inventoryList(mStocktakingType,page);
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
                        inventoryPresenter.inventoryList(mStocktakingType,page);
                    }
                }, 1000);
            }
        });
        inventoryPresenter = new InventoryPresenter(this,this);
        inventoryPresenter.inventoryList(mStocktakingType,page);
//        mAdapter = new RecyAdapter();
//        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initViews() {
        super.initViews();
        mEmptyLayout = findViewById(R.id.empty_layout);
        mAllLayout = findViewById(R.id.title_layout1);
        mOverallLayout = findViewById(R.id.title_layout2);
        mChoseLayout = findViewById(R.id.title_layout3);
        mAllLayout.setOnClickListener(this);
        mOverallLayout.setOnClickListener(this);
        mChoseLayout.setOnClickListener(this);

        mAllLayout = findViewById(R.id.title_layout1);
        mOverallLayout = findViewById(R.id.title_layout2);
        mChoseLayout = findViewById(R.id.title_layout3);

        mAllText = findViewById(R.id.tv_title);
        mOverallText = findViewById(R.id.tv_title2);
        mChoseText = findViewById(R.id.tv_title3);

        mAllView = findViewById(R.id.title_view1);
        mOverallView = findViewById(R.id.title_view2);
        mChoseView = findViewById(R.id.title_view3);

        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mTitleText = findViewById(R.id.tv_title);
        mRecyclerView = findViewById(R.id.inventory_list_recyview);
    }
    private void initBack(){
        mAllText.setTextColor(getResources().getColor(R.color.color30));
        mAllView.setVisibility(View.GONE);
        mOverallText.setTextColor(getResources().getColor(R.color.color30));
        mOverallView.setVisibility(View.GONE);
        mChoseText.setTextColor(getResources().getColor(R.color.color30));
        mChoseView.setVisibility(View.GONE);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_layout1:
                initBack();
                mAllView.setVisibility(View.VISIBLE);
                mAllText.setTextColor(getResources().getColor(R.color.white));
                x = 1;
                page = 1;
                mStocktakingType = "";
                inventoryPresenter.inventoryList(mStocktakingType,page);
                break;
            case R.id.title_layout2:
                initBack();
                mOverallView.setVisibility(View.VISIBLE);
                mOverallText.setTextColor(getResources().getColor(R.color.white));
                x = 1;
                page = 1;
                mStocktakingType = "20";
                inventoryPresenter.inventoryList(mStocktakingType,page);
                break;
            case R.id.title_layout3:
                initBack();
                mChoseView.setVisibility(View.VISIBLE);
                mChoseText.setTextColor(getResources().getColor(R.color.white));
                x = 1;
                page = 1;
                mStocktakingType = "10";
                inventoryPresenter.inventoryList(mStocktakingType,page);
                break;
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
    public void onFail(String errorMsg) {

    }

    @Override
    public void onSuccess(String type, Object object) {
        mList = (ArrayList<ArrayMap<String, Object>>) object;
        if (x == 1) {
            mList = (ArrayList) object;
            if (mList != null && mList.size() > 0) {
                if(mAdapter==null){
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    mAdapter.notifyDataSetChanged();
                }
                mEmptyLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }else{
                mEmptyLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
            if (mList.size() < 10) {
                mRecyclerView.setLoadingMoreEnabled(false);
            }else{
                mRecyclerView.setLoadingMoreEnabled(true);
            }

        } else if (x == 2) {
            mAddList = (ArrayList) object;
            mAdapter.notifiList(mAddList);
            mRecyclerView.loadMoreComplete();
            if (mAddList.size() < 10) {
                mRecyclerView.setLoadingMoreEnabled(false);
            } else {
                mRecyclerView.setLoadingMoreEnabled(true);
            }
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
//        logcat(resultCode+"是否回去刷新状态===" + requestCode);
        x = 1;
        page = 1;
        inventoryPresenter.inventoryList(mStocktakingType,page);
        mRecyclerView.refreshComplete();
        mRecyclerView.setLoadingMoreEnabled(true);
    }


    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {

        @Override
        public RecyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyAdapter.MyViewHolder holder = new RecyAdapter.MyViewHolder(LayoutInflater.from(
                    InventoryChoseActivity.this).inflate(R.layout.inventory_list_item, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {

            if(mList.get(position).get("stocktakingCode")!=null){
                holder.mOrderName.setText("盘点单号："+mList.get(position).get("stocktakingCode"));
            }
            if(mList.get(position).get("stocktakingType")!=null){
                if(mList.get(position).get("stocktakingType").toString().equals("10")){
                    holder.mOrderQx.setText("选");
                    holder.mOrderQx.setBackgroundResource(R.mipmap.label_select);
                }else if(mList.get(position).get("stocktakingType").toString().equals("20")){
                    holder.mOrderQx.setText("全");
                    holder.mOrderQx.setBackgroundResource(R.mipmap.label_overall);
                }
            }
            if(mList.get(position).get("warehouseName")!=null){
                holder.mWarehouse.setText(""+mList.get(position).get("warehouseName"));
            }
            if(mList.get(position).get("inventoryTotalNum")!=null){
                holder.mNumbers.setText(""+mList.get(position).get("inventoryTotalNum"));
            }
            if(mList.get(position).get("stocktakingTotalNum")!=null){
                holder.mStock.setText(""+mList.get(position).get("stocktakingTotalNum"));
            }
            if(mList.get(position).get("stocktakingStatus")!=null){
                if(mList.get(position).get("stocktakingStatus").toString().equals("20")){
                    holder.mOrderStatus.setText("未盘点");
                    holder.mOrderStatus.setBackgroundResource(R.drawable.bg_inventory_item_style1);
                    holder.mOrderStatus.setTextColor(getResources().getColor(R.color.red));
                }else if(mList.get(position).get("stocktakingStatus").toString().equals("30")){
                    holder.mOrderStatus.setText("盘点中");
                    holder.mOrderStatus.setBackgroundResource(R.drawable.bg_inventory_item_style2);
                    holder.mOrderStatus.setTextColor(getResources().getColor(R.color.color31));
                }
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type",mList.get(position).get("stocktakingTypeName")+"");
                    bundle.putString("id",mList.get(position).get("stocktakingId")+"");
                    openActivityForResult(ChoseInvertoryDetailListActivity.class, 0, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LinearLayout mContentLayout;
            private TextView mOrderName,mOrderQx,mWarehouse,mNumbers,mStock,mOrderStatus;
            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                mOrderName = view.findViewById(R.id.order_name);
                mOrderQx = view.findViewById(R.id.order_quan_xuan);
                mWarehouse = view.findViewById(R.id.translate_name);
                mNumbers = view.findViewById(R.id.numbers);
                mStock = view.findViewById(R.id.stock);
                mOrderStatus = view.findViewById(R.id.order_status);
            }
        }
    }
}
