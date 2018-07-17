package com.whmaster.tl.whmaster.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.http.RetrofitHttp;
import com.whmaster.tl.whmaster.impl.PickingInterface;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLHandshakeException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by admin on 2017/11/8.
 */

public class PickingPresenter extends BasePresenter implements PickingInterface{
    public PickingPresenter(Context context, IMvpView mvpView){
        this.mImvpView = mvpView;
        this.mContext = context;
    }
    @Override
    public void pickingList(String pickCode,String type,int page) {
        Map map = new HashMap();
        Log.i("com.whmaster.tl.whmaster>>返回数据",Constants.token+"=====token数据=======");
        map.put("token",Constants.token);
        map.put("pickCode",pickCode);
        map.put("pageNo",page+"");
        map.put("pageSize","10");
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).post(Constants.pickingListOrder, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mTempList = Constants.getJsonArray(mTempMap.get("records").toString());
                        mImvpView.onSuccess("list",mTempList);

                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void pickingGoodsList(String id) {
        Map map = new HashMap();
        map.put("stockOutId",id);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).post(Constants.pickingDetailList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mTempList = Constants.getJsonArray(mTempMap.get("result").toString());
                        mImvpView.onSuccess("list",mTempList);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void pickingGoodsCheck(String id, String type) {
        Map map = new HashMap();
        map.put("detailId",id);
        map.put("businessType",type);
        RetrofitHttp.getInstance(mContext).post(Constants.pickingDetailList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mTempList = Constants.getJsonArray(mTempMap.get("result").toString());
                        mImvpView.onSuccess("list",mTempList);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }
    @Override
    public void executeStockOutTask(String stockInId,String postId) {
        Map map = new HashMap();
        map.put("stockOutId",stockInId);
        map.put("postId",postId);
        mImvpView.showLoading();
        Log.i("com.whmaster.tl.whmaster>>",postId+"======获取快递单号======");
        RetrofitHttp.getInstance(mContext).post(Constants.executePick, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mImvpView.onSuccess("execute",mTempMap);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>执行完毕",s+"====onNext====");
            }
        });
    }

    @Override
    public void fhCheck(String stockOutId) {
        Map map = new HashMap();
        map.put("stockOutId",stockOutId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).post(Constants.fhcheck, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mImvpView.onSuccess("execute",mTempMap);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>执行完毕",s+"====onNext====");
            }
        });
    }

    @Override
    public void pickList(String pickCode, int pageNo) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pickCode",pickCode);
        map.put("pageNo",pageNo);
        map.put("pageSize",10);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.pickingListOrder, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    mImvpView.onSuccess("list",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取拣货单数",s+"========");
            }
        });
    }

    @Override
    public void pickingDetailList( String pickCode) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pickCode",pickCode);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.pickingDetailListOrder, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
                    mImvpView.onSuccess("list",mTempMap2);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取拣货单详情",s+"====onNext====");
            }
        });
    }

    @Override
    public void pickingDetail(String pickDetlId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pickDetlId",pickDetlId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.pickingDetail, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
                    mImvpView.onSuccess("data",mTempMap2);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void pickDetailupdateStatus(String pickCode, int pickStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pickCode",pickCode);
        map.put("pickStatus",pickStatus);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.pickDetailupdateStatus, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mImvpView.onSuccess("update","success");
                }else{
                    mImvpView.onSuccess("update",mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void save(String pickDetlId, int actlPickInt, int actlPickRmnd, String memo, int pickDetlStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pickDetlId",pickDetlId);
        map.put("actlPickInt",actlPickInt);
        map.put("actlPickRmnd",actlPickRmnd);
        map.put("memo",memo);
        map.put("pickDetlStatus",pickDetlStatus);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.pickingSave, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mImvpView.onSuccess("success",mDataMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void reviewList(String outCode, int page) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("outCode",outCode);
        map.put("outStatus","140");
        map.put("pageNo",page);
        map.put("pageSize",10);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.reviewList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
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
    public void reviewDetailList(String outCode,String outId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("outCode",outCode);
        map.put("outId",outId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.reviewDetailList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("data",Constants.getJsonObject(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void outDetailupdateStatus(String outCode, int outStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("outCode",outCode);
        map.put("outStatus",outStatus);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.outUpdateStatus, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.onSuccess("update",e+"");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mImvpView.onSuccess("update","success");
                }else{
                    mImvpView.onSuccess("update",mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void reviewDetail( String outDetlId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("outDetlId",outDetlId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.reviewInfo, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("data",Constants.getJsonObject(mTempMap.get("value").toString()));
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void outSave(String outDetlId,int actlOutRmnd, String memo, int outDetlStatus) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("actOutCount",actlOutRmnd);
        map.put("memo",memo);
        map.put("outDetlId",outDetlId);
        map.put("outDetlStatus",outDetlStatus);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.reviewInfoSave, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError失败=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mTempList = Constants.getJsonArray(mTempMap.get("result").toString());
//                        mImvpView.onSuccess("list",mTempList);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

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


    private String getException(Exception e){
        if(e instanceof HttpException){
            //获取对应statusCode和Message
            HttpException exception = (HttpException)e;
            int code = exception.response().code();
            return code+"";
        }else if(e instanceof SSLHandshakeException){
            //接下来就是各种异常类型判断...
            return e+"";
        }
        return e+"";
    }
}
