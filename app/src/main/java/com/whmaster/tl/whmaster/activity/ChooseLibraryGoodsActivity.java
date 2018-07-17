package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.LibraryPresenter;
import com.whmaster.tl.whmaster.utils.AtyContainerUtils;
import com.whmaster.tl.whmaster.utils.DensityUtils;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;
import com.whmaster.tl.whmaster.widget.SlideView;
import com.whmaster.tl.whmaster.widget.SlideView2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2017/11/14.
 * 选择货品
 */

public class ChooseLibraryGoodsActivity extends BaseActivity implements IMvpView,SlideView2.onSuccessInterface{

    private XRecyclerView xRecyclerView;
    private RecyAdapter mAdapter;
    private SlideView2 mNextBtn;
    private Bundle mBundle;
    private String mPositionId,mCode,mPosCode;
    private LibraryPresenter libraryPresenter;
    private ArrayList<ArrayMap<String,Object>> mList;
    private TextView mOldText;
    private boolean isNext = false;
    private LinearLayout mEmptyLayout,mTitleLayout;
    private ArrayList<ArrayMap<String,Object>> mDataList;
    private ArrayMap<String,Object> mDataMap;
    private ImageView mBackImage;
    private Map<Integer, Boolean> mIsCheckMap = new HashMap<Integer, Boolean>();
    @Override
    protected int getLayoutId() {
        return R.layout.chose_goods_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addLibraryActivity(this);
        libraryPresenter = new LibraryPresenter(this,this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mPositionId = mBundle.getString("positionId");
            mCode = mBundle.getString("code");
            mPosCode = mBundle.getString("posCode");
//            libraryPresenter.getListByPositionCode(moldPositionCode);
            mOldText.setText("库位码："+mPosCode);
        }
        RecyclerUtil.init(xRecyclerView,this);
        xRecyclerView.setLoadingMoreEnabled(false);
        xRecyclerView.setPullRefreshEnabled(false);
        libraryPresenter.getProductByPosition(mCode);
//        libraryPresenter.getProductByPosition(mPositionCode);
        mDataList = new ArrayList<>();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_image:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("是否重新选择库位？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("是否重新选择库位？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAlertDialog.dismiss();
                            }
                        })
                        .show();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mNextBtn = findViewById(R.id.sub_btn);
        mNextBtn.setOnSuccessListener(this);
        xRecyclerView = findViewById(R.id.choose_goods_recy_view);
        mOldText = findViewById(R.id.old_code);
        mEmptyLayout = findViewById(R.id.empty_layout);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                isNext = false;
                mNextBtn.resetXy();
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
        mTitleLayout.setVisibility(View.GONE);
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
                mDataMap = (ArrayMap<String, Object>) object;
                mPositionId = mDataMap.get("positionId").toString();
                mList = Constants.getJsonArray(mDataMap.get("moveProductList").toString());
//                mList = (ArrayList<ArrayMap<String, Object>>) object;
                if(mList!=null && mList.size()>0){
                    for (int i = 0; i < mList.size(); i++) {
                        // 设置默认的显示
                        mIsCheckMap.put(i, false);
                    }
                    mAdapter = new RecyAdapter();
                    xRecyclerView.setAdapter(mAdapter);
                    xRecyclerView.setVisibility(View.VISIBLE);
                    mEmptyLayout.setVisibility(View.GONE);
                    mNextBtn.setVisibility(View.VISIBLE);
                }else{
                    mNextBtn.setVisibility(View.GONE);
                    xRecyclerView.setVisibility(View.GONE);
                    mEmptyLayout.setVisibility(View.VISIBLE);
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
    public void onExcute() {
        if(mDataList!=null && mDataList.size()>0){
            Bundle bundle = new Bundle();
            bundle.putSerializable("list",mDataList);
            bundle.putString("code",mCode);
            bundle.putString("positionId",mPositionId);
            bundle.putString("posCode",mPosCode);
            openActivityForResult(ChooseGoodsNumbersActivity.class,0,bundle);
//            startActivity(ChooseGoodsNumbersActivity.class,bundle);
        }else{
            handler.sendEmptyMessageDelayed(0, 500);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mNextBtn.resetXy();
                    break;
            }
        }
    };
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        MyViewHolder holder;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            holder = new MyViewHolder(LayoutInflater.from(
                    ChooseLibraryGoodsActivity.this).inflate(R.layout.chose_goods_list_item_layout, parent,
                    false));

            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            if(mList.get(position).get("productName")!=null){
                holder.productName.setText(mList.get(position).get("productName").toString());
            }
            if(mList.get(position).get("productSkuCode")!=null){
                holder.productSku.setText(mList.get(position).get("productSkuCode").toString());
            }
            if(mList.get(position).get("batchNo")!=null){
                holder.productNo.setText(mList.get(position).get("batchNo").toString());
            }
            if(mList.get(position).get("inventoryNum")!=null){
                holder.sumNumbers.setText(mList.get(position).get("inventoryNum").toString()+mList.get(position).get("baseUnitCn")+"");
            }
            if(mList.get(position).get("packageSpec")!=null){
                holder.productGuige.setText(mList.get(position).get("packageSpec").toString());
            }
            if(mList.get(position).get("packageCount")!=null){
                int packCount =  Integer.parseInt(mList.get(position).get("packageCount").toString());
                if(packCount>0){
//                logcat(mList.get(position).get("packageCount").toString()+"===="+packCount);
                int moveNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) % packCount;
                int moveZs = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) / packCount;
                if(moveZs>0){
                    holder.productNumbers.setText(moveZs+mList.get(position).get("packageUnitCn").toString() + "" + moveNum+mList.get(position).get("baseUnitCn").toString());
                }else{
                    holder.productNumbers.setText(moveNum+mList.get(position).get("baseUnitCn").toString());
                }
                }
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mIsCheckMap.put(position, isChecked);
                    if(isChecked){
                        mDataList.add(mList.get(position));
                    }else{
                        mDataList.remove(mList.get(position));
                    }
                }
            });
            if (mIsCheckMap.get(position) == null) {
                mIsCheckMap.put(position, false);
            }
            holder.checkBox.setChecked(mIsCheckMap.get(position));
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, productSku,productNo,productNumbers,productGuige,sumNumbers;
            LinearLayout mContentLayout;
            CheckBox checkBox;
            public MyViewHolder(View view) {
                super(view);
                checkBox = view.findViewById(R.id.check_box);
                mContentLayout = view.findViewById(R.id.content_layout);
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mContentLayout.getLayoutParams();
//                params.height = DensityUtils.dp2px(ChooseLibraryGoodsActivity.this,250);
//                mContentLayout.setLayoutParams(params);
                productName = view.findViewById(R.id.product_name);
                productSku = view.findViewById(R.id.product_sku_value);
                productNo = view.findViewById(R.id.product_no_value);
                productNumbers = view.findViewById(R.id.product_ke_move_value);
                productGuige = view.findViewById(R.id.product_guige_value);
                sumNumbers = view.findViewById(R.id.product_he_ji_value);
            }
        }
    }
}
