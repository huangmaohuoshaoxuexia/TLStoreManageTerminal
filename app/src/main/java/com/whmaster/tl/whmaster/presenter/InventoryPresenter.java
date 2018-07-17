package com.whmaster.tl.whmaster.presenter;

import android.content.Context;
import android.util.Log;

import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.http.RetrofitHttp;
import com.whmaster.tl.whmaster.impl.InventoryInterface;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by admin on 2018/6/27.
 */

public class InventoryPresenter extends BasePresenter implements InventoryInterface {
    public InventoryPresenter(Context context, IMvpView mvpView){
        this.mImvpView = mvpView;
        this.mContext = context;
    }
    @Override
    public void inventoryList(String stocktakingType,int page) {
        Map map = new HashMap();
        map.put("token", Constants.token);
        map.put("pageNum", page);
        map.put("pageSize", "10");
        map.put("stocktakingType",stocktakingType);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.inventoryList, map, new Subscriber<String>() {
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
                    mTempList = Constants.getJsonArrayByValue(mTempMap.get("value").toString());
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
    public void detailList(String stocktakingId) {
        Map map = new HashMap();
        map.put("token", Constants.token);
        map.put("stocktakingId",stocktakingId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.detailList, map, new Subscriber<String>() {
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
                    mTempMap = Constants.getJsonObject(mTempMap.get("value").toString());
                    mImvpView.onSuccess("data",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void update(int stocktakingNum, String memo, String stocktakingDetailId, int inventoryNum) {
        Map map = new HashMap();
        map.put("token", Constants.token);
        map.put("stocktakingNum",stocktakingNum+"");
        map.put("memo",memo);
        map.put("stocktakingDetailId",stocktakingDetailId);
        map.put("inventoryNum",inventoryNum+"");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.update, map, new Subscriber<String>() {
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
                    mImvpView.onSuccess("success",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void end(String stocktakingId,int stocktakingTotalNum) {
        Map map = new HashMap();
        map.put("token", Constants.token);
        map.put("stocktakingId",stocktakingId);
        map.put("stocktakingTotalNum",stocktakingTotalNum);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.end, map, new Subscriber<String>() {
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
                    mImvpView.onSuccess("updateSuccess","0");
                }else{
                    mImvpView.onSuccess("updateSuccess",mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }
}
