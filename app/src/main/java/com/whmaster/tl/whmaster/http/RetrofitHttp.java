package com.whmaster.tl.whmaster.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.whmaster.tl.whmaster.common.Constants;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/15.
 */
public class RetrofitHttp {
    private ApiService apiService;
    private volatile static RetrofitHttp INSTANCE;
    private OkHttpClient okHttpClient;
    //构造方法私有
    private RetrofitHttp(Context context) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
//                .cookieJar(new CookieManager(context))
//                .addInterceptor(new ReceivedCookiesInterceptor(context))
//                .addInterceptor(new AddCookiesInterceptor(context))
                .addInterceptor(new TokenInterceptor(context))
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
//        okHttpClient = httpClient.cookieJar(new CookieManager(context)).build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
//                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.apiHead)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    //获取单例
    public static RetrofitHttp getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (RetrofitHttp.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitHttp(context);
                }
            }
        }
        return INSTANCE;
    }
    public void get(String url, Map<String,String> map, Subscriber<String> subscriber) {
        apiService.get(url, map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
    public void post(String url, Map<String,String> map, Subscriber<String> subscriber) {
        apiService.post(url, map)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void postJson(String url, Map<String,String> map, Subscriber<String> subscriber) {
        String bodyJson = JSONObject.toJSONString(map);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bodyJson);
        Log.i("com.whmaster.tl.whmaster>>返回数据",body+"=====请求json数据======="+bodyJson+"="+url);
        apiService.postJson(url, body)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
