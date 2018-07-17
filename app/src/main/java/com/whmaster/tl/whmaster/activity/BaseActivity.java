package com.whmaster.tl.whmaster.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.whmaster.tl.whmaster.R;
import com.whmaster.tl.whmaster.base.BaseInit;
import com.whmaster.tl.whmaster.base.PublishActivityCallBack;
import com.whmaster.tl.whmaster.utils.AtyContainerUtils;
import com.whmaster.tl.whmaster.widget.AlertDialog;
import com.whmaster.tl.whmaster.widget.LoadingDialog;
import com.whmaster.tl.whmaster.widget.MsgLoadingDialog;

/**
 * Created by admin on 2017/10/23.
 */

public abstract class BaseActivity extends Activity implements BaseInit,View.OnClickListener, PublishActivityCallBack {
    protected ImageView mBackImage;//返回按钮
    protected TextView mTitle, mRight;
    protected AlertDialog mAlertDialog;
    protected LoadingDialog loadingDialog;
    protected MsgLoadingDialog msgLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AtyContainerUtils.getInstance().addActivity(this);
        setImmerseLayout(findViewById(R.id.head_id));
        initViews();
        setHeader();
        initData();
        initListeners();
        mAlertDialog = new AlertDialog(this);
        loadingDialog = new LoadingDialog(this);
        msgLoadingDialog = new MsgLoadingDialog(this);
//                if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
//            finish();
//            return;
//        }

    }
    protected abstract int getLayoutId();
    @Override
    public void initViews() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void logcat(String msg) {
        Log.i("com.whmaster.tl.whmaster>>",msg+"=======");
    }

    @Override
    public void setHeader() {
        mBackImage = findViewById(R.id.back);
        mTitle = findViewById(R.id.tv_title);
        mRight = findViewById(R.id.tv_right);
        mBackImage.setOnClickListener(this);
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void setResultOk(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }
    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    /**
     * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
     *
     * @return 返回状态栏高度的像素值。
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
