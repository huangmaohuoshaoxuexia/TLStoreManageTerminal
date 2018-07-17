package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.PickingPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

/**
 * Created by admin on 2018/1/16.
 */

public class ReviewGoodsDetailActivity extends BaseActivity implements IMvpView{

    private PickingPresenter pickingPresenter;
    private TextView mProductName,mNoText,mTypeText,mTijiText,mZlText;
    private TextView mPlanZs,mPlanNum,mPlanPackBasic,mPlanNumBasic,mActualZs,mActualNum;
    private EditText mMemoEdit;
    private ImageView mActualZsAdd,mActualZsReduce,mActualNumAdd,mActualNumReduce,mBackImage;
    private Bundle mBundle;
    private String mOutDetlId;
    private Button mSubBtn;
    private ArrayMap<String, Object> mDataMap;
    private int mLeft = 0,mRight = 0,mPlanLeft,mPlanRight,mMaxNum,mPackageCount;
    private String mWeight,mVolume;
    private LinearLayout mTitleLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.review_goods_detail_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mOutDetlId = mBundle.getString("outDetlId");
            pickingPresenter = new PickingPresenter(this,this);
            pickingPresenter.reviewDetail(mOutDetlId);
            mWeight = mBundle.getString("weight");
            mVolume = mBundle.getString("volume");
        }
        mTijiText.setText(mVolume+"M³");
        mZlText.setText(mWeight+"KG");
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mProductName = findViewById(R.id.product_name);
        mPlanPackBasic = findViewById(R.id.plan_pack_basic);
        mPlanNumBasic = findViewById(R.id.plan_num_basic);
        mNoText = findViewById(R.id.no_name);
        mTypeText = findViewById(R.id.type_text);
        mTijiText = findViewById(R.id.tiji_text);
        mZlText = findViewById(R.id.zl_text);
        mPlanZs = findViewById(R.id.left_text);
        mPlanNum = findViewById(R.id.right_text);
        mActualZs = findViewById(R.id.left_edit);
        mActualNum = findViewById(R.id.right_edit);
        mMemoEdit = findViewById(R.id.memo_edit);

        mActualZsAdd = findViewById(R.id.left_add_image);
        mActualZsReduce = findViewById(R.id.left_reduce_image);
        mActualNumAdd = findViewById(R.id.right_add_image);
        mActualNumReduce = findViewById(R.id.right_reduce_image);
        mSubBtn = findViewById(R.id.sub_btn);
        mActualZsAdd.setOnClickListener(this);
        mActualZsReduce.setOnClickListener(this);
        mActualNumAdd.setOnClickListener(this);
        mActualNumReduce.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_image:
                finish();
                break;
            case R.id.left_reduce_image:
                if (mLeft > 0) {
                    mLeft--;
                    mActualZs.setText(mLeft + "");
                }
                break;
            case R.id.left_add_image:
                if (mLeft < mPlanLeft) {
                    mLeft++;
                    mActualZs.setText(mLeft + "");
                }
                break;
            case R.id.right_reduce_image:
                if (mRight > 0) {
                    mRight--;
                    mActualNum.setText(mRight + "");
                }
                break;
            case R.id.right_add_image:
                if (mRight < mMaxNum) {
                    mRight++;
                    mActualNum.setText(mRight + "");
                }
                break;
            case R.id.sub_btn:
                if(mLeft>0 || mRight>0){
                    int sum = mLeft*mPackageCount + mRight;
                    if(sum<=mMaxNum){
                        pickingPresenter.outSave(mOutDetlId,sum,mMemoEdit.getText().toString(),150);
                    }else{
                        Toast.makeText(this,"拣货数量不能大于计划拣货总量！",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"拣货整数或零散数不能为0！",Toast.LENGTH_SHORT).show();
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
        mTitle.setText("");
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
            case "data":
                mDataMap = (ArrayMap<String, Object>) object;
                if(mDataMap!=null && mDataMap.size()>0){
                    mProductName.setText("货品名称："+mDataMap.get("prodName")+"");
//                    mNoText.setText(""+mDataMap.get("batchNo"));

                    mPlanPackBasic.setText(mDataMap.get("packageUnit")+"");
                    mPlanNumBasic.setText(mDataMap.get("baseUnit")+"");

                    mTypeText.setText(""+mDataMap.get("sku"));

                    mPackageCount = Integer.parseInt(mDataMap.get("packageCount").toString());
                    mMaxNum = Integer.parseInt(mDataMap.get("planOutCount").toString());
                    mPlanLeft = Integer.parseInt(mDataMap.get("planOutCount").toString()) / mPackageCount;

                    mPlanRight = Integer.parseInt(mDataMap.get("planOutCount").toString()) % mPackageCount;
                    mRight = Integer.parseInt(mDataMap.get("actlOutCount").toString()) % mPackageCount;
                    mLeft = Integer.parseInt(mDataMap.get("actlOutCount").toString()) / mPackageCount;
                    mPlanZs.setText(mPlanLeft+"");
                    mPlanNum.setText(mPlanRight+"");
                    mActualZs.setText(mLeft+"");
                    mActualNum.setText(mRight+"");

//                    float volume = Float.parseFloat(mDataMap.get("packageLength").toString())*Float.parseFloat(mDataMap.get("packageWidth").toString())*Float.parseFloat(mDataMap.get("packageHeight").toString()) * Float.parseFloat(mDataMap.get("actlOutCount").toString()) / mPackageCount/ 1000000;
//                    mTijiText.setText(Constants.format4(volume+"")+"M³");
//
//                    float weight = Float.parseFloat(mDataMap.get("packageWeight").toString()) / mPackageCount;
//                    mZlText.setText(weight+"KG");

                    mActualZs.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s!=null && !s.toString().equals("")){
                                int a = Integer.parseInt(s.toString());
                                if(a < 0){
                                    mActualZs.setText("0");
                                }else if(a > mPlanLeft){
                                    mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于"+mPlanLeft)
                                            .setPositiveButton("确认", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {}}).show();
                                    mActualZs.setText(mPlanLeft+"");
                                }
                            }else{
                                mActualZs.setText("0");
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    mActualNum.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s!=null && !s.toString().equals("")){
                                int a = Integer.parseInt(s.toString());
                                if(a<0){
                                    mActualNum.setText("0");
                                }else if(a>mMaxNum){
                                    mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于"+mMaxNum)
                                            .setPositiveButton("确认", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {}}).show();
                                    mActualNum.setText(mMaxNum+"");
                                }
                            }else{
                                mActualNum.setText("0");
                            }
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                }
                break;
            case "success":
                Bundle bundle = new Bundle();
                bundle.putString("type", "success");
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

}
