package com.whmaster.tl.whmaster.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.whmaster.tl.whmaster.common.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/5/16.
 */
public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("whmasterUser", Context.MODE_PRIVATE);
//这里用了RxJava
        Observable.just(sharedPreferences.getString("cookie", ""))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        //添加cookie 
//                        builder.addHeader("Cookie", cookie);
                        builder.addHeader("Cookie", Constants.token);
//                        Log.i("com.whmaster.tl.whmaster>>",cookie+"=======添加cookie到header===11=====");
                    }
                });
        return chain.proceed(builder.build());
    }
}
