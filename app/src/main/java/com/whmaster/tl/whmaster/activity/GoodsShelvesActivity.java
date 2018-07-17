package com.whmaster.tl.whmaster.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/6.
 */

public class GoodsShelvesActivity extends BaseActivity implements IMvpView {
    private ImageView mOpenOrClose;
    private LinearLayout mVisiableLayout;
    //    private ImageView mLeftAddImage, mLeftReduceImage, mRightAddImage, mRightReduceImage;
//    private EditText mLeftText, mRightText;
    private Bundle mBundle;
    private String mType = "", mDetailId, mBusinessType, storePositionId;
    private Button mSubBtn;
    private int mLeft = 0, mRight = 0, mMaxLeft = 0, mMaxRight = 0;
    private StoragePresenter storagePresenter;
    private TextView mNameText, mSkuText, mJhsjdwText, mSjsjdwText, mJhsjgText, mSjsjgText;
    private ArrayList<ArrayMap<String, Object>> mList;
    //    private TextView mKqText, mKwText, mNumberText, mJhsjdwText2, mSjsjdwText2, mJhsjgText2, mSjsjgText2;
//    private MsgLoadingDialog msgLoadingDialog;
    private String mJhzLeft, mSjzLeft, mJhgRight, mSjgRight;
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private ArrayList<ArrayMap<String,Object>> list;
    private ArrayMap<String,Object> map;

    @Override
    protected int getLayoutId() {
        return R.layout.goods_shelves_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        storagePresenter = new StoragePresenter(this, this);
        storagePresenter.queryStorePositionDetail(mDetailId, mBusinessType);
//        msgLoadingDialog = new MsgLoadingDialog(this);

    }

    @Override
    public void initViews() {
        super.initViews();
        mRecyclerView = findViewById(R.id.goods_shelves_recyview);
//        mKqText = findViewById(R.id.kq_text);
//        mKwText = findViewById(R.id.kw_text);
//        mNumberText = findViewById(R.id.number_text);
//        mJhsjdwText2 = findViewById(R.id.jhsj_dw_text2);
//        mSjsjdwText2 = findViewById(R.id.sjsj_dw_text2);
//        mJhsjgText2 = findViewById(R.id.jhsj_g_text2);
//        mSjsjgText2 = findViewById(R.id.sjsj_g_text2);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mType = mBundle.getString("type");
            mDetailId = mBundle.getString("detailId");

            mBusinessType = "20";
            mJhzLeft = "计划拣出整数：";
            mSjzLeft = "实际拣出整数：";
            mJhgRight = "计划拣出零散数：";
            mSjgRight = "计划拣出零散数：";
//                mSjsjdwText2.setText("实际拣出整数：");
//                mSjsjgText2.setText("实际拣出零散数：");
        }
        mSjsjgText = findViewById(R.id.sjsj_g_text);
        mSjsjdwText = findViewById(R.id.sjsj_dw_text);
        mJhsjdwText = findViewById(R.id.jhsj_dw_text);
        mJhsjgText = findViewById(R.id.jhsj_g_text);
        mNameText = findViewById(R.id.goods_name_text);
        mSkuText = findViewById(R.id.goods_sku_text);
        mOpenOrClose = findViewById(R.id.open_close_image);
        mOpenOrClose.setOnClickListener(this);
        mVisiableLayout = findViewById(R.id.visiable_layout);
        mSubBtn = findViewById(R.id.sub_btn);
//        mLeftAddImage = findViewById(R.id.left_add_image);
//        mLeftReduceImage = findViewById(R.id.left_reduce_image);
//        mLeftText = findViewById(R.id.left_text);
//        mRightAddImage = findViewById(R.id.right_add_image);
//        mRightReduceImage = findViewById(R.id.right_reduce_image);
//        mRightText = findViewById(R.id.right_text);
//        mLeftAddImage.setOnClickListener(this);
//        mLeftReduceImage.setOnClickListener(this);
//        mRightAddImage.setOnClickListener(this);
//        mRightReduceImage.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //上架完毕
            case R.id.sub_btn:
//                if(Integer.parseInt(mLeftText.getText().toString())>0 || Integer.parseInt(mRightText.getText().toString())>0){
//                        ArrayList list = new ArrayList();
//                        ArrayMap map = new ArrayMap();
//                        map.put("actPackageNum", mLeft + "");
//                        map.put("actNum", mRight + "");
//                        map.put("storePositionId", storePositionId);
//                        list.add(map);
                logcat("list数据请求"+list);
                if(mLeft>0 || mRight>0){
                    storagePresenter.shelfProductStockOutTask(mDetailId, mLeft + "", mRight + "", JSON.toJSONString(list));
                }else{
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("整数或零散数为0！")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.left_add_image:
//                if (mLeft < mMaxLeft) {
//                    mLeft++;
//                    mLeftText.setText(mLeft + "");
//                    mSjsjdwText.setText(mSjzLeft + mLeft + "");
//                } else {
//                    mAlertDialog.builder().setTitle("提示")
//                            .setMsg("数量不能大于" + mMaxLeft)
//                            .setPositiveButton("确认", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                }
//                            }).show();
//                }
                break;
            case R.id.left_reduce_image:
//                if (mLeft > 0) {
//                    mLeft--;
//                    mLeftText.setText(mLeft + "");
//                    mSjsjdwText.setText(mSjzLeft + mLeft + "");
//                }
                break;
            case R.id.right_add_image:
//                if (mRight < mMaxRight) {
//                    mRight++;
//                    mRightText.setText(mRight + "");
//                    mSjsjgText.setText(mSjgRight + mRight + "");
//                } else {
//                    mAlertDialog.builder().setTitle("提示")
//                            .setMsg("数量不能大于" + mMaxRight)
//                            .setPositiveButton("确认", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                }
//                            }).show();
//                }
                break;
            case R.id.right_reduce_image:
//                if (mRight > 0) {
//                    mRight--;
//                    mRightText.setText(mRight + "");
//                    mSjsjgText.setText(mSjgRight + mRight + "");
//                }
                break;
            case R.id.open_close_image:
                if (mVisiableLayout.getVisibility() == View.VISIBLE) {
                    mVisiableLayout.setVisibility(View.GONE);
                    mOpenOrClose.setImageResource(R.mipmap.icondown);
                } else {
                    mVisiableLayout.setVisibility(View.VISIBLE);
                    mOpenOrClose.setImageResource(R.mipmap.iconup);
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
    public void setHeader() {
        super.setHeader();
        mTitle.setText("数量拣货");
        mSubBtn.setText("拣货完毕");
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
        final ArrayMap<String, Object> map = (ArrayMap<String, Object>) object;
        switch (type) {
            case "detail":
                if (map != null) {
                    list = new ArrayList();
                    mList = Constants.getJsonArray(map.get("storePositionList") + "");
                    mNameText.setText("货品名称：" + map.get("productName"));
                    mSkuText.setText("货品SKU码：" + map.get("productSku"));
                    mJhsjdwText.setText("计划拣出整数：" + map.get("planPackageNum"));
                    mSjsjdwText.setText("实际拣出整数：" + map.get("planPackageNum"));
                    mJhsjgText.setText("计划拣出零散数：" + map.get("planNum"));
                    mSjsjgText.setText("实际拣出零散数：" + map.get("planNum"));

                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
//                        mJhsjdwText2.setText(mJhzLeft+ map.get("planPackageNum"));
//                        mJhsjgText2.setText(mJhgRight+ map.get("planNum"));
//                        mLeftText.setText(map.get("planPackageNum") + "");
//                        mRightText.setText(map.get("planNum") + "");
                    mLeft = Integer.parseInt(map.get("planPackageNum") + "");
                    mRight = Integer.parseInt(map.get("planNum") + "");
//                        if (mList != null && mList.size() > 0) {
//                            storePositionId = mList.get(0).get("storePositionId") + "";
//                            String s = mList.get(0).get("positionCode").toString();
//                            int index = s.indexOf('-', s.indexOf('-') + 1);
//                            if (index >= 0) {
//                                String ss = s.substring(index + 1);
//                                mKwText.setText("库位：" + ss);
//                            }
//                            mKqText.setText("库区：" + mList.get(0).get("regionName"));
//                            mNumberText.setText("批次号：" + mList.get(0).get("batchNo"));
//                        }
//                        mLeftText.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                            }
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                if(s!=null && !s.toString().equals("")){
//                                    int a = Integer.parseInt(s.toString());
//                                    if(a<0){
//                                        mLeftText.setText("0");
//                                    }else if(a>mMaxLeft){
//                                        mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于"+mMaxLeft)
//                                                .setPositiveButton("确认", new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {}}).show();
//                                        mLeftText.setText(mMaxLeft+"");
//                                    }
//                                }else{
//                                    mLeftText.setText("0");
//                                }
//                            }
//                            @Override
//                            public void afterTextChanged(Editable s) {
//                            }
//                        });
//                        mRightText.addTextChangedListener(new TextWatcher() {
//                            @Override
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                            }
//                            @Override
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                if(s!=null && !s.toString().equals("")){
//                                    int a = Integer.parseInt(s.toString());
//                                    if(a<0){
//                                        mRightText.setText("0");
//                                    }else if(a>mMaxRight){
//                                        mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于"+mMaxRight)
//                                                .setPositiveButton("确认", new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {}}).show();
//                                        mRightText.setText(mMaxRight+"");
//                                    }
//                                }else{
//                                    mRightText.setText("0");
//                                }
//                            }
//                            @Override
//                            public void afterTextChanged(Editable s) {
//                            }
//                        });
                }
                break;
            case "up":
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("确定返回吗？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        }).show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        int left = 0, right = 0, maxLeft = 0, maxRight = 0;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    GoodsShelvesActivity.this).inflate(R.layout.goods_shelves_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyAdapter.MyViewHolder holder, final int position) {

            if (mList.get(position).get("planPackageNum") != null && !mList.get(position).get("planPackageNum").toString().equals("")) {
                holder.planZs.setText("计划拣出整数：" + mList.get(position).get("planPackageNum").toString());
                holder.mZs.setText(mList.get(position).get("planPackageNum").toString());
                maxLeft = Integer.parseInt(mList.get(position).get("planPackageNum") + "");
                left = maxLeft;
            } else {
                holder.planZs.setText("计划拣出整数：0");
                holder.mZs.setText("0");
            }
            if (mList.get(position).get("planNum") != null && !mList.get(position).get("planNum").toString().equals("")) {
                holder.planNum.setText("计划拣出零散数：" + mList.get(position).get("planNum").toString());
                holder.mNum.setText(mList.get(position).get("planNum").toString());
                maxRight = Integer.parseInt(mList.get(position).get("planNum") + "");
                right = maxRight;
            } else {
                holder.planNum.setText("计划拣出零散数：0");
                holder.mNum.setText("0");
            }
            if (mList.get(position).get("positionCode") != null) {
                String s = mList.get(position).get("positionCode").toString();
                int index = s.indexOf('-', s.indexOf('-') + 1);
                if (index >= 0) {
                    String ss = s.substring(index + 1);
                    holder.kuwei.setText("库位：" + ss);
                }
            }
            if (mList.get(position).get("regionName") != null) {
                holder.kuqu.setText("库区：" + mList.get(position).get("regionName"));
            }
            if (mList.get(position).get("batchNo") != null) {
                holder.number.setText("批次号：" + mList.get(position).get("batchNo"));
            }
            holder.mZs.setTag(position);
            holder.mNum.setTag(position);
            holder.mLeftReduce.setOnClickListener(new onClick(holder));
            holder.mLeftAdd.setOnClickListener(new onClick(holder));
            holder.mRightReduce.setOnClickListener(new onClick(holder));
            holder.mRightAdd.setOnClickListener(new onClick(holder));
            holder.mZs.addTextChangedListener(new leftTextWatch(holder));
            holder.mNum.addTextChangedListener(new rightTextWatch(holder));
            map = new ArrayMap();
            map.put("actPackageNum", left + "");
            map.put("actNum", right + "");
            map.put("storePositionId", mList.get(position).get("storePositionId") + "");
            list.add(map);
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
                        left = Integer.parseInt(holder.mZs.getText().toString());
                        if (left > 0) {
                            left--;
                            mLeft--;
                            holder.mZs.setText(left + "");
                            mSjsjdwText.setText("实际拣出整数：" + mLeft + "");
                            list.get(position).put("actPackageNum",left+"");
                        }
                        break;
                    case R.id.left_add_image:
                        maxLeft = Integer.parseInt(mList.get(position).get("planPackageNum") + "");
                        left = Integer.parseInt(holder.mZs.getText().toString());
                        if (left < maxLeft) {
                            left++;
                            mLeft++;
                            holder.mZs.setText(left + "");
                            mSjsjdwText.setText("实际拣出整数：" + mLeft + "");
                            list.get(position).put("actPackageNum",left+"");
                        }
                        break;
                    case R.id.right_reduce_image:
                        right = Integer.parseInt(holder.mNum.getText().toString());
                        if (right > 0) {
                            right--;
                            mRight--;
                            holder.mNum.setText(right + "");
                            mSjsjgText.setText("实际拣出零散数：" + mRight + "");
                            list.get(position).put("actNum",right+"");
                        }
                        break;
                    case R.id.right_add_image:
                        maxRight = Integer.parseInt(mList.get(position).get("planNum") + "");
                        right = Integer.parseInt(holder.mNum.getText().toString());
                        logcat("right====" + right);
                        if (right < maxRight) {
                            right++;
                            mRight++;
                            holder.mNum.setText(right + "");
                            mSjsjgText.setText("实际拣出零散数：" + mRight + "");
                            list.get(position).put("actNum",right+"");
                        }
                        break;
                }
            }
        }

        public class leftTextWatch implements TextWatcher {
            private MyViewHolder holder;
            int b = 0;
            public leftTextWatch(MyViewHolder viewHolder) {
                this.holder = viewHolder;
                b = Integer.parseInt(holder.mZs.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                {
                    int position = (int) holder.mZs.getTag();
                    maxLeft = Integer.parseInt(mList.get(position).get("planPackageNum") + "");
                    int a = 0;
                    if (s != null && !s.toString().equals("")) {
                        a = Integer.parseInt(s.toString());
                        if (a < 0) {
                            holder.mZs.setText("0");
                        } else if (a > maxLeft) {
                            mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于" + maxLeft)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                            holder.mZs.setText(maxLeft + "");
                        }
                    } else {
                        holder.mZs.setText("0");
                    }
                    left = Integer.parseInt(holder.mZs.getText().toString());
                    list.get(position).put("actPackageNum",left+"");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                mLeft = 0;
                for(int i=0;i<list.size();i++){
                    mLeft = mLeft+Integer.parseInt(list.get(i).get("actPackageNum").toString());
                }
                mSjsjdwText.setText("实际拣出整数：" + mLeft + "");
            }
        }
        public class rightTextWatch implements TextWatcher {
            private MyViewHolder holder;
            int b = 0;
            public rightTextWatch(MyViewHolder viewHolder) {
                this.holder = viewHolder;
                b = Integer.parseInt(holder.mNum.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                {
                    int position = (int) holder.mNum.getTag();
                    maxRight = Integer.parseInt(mList.get(position).get("planNum") + "");
                    int a = 0;
                    if (s != null && !s.toString().equals("")) {
                        a = Integer.parseInt(s.toString());
                        if (a < 0) {
                            holder.mNum.setText("0");
                        } else if (a > maxRight) {
                            mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于" + maxRight)
                                    .setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                        }
                                    }).show();
                            holder.mNum.setText(maxRight + "");
                        }
                    } else {
                        holder.mNum.setText("0");
                    }
                    right = Integer.parseInt(holder.mNum.getText().toString());
                    list.get(position).put("actNum",right+"");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                mRight = 0;
                for(int i=0;i<list.size();i++){
                    mRight = mRight+Integer.parseInt(list.get(i).get("actNum").toString());
                }
                mSjsjgText.setText("实际拣出零散数：" + mRight + "");
            }
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView planNum, planZs, kuqu, kuwei, number;
            EditText mNum, mZs;
            ImageView mLeftReduce, mLeftAdd, mRightReduce, mRightAdd;

            public MyViewHolder(View view) {
                super(view);
                mLeftReduce = view.findViewById(R.id.left_reduce_image);
                mLeftAdd = view.findViewById(R.id.left_add_image);
                mRightReduce = view.findViewById(R.id.right_reduce_image);
                mRightAdd = view.findViewById(R.id.right_add_image);

                planZs = view.findViewById(R.id.jhsj_dw_text2);
                planNum = view.findViewById(R.id.jhsj_g_text2);
                kuqu = view.findViewById(R.id.kq_text);
                kuwei = view.findViewById(R.id.kw_text);
                number = view.findViewById(R.id.number_text);
                mZs = view.findViewById(R.id.left_text);
                mNum = view.findViewById(R.id.right_text);
            }
        }
    }
}
