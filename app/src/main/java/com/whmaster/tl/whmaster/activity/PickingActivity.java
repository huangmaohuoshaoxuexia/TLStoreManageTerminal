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
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/10/23.
 * 拣货单/拣货单复核
 */

public class PickingActivity extends BaseActivity implements IMvpView{
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private PickingPresenter pickingPresenter;
    private int page = 1,x = 1;
    private ArrayList<ArrayMap<String, Object>> mList, mAddList;
    private LinearLayout mEmptyLayout;
    private ImageView mSearchImage;
    private EditText mSearchEdit;
    private String mType;
    private Bundle mBundle;
    private String m_Broadcastname;

    @Override
    protected int getLayoutId() {
        return R.layout.picking_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickingPresenter = new PickingPresenter(this,this);
        RecyclerUtil.init(mRecyclerView,this);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        x = 1;
                        page = 1;
                        pickingPresenter.pickingList("",mType,page);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        page++;
                        x = 2;
                        pickingPresenter.pickingList("",mType,page);
                    }
                }, 1000);
            }
        });
        pickingPresenter.pickingList("",mType,page);
        //
    }

    @Override
    public void initViews() {
        super.initViews();
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mType = mBundle.getString("type");
        }
        mRecyclerView = findViewById(R.id.picking_recyview);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mSearchImage = findViewById(R.id.search_image);
        mSearchEdit = findViewById(R.id.search_edit);
        mSearchImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.search_image:
                x = 1;
                page = 1;
                pickingPresenter.pickingList(mSearchEdit.getText().toString(),mType,page);
                mRecyclerView.setLoadingMoreEnabled(true);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchImage.getWindowToken(), 0);
                break;
            case R.id.back:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast,null);
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
    public void setHeader() {
        super.setHeader();
        if(mType.equals("50")){
            mTitle.setText("拣货单");
        }else{
            mTitle.setText("拣货单复核");
        }
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
        switch (type){
            case "list":
                if (x == 1) {
                    mList = (ArrayList) object;
                    if(mList!=null && mList.size()>0){
                        mAdapter = new RecyAdapter();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mAddList = (ArrayList) object;
                    mAdapter.notifiList(mAddList);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast,null);
                finish();
                break;
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logcat("是否回去刷新状态==="+requestCode);
        switch (requestCode){
            case 0:
                x = 1;
                page = 1;
                pickingPresenter.pickingList("",mType,page);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
                break;
        }
    }
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PickingActivity.this).inflate(R.layout.storage_list_item, parent,
                    false));
            return holder;
        }
        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.nameText.setText("拣货订单编号："+mList.get(position).get("stockOutCode"));
            if(mType.equals("50")){
                if (mList.get(position).get("alreadyPickNum") != null && mList.get(position).get("alreadyPickPackageNum") != null){
                    if(Integer.parseInt(mList.get(position).get("alreadyPickNum").toString())>0 || Integer.parseInt(mList.get(position).get("alreadyPickPackageNum").toString())>0){
                        holder.mStates.setText("执行中");
                        holder.mStates.setBackgroundResource(R.mipmap.bg_orange);
                    }else{
                        holder.mStates.setText("未执行");
                        holder.mStates.setBackgroundResource(R.mipmap.bg_red);
                    }
                }
            }else if(mType.equals("65")){
                holder.mStates.setText("未复核");
            }
                if (mList.get(position).get("alreadyPickPackageNum") != null) {
                holder.mYjcdwText.setText("已拣出整数：" + mList.get(position).get("alreadyPickPackageNum").toString());
            }
            if (mList.get(position).get("noPickPackageNum") != null) {
                holder.mNojcdwText.setText("待拣出整数：" + mList.get(position).get("noPickPackageNum").toString());
            }
            if (mList.get(position).get("alreadyPickNum") != null) {
                holder.mYjcgText.setText("已拣出零散数：" + mList.get(position).get("alreadyPickNum").toString());
            }
            if (mList.get(position).get("noPickNum") != null) {
                holder.mNojcgText.setText("待拣出零散数：" + mList.get(position).get("noPickNum").toString());
            }
//            if (mList.get(position).get("noPickNum") != null) {
//                holder.mProductUser.setText("货主：" + mList.get(position).get("noPickNum").toString());
//            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("stockInId",mList.get(position).get("stockOutId")+"");
                    if(mType.equals("50")){//捡出货品
                        bundle.putString("sendType",mList.get(position).get("sendType")+"");
                        openActivityForResult(PickingGoodsActivity.class,0,bundle);
//                        openActivityForResult(PickingGoodsDetailListActivity.class,0,bundle);
                    }else if(mType.equals("65")){//货品复核
//                        openActivityForResult(ReviewGoodsActivity.class,0,bundle);
                        openActivityForResult(ReviewGoodsDetailListActivity.class,0,bundle);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView nameText,mYjcdwText,mNojcdwText,mYjcgText,mNojcgText,mProductUser;
            Button mStates;
            LinearLayout mContentLayout,mUserLayout;
            public MyViewHolder(View view) {
                super(view);
                mUserLayout = view.findViewById(R.id.product_user_layout);
                mUserLayout.setVisibility(View.GONE);
                mProductUser = view.findViewById(R.id.product_user);

                mStates = view.findViewById(R.id.item_state);
                nameText = view.findViewById(R.id.order_name);
                mContentLayout = view.findViewById(R.id.content_layout);
                mYjcdwText = view.findViewById(R.id.already_packge_text);
                mNojcdwText = view.findViewById(R.id.no_packge_text);
                mYjcgText = view.findViewById(R.id.already_num_text);
                mNojcgText = view.findViewById(R.id.no_num_text);
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
                    x = 1;
                    page = 1;
                    pickingPresenter.pickingList(str,mType,page);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
            }
        }
    };
}
