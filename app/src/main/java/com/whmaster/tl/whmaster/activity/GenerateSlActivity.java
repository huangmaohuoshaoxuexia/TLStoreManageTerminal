package com.whmaster.tl.whmaster.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/24.
 * 生成上架单
 */

public class GenerateSlActivity extends BaseActivity implements IMvpView {
    private Button mDetermineBtn;
    private TextView mWarehouseQu, mWarehouseWei, mDate, mProductName, mProductSku, mPlanZs, mPlanNum;
    //    private ArrayList<String> options1Items;
    private HashMap<String, Object> mMap;
    private String mPosition = "0", mWharehouseId, regionId, positionId;
    private Bundle mBundle;
    private StoragePresenter storagePresenter;
    private ArrayList<ArrayMap<String, Object>> mKuquList, mKuweiList;
    private TimePickerView mTimePickerView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private ArrayList<String> mKuquNameList, mKuweiNameList;
    private OptionsPickerView pvOptions;
    private boolean isHaveId = false;

    @Override
    protected int getLayoutId() {
        return R.layout.generate_sl_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storagePresenter = new StoragePresenter(this, this);

        if (mBundle != null) {
            mMap = (HashMap<String, Object>) mBundle.getSerializable("map");
            mPosition = mBundle.getString("position");
            mWharehouseId = mBundle.getString("wharehouseId");
            if (mBundle.getString("regionId") != null && !mBundle.getString("regionId").toString().equals("")) {
                mDate.setText(mBundle.getString("productDate") + "");
                mWarehouseQu.setText(mBundle.getString("regionName") + "");
                mWarehouseWei.setText(mBundle.getString("positionName") + "");
                regionId = mBundle.getString("regionId") + "";
                positionId = mBundle.getString("positionId") + "";
                storagePresenter.getPosition(regionId);
            }
        }
        if (mMap != null && mMap.size() > 0) {
            mProductName.setText("货品名称：" + mMap.get("productName"));
            mProductSku.setText("SKU：" + mMap.get("productSku"));
            mPlanZs.setText(mMap.get("planPackageNum") + "");
            mPlanNum.setText(mMap.get("planNum") + "");
        }
        storagePresenter.getRegion(mWharehouseId);
    }

    @Override
    public void initViews() {
        super.initViews();
        mBundle = getIntent().getExtras();

        mProductName = findViewById(R.id.product_name);
        mProductSku = findViewById(R.id.product_sku);
        mPlanZs = findViewById(R.id.plan_zs);
        mPlanNum = findViewById(R.id.plan_num);
        mDetermineBtn = findViewById(R.id.sub_btn);
        mDetermineBtn.setOnClickListener(this);
        mWarehouseQu = findViewById(R.id.warehouse_qu);
        mWarehouseQu.setOnClickListener(this);
        mWarehouseWei = findViewById(R.id.warehouse_wei);
        mWarehouseWei.setOnClickListener(this);
        mDate = findViewById(R.id.date_edit);
        mDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sub_btn:
                try {
                    if (!mWarehouseQu.getText().toString().equals("") && !mWarehouseWei.getText().toString().equals("") && !mDate.getText().toString().equals("")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("isSplit", "generate");
                        bundle.putString("position", mPosition);
                        bundle.putString("regionName", mWarehouseQu.getText().toString());
                        bundle.putString("positionName", mWarehouseWei.getText().toString());
                        bundle.putString("regionId", regionId);
                        bundle.putString("positionId", positionId);
                        bundle.putString("productDate", mDate.getText().toString());
                        if (mMap.get("regionName") != null) {
                            bundle.putString("isReduce", "false");
                        } else {
                            bundle.putString("isReduce", "true");
                        }
                        setResultOk(bundle);
                    } else {
                        mAlertDialog.builder().setTitle("提示")
                                .setMsg("库区库位或生产日期不能为空！")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.date_edit:
                //时间选择器
                isHaveId = true;
                mTimePickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        mDate.setText(simpleDateFormat.format(date) + "");
                    }
                }).setCancelText("取消")//取消按钮文字
                        .setSubmitText("确认")//确认按钮文字
                        .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                        .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                        .build();
                mTimePickerView.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                mTimePickerView.show();
                break;
            //库区
            case R.id.warehouse_qu:
                isHaveId = true;
                if (mKuquNameList != null && mKuquNameList.size() > 0) {
                    pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            mWarehouseQu.setText(mKuquNameList.get(options1));
                            regionId = mKuquList.get(options1).get("regionId") + "";
                            storagePresenter.getPosition(regionId);
                        }
                    })
                            .setTitleText("")
                            .setSubmitText("确定")//确定按钮文字
                            .setCancelText("取消")//取消按钮文字
                            .setDividerColor(getResources().getColor(R.color.color18))
                            .setTextColorCenter(Color.BLACK)
                            .isCenterLabel(true)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                            .setLinkage(true)//设置是否联动
                            .setContentTextSize(20)
                            .setSelectOptions(0)  //设置默认选中项
//                        .isDialog(true)//是否显示为对话框样式
                            .build();
                    pvOptions.setPicker(mKuquNameList);
                    pvOptions.show();
                }
                break;
            //库位
            case R.id.warehouse_wei:
                if (mKuweiNameList != null && mKuweiNameList.size() > 0) {
                    pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            mWarehouseWei.setText(mKuweiNameList.get(options1));
                            positionId = mKuweiList.get(options1).get("positionId") + "";
                        }
                    })
                            .setTitleText("")
                            .setSubmitText("确定")//确定按钮文字
                            .setCancelText("取消")//取消按钮文字
                            .setDividerColor(getResources().getColor(R.color.color18))
                            .setTextColorCenter(Color.BLACK)
                            .isCenterLabel(true)//是否只显示中间选中项的label文字，false则每项item全部都带有label。
                            .setLinkage(true)//设置是否联动
                            .setContentTextSize(20)
                            .setSelectOptions(0)  //设置默认选中项
//                        .isDialog(true)//是否显示为对话框样式
                            .build();
                    pvOptions.setPicker(mKuweiNameList);
                    pvOptions.show();
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
        mTitle.setText("生成上架单");
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
        switch (type) {
            case "wharehouse":
//                mKuList = (ArrayList<HashMap<String, Object>>) object;
//                for (int i = 0; i < mKuList.size(); i++) {
//                    if (mWharehouseId.equals(mKuList.get(i).get("wharehouseId").toString())) {
//                        logcat("库位List" + mKuList.get(i));
//                        mKuweiList = Constants.getJsonArray2(mKuList.get(i).get("positionList").toString());
//                        mKuquList = Constants.getJsonArray2(mKuList.get(i).get("regionList").toString());
//                    }
//                }
//                //"positionName":"I01-23" "positionName":"I01-31"
//                mKuquNameList = new ArrayList<>();
//                mKuweiNameList = new ArrayList<>();
//                for (int i = 0; i < mKuquList.size(); i++) {
//                    mKuquNameList.add(mKuquList.get(i).get("regionName") + "");
//                }
//                for (int i = 0; i < mKuweiList.size(); i++) {
//                    mKuweiNameList.add(mKuweiList.get(i).get("positionName") + "");
//                }
                break;
            case "getRegion":
                mKuquList = (ArrayList<ArrayMap<String, Object>>) object;
                mKuquNameList = new ArrayList<>();
                for (int i = 0; i < mKuquList.size(); i++) {
                    mKuquNameList.add(mKuquList.get(i).get("regionName") + "");
                }
                if(isHaveId){
                    mWarehouseQu.setText(mKuquNameList.get(0));
                    regionId = mKuquList.get(0).get("regionId") + "";
                    storagePresenter.getPosition(mKuquList.get(0).get("regionId")+"");
                }
                break;
            case "getPosition":
                mKuweiList = (ArrayList<ArrayMap<String, Object>>) object;
                mKuweiNameList = new ArrayList<>();
                for (int i = 0; i < mKuweiList.size(); i++) {
                    mKuweiNameList.add(mKuweiList.get(i).get("positionName") + "");
                }
                if(isHaveId){
                    mWarehouseWei.setText(mKuweiNameList.get(0));
                    positionId = mKuweiList.get(0).get("positionId") + "";
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
}
