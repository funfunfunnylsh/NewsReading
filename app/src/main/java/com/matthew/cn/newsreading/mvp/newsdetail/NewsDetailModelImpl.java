package com.matthew.cn.newsreading.mvp.newsdetail;

import com.matthew.cn.newsreading.entity.NewsDetail;
import com.matthew.cn.newsreading.global.HostType;
import com.matthew.cn.newsreading.global.RetrofitManager;
import com.matthew.cn.newsreading.global.RxSchedulers;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.util.MyUtil;

import java.util.List;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/10/28.
 */
public class NewsDetailModelImpl implements NewsDetailContract.NewsDetailModel{


    @Override
    public Subscription loadNewsDetail(final RequestCallBack<NewsDetail> callBack,final  String postId) {
        return RetrofitManager.getInstance(HostType.NETEASE_NEWS_VIDEO).getNewsDetailObservable(postId)
                .map(new Func1<Map<String, NewsDetail>, NewsDetail>() {
                    @Override
                    public NewsDetail call(Map<String, NewsDetail> map) {

                        NewsDetail newsDetail = map.get(postId);
                        changeNewsDetail(newsDetail);
                        return newsDetail;
                    }
                })
                .compose(RxSchedulers.<NewsDetail>defaultSchedulers())
                .subscribe(new Observer<NewsDetail>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(MyUtil.analyzeNetworkError(e));
                    }

                    @Override
                    public void onNext(NewsDetail newsDetail) {
                        callBack.success(newsDetail);
                    }
                });
    }

    private void changeNewsDetail(NewsDetail newsDetail) {
        List<NewsDetail.ImgBean> imgSrcs = newsDetail.getImg();
        if (isChange(imgSrcs)) {
            String newsBody = newsDetail.getBody();
            newsBody = changeNewsBody(imgSrcs, newsBody);
            newsDetail.setBody(newsBody);
        }
    }

    private boolean isChange(List<NewsDetail.ImgBean> imgSrcs) {
        return imgSrcs != null && imgSrcs.size() >= 2 ;
    }

    private String changeNewsBody(List<NewsDetail.ImgBean> imgSrcs, String newsBody) {
        for (int i = 0; i < imgSrcs.size(); i++) {
            String oldChars = "<!--IMG#" + i + "-->";
            String newChars;
            if (i == 0) {
                newChars = "";
            } else {
                newChars = "<img src=\"" + imgSrcs.get(i).getSrc() + "\" />";
            }
            newsBody = newsBody.replace(oldChars, newChars);

        }
        return newsBody;
    }
}
