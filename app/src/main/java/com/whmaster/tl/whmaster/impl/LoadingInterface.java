package com.whmaster.tl.whmaster.impl;

/**
 * Created by admin on 2017/11/14.
 */

public interface LoadingInterface {
    void getLoadingList(String vehicleTripCode, int page);
    void getLoadingGoodsList(String deliveryId, int page);
    void getscanCompletedList(String deliveryId, int page);
    void loadCompleted(String deliveryId);
    void scanCompleted(String list);
}
