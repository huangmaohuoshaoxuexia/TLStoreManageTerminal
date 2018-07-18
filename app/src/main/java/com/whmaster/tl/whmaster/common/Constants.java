package com.whmaster.tl.whmaster.common;

import com.whmaster.tl.whmaster.BuildConfig;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by Administrator on 2016/9/7.
 */
public class Constants {
    public static final String apiHead = BuildConfig.API_HOST;//开发环境
    //public static final String apiHead = "http://api.dev.tianlu56.com.cn/";//开发环境
   // public static final String apiHead = "http://api.fat.tianlu56.com.cn/";//测试环境
   // public static final String apiHead = "http://api.local.tianlu56.com.cn/";//测试环境
    //  public static final String apiHead = "https://wmsapi.tianlu56.com.cn/";//生产环境
    public static String token = "", mUsername = "", mPwd = "";
    //登陆
//    public static final String login = apiHead + "sys/sys/loginApp";
    public static final String login = apiHead + "usc/user/login";
    //是否登录
    public static final String islogin = apiHead + "sys/isLogin";
    //注销
//    public static final String loginout = apiHead + "sys/logout";
    public static final String loginout = apiHead + "usc/user/removeToken";
    //获取权限
    public static final String perms = apiHead + "sys/menu/perms";
    //入库单列表 page limit stockInCode
    public static final String getStorageList = apiHead + "order/stockIn/queryStockInTaskListByShelfUserId";
    //入库单货品列表
    public static final String getStorageProductList = apiHead + "order/stockIn/queryStockInProductListById";
    //未完成入库任务数获取接口
    public static final String getStorageCount = apiHead + "wh/stockIn/queryUnfinishedStockInCount";
    //拣货出库任务数获取接口
    public static final String getTaskCount = apiHead + "wh/stockOut/task/count";
    //货品数量上架or拣货详情获取接口
    public static final String queryStorePositionDetail = apiHead + "order/stockIn/queryStorePositionDetail";
    //拣货单列表
    public static final String pickingList = apiHead + "wh/stockOut/task/list";
    //拣出货品列表
    public static final String pickingDetailList = apiHead + "wh/stockOut/detail/list";
    //数量上架
    public static final String shelfProductStockInTask = apiHead + "order/stockIn/shelfProductStockInTask";
    //入库执行完毕
    public static final String executeStockInTask = apiHead + "order/stockIn/executeStockInTask";
    //拣出货品
    public static final String executeStockOutTask = apiHead + "wh/stockOut/pick/num";
    //拣货执行完毕
    public static final String executePick = apiHead + "wh/stockOut/execute/pick";
    //复核完毕
    public static final String fhcheck = apiHead + "wh/stockOut/execute/check";
    //装车确认列表
    public static final String toConfirmLoad = apiHead + "delivery/delivery/toConfirmLoad";
    //装车货品列表
    public static final String getDetailListOnCar = apiHead + "delivery/delivery/getDetailListOnCar";
    //扫码确认列表
    public static final String scanCompleted = apiHead + "delivery/delivery/scanCompleted";
    //是否有权限
    public static final String isPermission = apiHead + "sys/position/checkUserHasPermissionByCode";
    //库位库存
//    public static final String queryWhStockInfoByCode = apiHead + "wh/stockLedger/queryWhStockInfoByCode";
    public static final String queryWhStockInfoByCode = apiHead + "whsc/app/inventory/list";
    //选择货品
//    public static final String getListByPositionCode = apiHead + "wh/goods/inventory/getListByPositionCode";
    public static final String getListByPositionCode = apiHead + "whsc/app/inventory/product/list";
    //扫描完毕
    public static final String updateDeliveryLoadStatus = apiHead + "delivery/delivery/updateDeliveryLoadStatus";
    //装车完毕
    public static final String loadCompleted = apiHead + "delivery/delivery/loadCompleted";
    //取消条码
    public static final String updateBatchByCaseCode = apiHead + "wh/caseCode/updateBatchByCaseCode";
    //是否是同一库位
    public static final String checkIsSameWharehouseByCode = apiHead + "sys/position/checkIsSameWharehouseByCode";
    //移库完毕
    public static final String transferFinished = apiHead + "wh/transfer/transferFinished";
    //实物收货列表
    public static final String goodsReceiptList = apiHead + "order/orderIn/app/material/list";
    //实物收货详情列表
    public static final String goodsgetDetailList = apiHead + "order/orderIn/app/material/getDetailList";
    //实物收货确认收货
    public static final String updateStatus = apiHead + "order/orderIn/updateMateralStatus";
    //上架单查看列表
    public static final String stockList = apiHead + "order/orderIn/app/stock/list";
    //上架单数量获取
    public static final String stockTotal = apiHead + "order/orderIn/app/stock/total";
    //上架单货品列表
    public static final String getOrderDetailList = apiHead + "order/orderIn/app/getOrderDetail";
    //获取仓库信息
    public static final String getWharehouse = apiHead + "order/orderIn/app/list/wharehouse";
    //生成上架单
    public static final String addGenerateList = apiHead + "order/stockIn/app/add";
    //实物收货数量获取
    public static final String queryUnfinishedMaterialCount = apiHead + "order/orderIn/app/queryUnfinishedMaterialCount";
    //获取仓库
//    public static final String queryWarehouse = apiHead + "order/orderIn/queryWarehouseByorgId";
    public static final String queryWarehouse = apiHead + "whsc/app/warehouse/list/orgid";
    //获取库区
//    public static final String queryRegion = apiHead + "order/orderIn/queryRegionListByWharehouseId";
    public static final String queryRegion = apiHead + "whsc/app/region/list/warehouseid";
    //获取Position库位
//    public static final String queryPosition = apiHead + "order/orderIn/queryPositoinByRegionId";
    public static final String queryPosition = apiHead + "whsc/app/position/list/regionid";

    //拣货出库数量
    public static final String pickCount = apiHead + "order/pick/app/pickCount";
    //复核数量
    public static final String verifyCount = apiHead + "order/orderOut/app/verifyCount";

    //拣货单列表
    public static final String pickingListOrder = apiHead + "order/pick/app/listfmob";
    //拣货单详情列表
    public static final String pickingDetailListOrder = apiHead + "order/pick/app/pickDetlsfmob";
    //拣货单货品详情
    public static final String pickingDetail = apiHead + "order/pickDetl/app/info";
    //拣货单详情任务完成
    public static final String pickDetailupdateStatus = apiHead + "order/pick/app/updateStatus";
    //确认拣货
    public static final String pickingSave = apiHead + "order/pickDetl/app/save";
    //拣货复核列表
    public static final String reviewList = apiHead + "order/orderOut/app/verifyOrders";
    //拣货复核详情列表
    public static final String reviewDetailList = apiHead + "order/orderOut/app/orderDetls";
    //复核货品详情
    public static final String reviewInfo = apiHead + "order/orderOut/app/prod/info";
    //确认复核货品详情
    public static final String reviewInfoSave = apiHead + "order/orderOut/app/prod/save";
    //完成复核详情任务
    public static final String outUpdateStatus = apiHead + "order/orderOut/app/updateStatus";
    //库位选择操作
    public static final String getMovePosition = apiHead + "whsc/transferWarehouse/getMovePosition";
    //根据选择的库位编码查询货品信息
    public static final String getProductByPosition = apiHead + "whsc/transferWarehouse/getProductByPosition";
    //移入库位
    public static final String save = apiHead + "whsc/transferWarehouse/save";
    //查询盘点单列表
    public static final String inventoryList = apiHead + "whsc/stocktaking/app/list";
    //查询盘点单详情列表
    public static final String detailList = apiHead + "whsc/stocktaking/app/detail";
    //盘点明细确定操作
    public static final String update = apiHead + "whsc/stocktaking/app/update";
    //完成盘点
    public static final String end = apiHead + "whsc/stocktaking/app/end";
    //完成盘点
    public static final String getInventoryCount = apiHead + "whsc/stocktaking/app/queryStocktakingNum";

    public static Object getGsonObject(String json, Object o) {
        Gson g = new Gson();
        o = g.fromJson(json, o.getClass());
        return o;
    }

    public static ArrayMap<String, Object> getJson2Object(String json)
            throws JSONException {
        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
        JSONObject jsonObject = new JSONObject(json);
        Iterator<?> it = jsonObject.keys();
        String a;
        while (it.hasNext()) {

            a = it.next().toString();
            map.put(a, jsonObject.get(a).toString());
        }
        return map;
    }

    public static ArrayMap<String, Object> getJsonObject(String json) {
        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static ArrayMap<String, Object> getJsonObjectByData(String json) {
        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
        ArrayMap<String, Object> map2 = new ArrayMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
            map2 = getJsonObject(map.get("resultStatus").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map2;
    }

    public static ArrayList<ArrayMap<String, Object>> getJsonArrayByValue(String json) {
        ArrayMap<String, Object> map = new ArrayMap<String, Object>();
        ArrayList<ArrayMap<String, Object>> list = new ArrayList<ArrayMap<String, Object>>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
            list = getJsonArray(map.get("list").toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 解析json数组数据
    public static ArrayList<ArrayMap<String, Object>> getJsonArray(
            String jsonString) {
        ArrayList<ArrayMap<String, Object>> list = new ArrayList<ArrayMap<String, Object>>();
        try {
            list = JSON.parseObject(jsonString,
                    new TypeReference<ArrayList<ArrayMap<String, Object>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static HashMap<String, Object> getJsonObject2(String json) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            Iterator<?> it = jsonObject.keys();
            String a;
            while (it.hasNext()) {
                a = it.next().toString();
                map.put(a, jsonObject.get(a).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // 解析json数组数据
    public static ArrayList<HashMap<String, Object>> getJsonArray2(
            String jsonString) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        try {
            list = JSON.parseObject(jsonString,
                    new TypeReference<ArrayList<HashMap<String, Object>>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result.toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String format2(String value) {

        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(Double.parseDouble(value));
    }

    public static String format4(String value) {

        DecimalFormat df = new DecimalFormat("0.0000");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(Double.parseDouble(value));
    }
}
