package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.widget.MsgLoadingDialog;
import com.whmaster.tl.whmaster.widget.PostDialog;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/8.
 * 拣出货品
 */

public class PickingGoodsActivity extends BaseActivity implements IMvpView {
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private Bundle mBundle;
    private String mId,mSendType="";
    private ArrayList<ArrayMap<String, Object>> mList;
    private PickingPresenter pickingPresenter;
    private boolean isExecute = true;
    private Button mSubBtn;
    private String mPostId = "";//快递单号
    private PostDialog postDialog;
    private String m_Broadcastname;

    @Override
    protected int getLayoutId() {
        return R.layout.storage_goods_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postDialog = new PostDialog(this);
        postDialog.builder().setTitle("快递单号");
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
//        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
//        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        pickingPresenter = new PickingPresenter(this, this);
        pickingPresenter.pickingGoodsList(mId);
        msgLoadingDialog = new MsgLoadingDialog(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sub_btn:
                logcat("获取快点单号状态" + mSendType);
                if (isExecute) {
                    if(mSendType.equals("30")){
                        postDialog.setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPostId = postDialog.getPost();
                                logcat("输入快递单号" + mPostId);
                                if(mPostId!=null && !mPostId.equals("")){
                                    pickingPresenter.executeStockOutTask(mId, mPostId);
                                    mAlertDialog.dismiss();
                                }else{
                                    Toast.makeText(PickingGoodsActivity.this,"快递单号不能为空！",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                            }
                        }).show();
                    }else{
                        pickingPresenter.executeStockOutTask(mId, mPostId);
                    }
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("你仍有未拣货货品！")
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

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.storage_goods_recyview);
        mBundle = getIntent().getExtras();
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
        if (mBundle != null) {
            mId = mBundle.getString("stockInId");
            mSendType = mBundle.getString("sendType");
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
        mTitle.setText("拣出货品");
        mSubBtn.setText("执行完毕");
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
                mList = (ArrayList<ArrayMap<String, Object>>) object;
                mAdapter = new RecyAdapter();
                mRecyclerView.setAdapter(mAdapter);
                break;
            case "execute":
                Bundle bundle = new Bundle();
                setResultOk(bundle);
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
        isExecute = true;
        switch (requestCode) {
            case 0:
                pickingPresenter.pickingGoodsList(mId);
                break;
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PickingGoodsActivity.this).inflate(R.layout.storage_list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {
            if (mList.get(position).get("productName") != null) {
                holder.name.setText("货品名称：" + mList.get(position).get("productName"));
            }
            if (mList.get(position).get("noPickNum") != null) {
                holder.mNoNum.setText("待拣出零散数：" + mList.get(position).get("noPickNum").toString());
            }
            if (mList.get(position).get("noPickPackageNum") != null) {
                holder.mNoPackgeNum.setText("待拣出整数：" + mList.get(position).get("noPickPackageNum").toString());
            }
            if (mList.get(position).get("alreadyPickNum") != null) {
                holder.mAlreadyNum.setText("已拣出零散数：" + mList.get(position).get("alreadyPickNum").toString());
            }
            if (mList.get(position).get("alreadyPickPackageNum") != null) {
                holder.mAlreadyPackgeNum.setText("已拣出整数：" + mList.get(position).get("alreadyPickPackageNum").toString());
            }
            if (mList.get(position).get("stockOutDetailStatusName") != null) {
                holder.mStates.setText(mList.get(position).get("stockOutDetailStatusName").toString());
                if (mList.get(position).get("alreadyPickNum") != null && mList.get(position).get("alreadyPickPackageNum") != null) {
                    if (Integer.parseInt(mList.get(position).get("alreadyPickNum").toString()) == 0 && Integer.parseInt(mList.get(position).get("alreadyPickPackageNum").toString()) == 0) {
                        holder.mStates.setBackgroundResource(R.mipmap.bg_red);
                    } else if (Integer.parseInt(mList.get(position).get("alreadyPickNum").toString()) > 0 || Integer.parseInt(mList.get(position).get("alreadyPickPackageNum").toString()) > 0) {
                        holder.mStates.setBackgroundResource(R.mipmap.bg_green);
                    }
                }
            }

            if (mList.get(position).get("stockOutDetailStatus").toString().equals("10")) {
                isExecute = false;
            }
            holder.imageView.setImageResource(R.mipmap.icon02);
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mList.get(position).get("stockOutDetailStatus").toString().equals("10")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("detailId", mList.get(position).get("stockOutDetailId") + "");
                        bundle.putString("businessType", mList.get(position).get("stockInDetailStatus") + "");
                        bundle.putString("type", "picking");
                        openActivityForResult(GoodsShelvesActivity.class, 0, bundle);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name, mNoPackgeNum, mNoNum, mAlreadyNum, mAlreadyPackgeNum;
            ImageView imageView;
            LinearLayout mContentLayout, mUserLayout;
            private Button mStates;

            public MyViewHolder(View view) {
                super(view);
                mUserLayout = view.findViewById(R.id.product_user_layout);
                mUserLayout.setVisibility(View.GONE);
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
                    mPostId = str;
                    postDialog.setMsg(str);
                }
            }
        }
    };
}
