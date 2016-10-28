package com.matthew.cn.newsreading.mvp.newsdetail;

import com.matthew.cn.newsreading.entity.NewsDetail;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.mvp.BasePresenterImpl;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/28.
 */
public class NewsDetailPresenterImpl extends BasePresenterImpl implements NewsDetailContract.NewsDetailPresenter{

    private final NewsDetailModelImpl mNewsDetailModel;
    private final NewsDetailContract.NewsDetailView mView;

    public NewsDetailPresenterImpl(NewsDetailContract.NewsDetailView mView){
        this.mView = mView;
        mNewsDetailModel = new NewsDetailModelImpl();

    }

    @Override
    public void loadNewsDetail(String postId) {
        Subscription subscription = mNewsDetailModel.loadNewsDetail(new RequestCallBack<NewsDetail>() {
            @Override
            public void success(NewsDetail data) {
                mView.showDetail(data);
            }

            @Override
            public void onError(String errorMsg) {

            }
        }, postId);

        addSubscription(subscription);

    }
}
