package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.widget.SlidingButtonView;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.utils.DensityUtils;
import com.whmaster.tl.whmaster.utils.ScreenUtils;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2017/11/24.
 * 生成上架单列表
 */

public class GenerateSlListActivity extends BaseActivity implements IMvpView {

    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private Button mSubBtn;
    private Bundle mBundle;
    private String mOrderInId,mBuyerId,mOrgId;
    private StoragePresenter storagePresenter;
    private ArrayList<HashMap<String, Object>> mList;
    private ArrayList<ArrayMap<String, Object>> mKuList;
    private ArrayMap<String, Object> mMap;
    private TextView mTotalNum, mUnassigned, mKuText;
    private String mWharehouseId, mWharehouseName;
    private ArrayList<String> mWhareList;
    private ArrayList<HashMap<String, Object>> mDetailList;
    private boolean isGenerate = true;
    private int mUnassignedNum = 0,mPlanPackageNum = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.generate_sl_list_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView,this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        storagePresenter.getStorageDetailList(mOrderInId);
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                    }
                }, 1000);
            }
        });
        storagePresenter = new StoragePresenter(this, this);
        logcat(mOrderInId);
        storagePresenter.getStorageDetailList(mOrderInId);
        storagePresenter.getWharehouse("");
    }

    @Override
    public void initViews() {
        super.initViews();
        mTotalNum = findViewById(R.id.total_num);
        mUnassigned = findViewById(R.id.unassigned);
        mRecyclerView = findViewById(R.id.generate_sl_rcview);
        mSubBtn = findViewById(R.id.sub_btn);
        mKuText = findViewById(R.id.warehouse);
        mKuText.setOnClickListener(this);
        mSubBtn.setOnClickListener(this);
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mOrderInId = mBundle.getString("orderInId");
            mBuyerId = mBundle.getString("buyerId");
            mOrgId = mBundle.getString("orgId");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sub_btn:
                if (isGenerate) {
                    mDetailList = new ArrayList<>();
                    HashMap<String, Object> map;
                        for (int i = 0; i < mList.size(); i++) {
                            map = new HashMap<>();
                            map.put("productId", mList.get(i).get("productId") + "");
                            map.put("regionId", mList.get(i).get("regionId") + "");
                            map.put("positionId", mList.get(i).get("positionId") + "");
                            map.put("productDate", mList.get(i).get("productDate") + "");
                            map.put("wharehouseId", mWharehouseId);
                            map.put("planPackageNum", mList.get(i).get("planPackageNum") + "");
                            int allCount = Integer.parseInt(mList.get(i).get("planPackageNum")+"")*Integer.parseInt(mList.get(i).get("packageCount")+"")+Integer.parseInt(mList.get(i).get("planNum")+"");
                            map.put("planNum", allCount + "");
                            map.put("inDetailId", mList.get(i).get("inDetailId") + "");
                        mDetailList.add(map);
                    }
                    storagePresenter.addGenerateList(mBuyerId,mOrderInId,mWharehouseId,mWharehouseName, JSON.toJSONString(mDetailList));
               } else {
                    mAlertDialog.builder().setTitle("提示")
                            .setMsg("你还有未分配库区或库位的货品")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                }
                break;
            case R.id.warehouse:
                if (mWhareList != null && mWhareList.size() > 0) {
                    OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            mKuText.setText(mWhareList.get(options1) + "");
                            mWharehouseId = mKuList.get(options1).get("wareHouseId") + "";
                            mWharehouseName = mWhareList.get(options1) + "";
                            logcat("仓库ID" + mWharehouseId);
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
                    pvOptions.setPicker(mWhareList);
                    pvOptions.show();
                }
                break;
        }
    }

    private void callBack() {
        Bundle bundle = new Bundle();
        bundle.putString("type", "rkscList");
        setResultOk(bundle);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                callBack();
                break;
        }
        return true;
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
            case "map":
                mMap = (ArrayMap<String, Object>) object;
                if (mMap != null && mMap.get("detailList") != null) {
                    mList = Constants.getJsonArray2(mMap.get("detailList") + "");
                    mAdapter = new RecyAdapter();
                    mRecyclerView.setAdapter(mAdapter);
                    mTotalNum.setText("货品总数：" + mMap.get("totalCount"));
                    mUnassignedNum = Integer.parseInt(mMap.get("totalCount").toString());
                    for(int i=0;i<mList.size();i++){
                        if (mList.get(i).get("regionName") != null) {
                            if(mList.get(i).get("planPackageNum")!=null && !mList.get(i).get("planPackageNum").toString().equals("")){
                                mUnassignedNum = mUnassignedNum - Integer.parseInt(mList.get(i).get("planPackageNum").toString());
                            }
//                            if(mList.get(i).get("planNum")!=null && !mList.get(i).get("planNum").toString().equals("")){
//                                mUnassignedNum = mUnassignedNum - Integer.parseInt(mList.get(i).get("planNum").toString());
//                            }
                        }
                    }
                }
                mUnassigned.setText("未分配：" + mUnassignedNum);
                break;
            case "wharehouse":
                mWhareList = new ArrayList<>();
                mKuList = (ArrayList<ArrayMap<String, Object>>) object;
                for (int i = 0; i < mKuList.size(); i++) {
                    mWhareList.add(mKuList.get(i).get("wareHouseName").toString());
                }
                break;
            case "addGenerateListSuccess":
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        HashMap<String, Object> map;
        logcat("data数据" + data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                try{
                    if (bundle.getString("isSplit").equals("true")) {//拆分上架单
                        isGenerate = true;
                        map = (HashMap<String, Object>) bundle.getSerializable("splitMap");
                        logcat("拆分的map"+map);
                        int position = Integer.parseInt(bundle.getString("position"));
                        mList.get(position).put("planPackageNum", bundle.getString("planPackageNum") + "");
                        mList.get(position).put("planNum", bundle.getString("planNum") + "");
                        mList.add(position + 1, map);
                        mAdapter.notifyDataSetChanged();
                        logcat("刷新列表" + mList);
                    } else if (bundle.getString("isSplit").equals("generate")) {//生成上架单
                        isGenerate = true;
                        int position = Integer.parseInt(bundle.getString("position"));
                        mList.get(position).put("regionName", bundle.getString("regionName"));
                        mList.get(position).put("positionName", bundle.getString("positionName"));
                        mList.get(position).put("regionId", bundle.getString("regionId"));
                        mList.get(position).put("positionId", bundle.getString("positionId"));
                        mList.get(position).put("productDate", bundle.getString("productDate"));
                        logcat("选择后的仓库数据" + mList);
                        mAdapter.notifyDataSetChanged();
                        if(bundle.getString("isReduce").equals("true")){
                            if(mList.get(position).get("planNum")!=null && !mList.get(position).get("planNum").toString().equals("")){
                                int num = Integer.parseInt(mList.get(position).get("planNum").toString())+Integer.parseInt(mList.get(position).get("planPackageNum")+"")*Integer.parseInt(mList.get(position).get("packageCount")+"");
//                                mUnassignedNum = mUnassignedNum - Integer.parseInt(mList.get(position).get("planNum").toString());
                                mUnassignedNum = mUnassignedNum - num;
                            }
//                            if(mList.get(position).get("planNum")!=null && !mList.get(position).get("planNum").toString().equals("")){
//                                mUnassignedNum = mUnassignedNum - Integer.parseInt(mList.get(position).get("planNum").toString());
//                            }
                            mUnassigned.setText("未分配：" + mUnassignedNum);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> implements SlidingButtonView.IonSlidingButtonListener {

        private SlidingButtonView mMenu = null;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    GenerateSlListActivity.this).inflate(R.layout.generate_sl_list_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            //设置内容布局的宽为屏幕宽度
            try{


            holder.mContentLayout.getLayoutParams().width = ScreenUtils.getScreenWidth(GenerateSlListActivity.this);
            holder.mProductName.setText("货品名称：" + mList.get(position).get("productName"));
            int sum = 0;
            if(mList.get(position).get("planPackageNum")!=null && ! mList.get(position).get("planPackageNum").toString().equals("") &&  !mList.get(position).get("planPackageNum").toString().equals("null")){
               sum = Integer.parseInt(mList.get(position).get("planPackageNum").toString()) * Integer.parseInt(mList.get(position).get("packageCount").toString()) +Integer.parseInt(mList.get(position).get("planNum").toString());
            }else{
                sum = Integer.parseInt(mList.get(position).get("planNum").toString());
            }
            if(mList.get(position).get("planNum")!=null && mList.get(position).get("packageCount")!=null){
                int packageCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
                if(packageCount<=0) packageCount = 1;
                if(packageCount>0){
                    mPlanPackageNum = sum / packageCount;
                    int numCount = sum % packageCount;
                    mList.get(position).put("planPackageNum",mPlanPackageNum+"");
                    mList.get(position).put("planNum",numCount+"");
                }
            }
            holder.mProductNum.setText("货品数量：" + sum);

            if (mList.get(position).get("regionName") != null) {
                holder.mKuqu.setText("库区：" + mList.get(position).get("regionName"));
            }else{
                holder.mKuqu.setText("库区：未分配");
                isGenerate = false;
            }
            if (mList.get(position).get("positionName") != null) {
                holder.mKuwei.setText("库位：" + mList.get(position).get("positionName"));
            }else{
                holder.mKuwei.setText("库位：未分配");
                isGenerate = false;
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {//上架单
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map", mList.get(position));
                    bundle.putString("position", position + "");

                    bundle.putString("wharehouseId", mWharehouseId);
                    if(mList.get(position).get("regionId")!=null){
                        bundle.putString("regionName", mList.get(position).get("regionName")+"");
                        bundle.putString("positionName", mList.get(position).get("positionName")+"");
                        bundle.putString("productDate", mList.get(position).get("productDate")+"");

                        bundle.putString("regionId", mList.get(position).get("regionId")+"");
                        bundle.putString("positionId", mList.get(position).get("positionId")+"");
                    }
                    if (mWharehouseId != null && !mWharehouseId.equals("")) {
                        openActivityForResult(GenerateSlActivity.class, 0, bundle);
                    } else {
                        Toast.makeText(GenerateSlListActivity.this, "请选择仓库", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.mSlitText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//拆分
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("map",mList.get(position));
                    bundle.putString("position", position + "");
                    openActivityForResult(SplitSjdActivity.class, 0, bundle);
                }
            });

            holder.mDeleteText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logcat("选择的数据============"+ position);
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).get("productSku").toString().equals(mList.get(position).get("productSku").toString())) {

                            if (i != position) {
                                int oldPlanPackageNum = Integer.parseInt(mList.get(i).get("planPackageNum").toString());
                                int newPlanPackageNum = Integer.parseInt(mList.get(position).get("planPackageNum").toString());
                                int sumPlanPackageNum = oldPlanPackageNum + newPlanPackageNum;
                                mList.get(i).put("planPackageNum", sumPlanPackageNum + "");
                                int oldplanNum = Integer.parseInt(mList.get(i).get("planNum").toString());
                                int newplanNum = Integer.parseInt(mList.get(position).get("planNum").toString());
                                int sumplanNum = oldplanNum + newplanNum;
                                mList.get(i).put("planNum", sumplanNum + "");
                                logcat(position+"选择的数据============"+ mList.get(i));
                                break;
                            }
                        }
                    }
                    if (mList.get(position).get("regionName") != null) {
                        if(mList.get(position).get("planPackageNum")!=null && !mList.get(position).get("planPackageNum").toString().equals("")){
                            int num = Integer.parseInt(mList.get(position).get("planPackageNum").toString()) * Integer.parseInt(mList.get(position).get("packageCount").toString()) + Integer.parseInt(mList.get(position).get("planNum").toString());
//                                        mUnassignedNum = mUnassignedNum + Integer.parseInt(mList.get(position).get("planPackageNum").toString());
                            mUnassignedNum = mUnassignedNum + num;
                        }
                        mUnassigned.setText("未分配：" + mUnassignedNum);
                    }
                    mList.remove(position);
                    notifyDataSetChanged();
                }
            });
            if (mList.get(position).get("isSplit") != null) {
                holder.mSlidRight.getLayoutParams().width = DensityUtils.dp2px(GenerateSlListActivity.this, 150);
                holder.mDeleteText.setVisibility(View.VISIBLE);
            } else {
                holder.mSlidRight.getLayoutParams().width = DensityUtils.dp2px(GenerateSlListActivity.this, 75);
                holder.mDeleteText.setVisibility(View.GONE);
            }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public void onMenuIsOpen(View view) {
            mMenu = (SlidingButtonView) view;
        }

        @Override
        public void onDownOrMove(SlidingButtonView slidingButtonView) {
            if (menuIsOpen()) {
                if (mMenu != slidingButtonView) {
                    closeMenu();
                }
            }
        }
        //关闭菜单
        public void closeMenu() {
            mMenu.closeMenu();
            mMenu = null;
        }

        //判断是否有菜单打开
        public Boolean menuIsOpen() {
            if (mMenu != null) {
                return true;
            }
            return false;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            LinearLayout mContentLayout, mSlidRight;
            TextView mDeleteText, mSlitText, mProductName, mProductNum, mKuqu, mKuwei;

            public MyViewHolder(View view) {
                super(view);
                mProductName = view.findViewById(R.id.product_name);
                mProductNum = view.findViewById(R.id.product_numbers);
                mKuqu = view.findViewById(R.id.warehouse_qu_name);
                mKuwei = view.findViewById(R.id.warehouse_wei_name);
                mContentLayout = view.findViewById(R.id.layout_content);
                mDeleteText = view.findViewById(R.id.tv_delete);
                mSlitText = view.findViewById(R.id.tv_split);
                mSlidRight = view.findViewById(R.id.sliding_right);
                ((SlidingButtonView) view).setSlidingButtonListener(RecyAdapter.this);
            }
        }

    }
}
