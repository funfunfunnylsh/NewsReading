package com.matthew.cn.newsreading.mvp.newsdetail;

import com.matthew.cn.newsreading.entity.NewsDetail;
import com.matthew.cn.newsreading.listener.RequestCallBack;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/28.
 */
public interface NewsDetailContract {

    interface NewsDetailView{
        void showDetail(NewsDetail data);
    }

    interface NewsDetailPresenter{

        void loadNewsDetail(String postId);
    }

    interface NewsDetailModel{
        Subscription loadNewsDetail(RequestCallBack<NewsDetail> callBack, String postId);
    }
}
