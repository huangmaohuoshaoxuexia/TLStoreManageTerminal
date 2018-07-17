package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
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
import com.whmaster.tl.whmaster.impl.SlideInterface;
import com.whmaster.tl.whmaster.model.MyDecoration;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;
import com.whmaster.tl.whmaster.widget.SlideView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/1/15.
 * 复核详情
 */

public class ReviewGoodsDetailListActivity extends BaseActivity implements IMvpView, SlideView.onSuccessInterface {

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private PickingPresenter pickingPresenter;
    private SlideView mSubBtn;
    private Bundle mBundle;
    private String mOutId, mOutCode;
    private ArrayList<ArrayMap<String, Object>> mList;
    private ArrayMap<String, Object> mDataMap;
    private TextView mOrderTex, mTijiText, mZlText, mPlanNumberText, mNoNumberText;
    private float mVolume, mWeight;
    private int mPlanOutCount;
    private LinearLayout mTitleLayout;
    private ImageView mBackImage;

    @Override
    protected int getLayoutId() {
        return R.layout.review_goods_detail_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mVolume = 0;
                        mWeight = 0;
                        mPlanOutCount = 0;
                        pickingPresenter.reviewDetailList(mOutCode, mOutId);
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
            mOutCode = mBundle.getString("outCode");
            mOutId = mBundle.getString("outId");
            pickingPresenter = new PickingPresenter(this, this);
            pickingPresenter.reviewDetailList(mOutCode, mOutId);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mRecyclerView = findViewById(R.id.picking_goods_detail_recyview);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnSuccessListener(this);
        mOrderTex = findViewById(R.id.order_text);
        mTijiText = findViewById(R.id.tiji_text);
        mPlanNumberText = findViewById(R.id.plan_num_text);
        mZlText = findViewById(R.id.zl_text);
        mNoNumberText = findViewById(R.id.no_num_text);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_image:
                Intent broadcast = new Intent("review");
                sendBroadcast(broadcast, null);
                finish();
                break;
//            case R.id.back:
//                Intent broadcast = new Intent("review");
//                sendBroadcast(broadcast, null);
//                finish();
//                break;
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
        mTitle.setText("复核详情");
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
                if (mDataMap != null) {
                    mOrderTex.setText("出库单号：" + mDataMap.get("outCode"));
                    mList = Constants.getJsonArray(mDataMap.get("outDetlList").toString());
                    if (mList != null && mList.size() > 0) {
                        for (int i = 0; i < mList.size(); i++) {
                            float packageCount = Float.parseFloat(mList.get(i).get("packageCount").toString());
                           float actlOutCount = Float.parseFloat(mList.get(i).get("actlOutCount").toString());
                            float volume = Float.parseFloat(mList.get(i).get("packageLength").toString()) * Float.parseFloat(mList.get(i).get("packageWidth").toString()) * Float.parseFloat(mList.get(i).get("packageHeight").toString()) / packageCount * actlOutCount / 1000000;
                            mVolume = mVolume + volume;
                            float weight = Float.parseFloat(mList.get(i).get("packageWeight").toString()) / packageCount * actlOutCount;
                            mWeight = mWeight + weight;
                            mPlanOutCount = mPlanOutCount + Integer.parseInt(mList.get(i).get("actlOutCount") + "");
                            if (i == mList.size() - 1) {
                                handler.sendEmptyMessageDelayed(1, 0);
                            }
                        }
                    }
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);

                }
                break;
            case "update":
                String message = (String) object;
                if (message.equals("success")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "success");
                    setResultOk(bundle);
                } else {
                    onFail(message);
                    handler.sendEmptyMessageDelayed(0, 200);
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
    public void onExcute() {
        pickingPresenter.outDetailupdateStatus(mOutCode, 150);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logcat(resultCode + "是否回去刷新状态===" + requestCode);
        if (data != null) {
            switch (data.getExtras().getString("type")) {
                case "success":
                    mVolume = 0;
                    mWeight = 0;
                    mPlanOutCount = 0;
                    pickingPresenter.reviewDetailList(mOutCode, mOutId);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                    break;
            }
        }
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
                    if((mPlanOutCount+"").toString().length()>5){
                        mPlanNumberText.setTextSize(13);
                    }else{
                        mPlanNumberText.setTextSize(16);
                    }
                    mTijiText.setText(volumeV + "M³ /");
                    mZlText.setText(weightV + "KG /");
                    mPlanNumberText.setText(mPlanOutCount + " /");
                    break;
            }
        }
    };

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        float volume, weight,packageCount,actlOutCount;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ReviewGoodsDetailListActivity.this).inflate(R.layout.review_goods_detail_list_item, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            try {
                packageCount = Float.parseFloat(mList.get(position).get("packageCount").toString());
                actlOutCount = Float.parseFloat(mList.get(position).get("actlOutCount").toString());
                volume = Float.parseFloat(mList.get(position).get("packageLength").toString()) * Float.parseFloat(mList.get(position).get("packageWidth").toString()) * Float.parseFloat(mList.get(position).get("packageHeight").toString()) / packageCount * actlOutCount / 1000000;
//                mVolume = mVolume + volume;
                weight = Float.parseFloat(mList.get(position).get("packageWeight").toString()) / packageCount * actlOutCount;
//                mWeight = mWeight + weight;
//                mPlanOutCount = mPlanOutCount + Integer.parseInt(mList.get(position).get("actlOutCount") + "");

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mList.get(position).get("prodName") != null) {
                holder.productName.setText(mList.get(position).get("prodName") + "");
            }
            if (mList.get(position).get("outDetlStatus") != null) {
                if(mList.get(position).get("outDetlStatus").toString().equals("已完成")){
                    holder.statusText.setText(mList.get(position).get("outDetlStatus") + "");
                    holder.statusText.setTextColor(getResources().getColor(R.color.color14));
                }else{
//                    holder.statusText.setText(mList.get(position).get("outDetlStatus") + "");
//                    holder.statusText.setTextColor(getResources().getColor(R.color.color14));
                    holder.statusText.setText("已拣货");
                    holder.statusText.setTextColor(getResources().getColor(R.color.color31));
                }
            }
            if (mList.get(position).get("sku") != null) {
                holder.skuText.setText(mList.get(position).get("sku") + "");
            }
            if (mList.get(position).get("actlOutCount") != null) {
                holder.outNumberText.setText(mList.get(position).get("actlOutCount").toString() + mList.get(position).get("packageUnit"));
            }

           String volumeV = Constants.format4(volume+"");
            if (volumeV.length() > 7) {
                holder.tijiText.setTextSize(13);
            } else {
                holder.tijiText.setTextSize(16);
            }
            holder.tijiText.setText(volumeV + "M³");

           String weightV = Constants.format2(weight+"");
            if(weightV.length()>7){
                holder.zlText.setTextSize(13);
            }else{
                holder.zlText.setTextSize(16);
            }
            holder.zlText.setText(weightV + "KG");

            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("outDetlId", mList.get(position).get("outDetlId") + "");
                    bundle.putString("weight", Constants.format2(weight+""));
                    bundle.putString("volume", Constants.format4(volume+""));
                    openActivityForResult(ReviewGoodsDetailActivity.class, 0, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, statusText, noText, skuText, outNumberText, tijiText, zlText;
            LinearLayout mContentLayout;

            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                productName = view.findViewById(R.id.product_name);
                statusText = view.findViewById(R.id.status_text);
                noText = view.findViewById(R.id.no_text);
                skuText = view.findViewById(R.id.sku_text);
                outNumberText = view.findViewById(R.id.out_number_text);
                tijiText = view.findViewById(R.id.tiji_text);
                zlText = view.findViewById(R.id.zl_text);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent broadcast = new Intent("review");
                sendBroadcast(broadcast, null);
                finish();
                break;
        }
        return true;
    }
}
