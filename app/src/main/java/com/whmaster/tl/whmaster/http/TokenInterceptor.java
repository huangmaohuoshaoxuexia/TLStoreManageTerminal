package com.whmaster.tl.whmaster.http;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.whmaster.tl.whmaster.broadcastReceiver.LoginBroadcastReceiver;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.presenter.UserPresenter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.CopyOnWriteArrayList;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Call;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by Administrator on 2017/5/16.
 */
public class TokenInterceptor implements Interceptor {
    private Context context;
    private UserPresenter userPresenter;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public TokenInterceptor(Context context) {
        super();
        this.context = context;
        userPresenter = new UserPresenter(context,null);
    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        // try the request
        Response originalResponse = chain.proceed(request);

        /**通过如下的办法曲线取到请求完成的数据
         *
         * 原本想通过  originalResponse.body().string()
         * 去取到请求完成的数据,但是一直报错,不知道是okhttp的bug还是操作不当
         *
         * 然后去看了okhttp的源码,找到了这个曲线方法,取到请求完成的数据后,根据特定的判断条件去判断token过期
         */
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        String bodyString = buffer.clone().readString(charset);
        try{
            ArrayMap mDataMap = Constants.getJsonObject(Constants.getJsonObject(bodyString).get("resultStatus").toString());

                if(mDataMap.get("resultCode").toString().equals("401")){
                    Log.i("com.whmaster.tl.whmaster>>获取拦截器数据3","======="+mDataMap);
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    LoginBroadcastReceiver myBroadcastReceiver = new LoginBroadcastReceiver();
                    context.registerReceiver(myBroadcastReceiver, intentFilter);

                    Intent intent = new Intent("android.net.conn.CONNECTIVITY_CHANGE");
                    context.sendBroadcast(intent); // 发送广播
                }
        }catch (Exception e){
            e.printStackTrace();
        }


//        LogUtil.debug("body---------->" + bodyString);

        /***************************************/


        // otherwise just pass the original response on
        return originalResponse;

    }


}
