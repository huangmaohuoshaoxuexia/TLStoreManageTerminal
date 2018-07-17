package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.whmaster.tl.whmaster.widget.MsgLoadingDialog;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/29.
 * 货品复核
 */

public class ReviewGoodsActivity extends BaseActivity implements IMvpView{

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private Bundle mBundle;
    private String mType="",mId;
    private ArrayList<ArrayMap<String,Object>> mList;
    private PickingPresenter pickingPresenter;
    private boolean isExecute = true;
    private Button mSubBtn;
//    private MsgLoadingDialog msgLoadingDialog;
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
        pickingPresenter = new PickingPresenter(this,this);
        pickingPresenter.pickingGoodsList(mId);
        msgLoadingDialog = new MsgLoadingDialog(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.storage_goods_recyview);
        mBundle = getIntent().getExtras();
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
        if(mBundle!=null){
            mId = mBundle.getString("stockInId");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub_btn:
//                    msgLoadingDialog.builder().setMsg("正在复核").show();
                    pickingPresenter.fhCheck(mId);
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
        mTitle.setText("货品复核");
        mSubBtn.setText("复核完毕");
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

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ReviewGoodsActivity.this).inflate(R.layout.review_goods_list_item, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {
            if(mList.get(position).get("productName")!=null){
                holder.name.setText("货品名称："+mList.get(position).get("productName"));
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

            if(mList.get(position).get("stockOutDetailStatus").toString().equals("10")){
                isExecute = false;
            }
            holder.imageView.setImageResource(R.mipmap.icon02);
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mList.get(position).get("stockOutDetailStatus").toString().equals("10")){
                        Bundle bundle = new Bundle();
                        bundle.putString("detailId",mList.get(position).get("stockOutDetailId")+"");
                        bundle.putString("businessType",mList.get(position).get("stockInDetailStatus")+"");
                        bundle.putString("type","picking");
                        openActivityForResult(GoodsShelvesActivity.class,0,bundle);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,mNoPackgeNum,mNoNum,mAlreadyNum,mAlreadyPackgeNum;
            ImageView imageView;
            LinearLayout mContentLayout,mUserLayout;
            public MyViewHolder(View view) {
                super(view);
                mUserLayout = view.findViewById(R.id.user_layout);
                mUserLayout.setVisibility(View.GONE);
                name = view.findViewById(R.id.order_name);
                imageView = view.findViewById(R.id.icon_image);
                mContentLayout = view.findViewById(R.id.content_layout);
                mAlreadyPackgeNum = view.findViewById(R.id.already_packge_text);
                mNoPackgeNum = view.findViewById(R.id.no_packge_text);
                mAlreadyNum = view.findViewById(R.id.already_num_text);
                mNoNum = view.findViewById(R.id.no_num_text);

            }
        }
    }
}
