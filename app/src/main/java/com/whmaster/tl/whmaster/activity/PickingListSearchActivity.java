package com.whmaster.tl.whmaster.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.whmaster.tl.whmaster.widget.AlertDialog;
import com.whmaster.tl.whmaster.widget.LoadingDialog;

import java.util.ArrayList;


/**
 * Created by admin on 2018/1/18.
 */

public class PickingListSearchActivity extends Activity implements View.OnClickListener,IMvpView{

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private ImageView mBackImage,mXimage;
    private TextView mSearchText;
    private PickingPresenter pickingPresenter;
    private String mPickCode="";
    private int page = 1,x = 1;
    private AlertDialog mAlertDialog;
    private ArrayList<ArrayMap<String, Object>> mList,mAddList;
    protected LoadingDialog loadingDialog;
    private String m_Broadcastname;
    private EditText mSearchEdit;
    private LinearLayout mEmptyLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picking_search_layout);
        init();
        mAlertDialog = new AlertDialog(this);
        loadingDialog = new LoadingDialog(this);
        RecyclerUtil.init(mRecyclerView,this);
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

        mRecyclerView.addItemDecoration(new MyDecoration(this, MyDecoration.HORIZONTAL_LIST));
    }
    private void init(){
        mRecyclerView = findViewById(R.id.picking_search_list_recyview);
        mBackImage = findViewById(R.id.back);
        mBackImage.setOnClickListener(this);
        mSearchText = findViewById(R.id.search_text);
        mSearchText.setOnClickListener(this);
        mSearchEdit = findViewById(R.id.search_edit);
        mXimage = findViewById(R.id.x_image);
        mXimage.setOnClickListener(this);
        mEmptyLayout = findViewById(R.id.empty_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.search_text:
                pickingPresenter.pickList(mSearchEdit.getText().toString(),page);
                break;
            case R.id.x_image:
                mSearchEdit.setText("");
                break;
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
                if(x == 1){
                    mList = (ArrayList<ArrayMap<String, Object>>) object;
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
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
    public void showLoading() {
        loadingDialog.builder().show();
    }
    @Override
    public void hideLoading() {
        loadingDialog.dismiss();
    }


    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        float mVolume,packageCount = 0,mWeight,planPickCount;
        ArrayList<ArrayMap<String, Object>> mDetlList;
        ArrayMap<String, Object> proMap,productMap;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    PickingListSearchActivity.this).inflate(R.layout.picking_list_item, parent,
                    false));
            return holder;
        }
        public void notifiList(ArrayList<ArrayMap<String, Object>> list) {

            this.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            try{
                mDetlList = Constants.getJsonArray(mList.get(position).get("detlList").toString());
                proMap = Constants.getJsonObject(mDetlList.get(0).get("productPo").toString());
                productMap = Constants.getJsonObject(proMap.get("product").toString());
                packageCount = Float.parseFloat(productMap.get("packageCount").toString());

                planPickCount = Float.parseFloat(mList.get(position).get("actlPickCount").toString());
                mVolume = Float.parseFloat(mList.get(position).get("packageLength").toString())*Float.parseFloat(mList.get(position).get("packageWidth").toString())*Float.parseFloat(mList.get(position).get("packageHeight").toString()) / packageCount * planPickCount / 1000000;

                mWeight = Float.parseFloat(productMap.get("packageWeight").toString()) / packageCount * planPickCount;
                String weightV = Constants.format2(mWeight+"");
                if(weightV.length()>7){
                    holder.zlText.setTextSize(13);
                }else{
                    holder.zlText.setTextSize(16);
                }
                holder.zlText.setText(weightV+"KG");

                if(mList.get(position).get("actlPickCount")!=null){
                    if(mList.get(position).get("actlPickCount").toString().length()>7){
                        holder.slText.setTextSize(13);
                    }else{
                        holder.slText.setTextSize(16);
                    }
                    holder.slText.setText(mList.get(position).get("actlPickCount").toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            holder.pickReviewText.setText("拣货数量");
            if(mList.get(position).get("pickCode")!=null){
                holder.orderText.setText("拣货单号："+mList.get(position).get("pickCode"));
            }

            String volumeV = Constants.format4(mVolume+"");
            if(volumeV.length()>7){
                holder.tijiText.setTextSize(13);
            }else{
                holder.tijiText.setTextSize(16);
            }
            holder.tijiText.setText(volumeV+"M³");

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
                        Intent intent = new Intent(PickingListSearchActivity.this, PickingGoodsDetailListActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 0);
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
                    mSearchEdit.setText(str);
                    mPickCode = str;
                    x = 1;
                    page = 1;
                    pickingPresenter.pickList(mPickCode,page);
                    mRecyclerView.refreshComplete();
                    mRecyclerView.setLoadingMoreEnabled(true);
                }
            }
        }
    };
}
