package com.whmaster.tl.whmaster.presenter;

import android.content.Context;
import android.util.Log;

import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.http.RetrofitHttp;
import com.whmaster.tl.whmaster.impl.GoodsrikInterface;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by admin on 2017/11/24.
 *
 */

public class GoodsRikPresenter extends BasePresenter implements GoodsrikInterface{

    public GoodsRikPresenter(Context context, IMvpView mvpView){
        this.mImvpView = mvpView;
        this.mContext = context;
    }

    @Override
    public void goodsrikList(int page,String orderInType, String orderInCode) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pageNo",page+"");
        map.put("pageSize","10");
        map.put("orderInCode",orderInCode);
//        map.put("orderInType",orderInType);
//        map.put("orderInStatus","");
//        map.put("resourceFlag","");
        map.put("buyerName","");
//        map.put("updateUserName","");
//        map.put("product_sku","");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.goodsReceiptList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"====onError====");
                mImvpView.hideLoading();
//                mImvpView.onFail(e+"");
            }
            @Override
            public void onNext(String s) {
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("success",Constants.getJsonArray(mTempMap.get("records").toString()));
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    mImvpView.onSuccess("list",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",mTempList+"====onNext====");
            }
        });
    }

    @Override
    public void getGoodsDetailList(int page,String orderInId, String orderInCode, String orderInType, String orderInStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pageNo",page+"");
        map.put("pageSize","10");
        map.put("orderInId",orderInId);
//        map.put("orderInCode",orderInCode);
//        map.put("orderInType",orderInType);
//        map.put("orderInStatus",orderInStatus);
//        map.put("resourceFlag","");
//        map.put("buyerName","");
//        map.put("updateUserName","");
//        map.put("product_sku","");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.goodsgetDetailList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"====onError====");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
                    mImvpView.onSuccess("list",mTempMap2);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",mTempList+"====onNext====");
            }
        });
    }

    @Override
    public void updateStatus(String orderInId, String orderInType, String orderInStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("orderInId",orderInId);
        map.put("orderInStatus",orderInStatus);
        map.put("resourceFlag","");
        map.put("buyerName","");
        map.put("updateUserName","");
        map.put("product_sku","");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.updateStatus, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"====onError====");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("updateSuccess",mTempMap);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mImvpView.onSuccess("updateSuccess",mDataMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

}
