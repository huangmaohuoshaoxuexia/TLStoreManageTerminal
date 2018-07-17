package com.whmaster.tl.whmaster.impl;

/**
 * Created by admin on 2017/11/24.
 */

public interface GoodsrikInterface {
    void goodsrikList(int page,String orderInCode,String orderInType);
    void getGoodsDetailList(int page,String orderId,String orderInCode,String orderInType,String orderInStatus);
    void updateStatus(String orderInCode,String orderInType,String orderInStatus);
}
