package com.whmaster.tl.whmaster.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.whmaster.tl.whmaster.R;
import java.util.ArrayList;
/**
 * Created by admin on 2017/10/20.
 */

public class StorageListApter extends RecyclerView.Adapter<StorageListApter.MyViewHolder>{
    private Context mContext;
    public StorageListApter(Context context){
        this.mContext = context;
    }
    @Override
    public StorageListApter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.storage_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(StorageListApter.MyViewHolder holder, int position) {
//        holder.tv.setText("测试名字");
//        holder.mLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("点击跳转","=============");
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        LinearLayout mLayout;
        ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
        }
    }
}
