package com.matthew.cn.newsreading.ui.adapter;

import com.afa.tourism.greendao.gen.NewsChannelTable;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matthew.cn.newsreading.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */
public class NewsChannelAdapter extends BaseItemDraggableAdapter<NewsChannelTable> {


    public NewsChannelAdapter(List<NewsChannelTable> data) {
        super(R.layout.item_news_channel, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsChannelTable newsChannelTable) {
        baseViewHolder.setText(R.id.news_channel_tv,newsChannelTable.getNewsChannelName());
    }
}
