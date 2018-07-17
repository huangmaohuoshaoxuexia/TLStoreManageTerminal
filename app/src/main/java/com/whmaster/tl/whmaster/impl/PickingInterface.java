package com.whmaster.tl.whmaster.impl;

/**
 * Created by admin on 2017/11/8.
 */

public interface PickingInterface {
    public void pickingList(String stockOutCode,String type,int page);
    public void pickingGoodsList(String id);
    public void pickingGoodsCheck(String id,String type);
    void executeStockOutTask(String stockInId,String postId);//执行完毕
    void fhCheck(String stockOutId);//复核完毕

    void pickList(String pickCode,int pageNo);
    void pickingDetailList(String pickCode);
    void pickingDetail(String pickDetlId);
    void pickDetailupdateStatus(String pickCode,int pickStatus);
    void save(String pickDetlId,int actlPickInt,int actlPickRmnd,String memo,int pickDetlStatus);

    void reviewList(String outCode,int page);
    void reviewDetailList(String outCode,String outId);
    void outDetailupdateStatus(String outCode,int outStatus);
    void reviewDetail(String pickDetlId);
    void outSave(String outDetlId,int actlOutRmnd,String memo,int outDetlStatus);
}
