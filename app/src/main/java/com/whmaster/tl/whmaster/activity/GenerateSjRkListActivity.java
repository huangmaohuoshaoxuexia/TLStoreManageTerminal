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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/24.
 * 入库单生成
 */

public class GenerateSjRkListActivity extends BaseActivity implements IMvpView {

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private TextView mRkdscText, mRkText;
    private StoragePresenter storagePresenter;
    private int page = 1, x = 1;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private LinearLayout mEmptyLayout;
    private ImageView mSearchImage, mXImage;
    private EditText mSearchEdit;
    private LinearLayout mSearchLayout;
    private String mType = "rkscList",mOrderInStatus = "33";
//    private GoodsRikPresenter goodsRikPresenter;
    private String m_Broadcastname,mOrderCode = "";

    @Override
    protected int getLayoutId() {
        return R.layout.generate_sj_rk_layout;
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
                        page = 1;
                        x = 1;
                        if (mType.equals("rkscList")) {
                            storagePresenter.rkdscList(mOrderInStatus,mOrderCode, page);
                        } else if (mType.equals("rkList")) {
                            storagePresenter.getStorageList(mOrderCode, page, 10);
                        }
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        page = page+1;
                        x = 2;
                        logcat("开始刷新======"+page);
                        if (mType.equals("rkscList")) {
                            storagePresenter.rkdscList(mOrderInStatus,mOrderCode, page);
                        } else if (mType.equals("rkList")) {
                            storagePresenter.getStorageList(mOrderCode, page, 10);
                        }
                    }
                }, 1000);
            }
        });

        storagePresenter = new StoragePresenter(this, this);
        storagePresenter.rkdscList(mOrderInStatus,"", page);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchImage.getWindowToken(), 0);
        switch (v.getId()) {
            //入库单生成
            case R.id.rkd_sc_text:
                mType = "rkscList";
                if (mList == null) {
                    mList = new ArrayList<>();
                }
                mList.clear();
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
                mRkdscText.setBackgroundResource(R.drawable.bg_my_style8);
                mRkdscText.setTextColor(getResources().getColor(R.color.white));
                mRkText.setBackgroundResource(R.drawable.bg_my_style1);
                mRkText.setTextColor(getResources().getColor(R.color.color16));
                x = 1;
                page = 1;
                storagePresenter.rkdscList(mOrderInStatus,"", page);
                break;
            //入库
            case R.id.rk_text:
                mType = "rkList";
                if (mList == null) {
                    mList = new ArrayList<>();
                }
                mList.clear();
                if(mAdapter!=null){
                    mAdapter.notifyDataSetChanged();
                }
                mRkdscText.setBackgroundResource(R.drawable.bg_my_style1);
                mRkdscText.setTextColor(getResources().getColor(R.color.color16));
                mRkText.setBackgroundResource(R.drawable.bg_my_style8);
                mRkText.setTextColor(getResources().getColor(R.color.white));

                x = 1;
                page = 1;
                storagePresenter.getStorageList("", page, 10);
                break;
            case R.id.search_image:
                if (mSearchLayout.getVisibility() == View.VISIBLE) {
                    x = 1;
                    page = 1;
                    if (mType.equals("rkscList")) {
                        storagePresenter.rkdscList(mOrderInStatus,mSearchEdit.getText().toString(), page);
                    } else if (mType.equals("rkList")) {
                        storagePresenter.getStorageList(mSearchEdit.getText().toString(), page, 10);
                    }
                    mSearchEdit.setText("");
                } else {
                    mRkdscText.setVisibility(View.GONE);
                    mRkText.setVisibility(View.GONE);
                    mSearchLayout.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.x_image:
                mOrderCode = "";
                mRkdscText.setVisibility(View.VISIBLE);
                mRkText.setVisibility(View.VISIBLE);
                mSearchLayout.setVisibility(View.GONE);
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
        mXImage = findViewById(R.id.x_image);
        mXImage.setOnClickListener(this);
        mRecyclerView = findViewById(R.id.rkd_recyview);
        mRkdscText = findViewById(R.id.rkd_sc_text);
        mRkText = findViewById(R.id.rk_text);
        mRkdscText.setOnClickListener(this);
        mRkText.setOnClickListener(this);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mSearchImage = findViewById(R.id.search_image);
        mSearchImage.setOnClickListener(this);
        mSearchEdit = findViewById(R.id.search_edit);
        mSearchLayout = findViewById(R.id.search_layout);
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
        mTitle.setText("上架单生成");
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
            case "rklist"://入库单生成
                if (x == 1) {
                    mList = (ArrayList) object;
                    if (mList != null && mList.size() > 0) {
                        mAdapter = new RecyAdapter(10);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mAddList = (ArrayList) object;
                    mAdapter.notifyList(mAddList);
                    mRecyclerView.loadMoreComplete();
                    if (mAddList.size() < 10) {
                        mRecyclerView.setLoadingMoreEnabled(false);
                    }else{
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }
                break;
            case "list"://入库
                if (x == 1) {
                    mList = (ArrayList) object;
                    if (mList != null && mList.size() > 0) {
                        mAdapter = new RecyAdapter(20);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                } else if (x == 2){
                    mAddList = (ArrayList) object;
                    mAdapter.notifyList(mAddList);
                    logcat("刷新分页数据======="+mAddList.size());
                    mRecyclerView.loadMoreComplete();
                    if (mAddList.size() < 10) {
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
        if (data != null) {
            mType = data.getExtras().getString("type");
            switch (mType) {
                case "rkscList":
                    page = 1;
                    x = 1;
                    storagePresenter.rkdscList(mOrderInStatus,"", page);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                    break;
                case "rkList":
                    page = 1;
                    x = 1;
                    storagePresenter.getStorageList("", page, 10);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                    break;
            }
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        int mPosition = 10;

        public RecyAdapter(int position) {
            this.mPosition = position;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            if (viewType == 10) {
                holder = new MyViewHolder1(LayoutInflater.from(
                        GenerateSjRkListActivity.this).inflate(R.layout.rkd_sc_list_item, parent,
                        false));
            } else if (viewType == 20) {
                holder = new MyViewHolder2(LayoutInflater.from(
                        GenerateSjRkListActivity.this).inflate(R.layout.rksj_list_item, parent,
                        false));
            }
            return holder;
        }

        @Override
        public int getItemViewType(int position) {
            return mPosition;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof MyViewHolder1) {
                final ArrayMap<String,Object> map = Constants.getJsonObject(mList.get(position).get("orderInBase").toString());
                if (map.get("orderInCode") != null) {
                    ((MyViewHolder1) holder).mOrderCode.setText("入库订单编号：" + map.get("orderInCode"));
                }
                if (map.get("buyerName") != null) {
                    ((MyViewHolder1) holder).mProductName.setText("货主：" + map.get("buyerName"));
                }
                if (map.get("totalCount") != null) {
                    ((MyViewHolder1) holder).mProductNum.setText("货品数量：" + map.get("totalCount"));
                }
                ((MyViewHolder1) holder).mContentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("orderInId",map.get("orderInId")+"");
                        bundle.putString("buyerId",map.get("buyerId")+"");
                        bundle.putString("orgId",map.get("orgId")+"");
                        openActivityForResult(GenerateSlListActivity.class, 0, bundle);
                    }
                });
            } else if (holder instanceof MyViewHolder2) {
                if (mList.get(position).get("orderInCode") != null) {
                    ((MyViewHolder2) holder).orderName.setText("入库订单编号：" + mList.get(position).get("orderInCode").toString());
                }
                if (mList.get(position).get("executeStatus") != null) {
                    ((MyViewHolder2) holder).mStates.setText(mList.get(position).get("executeStatus").toString());
                    if (mList.get(position).get("executeStatus").toString().equals("未执行")) {
                        ((MyViewHolder2) holder).mStates.setBackgroundResource(R.mipmap.bg_red);
                    } else if (mList.get(position).get("executeStatus").toString().equals("执行中")) {
                        ((MyViewHolder2) holder).mStates.setBackgroundResource(R.mipmap.bg_orange);
                    } else {
                        ((MyViewHolder2) holder).mStates.setBackgroundResource(R.mipmap.bg_green);
                    }
                }
//                if (mList.get(position).get("noShelfNum") != null) {
//                    ((MyViewHolder2) holder).mPendingUpText.setText("待上架零散数：" + mList.get(position).get("noShelfNum").toString());
//                }
                if (mList.get(position).get("noShelfNum") != null) {
                    ((MyViewHolder2) holder).mNoUpText.setText("待上架总数：" + mList.get(position).get("noShelfNum").toString());
                }
//                if (mList.get(position).get("noShelfPackageNum") != null) {
//                    ((MyViewHolder2) holder).mAllPendingUpText.setText("待上架整数：" + mList.get(position).get("noShelfPackageNum").toString());
//                }
                if (mList.get(position).get("shelfAlreadyNum") != null) {
                    ((MyViewHolder2) holder).mAlareadyUpText.setText("已上架总数：" + mList.get(position).get("shelfAlreadyNum").toString());
                }

                if (mList.get(position).get("buyerName") != null) {
                    ((MyViewHolder2) holder).mProductUserLayout.setVisibility(View.VISIBLE);
                    ((MyViewHolder2) holder).mProductUser.setText("货主：" + mList.get(position).get("buyerName").toString());
                }else{
                    ((MyViewHolder2) holder).mProductUserLayout.setVisibility(View.GONE);
                }
                ((MyViewHolder2) holder).mContentLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("stockInId", mList.get(position).get("stockInId") + "");
                        bundle.putString("orderInId", mList.get(position).get("orderInId") + "");
                        openActivityForResult(StorageGoodsActivity.class, 0, bundle);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void notifyList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            notifyDataSetChanged();
        }

        class MyViewHolder1 extends RecyclerView.ViewHolder {
            LinearLayout mContentLayout;
            TextView mOrderCode,mProductName,mProductNum;
            public MyViewHolder1(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                mOrderCode = view.findViewById(R.id.order_code);
                mProductName = view.findViewById(R.id.product_name);
                mProductNum = view.findViewById(R.id.product_num);

            }
        }

        class MyViewHolder2 extends RecyclerView.ViewHolder {
            TextView orderName, mProductUser,mNoUpText,mAlareadyUpText;
            private Button mStates;
            LinearLayout mContentLayout,mProductUserLayout;

            public MyViewHolder2(View view) {
                super(view);
                mNoUpText = view.findViewById(R.id.no_up_text);
                mAlareadyUpText = view.findViewById(R.id.already_up_text);
                mProductUserLayout = view.findViewById(R.id.product_user_layout);
                mProductUser = view.findViewById(R.id.product_user);
                orderName = view.findViewById(R.id.order_name);
                mContentLayout = view.findViewById(R.id.content_layout);
                mStates = view.findViewById(R.id.item_state);
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
                    x = 1;
                    page = 1;
                    mOrderCode = str;
                    if (mType.equals("rkscList")) {
                        storagePresenter.rkdscList(mOrderInStatus,mOrderCode, page);
                    } else if (mType.equals("rkList")) {
                        storagePresenter.getStorageList(mOrderCode, page, 10);
                    }
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
            }
        }
    };
}
