package com.whmaster.tl.whmaster.http;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/11.
 */
public interface ApiService {
    //获取banner
//    @GET("Ad/GetAdsByCode?")
//    Call<bestore.com.model.Banner> getBanner(@Query("positionCode") String positionCode, @Query("top") String top);

//    @GET()
//    Call<Result<BannerEntity>> get(@Url String url, @QueryMap Map<String, String> maps);
    @GET()
    Observable<String> get(@Url String url, @QueryMap Map<String, String> maps);

    @POST()
    @FormUrlEncoded
    Observable<String> post(@Url String url, @FieldMap Map<String, String> maps, @Header("partner") String partner, @Header("sign") String sign);

    @POST()
    @FormUrlEncoded
    Observable<String> post(@Url String url, @FieldMap Map<String, String> maps);

    @POST()
    Observable<String> postJson(@Url String url, @Body RequestBody route);

    @HTTP(method = "DELETE",  hasBody = true)
    Observable<String> delete(@Url String url, @QueryMap Map<String, String> maps, @Header("partner") String partner, @Header("sign") String sign);
}
