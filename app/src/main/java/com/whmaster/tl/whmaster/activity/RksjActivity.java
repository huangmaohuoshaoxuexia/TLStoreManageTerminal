package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/27.
 * 入库上架
 */

public class RksjActivity extends BaseActivity implements IMvpView {

    private StoragePresenter storagePresenter;
    private Bundle mBundle;
    private String mBusinessType = "10", mDetailId, storePositionId;
    private TextView mProductName, mSkuText, mKuquText, mKuweiText, mPlanZs, mPlanNum;
//    private ImageView mLeftReduce, mLeftAdd, mRightReduce, mRightAdd;
    private EditText mActualZs, mActualNum;
    private ArrayList<ArrayMap<String, Object>> mList;
    private int mMaxLeft, mMaxRight, mLeft, mRight,mPackageCount = 0,mMax = 0;
    private Button mSubBtn;
    private XRecyclerView mRecyclerView;
//    private ArrayList<ArrayMap<String, Object>> list;
    private ArrayMap<String, Object> map;
    private RecyAdapter mAdapter;
    private HashMap<String, Object> mDatamap;
    @Override
    protected int getLayoutId() {
        return R.layout.rksj_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        storagePresenter = new StoragePresenter(this, this);
        storagePresenter.queryStorePositionDetail(mDetailId, mBusinessType);
    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.rksj_recyview);
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
        mProductName = findViewById(R.id.product_name);
        mSkuText = findViewById(R.id.product_sku);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mDetailId = mBundle.getString("detailId");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sub_btn:
                mDatamap = new HashMap<>();
                int counts = mLeft*mPackageCount + mRight;
                if(counts <= mMax){
                    mDatamap.put("stockInDetailId", mDetailId);
                    mDatamap.put("actNum", counts + "");
                    mDatamap.put("storePositionId", storePositionId);
                    storagePresenter.shelfProductStockInTask(mDatamap);
                }else{
                    Toast.makeText(this,"上架数量大于计划上架总量！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.left_reduce_image:
                if (mLeft > 0) {
                    mLeft--;
                    mActualZs.setText(mLeft + "");
                }
                break;
            case R.id.left_add_image:
                if (mLeft < mMaxLeft) {
                    mLeft++;
                    mActualZs.setText(mLeft + "");
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能大于" + mMaxLeft)
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.right_reduce_image:
                if (mRight > 0) {
                    mRight--;
                    mActualNum.setText(mRight + "");
                }
                break;
            case R.id.right_add_image:
                if (mRight < mMaxRight) {
                    mRight++;
                    mActualNum.setText(mRight + "");
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能大于" + mMaxRight)
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
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
        mTitle.setText("入库上架");
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

    private void callBack() {
        Bundle bundle = new Bundle();
        bundle.putString("type", "refresh");
        setResultOk(bundle);
    }

    @Override
    public void onSuccess(String type, Object object) {
        final ArrayMap<String, Object> map = (ArrayMap<String, Object>) object;
        switch (type) {
            case "detail":
                if (map != null && map.size() > 0) {
                    if (map.get("packageCount") != null && !map.get("packageCount").toString().equals("")) {
                        mPackageCount = Integer.parseInt(map.get("packageCount").toString());
                        int packageCount = Integer.parseInt(map.get("planNum").toString()) / mPackageCount;
                        int numCount = Integer.parseInt(map.get("planNum").toString()) % mPackageCount;
                        mMax = Integer.parseInt(map.get("planNum").toString());
                        mMaxLeft = packageCount;
                        mLeft = packageCount;
                        mMaxRight = Integer.parseInt(map.get("planNum").toString());
                        mRight = numCount;
                    }

                    mDatamap = new HashMap<>();
                    mList = new ArrayList<>();
                    mList.add(map);
                    logcat("获取json数据============");
                    mProductName.setText("货品名称：" + map.get("productName"));
                    mSkuText.setText("货品SKU码：" + map.get("productSku"));
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                }
                break;
            case "up":
                callBack();
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
                    RksjActivity.this).inflate(R.layout.rksj_item_layout, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyAdapter.MyViewHolder holder, final int position) {

            if (mList.get(position).get("packageCount") != null && !mList.get(position).get("packageCount").toString().equals("")) {
                holder.planZs.setText(mLeft+"");
                holder.mZs.setText(mLeft+"");

                holder.planNum.setText(mRight+"");
                holder.mNum.setText(mRight+"");
            } else {
                holder.planZs.setText("0");
                holder.mZs.setText("0");
                holder.planNum.setText("0");
                holder.mNum.setText("0");
            }
            storePositionId = mList.get(position).get("storePositionId") + "";
            holder.kuwei.setText("" + mList.get(position).get("positionName"));
            holder.kuqu.setText("" + mList.get(position).get("regionName"));

            holder.mZs.setTag(position);
            holder.mNum.setTag(position);
            holder.mLeftReduce.setOnClickListener(new onClick(holder));
            holder.mLeftAdd.setOnClickListener(new onClick(holder));
            holder.mRightReduce.setOnClickListener(new onClick(holder));
            holder.mRightAdd.setOnClickListener(new onClick(holder));
            holder.mZs.addTextChangedListener(new leftTextWatch(holder));
            holder.mNum.addTextChangedListener(new rightTextWatch(holder));
            mDatamap.put("stockInDetailId", mDetailId);
            mDatamap.put("actNum", (mLeft*mPackageCount + mRight) + "");
            mDatamap.put("storePositionId", storePositionId);
//            list.add(map);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public class onClick implements View.OnClickListener {
            private MyViewHolder holder;

            public onClick(MyViewHolder viewholder) {
                this.holder = viewholder;
            }

            @Override
            public void onClick(View v) {
                int position = (int) holder.mZs.getTag();
                switch (v.getId()) {
                    case R.id.left_reduce_image:
                        mLeft = Integer.parseInt(holder.mZs.getText().toString());
                        if (mLeft > 0) {
                            mLeft--;
                            holder.mZs.setText(mLeft + "");
//                            mDatamap.put("actPackageNum", left + "");
                        }
                        break;
                    case R.id.left_add_image:
//                        maxLeft = Integer.parseInt(mList.get(position).get("planPackageNum") + "");
                        mLeft = Integer.parseInt(holder.mZs.getText().toString());
                        if (mLeft < mMaxLeft) {
                            mLeft++;
                            holder.mZs.setText(mLeft + "");
//                            mDatamap.put("actPackageNum", left + "");
                        }
                        break;
                    case R.id.right_reduce_image:
                        mRight = Integer.parseInt(holder.mNum.getText().toString());
                        if (mRight > 0) {
                            mRight--;
                            holder.mNum.setText(mRight + "");
//                            mDatamap.put("actNum", mRight + "");
                        }
                        break;
                    case R.id.right_add_image:
//                        maxRight = Integer.parseInt(mList.get(position).get("planNum") + "");
                        mRight = Integer.parseInt(holder.mNum.getText().toString());
                        if (mRight < mMaxRight) {
                            mRight++;
                            holder.mNum.setText(mRight + "");
//                            mDatamap.put("actNum", right + "");
                        }
                        break;
                }
            }
        }

        public class leftTextWatch implements TextWatcher {
            private MyViewHolder holder;
            public leftTextWatch(MyViewHolder viewHolder) {
                this.holder = viewHolder;
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                {
                    int position = (int) holder.mZs.getTag();
//                    maxLeft = Integer.parseInt(mList.get(position).get("planPackageNum") + "");
//                    maxLeft = Integer.parseInt(mList.get(position).get("planNum").toString()) / Integer.parseInt(mList.get(position).get("packageCount").toString());
                    int a = 0;
                    if (s != null && !s.toString().equals("")) {
                        a = Integer.parseInt(s.toString());
                        if (a < 0) {
                            holder.mZs.setText("0");
                        } else if (a > mMaxLeft) {
                            mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于" + mMaxLeft)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                            holder.mZs.setText(mMaxLeft + "");
                        }
                    } else {
                        holder.mZs.setText("0");
                    }
                    mLeft = Integer.parseInt(holder.mZs.getText().toString());
//                    mDatamap.put("actPackageNum", left + "");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        }

        public class rightTextWatch implements TextWatcher {
            private MyViewHolder holder;
            public rightTextWatch(MyViewHolder viewHolder) {
                this.holder = viewHolder;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                {
                    int position = (int) holder.mNum.getTag();
//                    maxRight = Integer.parseInt(mList.get(position).get("planNum") + "");
                    int a = 0;
                    if (s != null && !s.toString().equals("")) {
                        a = Integer.parseInt(s.toString());
                        if (a < 0) {
                            holder.mNum.setText("0");
                        } else if (a > mMaxRight) {
                            mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于" + mMaxRight)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                            holder.mNum.setText(mMaxRight + "");
                        }
                    } else {
                        holder.mNum.setText("0");
                    }
                    mRight = Integer.parseInt(holder.mNum.getText().toString());
//                    mDatamap.put("actNum", right + "");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView planNum, planZs, kuqu, kuwei;
            EditText mNum, mZs;
            ImageView mLeftReduce, mLeftAdd, mRightReduce, mRightAdd;

            public MyViewHolder(View view) {
                super(view);
                mLeftReduce = view.findViewById(R.id.left_reduce_image);
                mLeftAdd = view.findViewById(R.id.left_add_image);
                mRightReduce = view.findViewById(R.id.right_reduce_image);
                mRightAdd = view.findViewById(R.id.right_add_image);

                planZs = view.findViewById(R.id.plan_zs_text);
                planNum = view.findViewById(R.id.plan_num_text);
                kuqu = view.findViewById(R.id.library_qu);
                kuwei = view.findViewById(R.id.library_wei);
                mZs = view.findViewById(R.id.left_text);
                mNum = view.findViewById(R.id.right_text);
            }
        }
    }
}
