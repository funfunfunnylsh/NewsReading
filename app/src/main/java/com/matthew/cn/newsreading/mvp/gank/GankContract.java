package com.matthew.cn.newsreading.mvp.gank;

import com.matthew.cn.newsreading.entity.PhotoGirl;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.mvp.BasePresenter;
import com.matthew.cn.newsreading.mvp.BaseView;

import java.util.List;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/27.
 */
public class GankContract {
    interface GankModel{
        Subscription loadGanks(RequestCallBack<List<PhotoGirl>> listener,int size, int page);
    }

    public interface GankView extends BaseView {

        void setGankList(List<PhotoGirl> photoGirls);

        void addList(List<PhotoGirl> photoGirls);
    }

    interface GankPresentre extends BasePresenter {
        void loadGanks(int size, int page,boolean isloadmore);

    }

}
