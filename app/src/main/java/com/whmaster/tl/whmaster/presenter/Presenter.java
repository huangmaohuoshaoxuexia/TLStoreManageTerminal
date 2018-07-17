package com.whmaster.tl.whmaster.presenter;

/**
 * Created by admin on 2017/10/13.
 */

public interface Presenter<V> {
    void attachView(V view);
    void detachView(V view);
}
