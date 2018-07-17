package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;
import com.whmaster.tl.whmaster.widget.SlideView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/1/15.
 * 拣货单详情
 */

public class PickingGoodsDetailListActivity extends BaseActivity implements IMvpView, SlideView.onSuccessInterface {

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private String pickCode;
    private Bundle mBundle;
    //    private TextView mTitleText;
    private PickingPresenter pickingPresenter;
    private ArrayList<ArrayMap<String, Object>> mList;
    private ArrayMap<String, Object> mDataMap;
    private TextView mOrderText, mTijiText, mZlText, mPlanNumberText, mNoNumberText;
    private SlideView mSubBtn;
    private String mType = "",mBaseUnitCn;
    private int mPlanNumbers, mNoNumbers = 0;
    private float mWeight, mVolume;
    private LinearLayout mTitlelayout;
    private ImageView mBackImage;

    @Override
    protected int getLayoutId() {
        return R.layout.picking_goods_detail_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();

        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mWeight = 0;
                        mVolume = 0;
                        mPlanNumbers = 0;
                        mNoNumbers = 0;
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                        pickingPresenter.pickingDetailList(pickCode);
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
            pickCode = mBundle.getString("pickCode");
        }
        pickingPresenter = new PickingPresenter(this, this);
        pickingPresenter.pickingDetailList(pickCode);
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.picking_goods_detail_recyview);
        mTitlelayout = findViewById(R.id.title);
        mTitlelayout.setVisibility(View.GONE);
        mOrderText = findViewById(R.id.orderid_text);
        mTijiText = findViewById(R.id.m_tiji_text);
        mZlText = findViewById(R.id.m_zl_text);
        mPlanNumberText = findViewById(R.id.m_number_text);
        mNoNumberText = findViewById(R.id.m_nonumbers_text);
        mSubBtn = findViewById(R.id.slideview_btn);
        mSubBtn.setOnSuccessListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_image:
                finish();
                break;
            case R.id.title_text:
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
        mTitle.setText("拣货单详情");
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
        handler.sendEmptyMessageDelayed(0, 200);
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type) {
            case "list":
                mDataMap = (ArrayMap<String, Object>) object;
                mList = Constants.getJsonArray(mDataMap.get("pickDetlList").toString());
                mAdapter = new RecyAdapter();
                mRecyclerView.setAdapter(mAdapter);
                if (mList != null && mList.size() > 0) {
                    mOrderText.setText("拣货单号：" + mDataMap.get("pickCode"));

                    for (int i = 0; i < mList.size(); i++) {
                        ArrayMap proMap = Constants.getJsonObject(mList.get(i).get("productPo").toString());
                        ArrayMap productMap = Constants.getJsonObject(proMap.get("product").toString());
                       float packageCount = Float.parseFloat(productMap.get("packageCount").toString());
                        float volume = Float.parseFloat(productMap.get("packageLength").toString()) * Float.parseFloat(productMap.get("packageWidth").toString()) * Float.parseFloat(productMap.get("packageHeight").toString()) / packageCount * Float.parseFloat(mList.get(i).get("planPickCount").toString()) / 1000000;
                        mVolume = mVolume + volume;
                        float weight = Float.parseFloat(productMap.get("packageWeight").toString()) / packageCount * Float.parseFloat(mList.get(i).get("planPickCount").toString());
                        mWeight = mWeight + weight;
                        int planNumbers = Integer.parseInt(mList.get(i).get("planPickCount").toString());
                        mPlanNumbers = mPlanNumbers + planNumbers;
                        mNoNumbers = mNoNumbers + Integer.parseInt(mList.get(i).get("unPickCount").toString());
                        if (i == mList.size() - 1) {
                            handler.sendEmptyMessageDelayed(1, 0);
                        }
                    }
                }
                break;
            case "update":
                logcat("返回状态====");
                String message = (String) object;
                if (message.equals("success")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "success");
                    setResultOk(bundle);
                } else {
                    onFail(message);
                    handler.sendEmptyMessageDelayed(0, 500);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logcat("是否回去刷新状态===" + requestCode);
        if (data != null) {
            mType = data.getExtras().getString("type");
            switch (mType) {
                case "success":
                    mWeight = 0;
                    mVolume = 0;
                    mPlanNumbers = 0;
                    mNoNumbers = 0;
                    pickingPresenter.pickingDetailList(pickCode);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                    break;
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
    public void onExcute() {
        pickingPresenter.pickDetailupdateStatus(pickCode, 20);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mSubBtn.resetXy();
                    break;
                case 1:
                    String volumeV = Constants.format4(mVolume+"");
                    String weightV = Constants.format2(mWeight+"");
                    if(volumeV.length()>7){
                        mTijiText.setTextSize(13);
                    }else{
                        mTijiText.setTextSize(16);
                    }
                    if(weightV.length()>7){
                        mZlText.setTextSize(13);
                    }else{
                        mZlText.setTextSize(16);
                    }
                    mTijiText.setText(volumeV + "M³/");
                    mPlanNumberText.setText(mPlanNumbers + " /");
                    mZlText.setText(weightV + "KG/");
                    mNoNumberText.setText(mNoNumbers + " /");
                    break;
            }
        }
    };

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        float weight = 0, volume = 0,packageCount;
        ArrayMap proMap,productMap;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PickingGoodsDetailListActivity.this).inflate(R.layout.picking_goods_detail_list_item, parent,
                    false));

            return holder;
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            try {
                proMap = Constants.getJsonObject(mList.get(position).get("productPo").toString());
                productMap = Constants.getJsonObject(proMap.get("product").toString());
                packageCount = Float.parseFloat(productMap.get("packageCount").toString());
                volume = Float.parseFloat(productMap.get("packageLength").toString()) * Float.parseFloat(productMap.get("packageWidth").toString()) * Float.parseFloat(productMap.get("packageHeight").toString()) / packageCount * Float.parseFloat(mList.get(position).get("planPickCount").toString()) / 1000000;
                weight = Float.parseFloat(productMap.get("packageWeight").toString()) / packageCount * Float.parseFloat(mList.get(position).get("planPickCount").toString());
//                  volume = Float.parseFloat(productMap.get("planPickVolume").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mList.get(position).get("batchNo") != null) {
                holder.noText.setText(mList.get(position).get("batchNo") + "");
            }
            if (mList.get(position).get("prodName") != null) {
                holder.nameText.setText(mList.get(position).get("prodName") + "");
            }
            if (mList.get(position).get("sku") != null) {
                holder.skuText.setText(mList.get(position).get("sku") + "");
            }
            if (mList.get(position).get("unPickCount") != null) {
                holder.numberText.setText(mList.get(position).get("unPickCount").toString() + proMap.get("baseUnitCn"));
            }

            final String volumeV = Constants.format4(volume+"");
            if(volumeV.length()>7){
                holder.tijiText.setTextSize(13);
            }else{
                holder.tijiText.setTextSize(16);
            }
            holder.tijiText.setText(volumeV + "M³");

            final String weightV = Constants.format2(weight+"");
            if(weightV.length()>7){
                holder.zlText.setTextSize(13);
            }else{
                holder.zlText.setTextSize(16);
            }
            holder.zlText.setText(weightV+ "KG");

            if (mList.get(position).get("regionName") != null) {
                holder.kuquText.setText("库区：" + mList.get(position).get("regionName"));
            }
            if (mList.get(position).get("positionName") != null) {
                holder.kuweiText.setText("库位：" + mList.get(position).get("positionName"));
            }
            if (mList.get(position).get("pickDetlStatus") != null) {
                if (mList.get(position).get("pickDetlStatus").toString().equals("已分配,未拣货")) {
                    holder.statusText.setText("未拣货");
                    holder.statusText.setTextColor(getResources().getColor(R.color.color24));
                } else if (mList.get(position).get("pickDetlStatus").toString().equals("已拣货,未完成")) {
                    holder.statusText.setText("拣货中");
                    holder.statusText.setTextColor(getResources().getColor(R.color.color31));
                } else {
                    holder.statusText.setText("已完成");
                    holder.statusText.setTextColor(getResources().getColor(R.color.color14));
                }
            }
            holder.whouseText.setText(mDataMap.get("whName") + "");
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pickDetlId", mList.get(position).get("pickDetlId") + "");
                    bundle.putString("weight", weightV);
                    bundle.putString("volume", volumeV);
                    openActivityForResult(PickingGoodsDetailActivity.class, 0, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView whouseText, kuquText, kuweiText, statusText, noText, nameText, skuText, numberText, tijiText, zlText;
            LinearLayout mContentLayout;

            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                whouseText = view.findViewById(R.id.whouse_text);
                kuquText = view.findViewById(R.id.kuqu_text);
                kuweiText = view.findViewById(R.id.kuwei_text);
                statusText = view.findViewById(R.id.status_text);
                noText = view.findViewById(R.id.no_text);
                nameText = view.findViewById(R.id.name_text);
                skuText = view.findViewById(R.id.sku_text);
                numberText = view.findViewById(R.id.numbers_text);
                tijiText = view.findViewById(R.id.tiji_text);
                zlText = view.findViewById(R.id.zl_text);
            }
        }
    }
}
