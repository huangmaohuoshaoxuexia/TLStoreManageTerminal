package com.whmaster.tl.whmaster.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.whmaster.tl.whmaster.activity.LoginActivity;
import com.whmaster.tl.whmaster.utils.AtyContainerUtils;
import com.whmaster.tl.whmaster.widget.AlertDialog;

/**
 * Created by admin on 2018/3/1.
 */

public class LoginBroadcastReceiver extends BroadcastReceiver {
    protected AlertDialog mAlertDialog;
    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i("com.whmaster.tl.whmaster>>获取拦截器数据","====广播接受===");
        Toast.makeText(context,"你的身份已过期，请重新登录！",Toast.LENGTH_SHORT).show();
        AtyContainerUtils.getInstance().finishAllActivity();
        Intent intent1 = new Intent();
        intent1.setClass(context, LoginActivity.class);
        context.startActivity(intent1);

    }
}
