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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.presenter.GoodsRikPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/24.
 * 实物收货
 */

public class GoodsReceiptListActivity extends BaseActivity implements IMvpView {
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private GoodsRikPresenter goodsRikPresenter;
    private int page = 1, x = 1;
    private LinearLayout mEmptyLayout;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private String m_Broadcastname;
    private String mOrderInCode = "";
    private EditText mSearchEdit;
    private ImageView mSearchImage;

    @Override
    protected int getLayoutId() {
        return R.layout.goods_receipt_in_kind;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView,this);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        x = 1;
                        page = 1;
                        goodsRikPresenter.goodsrikList(page, "", mOrderInCode);
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
                        goodsRikPresenter.goodsrikList(page, "", mOrderInCode);
                    }
                }, 1000);
            }
        });
        goodsRikPresenter = new GoodsRikPresenter(this,this);
        goodsRikPresenter.goodsrikList(page,"",mOrderInCode);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.search_image:
                page = 1;
                x = 1;
                goodsRikPresenter.goodsrikList(page, "", mSearchEdit.getText().toString());
                mRecyclerView.setLoadingMoreEnabled(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchImage.getWindowToken(), 0);
                break;
            case R.id.back:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast, null);
                break;
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.goods_rece_recyview);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mSearchEdit = findViewById(R.id.search_edit);
        mSearchImage = findViewById(R.id.search_image);
        mSearchImage.setOnClickListener(this);
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
        mTitle.setText("实物收货");
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
        if (x == 1) {
            mList = (ArrayList<ArrayMap<String, Object>>) object;
            if (mList != null && mList.size() > 0) {
                mAdapter = new RecyAdapter();
                mRecyclerView.setAdapter(mAdapter);
                mEmptyLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            } else {
                mEmptyLayout.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }
        } else if (x == 2) {
            mAddList = (ArrayList) object;
            mAdapter.notifiList(mAddList);
            mRecyclerView.loadMoreComplete();
            if (mAddList.size() < 10) {
                mRecyclerView.setLoadingMoreEnabled(false);
            }else{
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
        switch (requestCode) {
            case 0:
                x = 1;
                page = 1;
                goodsRikPresenter.goodsrikList(page, "", mOrderInCode);
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
                sendBroadcast(broadcast, null);
                finish();
                break;
        }
        return true;
    }
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    GoodsReceiptListActivity.this).inflate(R.layout.goods_rece_list, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

            if (mList.get(position).get("orderInCode") != null) {
                holder.mOrderCode.setText("入库订单编号：" + mList.get(position).get("orderInCode"));
            }
            if (mList.get(position).get("buyerName") != null) {
                holder.mProductName.setText("货主：" + mList.get(position).get("buyerName"));
            }
            if (mList.get(position).get("totalCount") != null) {
                holder.mProductNum.setText("货品数量：" + mList.get(position).get("totalCount"));
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("orderInCode", mList.get(position).get("orderInCode") + "");
                    bundle.putString("orderInId", mList.get(position).get("orderInId") + "");
                    openActivityForResult(GoodsRikActivity.class, 0, bundle);
                }
            });
        }

        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView mOrderCode, mProductName, mProductNum;
            LinearLayout mContentLayout;

            public MyViewHolder(View view) {
                super(view);
                mOrderCode = view.findViewById(R.id.order_name);
                mProductName = view.findViewById(R.id.the_owner_name);
                mProductNum = view.findViewById(R.id.product_num);
                mContentLayout = view.findViewById(R.id.content_layout);
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
                    mSearchEdit.setText(str);
                    mOrderInCode = str;
                    x = 1;
                    page = 1;
                    goodsRikPresenter.goodsrikList(page, "", mOrderInCode);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
            }
        }
    };
}
