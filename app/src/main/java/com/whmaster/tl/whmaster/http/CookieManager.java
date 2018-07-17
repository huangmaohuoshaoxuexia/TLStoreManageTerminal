package com.whmaster.tl.whmaster.http;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Administrator on 2017/5/15.
 */
public class CookieManager implements CookieJar{
    private static Context mContext;
//org.apache.http.cookie.Cookie;
    private static PersistentOkHttpCookieStore cookieStore;
    public CookieManager(Context context) {
        mContext = context;
        if (cookieStore == null ) {
            cookieStore = new PersistentOkHttpCookieStore(mContext);
        }
    }
    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        List<Cookie> cookies =cookieStore.get(url);
        return cookies;
    }
}
