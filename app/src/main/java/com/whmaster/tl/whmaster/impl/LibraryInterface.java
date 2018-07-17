package com.whmaster.tl.whmaster.impl;

import android.support.v4.util.ArrayMap;

import java.util.ArrayList;

/**
 * Created by admin on 2017/11/16.
 */

public interface LibraryInterface {
    void getMovePosition(String positionCode,String positionPointCode);
    void getProductByPosition(String positionCode);
    void save(ArrayMap<String,Object> movePosition, ArrayList<ArrayMap<String,Object>> mList);

}
