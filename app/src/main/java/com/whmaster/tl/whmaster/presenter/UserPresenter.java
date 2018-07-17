package com.whmaster.tl.whmaster.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.http.RetrofitHttp;
import com.whmaster.tl.whmaster.impl.UserInterface;
import com.whmaster.tl.whmaster.model.User;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by admin on 2017/10/26.
 */

public class UserPresenter extends BasePresenter implements UserInterface{
    public UserPresenter(Context context, IMvpView mvpView){
        this.mImvpView = mvpView;
        this.mContext = context;
    }
    @Override
    public void login(String username, String password,String type) {
        Map map = new HashMap();
        map.put("userName",username);
//        map.put("password","E10ADC3949BA59ABBE56E057F20F883E");
        map.put("password",password);
        RetrofitHttp.getInstance(mContext).postJson(Constants.login, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"============");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("resultStatus").toString());
                    if(mTempMap2!=null && mTempMap2.get("resultCode").equals("0000")){
                        Constants.token = mTempMap.get("value").toString();
                        mImvpView.onSuccess("login",mTempMap);
                    }else{
                        mImvpView.onFail(mTempMap2.get("resultMessage")+"");
                    }
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        User user = new User();
//                        user = (User) Constants.getGsonObject(mTempMap.get("result").toString(),user);
//                        mImvpView.onSuccess("login",Constants.getGsonObject(mTempMap.get("result").toString(),user));
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获得数据",s+"========");
            }
        });
    }

    @Override
    public void loginToken(String username, String password, String type) {
        Map map = new HashMap();
        map.put("userName",username);
//        map.put("password","E10ADC3949BA59ABBE56E057F20F883E");
        map.put("password",password);
        RetrofitHttp.getInstance(mContext).postJson(Constants.login, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"============");
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("resultStatus").toString());
                    if(mTempMap2!=null && mTempMap2.get("resultCode").equals("0000")){
                        Constants.token = mTempMap.get("value").toString();
                    }else{
                    }
                }
                Log.i("com.whmaster.tl.whmaster>>获得数据",s+"========");
            }
        });
    }

    @Override
    public void islogin() {
        Map map = new HashMap();
        RetrofitHttp.getInstance(mContext).get(Constants.islogin, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>是否登录",e+"============");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null){
                        mImvpView.onSuccess("islogin",mTempMap);
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>是否登录",s+"========");
            }
        });
    }

    @Override
    public void loginout() {
        Map map = new HashMap();
        map.put("tokenId",Constants.token);
        RetrofitHttp.getInstance(mContext).postJson(Constants.loginout, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"======onError======");
                mImvpView.onSuccess("loginout",null);
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mImvpView.onSuccess("loginout",null);
                    }else{
                        mImvpView.onSuccess("loginout",null);
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Constants.token = "";

                Log.i("com.whmaster.tl.whmaster>>退出登录",s+"========"+mTempMap);
            }
        });
    }

    @Override
    public void perms() {
        Map map = new HashMap();
        map.put("terminalType","10");
        RetrofitHttp.getInstance(mContext).post(Constants.perms, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"============");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mImvpView.onSuccess("perms",null);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取权限",s+"========");
            }
        });
    }


}
