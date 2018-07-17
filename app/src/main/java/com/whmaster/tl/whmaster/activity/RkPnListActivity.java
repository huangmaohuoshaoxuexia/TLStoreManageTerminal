package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.view.IMvpView;

/**
 * Created by admin on 2017/11/27.
 */

public class RkPnListActivity extends BaseActivity implements IMvpView{
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.rk_pn_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
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
        mAdapter = new RecyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.rk_pn_recyview);
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
        mTitle.setText("入库上架");
    }

    @Override
    public void onFail(String errorMsg) {

    }

    @Override
    public void onSuccess(String type, Object object) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    RkPnListActivity.this).inflate(R.layout.rk_pro_name_list_item, parent,
                    false));
            return holder;
        }


        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout mContentLayout;

            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
            }
        }
    }
}
