package com.whmaster.tl.whmaster.impl;

/**
 * Created by admin on 2018/6/27.
 */

public interface InventoryInterface {
    void inventoryList(String stocktakingType,int page);
    void detailList(String stocktakingId);
    void update(int stocktakingNum,String memo,String stocktakingDetailId,int inventoryNum);
    void end(String stocktakingId,int inventoryNum);
}
