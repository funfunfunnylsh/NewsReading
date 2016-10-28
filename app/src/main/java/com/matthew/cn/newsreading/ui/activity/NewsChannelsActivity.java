package com.matthew.cn.newsreading.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.afa.tourism.greendao.gen.NewsChannelTable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.event.ChannelChangeEvent;
import com.matthew.cn.newsreading.event.ChannelItemMoveEvent;
import com.matthew.cn.newsreading.mvp.newschannels.ChannelsPresenterImpl;
import com.matthew.cn.newsreading.mvp.newschannels.NewschannelsContract;
import com.matthew.cn.newsreading.ui.adapter.NewsChannelAdapter;
import com.matthew.cn.newsreading.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by Administrator on 2016/10/27.
 */
public class NewsChannelsActivity extends BaseActivity implements NewschannelsContract.ChannelsView{


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news_channel_mine_rv)
    RecyclerView newsChannelMineRv;
    @BindView(R.id.news_channel_more_rv)
    RecyclerView newsChannelMoreRv;


    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallbackMine;
    private ItemTouchHelper mItemTouchHelperMine;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallbackMore;
    private ItemTouchHelper mItemTouchHelperMore;


    private ChannelsPresenterImpl mChannelsPresenterImpl;

    private List<NewsChannelTable> newsChannelTablesMine = new ArrayList<>();
    private List<NewsChannelTable> newsChannelTablesMore = new ArrayList<>();
    private int fromPosition = -1;
    private boolean mIsChannelChanged = false;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_news_channel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().toObservable(ChannelItemMoveEvent.class)
                .subscribe(new Action1<ChannelItemMoveEvent>() {
                    @Override
                    public void call(ChannelItemMoveEvent channelItemMoveEvent) {
                        int fromPosition = channelItemMoveEvent.getFromPosition();
                        int toPosition = channelItemMoveEvent.getToPosition();
                        mChannelsPresenterImpl.onItemDrag(fromPosition, toPosition);
                    }
                });

    }

    @Override
    protected void initView() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        initRecyclerview();


    }

    private void initRecyclerview() {
        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder)viewHolder);
                TextView news_channel_tv = ((BaseViewHolder) viewHolder).getView(R.id.news_channel_tv);
                news_channel_tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                fromPosition = pos;
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                BaseViewHolder holder = ((BaseViewHolder)viewHolder);
                TextView news_channel_tv = ((BaseViewHolder) viewHolder).getView(R.id.news_channel_tv);
                news_channel_tv.setTextColor(getResources().getColor(R.color.primary_text));

                mChannelsPresenterImpl.onItemDrag(fromPosition, pos);
                mIsChannelChanged = true;
                fromPosition = -1;
            }
        };


        newsChannelMineRv.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        newsChannelMineRv.setItemAnimator(new DefaultItemAnimator());
        mNewsChannelAdapterMine = new NewsChannelAdapter(newsChannelTablesMine);
        mItemDragAndSwipeCallbackMine = new ItemDragAndSwipeCallback(mNewsChannelAdapterMine);
        mItemTouchHelperMine = new ItemTouchHelper(mItemDragAndSwipeCallbackMine);
        mItemTouchHelperMine.attachToRecyclerView(newsChannelMineRv);
        mNewsChannelAdapterMine.enableDragItem(mItemTouchHelperMine);
        mNewsChannelAdapterMine.setOnItemDragListener(listener);
        newsChannelMineRv.setAdapter(mNewsChannelAdapterMine);


        newsChannelMoreRv.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        newsChannelMoreRv.setItemAnimator(new DefaultItemAnimator());
        mNewsChannelAdapterMore = new NewsChannelAdapter(newsChannelTablesMore);
        mItemDragAndSwipeCallbackMore = new ItemDragAndSwipeCallback(mNewsChannelAdapterMore);
        mItemTouchHelperMore = new ItemTouchHelper(mItemDragAndSwipeCallbackMore);
        mItemTouchHelperMore.attachToRecyclerView(newsChannelMoreRv);
        newsChannelMoreRv.setAdapter(mNewsChannelAdapterMore);


        //cared
        newsChannelMineRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMine.getData().get(i);
                boolean isNewsChannelFixed = newsChannel.getNewsChannelFixed();
                if (!isNewsChannelFixed) {
                    mNewsChannelAdapterMore.add(mNewsChannelAdapterMore.getItemCount(), newsChannel);
                    mNewsChannelAdapterMine.remove(i);

                    mChannelsPresenterImpl.onItemAddOrRemove(newsChannel, true);
                    mIsChannelChanged = true;
                }
            }
        });

        //not cared
        newsChannelMoreRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                NewsChannelTable newsChannel = mNewsChannelAdapterMore.getData().get(i);
                boolean isNewsChannelFixed = newsChannel.getNewsChannelFixed();
                if (!isNewsChannelFixed) {
                    mNewsChannelAdapterMine.add(mNewsChannelAdapterMine.getItemCount(), newsChannel);
                    mNewsChannelAdapterMore.remove(i);

                    mChannelsPresenterImpl.onItemAddOrRemove(newsChannel, false);
                    mIsChannelChanged = true;
                }
            }
        });
    }

    @Override
    protected void initData() {
        mChannelsPresenterImpl = new ChannelsPresenterImpl(this);
        mChannelsPresenterImpl.lodeNewsChannels();

    }

    @Override
    public void setDatas(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore) {
        newsChannelTablesMine.addAll(newsChannelsMine);
        mNewsChannelAdapterMine.notifyDataSetChanged();

        newsChannelTablesMore.addAll(newsChannelsMore);
        mNewsChannelAdapterMore.notifyDataSetChanged();
    }

    @Override
    public void showmsg(String msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChannelsPresenterImpl.unsubcrible();
        if (mIsChannelChanged) {
            RxBus.getInstance().post(new ChannelChangeEvent());
        }

    }
}
