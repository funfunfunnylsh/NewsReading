package com.matthew.cn.newsreading.mvp.news;

import com.matthew.cn.newsreading.entity.NewsSummary;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.mvp.BasePresenterImpl;

import java.util.List;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/26.
 */
public class NewsPresentreImpl extends BasePresenterImpl implements NewsContract.NewsPresentre{

    private final NewsModel mNewsModel;
    private final NewsContract.NewsView mNewsView;
    private boolean isFirst = true;

    public NewsPresentreImpl(NewsContract.NewsView newsView){
        this.mNewsView = newsView;
        this.mNewsModel = new NewsModel();
    }

    @Override
    public void loadNews(String type, String id, int startPage, final boolean isloadmore) {
        if(!isFirst){
            mNewsView.showProgress();
            isFirst= false;
        }
        Subscription subscription = mNewsModel.loadNews(new RequestCallBack<List<NewsSummary>>() {
            @Override
            public void success(List<NewsSummary> data) {
                if(isloadmore){
                    mNewsView.addList(data);
                }else{
                    mNewsView.setNewsList(data);
                }
                mNewsView.hideProgress();
            }

            @Override
            public void onError(String errorMsg) {
                mNewsView.showMsg(errorMsg);
            }
        }, type, id, startPage);


        addSubscription(subscription);
    }
}
