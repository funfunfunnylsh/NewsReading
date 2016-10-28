package com.matthew.cn.newsreading.mvp.gank;

import com.matthew.cn.newsreading.NewsApplication;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.GirlData;
import com.matthew.cn.newsreading.entity.PhotoGirl;
import com.matthew.cn.newsreading.global.HostType;
import com.matthew.cn.newsreading.global.RetrofitManager;
import com.matthew.cn.newsreading.global.RxSchedulers;
import com.matthew.cn.newsreading.listener.RequestCallBack;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by Administrator on 2016/10/27.
 */
public class GnakModelImpl implements GankContract.GankModel{

    @Override
    public Subscription loadGanks(final RequestCallBack<List<PhotoGirl>> listener, int size, int page) {
        return RetrofitManager.getInstance(HostType.GANK_GIRL_PHOTO)
                .getPhotoListObservable(size, page)
                .map(new Func1<GirlData, List<PhotoGirl>>() {
                    @Override
                    public List<PhotoGirl> call(GirlData girlData) {
                        return girlData.getResults();
                    }
                })
                .compose(RxSchedulers.<List<PhotoGirl>>defaultSchedulers())
                .subscribe(new Subscriber<List<PhotoGirl>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(NewsApplication.getAppContext().getString(R.string.load_error));
                    }

                    @Override
                    public void onNext(List<PhotoGirl> photoGirls) {
                        listener.success(photoGirls);
                    }
                })
                ;



    }
}
