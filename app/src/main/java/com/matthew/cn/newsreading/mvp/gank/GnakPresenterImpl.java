package com.matthew.cn.newsreading.mvp.gank;

import com.matthew.cn.newsreading.entity.PhotoGirl;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.mvp.BasePresenterImpl;

import java.util.List;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/27.
 */
public class GnakPresenterImpl extends BasePresenterImpl implements GankContract.GankPresentre{


    private final GankContract.GankView mGankView;
    private final GnakModelImpl mGnakModel;

    public GnakPresenterImpl(GankContract.GankView mGankView){
        this.mGankView = mGankView;

        mGnakModel = new GnakModelImpl();

    }

    @Override
    public void loadGanks(int size, int page, final boolean isloadmore) {
        mGankView.showProgress();
        Subscription subscription = mGnakModel.loadGanks(new RequestCallBack<List<PhotoGirl>>() {
            @Override
            public void success(List<PhotoGirl> data) {
                if(!isloadmore){
                    mGankView.setGankList(data);
                }else{
                    mGankView.addList(data);
                }
                mGankView.hideProgress();
            }

            @Override
            public void onError(String errorMsg) {
                mGankView.showMsg(errorMsg);
            }
        }, size, page);


        addSubscription(subscription);

    }
}
