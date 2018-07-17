package com.whmaster.tl.whmaster.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.whmaster.tl.whmaster.common.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Administrator on 2017/5/16.
 */
public class ReceivedCookiesInterceptor implements Interceptor {
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
//            Log.i("com.whmaster.tl.whmaster>>","=====获取cookie====="+originalResponse.headers("Set-Cookie"));
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];

                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie).append(";");
                        }
                    });
            SharedPreferences sharedPreferences = context.getSharedPreferences("whmasterUser", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", cookieBuffer.toString());
            Constants.token = cookieBuffer.toString();
//            Log.i("com.whmaster.tl.whmaster>>","=====获取cookie====="+cookieBuffer.toString());
//            Log.i("com.whmaster.tl.whmaster>>","=====保存cookie========"+cookieBuffer.toString());
            editor.commit();
        }
        return originalResponse;
    }
}
