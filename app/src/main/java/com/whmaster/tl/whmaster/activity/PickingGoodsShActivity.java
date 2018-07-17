package com.whmaster.tl.whmaster.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.widget.LoginLoadingDialog;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/10.
 */

public class PickingGoodsShActivity extends BaseActivity implements IMvpView{
    private ImageView mOpenOrClose;
    private LinearLayout mVisiableLayout;
    private ImageView mLeftAddImage, mLeftReduceImage, mRightAddImage, mRightReduceImage;
    private TextView mLeftText, mRightText;
    private Bundle mBundle;
    private String mType = "", mDetailId, mBusinessType, storePositionId;
    private Button mSubBtn;
    private int mLeft = 0, mRight = 0, mPlanLeft = 0, mPlanRight = 0;
    private StoragePresenter storagePresenter;
    private TextView mNameText, mSkuText, mJhsjdwText, mSjsjdwText, mJhsjgText, mSjsjgText;
    private ArrayList<HashMap<String, Object>> mList;
    private TextView mKqText, mKwText, mNumberText, mJhsjdwText2, mSjsjdwText2, mJhsjgText2, mSjsjgText2;
    private LoginLoadingDialog loadingDialog;
    @Override
    protected int getLayoutId() {
        return R.layout.goods_shelves_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storagePresenter = new StoragePresenter(this, this);
        storagePresenter.queryStorePositionDetail(mDetailId, mBusinessType);
        loadingDialog = new LoginLoadingDialog(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            //上架完毕
            case R.id.sub_btn:
                loadingDialog.builder().setMsg("正在上架").show();
                ArrayList list = new ArrayList();
                HashMap map = new HashMap();
                map.put("stockInDetailId",mDetailId);
                map.put("actPackageNum",mLeft+"");
                map.put("actNum",mRight+"");
                map.put("storePositionId",storePositionId);
                list.add(map);
                storagePresenter.pickAdd(mDetailId, JSON.toJSONString(list));
                break;
            case R.id.left_add_image:
                if (mLeft < mPlanLeft) {
                    mLeft++;
                    mLeftText.setText(mLeft + "");
                    mSjsjdwText.setText("实际上架单位数：" + mLeft + "");
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能大于" + mPlanLeft)
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.left_reduce_image:
                if (mLeft > 0) {
                    mLeft--;
                    mLeftText.setText(mLeft + "");
                    mSjsjdwText.setText("实际上架单位数：" + mLeft + "");
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能小于0")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.right_add_image:
                if (mRight < mPlanRight) {
                    mRight++;
                    mRightText.setText(mRight + "");
                    mSjsjgText.setText("实际上架个位数：" + mRight + "");
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能大于" + mPlanRight)
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
                    mRightText.setText(mRight + "");
                    mSjsjgText.setText("实际上架个位数：" + mRight + "");
                } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("数量不能小于0")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
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
    public void initViews() {
        super.initViews();
        mKqText = findViewById(R.id.kq_text);
        mKwText = findViewById(R.id.kw_text);
        mNumberText = findViewById(R.id.number_text);
        mJhsjdwText2 = findViewById(R.id.jhsj_dw_text2);
        mSjsjdwText2 = findViewById(R.id.sjsj_dw_text2);
        mJhsjgText2 = findViewById(R.id.jhsj_g_text2);
        mSjsjgText2 = findViewById(R.id.sjsj_g_text2);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mType = mBundle.getString("type");
            mDetailId = mBundle.getString("detailId");
            if (mType.equals("storage")) {
                mBusinessType = "10";
            } else {
                mBusinessType = "20";
            }
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
        mLeftAddImage = findViewById(R.id.left_add_image);
        mLeftReduceImage = findViewById(R.id.left_reduce_image);
        mLeftText = findViewById(R.id.left_text);
        mRightAddImage = findViewById(R.id.right_add_image);
        mRightReduceImage = findViewById(R.id.right_reduce_image);
        mRightText = findViewById(R.id.right_text);
        mLeftAddImage.setOnClickListener(this);
        mLeftReduceImage.setOnClickListener(this);
        mRightAddImage.setOnClickListener(this);
        mRightReduceImage.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
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
