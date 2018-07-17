package com.whmaster.tl.whmaster.presenter;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.http.RetrofitHttp;
import com.whmaster.tl.whmaster.impl.LibraryInterface;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by admin on 2017/11/16.
 */

public class LibraryPresenter extends BasePresenter implements LibraryInterface{
    public LibraryPresenter(Context context, IMvpView mvpView){
        this.mImvpView = mvpView;
        this.mContext = context;
    }

    @Override
    public void getMovePosition(String positionCode,String positionPointCode) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("positionCode",positionCode);
        map.put("positionPointCode",positionPointCode);
//        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getMovePosition, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(mTempMap.get("value").toString());
                    mImvpView.onSuccess("list",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void getProductByPosition(String positionCode) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("positionCode",positionCode);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getProductByPosition, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
//                    mTempList = Constants.getJsonArray(mTempMap.get("value").toString());
                    mImvpView.onSuccess("list",mTempMap2);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void save(ArrayMap<String, Object> movePosition, ArrayList<ArrayMap<String, Object>> mList) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("movePosition",movePosition);
        map.put("detlList",mList);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.save, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                if(e.toString().indexOf("SocketTimeoutException")!= -1){
                    mImvpView.onFail("连接超时，请重试。");
                }else{
                    mImvpView.onFail(e+"");
                }
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
//                    mTempMap = Constants.getJsonObject(s);
//                    mTempList = Constants.getJsonArray(mTempMap.get("value").toString());
                    mImvpView.onSuccess("saveSuccess",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }
}
