package com.matthew.cn.newsreading.mvp.news;

import com.matthew.cn.newsreading.entity.NewsSummary;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.mvp.BasePresenter;
import com.matthew.cn.newsreading.mvp.BaseView;

import java.util.List;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/26.
 */
public interface NewsContract {

    interface NewsModel{
        Subscription loadNews(RequestCallBack<List<NewsSummary>> listener, String type, String id, int startPage);
    }

    interface NewsView extends BaseView{

        void setNewsList(List<NewsSummary> newsSummary);

        void addList(List<NewsSummary> newsSummary);
    }

    interface NewsPresentre extends BasePresenter{
        void loadNews(String type, String id, int startPage,boolean isloadmore);

    }

}
