package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/27.
 * 拆分上架单
 */

public class SplitSjdActivity extends BaseActivity implements IMvpView{

    private ImageView mLeftReduceImage,mLeftAddImage,mRightReduceImage,mRightAddImage;
    private EditText mPlanZs,mPlanNum;
    private TextView mProductName,mProductSku;
    private int left = 0,right = 0,mSplitLeft = 0,mSplitRight = 0,mMaxLeft=0,mMaxRight=0,mMaxNum = 0,mPackageCount =0;
    private Button mSubBtn;
    private HashMap<String,Object> mSplitMap;
    private String mPosition = "0";
    private Bundle mBundle;
    @Override
    protected int getLayoutId() {
        return R.layout.split_sjd_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mSplitMap!=null && mSplitMap.size()>0){
            mProductName.setText("货品名称："+mSplitMap.get("productName"));
            mProductSku.setText("SKU："+mSplitMap.get("productSku"));
            mPlanZs.setText(mSplitMap.get("planPackageNum")+"");
            mPlanNum.setText(mSplitMap.get("planNum")+"");
            if(mSplitMap.get("planPackageNum")!=null && mSplitMap.get("packageCount")!=null && mSplitMap.get("planNum")!=null){
//                mMaxLeft = Integer.parseInt(mSplitMap.get("planPackageNum").toString());
                mPackageCount = Integer.parseInt(mSplitMap.get("packageCount").toString());
                mMaxNum = Integer.parseInt(mSplitMap.get("planPackageNum").toString())*Integer.parseInt(mSplitMap.get("packageCount").toString())+Integer.parseInt(mSplitMap.get("planNum").toString());
            }
            if(mSplitMap.get("planPackageNum")!=null && !mSplitMap.get("planPackageNum").toString().equals("")){
                mMaxLeft = Integer.parseInt(mSplitMap.get("planPackageNum").toString());
            }
            if(mSplitMap.get("planNum")!=null && !mSplitMap.get("planNum").toString().equals("")){
                mMaxRight = Integer.parseInt(mSplitMap.get("planNum").toString());
            }
        }
        mPlanZs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && !s.toString().equals("")){
                    int a = Integer.parseInt(s.toString());
                    if(a<0){
                        mPlanZs.setText("0");
                    }else if(a>mMaxLeft){
                        mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于"+mMaxLeft)
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {}}).show();
                        mPlanZs.setText(mMaxLeft+"");
                    }
                }else{
                    mPlanZs.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPlanNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s!=null && !s.toString().equals("")){
                    int a = Integer.parseInt(s.toString());
                    if(a<0){
                        mPlanNum.setText("0");
                    }else if(a>mMaxNum){
                        mAlertDialog.builder().setTitle("提示").setMsg("数量不能大于"+mMaxNum)
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {}}).show();
                        mPlanNum.setText(mMaxNum+"");
                    }
                }else{
                    mPlanNum.setText("0");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void initViews() {
        super.initViews();
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mSplitMap = (HashMap<String, Object>) mBundle.getSerializable("map");
            logcat("获取传递的map对象"+mSplitMap);
            mPosition = mBundle.getString("position");
        }
        mProductName = findViewById(R.id.product_name);
        mProductSku = findViewById(R.id.product_sku);
        mPlanZs = findViewById(R.id.left_text);
        mPlanNum = findViewById(R.id.right_text);

        mLeftReduceImage = findViewById(R.id.left_reduce_image);
        mLeftAddImage = findViewById(R.id.left_add_image);
        mRightReduceImage = findViewById(R.id.right_reduce_image);
        mRightAddImage = findViewById(R.id.right_add_image);
        mLeftReduceImage.setOnClickListener(this);
        mLeftAddImage.setOnClickListener(this);
        mRightReduceImage.setOnClickListener(this);
        mRightAddImage.setOnClickListener(this);
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(mPlanZs.getText().toString().equals("")){
            mPlanZs.setText("0");
        }
        if(mPlanNum.getText().toString().equals("")){
            mPlanNum.setText("0");
        }
        left = Integer.parseInt(mPlanZs.getText().toString());
        right = Integer.parseInt(mPlanNum.getText().toString());
        switch (v.getId()){
            case   R.id.left_reduce_image:
                if(left>0){
                    left--;
                    mPlanZs.setText(left+"");
                }
                break;
            case   R.id.left_add_image:
                if(left < mMaxLeft){
                    left++;
                    mPlanZs.setText(left+"");
                }

                break;
            case   R.id.right_reduce_image:
                if(right>0){
                    right--;
                    mPlanNum.setText(right+"");
                }
                break;
            case   R.id.right_add_image:
                if(right < mMaxNum){
                    right++;
                    mPlanNum.setText(right+"");
                }

                break;
            case R.id.sub_btn:
                Bundle bundle = new Bundle();
                try {
                    mSplitLeft = mMaxLeft - left;
                    mSplitRight = mMaxRight - right;
                    logcat(mSplitLeft+"获取拆分数量"+mSplitRight);
                    int sum = left * mPackageCount + right;
                    logcat(sum+"=====41==="+mMaxNum);
                    if(sum<=mMaxNum){
                        if((mSplitLeft > 0 || mSplitRight > 0) && (left > 0 || right > 0)){
                            mSplitMap.put("planPackageNum",mSplitLeft+"");
                            mSplitMap.put("planNum",mSplitRight+"");
                            mSplitMap.put("isSplit","true");
                            bundle.putSerializable("splitMap", mSplitMap);
                            bundle.putString("planPackageNum",left+"");
                            bundle.putString("planNum",right+"");
                            bundle.putString("position",mPosition);
                            bundle.putString("isSplit","true");
                        }else{
                            bundle.putString("isSplit","false");
                        }
                        setResultOk(bundle);
                    }else{
                        Toast.makeText(this,"拆分数量不能大于总数量"+mMaxNum+"！",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
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
    private void callBack(){
        Bundle bundle = new Bundle();
        bundle.putString("type","refresh");

        setResultOk(bundle);
    }
    @Override
    public void setHeader() {
        super.setHeader();
        mTitle.setText("拆分上架单");
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
}
