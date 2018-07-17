package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/6.
 * 入库货品
 */

public class StorageGoodsActivity extends BaseActivity implements IMvpView {

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private Bundle mBundle;
    private String mType = "", mStockInId,mOrderId;
    private StoragePresenter storagePresenter;
    private ArrayList<ArrayMap<String, Object>> mList;
    private Button mSubBtn;
//    private MsgLoadingDialog msgLoadingDialog;
    private boolean isExecute = true;
    @Override
    protected int getLayoutId() {
        return R.layout.storage_goods_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        storagePresenter = new StoragePresenter(this, this);
        storagePresenter.getStorageProductList(mStockInId);
//        msgLoadingDialog = new MsgLoadingDialog(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub_btn:
                if(isExecute){
//                    msgLoadingDialog.builder().setMsg("正在执行").show();
                    storagePresenter.executeStockInTask(mStockInId,mOrderId);
                }else{
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("你仍有未上架货品！")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mAlertDialog.dismiss();
                                }
                            }).show();
                }

                break;
        }
    }
    private void callBack(){
        Bundle bundle = new Bundle();
        bundle.putString("type","rkList");
        setResultOk(bundle);
    }
    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.storage_goods_recyview);
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mType = mBundle.getString("type");
            mStockInId = mBundle.getString("stockInId");
            mOrderId = mBundle.getString("orderInId");
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                callBack();
                break;
        }
        return true;
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
        mTitle.setText("入库货品");
    }

    @Override
    public void onFail(String errorMsg) {
        mAlertDialog.builder().setTitle("提示")
                .setMsg(errorMsg)
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAlertDialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type) {
            case "list":
                mList = (ArrayList<ArrayMap<String, Object>>) object;
                mAdapter = new RecyAdapter();
                mRecyclerView.setAdapter(mAdapter);
                break;
            case "execute":
                callBack();
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
        if(data!=null){
            isExecute = true;
            storagePresenter.getStorageProductList(mStockInId);
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        int packageCount = 0;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    StorageGoodsActivity.this).inflate(R.layout.storage_list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if (mList.get(position).get("productName") != null) {
                holder.name.setText("货品名称：" + mList.get(position).get("productName"));
            }
            try {
                packageCount = Integer.parseInt(mList.get(position).get("packageCount")+"");
                if (mList.get(position).get("planNum") != null) {
                    int planNum = Integer.parseInt(mList.get(position).get("planNum").toString()) % packageCount;
                    holder.mNoNum.setText("计划上架零散数：" + planNum);
                }
                if (mList.get(position).get("actNum") != null) {
                    int actNum = Integer.parseInt(mList.get(position).get("actNum").toString()) % packageCount;
                    holder.mAlreadyNum.setText("已上架零散数：" + actNum);
                }
                if (mList.get(position).get("packageCount") != null) {
                    int actZs = Integer.parseInt(mList.get(position).get("actNum").toString()) / packageCount;
                    holder.mAlreadyPackgeNum.setText("已上架整数：" + actZs);
                }
                if (mList.get(position).get("planPackageNum") != null) {
                    int planZs = Integer.parseInt(mList.get(position).get("planNum").toString()) / packageCount;
                    holder.mNoPackgeNum.setText("计划上架整数：" + planZs);
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            if (mList.get(position).get("buyerName") != null) {
                holder.mProductUserLayout.setVisibility(View.VISIBLE);
                holder.mProductUser.setText("货主：" + mList.get(position).get("buyerName").toString());
            }else{
                holder.mProductUserLayout.setVisibility(View.GONE);
            }
                if (mList.get(position).get("stockInDetailStatus").toString().equals("10")) {
                    holder.mStates.setBackgroundResource(R.mipmap.bg_red);
                    holder.mStates.setText("未执行");
                    isExecute = false;
                } else if (mList.get(position).get("stockInDetailStatus").toString().equals("20")) {
                    holder.mStates.setBackgroundResource(R.mipmap.bg_green);
                    holder.mStates.setText("已执行");
                }
            holder.imageView.setImageResource(R.mipmap.icon02);
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if(mList.get(position).get("stockInDetailStatus").toString().equals("10")){
                        Bundle bundle = new Bundle();
                        bundle.putString("detailId", mList.get(position).get("stockInDetailId") + "");
                        bundle.putString("businessType", mList.get(position).get("stockInDetailStatus") + "");
                        openActivityForResult(RksjActivity.class,0,bundle);
//                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, mNoPackgeNum, mNoNum, mAlreadyNum, mAlreadyPackgeNum,mProductUser;
            ImageView imageView;
            LinearLayout mContentLayout,mProductUserLayout;
            private Button mStates;

            public MyViewHolder(View view) {
                super(view);
                mProductUserLayout = view.findViewById(R.id.product_user_layout);
                mProductUser = view.findViewById(R.id.product_user);
                name = view.findViewById(R.id.order_name);
                imageView = view.findViewById(R.id.icon_image);
                mContentLayout = view.findViewById(R.id.content_layout);
                mAlreadyPackgeNum = view.findViewById(R.id.already_packge_text);
                mNoPackgeNum = view.findViewById(R.id.no_packge_text);
                mAlreadyNum = view.findViewById(R.id.already_num_text);
                mNoNum = view.findViewById(R.id.no_num_text);
                mStates = view.findViewById(R.id.item_state);
            }
        }
    }
}
