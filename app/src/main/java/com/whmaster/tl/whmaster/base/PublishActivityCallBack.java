package com.whmaster.tl.whmaster.base;

import android.os.Bundle;

/**
 * Created by admin on 2017/10/13.
 */

public interface PublishActivityCallBack {
    /**
     * 打开新界面
     * @param openClass
     * @param bundle
     */
    public void startActivity(Class<?> openClass, Bundle bundle);
    /**
     * 打开新界面，期待返回
     *
     * @param openClass 新界面
     * @param requestCode 请求码
     * @param bundle 参数
     */
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle);

    /**
     * 返回到上个页面
     *
     * @param bundle 参数
     */
    public void setResultOk(Bundle bundle);
}
