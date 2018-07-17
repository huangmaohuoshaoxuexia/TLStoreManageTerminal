package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
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
import com.whmaster.tl.whmaster.widget.LibraryAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 移入库位
 * Created by admin on 2018/6/11.
 */

public class ChooseGoodsMoveActivity extends BaseActivity implements IMvpView {
    private Bundle mBundle;
    private ArrayList<ArrayMap<String, Object>> mList, mDataList;
    private String mMemo, mOldPositionId ,mNewPosition,mOldCode;
    private LinearLayout mLayout, mTitleLayout;
    private LibraryPresenter libraryPresenter;
    private String m_Broadcastname;
    private EditText mKuquEdit, mKuweiEdit;
    private TextView mConfirmText, mPositionCodeText;
    private String mPositionCode = "", mPositionPointCode = "";
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private ArrayMap<String, Object> mPositionMap;
    private TextView mTitleText;
    private LibraryAlertDialog libraryAlertDialog;
    private ImageView mBackImage;

    @Override
    protected int getLayoutId() {
        return R.layout.chose_library_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AtyContainerUtils.getInstance().addLibraryActivity(this);
        libraryPresenter = new LibraryPresenter(this, this);
        libraryAlertDialog = new LibraryAlertDialog(this);
        mPositionMap = new ArrayMap<>();
        mBundle = getIntent().getExtras();
        if (mBundle != null) {
            mDataList = (ArrayList) mBundle.getSerializable("list");
            mMemo = mBundle.getString("memo");
            mOldCode = mBundle.getString("code");
            mOldPositionId = mBundle.getString("positionId");
            mPositionMap.put("moveOutPosition", mOldPositionId);
            mPositionMap.put("memo", mMemo);
        }
        RecyclerUtil.init(mRecyclerView, this);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
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
        logcat("获取到的list" + mDataList);
    }

    @Override
    public void initViews() {
        super.initViews();
        mBackImage = findViewById(R.id.back_image);
        mBackImage.setOnClickListener(this);
        mTitleText = findViewById(R.id.title_name);
        mTitleText.setText("移入库位");
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
            logcat("状态监听beforeTextChanged");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            logcat("状态监听onTextChanged");

            if (!s.toString().equals("")) {
                if (mRecyclerView.getVisibility() != View.VISIBLE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mPositionCode = s.toString();
                libraryPresenter.getMovePosition(mPositionCode, mKuweiEdit.getText().toString());
            } else {
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

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
            if (!s.toString().equals("")) {
                if (mRecyclerView.getVisibility() != View.VISIBLE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                mPositionPointCode = s.toString();
                libraryPresenter.getMovePosition(mKuquEdit.getText().toString(), mPositionPointCode);
            } else {
                mRecyclerView.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_image:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("是否返回数量选择页面？")
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
                                libraryAlertDialog.dismiss();
                            }
                        })

                        .show();
                break;
            case R.id.confirm_text:
                libraryAlertDialog.builder().setTitle("")
                        .setMsg("")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mDataList.size() > 0 && mPositionMap.size() > 0) {
                                    libraryPresenter.save(mPositionMap, mDataList);
                                }
                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                libraryAlertDialog.dismiss();
                            }
                        }) .show();
                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                mAlertDialog.builder().setTitle("提示")
                        .setMsg("是否返回数量选择页面？")
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
            case "list":
                mList = (ArrayList<ArrayMap<String, Object>>) object;
                mAdapter = new RecyAdapter();
                mRecyclerView.setAdapter(mAdapter);
                break;
            case "saveSuccess":
                AtyContainerUtils.getInstance().finishLibraryActivity();
                Toast.makeText(ChooseGoodsMoveActivity.this, "移库成功！", Toast.LENGTH_SHORT).show();
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

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder> {
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ChooseGoodsMoveActivity.this).inflate(R.layout.library_popup_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyAdapter.MyViewHolder holder, final int position) {
            if (mList.get(position).get("positionCode") != null) {
                holder.name.setText(mList.get(position).get("positionCode") + "");
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPositionCodeText.setText(mList.get(position).get("posCode").toString());
                    mNewPosition = mList.get(position).get("positionId").toString();
                    mPositionMap.put("moveInPosition", mNewPosition);
                    mPositionMap.put("movePositionCode", "");
                    mRecyclerView.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    mPositionCodeText.setText(str);
                    mPositionMap.put("moveInPosition", "");
                    mPositionMap.put("movePositionCode", str);
                    logcat("获取获取扫描条形码" + str);
                } else {
                    Toast.makeText(ChooseGoodsMoveActivity.this, "请扫描正确的条形码！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

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
}
