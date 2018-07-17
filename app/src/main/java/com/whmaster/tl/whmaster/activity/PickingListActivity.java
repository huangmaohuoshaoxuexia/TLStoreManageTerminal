package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.whmaster.tl.whmaster.model.MyDecoration;
import com.whmaster.tl.whmaster.presenter.GoodsRikPresenter;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2018/1/17.
 */

public class PickingListActivity extends BaseActivity implements IMvpView{
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private ImageView mSearchImage,mBackImage;
    private PickingPresenter pickingPresenter;
    private int page = 1,x = 1;
    private Bundle mBundle;
    private String mType,mPickCode="";
    private TextView mTitleText;
    private ArrayList<ArrayMap<String, Object>> mList,mAddList;
    private LinearLayout mEmptyLayout,mTitleLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.picking_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitleText.setText("拣货单");
        mBundle = getIntent().getExtras();
        RecyclerUtil.init(mRecyclerView,this);
        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        x = 1;
                        page = 1;
                        pickingPresenter.pickList(mPickCode,page);
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
                        pickingPresenter.pickList(mPickCode,page);
                    }
                }, 1000);
            }
        });
        pickingPresenter = new PickingPresenter(this,this);
        pickingPresenter.pickList(mPickCode,page);
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mRecyclerView = findViewById(R.id.picking_list_recyview);
        mSearchImage = findViewById(R.id.search);
        mSearchImage.setOnClickListener(this);
        mTitleText = findViewById(R.id.title_text);
        mEmptyLayout = findViewById(R.id.empty_layout);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_image:
                Intent broadcast = new Intent("main");
                sendBroadcast(broadcast, null);
                finish();
                break;
            case R.id.search:
                startActivity(PickingListSearchActivity.class,null);
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
//            mAlertDialog.builder().setTitle("提示")
//                    .setMsg(errorMsg)
//                    .setPositiveButton("确认", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                        }
//                    }).show();
        mRecyclerView.setVisibility(View.GONE);
        mEmptyLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String type, Object object) {
        switch (type){
            case "list":
                if(x == 1){
                    mList = (ArrayList<ArrayMap<String, Object>>) object;
                    if(mList!=null && mList.size()>0){
                        mAdapter = new RecyAdapter();
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mEmptyLayout.setVisibility(View.GONE);
                    }else{
                        mRecyclerView.setVisibility(View.GONE);
                        mEmptyLayout.setVisibility(View.VISIBLE);
                    }
                }else if(x == 2){
                    mAddList = (ArrayList<ArrayMap<String, Object>>) object;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logcat("是否回去刷新状态==="+requestCode);
        switch (requestCode){
            case 0:
                x = 1;
                page = 1;
                pickingPresenter.pickList(mPickCode,page);
                mRecyclerView.refreshComplete();
                mRecyclerView.setLoadingMoreEnabled(true);
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
//        float mVolume,packageCount = 0,mWeight,planPickCount;
//        ArrayList<ArrayMap<String, Object>> mDetlList;
//        ArrayMap<String, Object> proMap,productMap;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PickingListActivity.this).inflate(R.layout.picking_list_item, parent,
                    false));
            return holder;
        }
        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {
            mList.addAll(list);
            this.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            try{
                    String weightV = Constants.format2(mList.get(position).get("planPickWeigth")+"");
                    if(weightV.length()>7){
                        holder.zlText.setTextSize(13);
                    }else{
                        holder.zlText.setTextSize(16);
                    }
                    holder.zlText.setText(weightV+"KG");

                if(mList.get(position).get("planPickCount")!=null){
                    if(mList.get(position).get("planPickCount").toString().length()>7){
                        holder.slText.setTextSize(13);
                    }else{
                        holder.slText.setTextSize(16);
                    }
                    holder.slText.setText(mList.get(position).get("planPickCount").toString());
                }

                float volumes = Float.parseFloat(mList.get(position).get("planPickVolume").toString()) / 1000000;
                String volumeV = Constants.format4(volumes+"");
                if(volumeV.length()>7){
                    holder.tijiText.setTextSize(13);
                }else{
                    holder.tijiText.setTextSize(16);
                }
                holder.tijiText.setText(volumeV+"M³");
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.pickReviewText.setText("拣货数量");
             if(mList.get(position).get("pickCode")!=null){
                holder.orderText.setText("拣货单号："+mList.get(position).get("pickCode"));
            }

            if(mList.get(position).get("pickStatusCode")!=null){
                switch (mList.get(position).get("pickStatusCode").toString()){
                    case "10":
                        holder.statusText.setText("未执行");
                        holder.statusText.setTextColor(getResources().getColor(R.color.color24));
                        holder.statusText.setBackgroundResource(R.drawable.bg_my_style12);
                        break;
                    case "20":
                        holder.statusText.setText("已完成");
                        holder.statusText.setTextColor(getResources().getColor(R.color.color14));
                        holder.statusText.setBackgroundResource(R.drawable.pick_status_complete_style);
                        break;
                    case "30":
                        holder.statusText.setText("执行中");
                        holder.statusText.setTextColor(getResources().getColor(R.color.color31));
                        holder.statusText.setBackgroundResource(R.drawable.pick_statusing_style);
                        break;
                }
            }

            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mList.get(position).get("pickStatusCode").toString().equals("20")){
                        Bundle bundle = new Bundle();
                        bundle.putString("pickCode",mList.get(position).get("pickCode")+"");
                        openActivityForResult(PickingGoodsDetailListActivity.class,0,bundle);
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView orderText,tijiText,zlText,slText,statusText,pickReviewText;
            LinearLayout mContentLayout;
            public MyViewHolder(View view) {
                super(view);
            mContentLayout = view.findViewById(R.id.content_layout);
                pickReviewText = view.findViewById(R.id.pick_review_status);
                orderText = view.findViewById(R.id.order_text);
                tijiText = view.findViewById(R.id.tiji_text);
                zlText = view.findViewById(R.id.zl_text);
                slText = view.findViewById(R.id.sl_text);
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
}
