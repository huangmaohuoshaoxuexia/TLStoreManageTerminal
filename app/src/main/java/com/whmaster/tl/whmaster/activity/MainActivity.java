package com.whmaster.tl.whmaster.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.activity.inventory.InventoryChoseActivity;
import com.whmaster.tl.whmaster.model.MyDecoration;
import com.whmaster.tl.whmaster.model.User;
import com.whmaster.tl.whmaster.presenter.StoragePresenter;
import com.whmaster.tl.whmaster.presenter.UserPresenter;
import com.whmaster.tl.whmaster.utils.ScreenUtils;
import com.whmaster.tl.whmaster.view.IMvpView;

public class MainActivity extends BaseActivity implements IMvpView{

//    private User mUser;
    private Bundle mBundle;
    private TextView mNameText;
    private Button mZxBtn;
    private XRecyclerView mRecyclerView;
    private RecyAdapter mAdapter;
    private int[] mImageIds={R.mipmap.ic_sh,R.mipmap.ic_rk,R.mipmap.ic_ck,R.mipmap.ic_fh,R.mipmap.ic_yd,R.mipmap.ic_pd};
    private String[] mNames={"实物收货","入库上架","拣货出库","装车复核","移库管理","盘点管理"};
    private UserPresenter userPresenter;
    private StoragePresenter storagePresenter;
    private String mStorageCount="0",mTaskCount="0",mFhCount="0",mShCount = "0",mInventoryCount = "0";
    private MyBroadcastReceiver myBroadcastReceiver;
    public static String broadcastFlag = "main";
    private LinearLayout mTitleLayout,mHeadLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userPresenter = new UserPresenter(this,this);
        storagePresenter = new StoragePresenter(this,this);
        mBundle = getIntent().getExtras();
        if(mBundle!=null){
//            mUser = (User) mBundle.getSerializable("object");
            mNameText.setText("你好，"+mBundle.getString("username"));
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.SysProgress);
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.LineSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.mipmap.pulltorefresh_arrow);
        mRecyclerView.setLoadingMoreEnabled(false);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        getHttpCount();
                        mRecyclerView.refreshComplete();
                    }
                }, 1000);
            }
            @Override
            public void onLoadMore() {}
    });
        mAdapter = new RecyAdapter();

        mRecyclerView.setAdapter(mAdapter);

        getHttpCount();
        myBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(broadcastFlag);    //只有持有相同的action的接受者才能接收此广播
        registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        super.initViews();
        mHeadLayout = findViewById(R.id.main_head_layout);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mHeadLayout.getLayoutParams();
        params.height = (int) (ScreenUtils.getScreenWidth(this)*0.5);
        mHeadLayout.setLayoutParams(params);
        mNameText = findViewById(R.id.user_name_text);
        mZxBtn = findViewById(R.id.zx_btn);
        mRecyclerView = findViewById(R.id.main_recyview);
        mTitleLayout = findViewById(R.id.title);
        mTitleLayout.setVisibility(View.GONE);
        mZxBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            //注销 
            case R.id.zx_btn:
                userPresenter.loginout();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logcat("获取返回的监听");
        switch (requestCode){
            case 0:
                getHttpCount();
                break;
        }
    }
    private void getHttpCount(){
        storagePresenter.queryUnfinishedMaterialCount();
        storagePresenter.getStockConut();
        storagePresenter.getTaskCount();
        storagePresenter.getReviewCount();
        storagePresenter.getInventoryCount();
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
    public void setHeader() {
        super.setHeader();
        mBackImage.setVisibility(View.GONE);
        mRight.setVisibility(View.GONE);
    }

    @Override
    public void onFail(String errorMsg) {
    }


    @Override
    public void onSuccess(String type, Object object) {
        ArrayMap map = (ArrayMap) object;
        switch (type){
            case "loginout":
                Bundle bundle = new Bundle();
                bundle.putString("type","zhuxiao");
                startActivity(LoginActivity.class,bundle);
                finish();
                break;
            //入库单数
            case "getStorageCount":
                mStorageCount = map.get("value").toString();
                mAdapter.notifiAdapter(mStorageCount,mTaskCount,mFhCount,mShCount,mInventoryCount);
                break;
            //拣货单数
            case "getTaskCount":
                mTaskCount = map.get("value").toString();
                mAdapter.notifiAdapter(mStorageCount,mTaskCount,mFhCount,mShCount,mInventoryCount);
                break;
            //拣货复核
            case "getFhCount":
                mFhCount = map.get("value").toString();
                mAdapter.notifiAdapter(mStorageCount,mTaskCount,mFhCount,mShCount,mInventoryCount);
                break;
            //实物收货
            case "queryUnfinishedMaterialCount":
                mShCount = map.get("value").toString();
                mAdapter.notifiAdapter(mStorageCount,mTaskCount,mFhCount,mShCount,mInventoryCount);
                break;
            case "getInventoryCount":
                mInventoryCount = map.get("value").toString();
                mAdapter.notifiAdapter(mStorageCount,mTaskCount,mFhCount,mShCount,mInventoryCount);
                break;
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (myBroadcastReceiver != null) {
                unregisterReceiver(myBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            logcat("接收广播==============");
           getHttpCount();
        }
    }

    class RecyAdapter extends RecyclerView.Adapter<RecyAdapter.MyViewHolder>{
        private String storagCount="0",taskCount="0",fhCount = "0",shshCount = "0",minventoryCount = "0";
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    MainActivity.this).inflate(R.layout.main_item_layout, parent,
                    false));
            return holder;
        }
        public void notifiAdapter(String storagecount,String taskcount,String fhcount,String shshcount,String inventoryCount){
            storagCount = storagecount;
            taskCount = taskcount;
            fhCount = fhcount;
            shshCount = shshcount;
            minventoryCount = inventoryCount;
            this.notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            if(position%2==1){
                holder.mVerView.setVisibility(View.GONE);
            }else{
                holder.mVerView.setVisibility(View.VISIBLE);
            }
            if (position>3){
                holder.mHorView.setVisibility(View.GONE);
            }else{
                holder.mHorView.setVisibility(View.VISIBLE);
            }
           holder.imageView.setImageResource(mImageIds[position]);
            holder.textView.setText(mNames[position]);

            switch (position){
                case 0:
                    if(!shshCount.equals("") && !shshCount.equals("0")){
                        holder.mConuntText.setText(shshCount);
                        holder.mConuntText.setVisibility(View.VISIBLE);
                    }else{
                        holder.mConuntText.setVisibility(View.GONE);
                    }
                    break;
                case 1:
                    if(!storagCount.equals("") && !storagCount.equals("0")){
                        holder.mConuntText.setText(storagCount);
                        holder.mConuntText.setVisibility(View.VISIBLE);
                    }else{
                        holder.mConuntText.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    if(!taskCount.equals("") && !taskCount.equals("0")){
                        holder.mConuntText.setText(taskCount);
                        holder.mConuntText.setVisibility(View.VISIBLE);
                    }else{
                        holder.mConuntText.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    if(!fhCount.equals("") && !fhCount.equals("0")){
                        holder.mConuntText.setText(fhCount);
                        holder.mConuntText.setVisibility(View.VISIBLE);
                    }else{
                        holder.mConuntText.setVisibility(View.GONE);
                    }
                    break;
                case 5:
                    if(!minventoryCount.equals("") && !minventoryCount.equals("0")){
                        holder.mConuntText.setText(minventoryCount);
                        holder.mConuntText.setVisibility(View.VISIBLE);
                    }else{
                        holder.mConuntText.setVisibility(View.GONE);
                    }
                    break;
                default:
                    holder.mConuntText.setVisibility(View.GONE);
                    break;
            }
            holder.mContentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position){
                        //实物收货
                        case 0:
                            startActivity(GoodsReceiptListActivity.class,null);
                            break;
                        //上架单生成
                        case 1:
                            startActivity(GenerateSjRkListActivity.class,null);
                            break;
//                        //生成出库单
//                        case 2:
//                            startActivity(GenerateCkdActivity.class,null);
//                            break;
//                        //生成拣货单
//                        case 3:
//                            startActivity(GeneratePickActivity.class,null);
//                            break;
                        //拣出货品
                        case 2:
//                            startActivity(PickingActivity.class,bundle);
                            startActivity(PickingListActivity.class,null);
                            break;
                        //拣货复核
                        case 3:
//                            startActivity(PickingActivity.class,bundle);
                            startActivity(ReviewListActivity.class,null);
                            break;
                        //装车确认
//                        case 3:
//                            startActivity(LoadingConfirmActivity.class,null);
//                            break;
                        //取消条码
//                        case 4:
//                            startActivity(CancelBarCodeActivity.class,null);
//                            break;
                        //库位库存
//                        case 4:
//                            startActivity(ScanLibraryActivity.class,null);
//                            break;
                        //库内移动
                        case 4:
                            startActivity(ChooseLibraryActivity.class,null);
                            break;
                        //盘点
                        case 5:
                            startActivity(InventoryChoseActivity.class,null);
                            break;
                    }
                }
            });
        }
        @Override
        public int getItemCount() {
            return 6;
        }
        class MyViewHolder extends RecyclerView.ViewHolder {
            FrameLayout mContentLayout;
            private ImageView imageView;
            private TextView textView,mConuntText;
            private View mHorView,mVerView;
            public MyViewHolder(View view) {
                super(view);
                textView = view.findViewById(R.id.item_name);
                imageView = view.findViewById(R.id.item_image);
                mContentLayout = view.findViewById(R.id.item_layout);
                mConuntText = view.findViewById(R.id.conunt_text);
                mHorView = view.findViewById(R.id.horizontal_line_view);
                mVerView = view.findViewById(R.id.vertical_line_view);
            }
        }
    }

}
