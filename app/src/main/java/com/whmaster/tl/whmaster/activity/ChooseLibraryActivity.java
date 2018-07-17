package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.popupwindow.LibraryPopup;
import com.whmaster.tl.whmaster.presenter.LibraryPresenter;
import com.whmaster.tl.whmaster.utils.AtyContainerUtils;
import com.whmaster.tl.whmaster.utils.RecyclerUtil;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by admin on 2017/11/16.
 * 库内移动
 */

public class ChooseLibraryActivity extends BaseActivity implements IMvpView {
    private LinearLayout mLayout,mTitleLayout;
    private String mOldPositionCode = "";//CK510112A-I01-31
    private LibraryPresenter libraryPresenter;
    private String m_Broadcastname;
    private EditText mKuquEdit, mKuweiEdit;
    private TextView mConfirmText,mPositionCodeText;
    private LibraryPopup libraryPopup;
    private String mPositionCode = "",mPositionPointCode="",positionId = "",mPosCode,mCode = "";
    private ArrayList<ArrayMap<String,Object>> mList;
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private TextView mTitleName;
    private ImageView mBackImage;
    private InputMethodManager imm;
    private String mKeyString = "";

    @Override
    protected int getLayoutId() {
        return R.layout.chose_library_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addLibraryActivity(this);
        libraryPresenter = new LibraryPresenter(this, this);
        libraryPopup = new LibraryPopup(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        RecyclerUtil.init(mRecyclerView,this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        mRecyclerView.refreshComplete();
                        mRecyclerView.setLoadingMoreEnabled(true);
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                    }
                }, 1000);
            }
        });
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleName = findViewById(R.id.title_name);
        mTitleName.setText("移出库位");
        mPositionCodeText = findViewById(R.id.position_code_text);
        mRecyclerView = findViewById(R.id.x_revyvler_view);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mConfirmText = findViewById(R.id.confirm_text);
        mConfirmText.setOnClickListener(this);

        mKuquEdit = findViewById(R.id.kuqu_edit);
        mKuweiEdit = findViewById(R.id.kuwei_edit);
        mLayout = findViewById(R.id.chose_layout);
        mLayout.setOnClickListener(this);
        mKuquEdit.addTextChangedListener(kuquTextwatcher);
        mKuweiEdit.addTextChangedListener(dianweiTextwatcher);
    }
    private TextWatcher kuquTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().equals("")){
                if(mRecyclerView.getVisibility()!=View.VISIBLE){
                    mRecyclerView.setVisibility(View.VISIBLE);
                }

                mKeyString = mPositionCode = s.toString();

                libraryPresenter.getMovePosition(mPositionCode,mKuweiEdit.getText().toString());
            }else{
                mRecyclerView.setVisibility(View.GONE);
                imm.hideSoftInputFromWindow(mKuquEdit.getWindowToken(), 0);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            logcat("afterTextChanged");

        }
    };
    private TextWatcher dianweiTextwatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            if(!s.toString().equals("")){
                if(mRecyclerView.getVisibility()!=View.VISIBLE){
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mKeyString =  mPositionPointCode = s.toString();
                libraryPresenter.getMovePosition(mKuquEdit.getText().toString(),mPositionPointCode);
            }else{

                imm.hideSoftInputFromWindow(mKuquEdit.getWindowToken(), 0);
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    };
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_image:
                    finish();
                break;
            case R.id.confirm_text:
                if(!mPositionCodeText.getText().toString().equals("")){
                    Bundle bundle = new Bundle();
                    bundle.putString("positionId",positionId);
                    bundle.putString("code",mCode);
                    bundle.putString("posCode",mPosCode);
                    startActivity(ChooseLibraryGoodsActivity.class,bundle);
                }else{
                    Toast.makeText(this,"请选择区域和点位",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.chose_layout:
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
        mList = (ArrayList<ArrayMap<String, Object>>) object;
        mAdapter = new RecyAdapter();
        mRecyclerView.setAdapter(mAdapter);
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
    protected void onResume() {
        super.onResume();
        final IntentFilter intentFilter = new IntentFilter();
        m_Broadcastname = "com.barcode.sendBroadcast";// com.barcode.sendBroadcastScan
        intentFilter.addAction(m_Broadcastname);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ChooseLibraryActivity.this).inflate(R.layout.library_popup_item, parent,
                    false));
            return holder;
        }
        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {

            if(mList.get(position).get("positionCode")!=null){
                SpannableStringBuilder builder = new SpannableStringBuilder(mList.get(position).get("positionCode").toString());
                int indexOf = mList.get(position).get("positionCode").toString().indexOf(mKeyString);
                if (indexOf != -1) {
                    builder.setSpan(new ForegroundColorSpan(Color.RED), indexOf, indexOf + mKeyString.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.name.setText(builder);
//                holder.name.setText(mList.get(position).get("positionCode")+"");
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPositionCodeText.setText(mList.get(position).get("posCode").toString());
                    mCode = mList.get(position).get("positionCode").toString();
                    positionId = mList.get(position).get("positionId").toString();
                    mPosCode = mList.get(position).get("posCode").toString();
                    mRecyclerView.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(mKuquEdit.getWindowToken(), 0);
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            LinearLayout mContentLayout;
            public MyViewHolder(View view) {
                super(view);
                mContentLayout = view.findViewById(R.id.content_layout);
                name = view.findViewById(R.id.name_text);
            }
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            if (arg1.getAction().equals(m_Broadcastname)) {
                String str = arg1.getStringExtra("BARCODE");
                if (!"".equals(str)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("code",str);
                    bundle.putString("posCode",str);
                    startActivity(ChooseLibraryGoodsActivity.class,bundle);
                    logcat("获取获取扫描条形码" + str);
                } else {
                    Toast.makeText(ChooseLibraryActivity.this, "请扫描正确的条形码！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
