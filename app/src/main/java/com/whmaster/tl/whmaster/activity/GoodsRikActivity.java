package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.GoodsRikPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;
import java.util.ArrayList;

/**
 * Created by admin on 2017/11/24.
 * 实物收货详情页
 */

public class GoodsRikActivity extends BaseActivity implements IMvpView{

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private Button mConfirmBtn;
    private GoodsRikPresenter goodsRikPresenter;
    private int page = 1,x = 1;
    private LinearLayout mContentLayout,mEmptyLayout;
    private String mOrderInId,mOrderInCode;
    private Bundle mBundle;
    private ArrayList<ArrayMap<String,Object>> mList,mAddList;
    private ArrayMap<String,Object> mMap;
    private TextView mRkdCode;
    //mOrderInId="TL8b8b2f4920a9451d9b58e642165ce697
    @Override
    protected int getLayoutId() {
        return R.layout.goods_rik_layout;
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
                        goodsRikPresenter.getGoodsDetailList(page,mOrderInId,"","","");
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
                        goodsRikPresenter.getGoodsDetailList(page,mOrderInId,"","","");
                    }
                }, 1000);
            }
        });
        mRkdCode.setText("入库订单编号："+mOrderInCode);
        goodsRikPresenter = new GoodsRikPresenter(this,this);
        goodsRikPresenter.getGoodsDetailList(page,mOrderInId,"","","");
    }

    @Override
    public void initViews() {
        super.initViews();
        mRkdCode = findViewById(R.id.rkd_code);
        mRecyclerView = findViewById(R.id.goods_rik_rcview);
        mConfirmBtn = findViewById(R.id.sub_btn);
        mConfirmBtn.setOnClickListener(this);
        mContentLayout = findViewById(R.id.content_layout);
        mEmptyLayout = findViewById(R.id.empty_layout);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mOrderInCode = mBundle.getString("orderInCode");
            mOrderInId = mBundle.getString("orderInId");

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub_btn:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("确认收货并生成上架单？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goodsRikPresenter.updateStatus(mOrderInId,"","33");
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
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
        mTitle.setText("实物收货");
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
        switch (type){
            case "list":
                mMap = (ArrayMap<String, Object>) object;
                if(x==1){
                    mList = Constants.getJsonArray(mMap.get("detailList").toString());
                    if(mList!=null && mList.size()>0){
                        mAdapter = new RecyAdapter();
                        mRecyclerView.setAdapter(mAdapter);
                        mEmptyLayout.setVisibility(View.GONE);
                        mContentLayout.setVisibility(View.VISIBLE);
                    }else{
                        mEmptyLayout.setVisibility(View.VISIBLE);
                        mContentLayout.setVisibility(View.GONE);
                    }
                }else if (x == 2) {
                    mAddList = Constants.getJsonArray(mMap.get("detailList").toString());
                    mAdapter.notifiList(mAddList);
                    mRecyclerView.loadMoreComplete();
                    if (mAddList.size() < 10) {
                        mRecyclerView.setLoadingMoreEnabled(false);
                    }else{
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }
                break;
            case "updateSuccess":
                finish();
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
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    GoodsRikActivity.this).inflate(R.layout.goods_rik_list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if(mList.get(position).get("productName")!=null){
                holder.mProductName.setText("货品名称："+mList.get(position).get("productName")+"");
            }
            if(mList.get(position).get("planNum")!=null){
                holder.mProductNum.setText("货品数量："+mList.get(position).get("planNum"));
            }
//            if(mList.get(position).get("planPackageNum")!=null && !mList.get(position).get("planPackageNum").toString().equals("")){
//                int planPack = Integer.parseInt(mList.get(position).get("planPackageNum").toString());
//                if(mList.get(position).get("planNum")!=null && !mList.get(position).get("planNum").toString().equals("")){
//                    if(Integer.parseInt(mList.get(position).get("planNum").toString())>0){
//                        planPack++;
//                        holder.mProductNum.setText("货品数量："+planPack+"");
//                    }else{
//                        holder.mProductNum.setText("货品数量："+planPack+"");
//                    }
//                }
//            }
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

            TextView mProductName,mProductNum;

            public MyViewHolder(View view) {
                super(view);
                mProductName = view.findViewById(R.id.order_name);
                mProductNum = view.findViewById(R.id.product_num);
            }
        }
    }
}
