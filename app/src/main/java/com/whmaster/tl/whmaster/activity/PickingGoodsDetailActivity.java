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
 * Created by admin on 2018/1/15.
 * 拣货详情
 */

public class PickingGoodsDetailActivity extends BaseActivity implements IMvpView{

    private PickingPresenter pickingPresenter;
    private TextView mKuquText,mKuweiText,mNoText,mTypeText,mTijiText,mZlText;
    private TextView mPlanZs,mPlanNum,mPlanPackBasic,mPlanNumBasic;
    private EditText mActualZs,mActualNum,mMemoEdit;
    private ImageView mActualZsAdd,mActualZsReduce,mActualNumAdd,mActualNumReduce,mBackImage;
    private Bundle mBundle;
    private String mPickDetlId,mBaseUnitCn;
    private Button mSubBtn;
    private ArrayMap<String, Object> mDataMap;
    private int mLeft = 0,mRight = 0,mPlanLeft,mPlanRight,mMaxNum = 0,mPackageCount = 0;
    private String mWeight,mVolume;
    private LinearLayout mTitleLayout;
    @Override
    protected int getLayoutId() {
        return R.layout.picking_goods_detail_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mPickDetlId = mBundle.getString("pickDetlId");
            pickingPresenter = new PickingPresenter(this,this);
            pickingPresenter.pickingDetail(mPickDetlId);
            mWeight = mBundle.getString("weight");
            mVolume = mBundle.getString("volume");
            mTijiText.setText(mVolume+"M³");
            mZlText.setText(mWeight+"KG");
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mPlanPackBasic = findViewById(R.id.plan_pack_basic);
        mPlanNumBasic = findViewById(R.id.plan_num_basic);
        mKuquText = findViewById(R.id.kuqu_text);
        mKuweiText = findViewById(R.id.kuwei_text);
        mNoText = findViewById(R.id.no_text);
        mTypeText = findViewById(R.id.product_type_text);
        mTijiText = findViewById(R.id.tiji_text);
        mZlText = findViewById(R.id.zl_text);
        mPlanZs = findViewById(R.id.plan_zs_text);
        mPlanNum = findViewById(R.id.plan_num_text);
        mActualZs = findViewById(R.id.actual_zs_text);
        mActualNum = findViewById(R.id.actual_num_text);
        mMemoEdit = findViewById(R.id.memo_edit);

        mActualZsAdd = findViewById(R.id.left_reduce_image);
        mActualZsReduce = findViewById(R.id.left_add_image);
        mActualNumAdd = findViewById(R.id.right_reduce_image);
        mActualNumReduce = findViewById(R.id.right_add_image);
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
                    if(sum == mMaxNum){
                        pickingPresenter.save(mPickDetlId,mLeft,sum,mMemoEdit.getText().toString(),20);
                    }else{
                        Toast.makeText(this,"拣货数量和计划拣货总量不相等",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"拣货整数或零散数不能为0！",Toast.LENGTH_SHORT).show();
                }
//                Bundle bundle = new Bundle();
//                bundle.putString("type","success");
//                setResultOk(bundle);
//                pickingPresenter.save(mPickDetlId,mLeft,mRight,mMemoEdit.getText().toString(),0);
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
                        ArrayMap productPoMap = Constants.getJsonObject(mDataMap.get("productPo").toString());
                        ArrayMap productMap = Constants.getJsonObject(productPoMap.get("product").toString());
                        float volume = Float.parseFloat(productMap.get("packageLength").toString())*Float.parseFloat(productMap.get("packageWidth").toString())*Float.parseFloat(productMap.get("packageHeight").toString()) / 1000000;
                        mKuquText.setText("库区："+mDataMap.get("regionName"));
                        mKuweiText.setText("库位："+mDataMap.get("positionName"));
                        mNoText.setText(""+mDataMap.get("batchNo"));
                        mTypeText.setText(""+mDataMap.get("prodLevel"));

                        mBaseUnitCn = productPoMap.get("baseUnitCn")+"";
                        mPlanPackBasic.setText(productPoMap.get("packageUnitCn")+"");
                        mPlanNumBasic.setText(productPoMap.get("baseUnitCn")+"");

                        mPackageCount = Integer.parseInt(productMap.get("packageCount").toString());
                        mPlanLeft = Integer.parseInt(mDataMap.get("planPickCount").toString()) / mPackageCount;
                        mLeft = mPlanLeft;
                        mPlanRight = Integer.parseInt(mDataMap.get("planPickCount").toString()) % mPackageCount;
                        mRight = mPlanRight;
                        mMaxNum = Integer.parseInt(mDataMap.get("planPickCount").toString());

                        mPlanZs.setText(mPlanLeft+"");
                        mPlanNum.setText(mPlanRight+"");
                        mActualZs.setText(mPlanLeft+"");
                        mActualNum.setText(mPlanRight+"");
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
                bundle.putString("type","success");
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
