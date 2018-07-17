package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.model.MyDecoration;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/1/23.
 * 拣货复核
 */

public class ReviewListActivity extends BaseActivity implements IMvpView {
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private ImageView mSearchImage,mBackImage;
    private PickingPresenter pickingPresenter;
    private int page = 1, x = 1;
    private TextView mTitleText;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private LinearLayout mEmptyLayout,mTitleLayout;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected int getLayoutId() {
        return R.layout.picking_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleText.setText("装车复核");
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        x = 1;
                        page = 1;
                        pickingPresenter.reviewList("", page);
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
                        pickingPresenter.reviewList("", page);
                    }
                }, 1000);
            }
        });
        pickingPresenter = new PickingPresenter(this, this);
        pickingPresenter.reviewList("", page);

        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("review");    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mRecyclerView = findViewById(R.id.picking_list_recyview);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        mSearchImage = findViewById(R.id.search);
        mSearchImage.setOnClickListener(this);
        mTitleText = findViewById(R.id.title_text);
        mEmptyLayout = findViewById(R.id.empty_layout);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_image:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast, null);
                finish();
                break;
            case R.id.search:
                startActivity(ReviewListSearchActivity.class, null);
                break;
//            case R.id.back:
//                Intent broadcast = new Intent("main");
//                sendBroadcast(broadcast, null);
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
        mTitle.setText("");
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
        switch (type) {
            case "list":
                if (x == 1) {
                    mList = (ArrayList) object;
                    if (mList != null && mList.size() > 0) {
                        mAdapter = new RecyAdapter();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
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
        logcat("是否回去刷新状态===" + requestCode);
        if (data != null) {
            switch (data.getExtras().getString("type")) {
                case "success":
                    x = 1;
                    page = 1;
                    pickingPresenter.reviewList("", page);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (myBroadcastReceiver != null) {
                unregisterReceiver(myBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        float volume,packageCount,weight,actlOutCount;

        @Override
        public RecyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyAdapter.MyViewHolder holder = new RecyAdapter.MyViewHolder(LayoutInflater.from(
                    ReviewListActivity.this).inflate(R.layout.picking_list_item, parent,
                    false));
            return holder;
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {
            ArrayList<ArrayMap<String, Object>> outDetlList = Constants.getJsonArray(mList.get(position).get("outDetlList").toString());
            try {
                actlOutCount = Float.parseFloat(mList.get(position).get("actlOutCount").toString());
                packageCount = Float.parseFloat(outDetlList.get(0).get("packageCount").toString());
                volume = Float.parseFloat(outDetlList.get(0).get("packageLength").toString()) * Float.parseFloat(outDetlList.get(0).get("packageWidth").toString()) * Float.parseFloat(outDetlList.get(0).get("packageHeight").toString()) / packageCount * actlOutCount / 1000000;

                if (mList.get(position).get("outCode") != null) {
                    holder.orderText.setText("出库单号：" + mList.get(position).get("outCode"));
                }
                String volumeV = Constants.format4(volume+"");
                if (volumeV.length() > 7) {
                    holder.tijiText.setTextSize(13);
                } else {
                    holder.tijiText.setTextSize(16);
                }
                holder.tijiText.setText(volumeV + "M³");
                holder.pickReviewText.setText("已出库数量");
                if (outDetlList.get(0).get("packageWeight") != null) {
                    weight = Float.parseFloat(outDetlList.get(0).get("packageWeight").toString()) / packageCount * actlOutCount;
                    String weightV = Constants.format2(weight+"");
                    if(weightV.length()>7){
                        holder.zlText.setTextSize(13);
                    }else{
                        holder.zlText.setTextSize(16);
                    }
                    holder.zlText.setText(weightV+ "KG");
                }

                if (mList.get(position).get("actlOutCount") != null) {
                    if(mList.get(position).get("actlOutCount").toString().length()>7){
                        holder.zlText.setTextSize(13);
                    }else{
                        holder.zlText.setTextSize(16);
                    }
                    holder.numberText.setText(mList.get(position).get("actlOutCount") + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mList.get(position).get("outStatus") != null) {
                if (mList.get(position).get("outStatus").toString().equals("已拣货,未复核")) {
                    holder.statusText.setText("未复核");
                    holder.statusText.setTextColor(getResources().getColor(R.color.color24));
                    holder.statusText.setBackgroundResource(R.drawable.bg_my_style12);
                }
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("outId", mList.get(position).get("outId") + "");
                    bundle.putString("outCode", mList.get(position).get("outCode") + "");
                    openActivityForResult(ReviewGoodsDetailListActivity.class, 0, bundle);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView orderText, tijiText, zlText, numberText, statusText,pickReviewText;
            LinearLayout mContentLayout;

            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                pickReviewText = view.findViewById(R.id.pick_review_status);
                orderText = view.findViewById(R.id.order_text);
                tijiText = view.findViewById(R.id.tiji_text);
                zlText = view.findViewById(R.id.zl_text);
                numberText = view.findViewById(R.id.sl_text);
                statusText = view.findViewById(R.id.status_text);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast, null);
                finish();
                break;
        }
        return true;
    }
    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            logcat("接收广播==============");
            x = 1;
            page = 1;
            pickingPresenter.reviewList("", page);
            mRecyclerView.refreshComplete();
            mRecyclerView.setLoadingMoreEnabled(true);
        }
    }
}
