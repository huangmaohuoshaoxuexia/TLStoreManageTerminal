package com.whmaster.tl.whmaster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.utils.AtyContainerUtils;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;
import com.whmaster.tl.whmaster.widget.MyEditText;
import com.whmaster.tl.whmaster.widget.SlideView;
import com.whmaster.tl.whmaster.widget.SlideView2;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 2018/6/11.
 * 移库数量选择
 */

public class ChooseGoodsNumbersActivity extends BaseActivity implements IMvpView,SlideView2.onSuccessInterface{

    private Bundle mBundle;
    private XRecyclerView mRecyclerView;
    private ArrayList<HashMap<String,Object>> mList;
    private RecyAdapter mAdapter;
    private LinearLayout mTitleLayout;
    private SlideView2 mBtn;
    private EditText mMemoEdit;
    private String mCode,mPositionId,mPosCode;
//    private TextView mCodeText;
    private boolean isNext = false;
    private ImageView mBackImage;
    private LinearLayout mScrollViewLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.choose_goods_numbers_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RecyclerUtil.init(mRecyclerView,this);
        AtyContainerUtils.getInstance().addLibraryActivity(this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
            mList = (ArrayList<HashMap<String, Object>>) mBundle.getSerializable("list");
            mCode = mBundle.getString("code");
            mPosCode = mBundle.getString("posCode");
            mPositionId = mBundle.getString("positionId");
        }
//        mCodeText.setText("移动单位："+mPosCode);
        mAdapter = new RecyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initViews() {
        super.initViews();
        mScrollViewLayout = findViewById(R.id.content_layout);

        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
//        mCodeText = findViewById(R.id.old_code);
        mTitleLayout = findViewById(R.id.title);
        mRecyclerView = findViewById(R.id.choose_goods_numbers_recy_view);
        LinearLayoutManager mManager = new LinearLayoutManager(this);
        mManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mManager);

        mBtn = findViewById(R.id.sub_btn);
        mBtn.setOnSuccessListener(this);
        mMemoEdit = findViewById(R.id.memo_edit);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_image:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("是否返回货品选择页面？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                setResultOk(bundle);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                mBtn.resetXy();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("是否返回货品选择页面？")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                setResultOk(bundle);
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
    public void onSuccess(String type, Object object) {

    }
    @Override
    public void onFail(String errorMsg) {

    }
    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onExcute() {
        for(int i=0;i<mList.size();i++){
            logcat("list的数量是否为0"+mList.get(i).get("moveNum"));
            if(!mList.get(i).get("moveNum").toString().equals("0")){
                isNext = true;
            }
        }
        if(isNext){
            Bundle bundle = new Bundle();
            bundle.putSerializable("list",mList);
            bundle.putString("memo",mMemoEdit.getText().toString());
            bundle.putString("code",mCode);
            bundle.putString("positionId",mPositionId);
            openActivityForResult(ChooseGoodsMoveActivity.class,0,bundle);
        }else{
            Toast.makeText(this,"未选择任何货品",Toast.LENGTH_SHORT).show();
            handler.sendEmptyMessageDelayed(0,500);
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mBtn.resetXy();
                    break;
            }
        }
    };
    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        MyViewHolder holder;
        int packageCount = 0,moveNum,moveZs,inventoryNum;
        boolean isNum = false,isZs = false;
        private TextWatcher zsTextWatcher,numTextWatcher;
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            holder = new MyViewHolder(LayoutInflater.from(
                    ChooseGoodsNumbersActivity.this).inflate(R.layout.chose_goods_numbers_list_item_layout, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            mList.get(position).put("moveNum","0");
            if(mList.get(position).get("productName")!=null){
                holder.productName.setText(mList.get(position).get("productName").toString());
            }
            if(mList.get(position).get("productSkuCode")!=null){
                holder.productSku.setText(mList.get(position).get("productSkuCode").toString());
            }
            if(mList.get(position).get("batchNo")!=null){
                holder.productNo.setText(mList.get(position).get("batchNo").toString());
            }
            if(mList.get(position).get("packageSpec")!=null){
                holder.productGuige.setText(mList.get(position).get("packageSpec").toString());
            }
            if(mList.get(position).get("inventoryNum")!=null){
                holder.sumNumbers.setText(mList.get(position).get("baseUnitCn").toString());
            }
            packageCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
            if(packageCount<=0) packageCount = 1;
            moveNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) % packageCount;
            moveZs = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) / packageCount;
            inventoryNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString());
            if(mList.get(position).get("packageCount")!=null){
                if(moveZs>0){
                    holder.productNumbers.setText(moveZs+mList.get(position).get("packageUnitCn").toString() + "" + moveNum+mList.get(position).get("baseUnitCn").toString());
                }else{
                    holder.productNumbers.setText(moveNum+mList.get(position).get("baseUnitCn").toString());
                }
            }
            holder.baseUnitEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    isNum = true;
                    int m = 0,sum = 0,zs = 0;
                    packageCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
                    if(packageCount<=0) packageCount = 1;
                    moveNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) % packageCount;
                    moveZs = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) / packageCount;
                    inventoryNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString());
                    if(s!=null && !s.toString().equals("")){
                        m = Integer.parseInt(s.toString());
                    }
                    if(holder.baseZsEdit.getText().toString()!=null && !holder.baseZsEdit.getText().toString().equals("")){
                        zs = Integer.parseInt(holder.baseZsEdit.getText().toString());
                    }
//                    if(m<=packageCount){
//                        sum = m + packageCount*zs;
//                    }else{
//                        sum = packageCount + packageCount*zs;
//                        holder.baseUnitEdit.setText(packageCount+"");
//                    }
//                    mList.get(position).put("moveNum",sum+"");
                        sum = m + packageCount*zs;
                        if(sum <= inventoryNum){
                            holder.sumNumbers.setText(sum + mList.get(position).get("baseUnitCn").toString());
                            mList.get(position).put("moveNum",sum+"");
                        }else{
                            holder.sumNumbers.setText(inventoryNum + mList.get(position).get("baseUnitCn").toString());
                            holder.baseUnitEdit.setText(moveNum+"");
                            mList.get(position).put("moveNum",inventoryNum+"");
                            Toast.makeText(ChooseGoodsNumbersActivity.this,"移库数量不能大于可移库总量",Toast.LENGTH_SHORT).show();
                        }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            holder.baseZsEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int m = 0,sum = 0,zs = 0;
                    packageCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
                    inventoryNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString());
                    if(packageCount<=0) packageCount = 1;
                    moveNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) % packageCount;
                    moveZs = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) / packageCount;
                    int packCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
                    if(s!=null && !s.toString().equals("")){
                        zs = Integer.parseInt(s.toString());
                    }
                    if(holder.baseUnitEdit.getText().toString()!=null && !holder.baseUnitEdit.getText().toString().equals("")){
                        m = Integer.parseInt(holder.baseUnitEdit.getText().toString());
                    }
                    sum = m + packCount*zs;
                    if(sum <= inventoryNum) {
                        holder.sumNumbers.setText(sum + mList.get(position).get("baseUnitCn").toString());
                        mList.get(position).put("moveNum",sum+"");
                    }else{
                        logcat(moveZs+"==="+mList.get(position).get("inventoryNum").toString());
                        holder.sumNumbers.setText(inventoryNum + mList.get(position).get("baseUnitCn").toString());
                        holder.baseZsEdit.setText("0");
                        mList.get(position).put("moveNum",inventoryNum+"");
                        Toast.makeText(ChooseGoodsNumbersActivity.this,"移库数量不能大于可移库总量",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });

//           numTextWatcher = new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    holder.baseZsEdit.clearTextChangedListeners();
//                    int m = 0,sum = 0,zs = 0;
//                    packageCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
//                    moveNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) % packageCount;
//                    moveZs = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) / packageCount;
//                    inventoryNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString());
//                    if(s!=null && !s.toString().equals("")){
//                        m = Integer.parseInt(s.toString());
//                    }
//                    if(holder.baseZsEdit.getText().toString()!=null && !holder.baseZsEdit.getText().toString().equals("")){
//                        zs = Integer.parseInt(holder.baseZsEdit.getText().toString());
//                    }
//                    sum = m + packageCount*zs;
//                    if(sum <= inventoryNum){
//                        holder.sumNumbers.setText(sum + mList.get(position).get("baseUnitCn").toString());
//                        mList.get(position).put("moveNum",sum+"");
//                    }else{
//                        holder.sumNumbers.setText(inventoryNum + mList.get(position).get("baseUnitCn").toString());
//                        holder.baseUnitEdit.setText(moveNum+"");
//                        mList.get(position).put("moveNum",inventoryNum+"");
//                        Toast.makeText(ChooseGoodsNumbersActivity.this,"移库数量不能大于可移库总量",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void afterTextChanged(Editable s) {
//                    holder.baseZsEdit.addTextChangedListener(zsTextWatcher);
//                }
//            };
//           zsTextWatcher = new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                }
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    holder.baseUnitEdit.clearTextChangedListeners();
//                    int m = 0,sum = 0,zs = 0;
//                    inventoryNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString());
//                    moveNum = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) % packageCount;
//                    moveZs = Integer.parseInt(mList.get(position).get("inventoryNum").toString()) / packageCount;
//                    packageCount = Integer.parseInt(mList.get(position).get("packageCount").toString());
//                    if(s!=null && !s.toString().equals("")){
//                        zs = Integer.parseInt(s.toString());
//                    }
//                    if(holder.baseUnitEdit.getText().toString()!=null && !holder.baseUnitEdit.getText().toString().equals("")){
//                        m = Integer.parseInt(holder.baseUnitEdit.getText().toString());
//                    }
//                    sum = m + packageCount*zs;
//                    if(sum <= inventoryNum) {
//                        holder.sumNumbers.setText(sum + mList.get(position).get("baseUnitCn").toString());
//                        mList.get(position).put("moveNum",sum+"");
//                    }else{
////                        logcat(moveZs+"==="+moveNum+"==="+inventoryNum+"==="+mList.get(position).get("inventoryNum").toString());
//                        holder.sumNumbers.setText(inventoryNum + mList.get(position).get("baseUnitCn").toString());
//                        holder.baseZsEdit.setText("0");
////                        holder.baseUnitEdit.setText("0");
//                        mList.get(position).put("moveNum",inventoryNum+"");
//                        Toast.makeText(ChooseGoodsNumbersActivity.this,"移库数量不能大于可移库总量",Toast.LENGTH_SHORT).show();
//                    }
//                }
//                @Override
//                public void afterTextChanged(Editable s) {
//                    holder.baseUnitEdit.addTextChangedListener(numTextWatcher);
//                }
//            };
//
//            holder.baseUnitEdit.addTextChangedListener(numTextWatcher);
//            holder.baseZsEdit.addTextChangedListener(zsTextWatcher);

            holder.baseUnit.setText(mList.get(position).get("baseUnitCn").toString());
            holder.baseZs.setText(mList.get(position).get("packageUnitCn").toString());
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView productName, productSku,productNo,productNumbers,productGuige,sumNumbers,baseUnit,baseZs;
            MyEditText baseUnitEdit,baseZsEdit;
            LinearLayout mContentLayout;
            public MyViewHolder(View view) {
                super(view);
                baseUnitEdit = view.findViewById(R.id.base_unit_edit);
                baseZsEdit = view.findViewById(R.id.base_zs_edit);
                baseUnit = view.findViewById(R.id.base_unit_text);
                baseZs = view.findViewById(R.id.base_zs_text);
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
