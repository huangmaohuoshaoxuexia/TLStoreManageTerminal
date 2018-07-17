package com.whmaster.tl.whmaster.activity.inventory;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.activity.BaseActivity;
import com.whmaster.tl.whmaster.presenter.InventoryPresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

/**
 * Created by admin on 2018/4/3.
 */

public class ChoseInvertoryDetailActivity extends BaseActivity implements IMvpView{

    private LinearLayout mTitleLayout;
    private ImageView mBackImage;
    private Bundle mBundle;
    private TextView mProductName,mSkuText,mNoText,mConsgonerName,mGuigeText,mKuquText,mKuweiText,mNumbersText,mTitleText;
    private String productName,skuCode,noCode,consgoner,guige,kuqu,kuwei,numbers,mStocktakingDetailId;
    private int packgeCount,inventoryNum,mZs,mNum,stocktakingNum;
    private EditText mZsEdit,mNumEdit,mMemoEdit;
    private ImageView mZsReduceImage,mZsAddImage,mNumReduceImage,mNumAddImage;
    private Button mSubBtn;
    private InventoryPresenter inventoryPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.chose_invertory_detail_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getIntent().getExtras();
        inventoryPresenter = new InventoryPresenter(this,this);
        if(mBundle!=null){
            mStocktakingDetailId = mBundle.getString("stocktakingDetailId");
            inventoryNum = Integer.parseInt(mBundle.getString("inventoryNum"));
            stocktakingNum = Integer.parseInt(mBundle.getString("stocktakingNum"));
            packgeCount = Integer.parseInt(mBundle.getString("packageCount"));
            if(packgeCount<=0) packgeCount = 1;
            if(packgeCount>0){
                mZs = stocktakingNum / packgeCount;
                if(mZs>900000) mZs = 900000;
                mNum = stocktakingNum % packgeCount;
            }

            productName = mBundle.getString("productName");
            skuCode = mBundle.getString("productSkuCode");
            noCode = mBundle.getString("batchNo");
            consgoner = mBundle.getString("consignorName");
            guige = mBundle.getString("packageSpec");
            kuqu = mBundle.getString("regionName");
            kuwei = mBundle.getString("positionName");
            numbers = mBundle.getString("inventoryNum");
            if(mBundle.getString("memo")!=null && !mBundle.getString("memo").toString().equals("null")){
                mMemoEdit.setText(mBundle.getString("memo"));
            }
            mTitleText.setText(mBundle.getString("mStocktakingType")+"-货品详情");
        }
        mZsEdit.setText(mZs+"");
        mNumEdit.setText(mNum+"");
        mNumbersText.setText(numbers);
        mKuweiText.setText(kuwei);
        mKuquText.setText(kuqu);
        mGuigeText.setText(guige);
        mProductName.setText(productName);
        mSkuText.setText(skuCode);
        mNoText.setText(noCode);
        mConsgonerName.setText(consgoner);
    }

    @Override
    public void initViews() {
        super.initViews();
        mSubBtn = findViewById(R.id.sub_btn);
        mSubBtn.setOnClickListener(this);
        mTitleText = findViewById(R.id.title_text);
        mZsReduceImage = findViewById(R.id.zs_reduce_image);
        mZsAddImage = findViewById(R.id.zs_add_image);
        mNumReduceImage = findViewById(R.id.num_reduce_image);
        mNumAddImage = findViewById(R.id.num_add_image);
        mZsReduceImage.setOnClickListener(this);
        mZsAddImage.setOnClickListener(this);
        mNumAddImage.setOnClickListener(this);
        mNumReduceImage.setOnClickListener(this);

        mMemoEdit = findViewById(R.id.memo_edit);
        mZsEdit = findViewById(R.id.actual_zs_text);
        mNumEdit = findViewById(R.id.actual_num_text);
        mZsEdit.addTextChangedListener(mZsTextWatcher);
        mNumEdit.addTextChangedListener(mNumTextWatcher);
        mProductName = findViewById(R.id.product_name_text);
        mSkuText = findViewById(R.id.sku_text);
        mNoText = findViewById(R.id.no_text);

        mConsgonerName = findViewById(R.id.cargo_owner_text);
        mGuigeText = findViewById(R.id.guige_text);
        mKuquText = findViewById(R.id.kuqu_text);
        mKuweiText = findViewById(R.id.kuwei_text);
        mNumbersText = findViewById(R.id.numbers_text);

        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.sub_btn:
                try{
                    int zs = mZs*packgeCount + mNum;
                    if(zs>0){
                        inventoryPresenter.update(zs,mMemoEdit.getText().toString(),mStocktakingDetailId,inventoryNum);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.zs_reduce_image:
                if(mZs>0){
                    mZs--;
                    mZsEdit.setText(mZs+"");
                }
                break;
            case R.id.zs_add_image:
                if (mZs < 900000) {
                    mZs++;
                    mZsEdit.setText(mZs + "");
                } else {
                    mZs = 900000;
                    mZsEdit.setText("900000");
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能大于900000")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.num_reduce_image:
                if(mNum>0){
                    mNum--;
                    mNumEdit.setText(mNum+"");
                }
                break;
            case R.id.num_add_image:
                if (mNum < packgeCount) {
                    mNum++;
                    mNumEdit.setText(mNum + "");
                } else {
                    mNum = packgeCount;
                    mNumEdit.setText(packgeCount + "");
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能大于"+packgeCount)
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.back_image:
                finish();
                break;
        }
    }
    private TextWatcher mZsTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s!=null && !s.toString().equals("")){
                mZs = Integer.parseInt(s.toString());
                if(mZs<0 ){
                    mZs = 0;
                    mZsEdit.setText("0");
                }else if(mZs > 900000){
                    mZs = 900000;
                    mZsEdit.setText("900000");
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private TextWatcher mNumTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s!=null && !s.toString().equals("")){
                mNum = Integer.parseInt(s.toString());
                if(mNum<0){
                    mNum = 0;
                    mNumEdit.setText("0");
                }else if(mNum > packgeCount){
                    mNum = packgeCount;
                    mNumEdit.setText(packgeCount+"");
                }
            }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

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
        Bundle bundle = new Bundle();
        setResultOk(bundle);
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
