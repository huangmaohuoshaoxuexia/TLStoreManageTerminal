package com.whmaster.tl.whmaster.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;

/**
 * Created by admin on 2017/12/1.
 */

public class RecyclerUtil {
//    private static XRecyclerView mRecyclerView;
    private RecyclerUtil(Context context){

    }
    public static void init(XRecyclerView mRecyclerView,Context context) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
    }
}
