package com.matthew.cn.newsreading.api;

import com.matthew.cn.newsreading.entity.GirlData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface GankService {
    @GET("data/福利/{size}/{page}")
    Observable<GirlData> getPhotoList(
            @Path("size") int size,
            @Path("page") int page);
}
