package com.matthew.cn.newsreading.mvp.newschannels;

import com.afa.tourism.greendao.gen.NewsChannelTable;
import com.matthew.cn.newsreading.global.Constants;
import com.matthew.cn.newsreading.listener.RequestCallBack;
import com.matthew.cn.newsreading.mvp.BasePresenterImpl;

import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/27.
 */
public class ChannelsPresenterImpl extends BasePresenterImpl implements NewschannelsContract.ChannelsPresenter{

    private final ChannelsModelImpl channelsModel;
    private final NewschannelsContract.ChannelsView mView;

    public ChannelsPresenterImpl(NewschannelsContract.ChannelsView mView){
        this.mView = mView;
        channelsModel = new ChannelsModelImpl();
    }

    @Override
    public void lodeNewsChannels() {
        Subscription subscription = channelsModel.lodeNewsChannels(new RequestCallBack<Map<Integer, List<NewsChannelTable>>>() {
            @Override
            public void success(Map<Integer, List<NewsChannelTable>> data) {
                mView.setDatas(data.get(Constants.NEWS_CHANNEL_MINE), data.get(Constants.NEWS_CHANNEL_MORE));
            }

            @Override
            public void onError(String errorMsg) {
                mView.showmsg(errorMsg);
            }
        });

        addSubscription(subscription);
    }

    @Override
    public void onItemDrag(int fromPosition, int toPosition) {
        channelsModel.dragDb(fromPosition,toPosition);
    }

    @Override
    public void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine) {
        channelsModel.updateDb(newsChannel,isChannelMine);
    }
}
