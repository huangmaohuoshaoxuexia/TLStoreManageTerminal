package com.whmaster.tl.whmaster.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.whmaster.tl.whmaster.common.Constants;
import com.whmaster.tl.whmaster.http.RetrofitHttp;
import com.whmaster.tl.whmaster.impl.StorageInterface;
import com.whmaster.tl.whmaster.view.IMvpView;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

/**
 * Created by admin on 2017/11/7.
 */

public class StoragePresenter extends BasePresenter implements StorageInterface{
    public StoragePresenter(Context context, IMvpView mvpView){
        this.mImvpView = mvpView;
        this.mContext = context;
    }
    //入库单列表
    @Override
    public void getStorageList(String code, int page, int pageSize) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pageNo",page+"");
        map.put("pageSize",pageSize+"");
        map.put("stockInCode",code);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getStorageList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaste+r.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mTempList = Constants.getJsonArray(mTempMap.get("records").toString());
//                            mImvpView.onSuccess("list",mTempList);
//
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
            }
        });
    }
    //入库货品列表
    @Override
    public void getStorageProductList(String orderid) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("stockInId",orderid);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getStorageProductList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
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
    public void getStorageCount() {
        Map map = new HashMap();
        RetrofitHttp.getInstance(mContext).post(Constants.getStorageCount, map, new Subscriber<String>() {
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
                        mImvpView.onSuccess("getStorageCount",mTempMap);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取入库单数",s+"========");
            }
        });
    }

    @Override
    public void getTaskCount() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        RetrofitHttp.getInstance(mContext).postJson(Constants.pickCount, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
               if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                   mTempMap = Constants.getJsonObject(s);
                   mImvpView.onSuccess("getTaskCount",mTempMap);
               }else{
                   mImvpView.onFail(mDataMap.get("resultMessage")+"");
               }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取拣货单数",s+"========");
            }
        });
    }

    @Override
    public void queryStorePositionDetail(String detailId, final String businessType) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("detailId",detailId);
        map.put("businessType",businessType);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryStorePositionDetail, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("detail",Constants.getJsonObject(mTempMap.get("result").toString()));
//
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
                    mImvpView.onSuccess("detail",mTempMap2);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
            }
        });
    }

    @Override
    public void pickAdd(String stockInDetailId, String list) {
        Map map = new HashMap();
        map.put("stockInDetailId",stockInDetailId);
        map.put("list", list);
        mImvpView.showLoading();
        Log.i("com.whmaster.tl.whmaster>>",map+"======获取请求数据====JSONObject.toJSON(goodsShelves).toString()==");
        RetrofitHttp.getInstance(mContext).post(Constants.shelfProductStockInTask, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
//                mImvpView.hideLoading();
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mImvpView.onSuccess("up",mTempMap);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>数量上架",s+"====onNext====");
            }
        });
    }

    @Override
    public void shelfProductStockInTask(HashMap map) {
//        Map map = new HashMap();
//        map.put("stockInDetailId",stockInDetailId);
//        map.put("actNum",actNum);
//        map.put("storePositionId",storePositionId);
//        map.put("list", list);
        map.put("token", Constants.token);
        mImvpView.showLoading();
        Log.i("com.whmaster.tl.whmaster>>",map+"======获取请求数据====JSONObject.toJSON(goodsShelves).toString()==");
        RetrofitHttp.getInstance(mContext).postJson(Constants.shelfProductStockInTask, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("up",mTempMap);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("up",mDataMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>数量上架",s+"====onNext====");
            }
        });
    }

    @Override
    public void executeStockInTask(String stockInId, String orderid) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("stockInId",stockInId);
        map.put("orderInId", orderid);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.executeStockInTask, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.onSuccess("execute",null);
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("execute",mTempMap);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("execute",mDataMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>执行完毕",s+"====onNext====");
            }
        });
    }

    @Override
    public void shelfProductStockOutTask(String stockInDetailId, String actPackageNum, String actNum, String list) {
        Map map = new HashMap();
        map.put("stockOutDetailId",stockInDetailId);
        map.put("actPackageNum", actPackageNum);
        map.put("actNum", actNum);
        map.put("list", list);
        mImvpView.showLoading();
        Log.i("com.whmaster.tl.whmaster>>",list+"======获取请求数据===="+stockInDetailId);
        RetrofitHttp.getInstance(mContext).post(Constants.executeStockOutTask, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
//                mImvpView.hideLoading();
                mImvpView.onSuccess("up",null);
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mTempMap = Constants.getJsonObject(s);
                if(mTempMap!=null){
                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
                        mImvpView.onSuccess("up",mTempMap);
                    }else{
                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
                    }
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>拣出上架",s+"====onNext====");
            }
        });
    }

    @Override
    public void rkdscList(String orderInStatus,String stockInCode, int page) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("pageNo",page+"");
        map.put("pageSize","10");
        map.put("orderInStatus",orderInStatus);
        map.put("orderInCode",stockInCode);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.stockList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>",s+"====onNext====");
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mTempList = Constants.getJsonArray(mTempMap.get("records").toString());
//                        mImvpView.onSuccess("rklist",mTempList);
//
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(Constants.getJsonObject(mTempMap.get("value").toString()).get("list").toString());
                    mImvpView.onSuccess("rklist",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>",mTempList+"====onNext====");
            }
        });
    }

    @Override
    public void getStockConut() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.stockTotal, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>获取上架单数量",s+"====onNext====");
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("getStorageCount",mTempMap);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("getStorageCount",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void getStorageDetailList(String orderId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("orderInId",orderId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.getOrderDetailList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
//                mHashMap = Constants.getJsonObject2(s);
//                if(mHashMap!=null){
//                    if(mHashMap.get("resultCode")!=null && mHashMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("map",Constants.getJsonObject2(mHashMap.get("result").toString()));
//                    }else{
//                        mImvpView.onFail(mHashMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempMap2 = Constants.getJsonObject(mTempMap.get("value").toString());
                    mImvpView.onSuccess("map",mTempMap2);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                Log.i("com.whmaster.tl.whmaster>>",mTempMap2+"====onNext====");
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void getWharehouse(String wharehouseId) {
        Log.i("com.whmaster.tl.whmaster>>","======选择仓库ID======"+wharehouseId);
        Map map = new HashMap();
        map.put("token",Constants.token);
//        map.put("orgId",wharehouseId);
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryWarehouse, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>获取仓库",s+"====onNext====");
//                mHashMap = Constants.getJsonObject2(s);
//                if(mHashMap!=null){
//                    if(mHashMap.get("resultCode")!=null && mHashMap.get("resultCode").toString().equals("0")){
//                        mHashList = Constants.getJsonArray2(mHashMap.get("result").toString());
//                        mImvpView.onSuccess("wharehouse",mHashList);
//                    }else{
//                        mImvpView.onFail(mHashMap.get("resultMsg")+"");
//                    }
//                }
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(mTempMap.get("value").toString());
                    mImvpView.onSuccess("wharehouse",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void getRegion(String wharehouseId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("warehouseId",wharehouseId);
//        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryRegion, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>获取库区",s+"====onNext====");
//                mHashMap = Constants.getJsonObject2(s);
//                if(mHashMap!=null){
//                    if(mHashMap.get("resultCode")!=null && mHashMap.get("resultCode").toString().equals("0")){
//                        mHashList = Constants.getJsonArray2(mHashMap.get("result").toString());
//                        mImvpView.onSuccess("getRegion",mHashList);
//                    }else{
//                        mImvpView.onFail(mHashMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(mTempMap.get("value").toString());
                    mImvpView.onSuccess("getRegion",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void getPosition(String regionId) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("regionId",regionId);
        mImvpView.showLoading();
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryPosition, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>获取库位",s+"====onNext====");
//                mHashMap = Constants.getJsonObject2(s);
//                if(mHashMap!=null){
//                    if(mHashMap.get("resultCode")!=null && mHashMap.get("resultCode").toString().equals("0")){
//                        mHashList = Constants.getJsonArray2(mHashMap.get("result").toString());
//                        mImvpView.onSuccess("getPosition",mHashList);
//                    }else{
//                        mImvpView.onFail(mHashMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mTempList = Constants.getJsonArray(mTempMap.get("value").toString());
                    mImvpView.onSuccess("getPosition",mTempList);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void addGenerateList(String buyerId,String orderInId, String wharehouseId, String wharehouseName,String detail) {
        Map map = new HashMap();
        map.put("token",Constants.token);
        map.put("buyerId",buyerId);
        map.put("orderInId",orderInId);
        map.put("wharehouseId",wharehouseId);
        map.put("wharehouseName",wharehouseName);
        map.put("detail",detail);
        mImvpView.showLoading();
        Log.i("com.whmaster.tl.whmaster>>生成上架单",map.toString());
        RetrofitHttp.getInstance(mContext).postJson(Constants.addGenerateList, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>生成上架单",s+"====onNext====");
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("addGenerateListSuccess",mTempList);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("addGenerateListSuccess",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void queryUnfinishedMaterialCount() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        RetrofitHttp.getInstance(mContext).postJson(Constants.queryUnfinishedMaterialCount, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>",e+"======onError======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                Log.i("com.whmaster.tl.whmaster>>获取实物收货",s+"====onNext====");
//                mTempMap = Constants.getJsonObject(s);
//                if(mTempMap!=null){
//                    if(mTempMap.get("resultCode")!=null && mTempMap.get("resultCode").toString().equals("0")){
//                        mImvpView.onSuccess("queryUnfinishedMaterialCount",mTempMap);
//                    }else{
//                        mImvpView.onFail(mTempMap.get("resultMsg")+"");
//                    }
//                }

                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("queryUnfinishedMaterialCount",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
            }
        });
    }

    @Override
    public void getReviewCount() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        RetrofitHttp.getInstance(mContext).postJson(Constants.verifyCount, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("getFhCount",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取复核单数",s+"========");
            }
        });
    }

    @Override
    public void getInventoryCount() {
        Map map = new HashMap();
        map.put("token",Constants.token);
        RetrofitHttp.getInstance(mContext).postJson(Constants.getInventoryCount, map, new Subscriber<String>() {
            @Override
            public void onCompleted() {
                mImvpView.hideLoading();
            }
            @Override
            public void onError(Throwable e) {
                Log.i("com.whmaster.tl.whmaster>>返回数据",e+"=====onError=======");
                mImvpView.hideLoading();
            }
            @Override
            public void onNext(String s) {
                mDataMap = Constants.getJsonObjectByData(s);
                if(mDataMap!=null && mDataMap.get("resultCode").equals("0000")){
                    mTempMap = Constants.getJsonObject(s);
                    mImvpView.onSuccess("getInventoryCount",mTempMap);
                }else{
                    mImvpView.onFail(mDataMap.get("resultMessage")+"");
                }
                mImvpView.hideLoading();
                Log.i("com.whmaster.tl.whmaster>>获取盘点数量",s+"========");
            }
        });
    }


}
