package com.matthew.cn.newsreading.mvp.newschannels;

import com.afa.tourism.greendao.gen.NewsChannelTable;
import com.matthew.cn.newsreading.listener.RequestCallBack;

import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * Created by Administrator on 2016/10/27.
 */
public class NewschannelsContract {

    public interface ChannelsView {
        void setDatas(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore);

        void showmsg(String msg);
    }

    interface ChannelsPresenter{
        void lodeNewsChannels();

        void onItemDrag(int fromPosition, int toPosition);

        void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine);
    }

    interface ChannelsModel{
        Subscription lodeNewsChannels(RequestCallBack<Map<Integer, List<NewsChannelTable>>> callback);

        void dragDb(int fromPosition,int toPosition);

        void updateDb(NewsChannelTable newsChannel, boolean isChannelMine);

    };
}
