package com.matthew.cn.newsreading.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.NewsPhotoDetail;
import com.matthew.cn.newsreading.entity.NewsSummary;
import com.matthew.cn.newsreading.global.Constants;
import com.matthew.cn.newsreading.mvp.news.NewsContract;
import com.matthew.cn.newsreading.mvp.news.NewsPresentreImpl;
import com.matthew.cn.newsreading.ui.activity.NewsDetailActivity;
import com.matthew.cn.newsreading.ui.adapter.NewsListAdapter;
import com.matthew.cn.newsreading.util.NetUtil;
import com.matthew.cn.newsreading.util.SnackBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/26.
 */
public class NewsListFragment extends BaseFragment implements NewsContract.NewsView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.news_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private NewsPresentreImpl newsPresentre;
    private String mNewsType;
    private String mNewsId ;
    private int mStartPage = 0;
    private NewsListAdapter newsListAdapter;


    @Override
    protected int initLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mNewsId = getArguments().getString(Constants.NEWS_ID);
            mNewsType = getArguments().getString(Constants.NEWS_TYPE);
        }

        newsPresentre = new NewsPresentreImpl(this);

        newsPresentre.loadNews(mNewsType, mNewsId, mStartPage, false);


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors));
//        //实现首次自动显示加载提示
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });

        initRecyclerView();
    }

    @Override
    public void onRefresh() {
        mStartPage = 0;
        newsPresentre.loadNews(mNewsType, mNewsId, mStartPage, false);
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsListAdapter = new NewsListAdapter(new ArrayList<NewsSummary>());
        newsListAdapter.openLoadMore(10);
        newsListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecyclerView.setAdapter(newsListAdapter);
        newsListAdapter.setLoadingView(LayoutInflater.from(getActivity()).inflate(R.layout.load_loading_layout, mRecyclerView, false));
        newsListAdapter.setOnLoadMoreListener(this);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                //TODO
                if (!TextUtils.isEmpty(newsListAdapter.getData().get(i).getDigest())) {
                    goToNewsDetailActivity(view, i);

                } else {//photo
                    NewsPhotoDetail newsPhotoDetail = getPhotoDetail(i);
//                    goToPhotoDetailActivity(newsPhotoDetail);
                    //TODO
                }


            }
        });

    }

    private NewsPhotoDetail getPhotoDetail(int position) {
        NewsSummary newsSummary = newsListAdapter.getData().get(position);
        NewsPhotoDetail newsPhotoDetail = new NewsPhotoDetail();
        newsPhotoDetail.setTitle(newsSummary.getTitle());
        setPictures(newsSummary, newsPhotoDetail);
        return newsPhotoDetail;
    }

    private void setPictures(NewsSummary newsSummary, NewsPhotoDetail newsPhotoDetail) {
        List<NewsPhotoDetail.Picture> pictureList = new ArrayList<>();

        if (newsSummary.getAds() != null) {
            for (NewsSummary.AdsBean entity : newsSummary.getAds()) {
                setValuesAndAddToList(pictureList, entity.getTitle(), entity.getImgsrc());
            }
        } else if (newsSummary.getImgextra() != null) {
            for (NewsSummary.ImgextraBean entity : newsSummary.getImgextra()) {
                setValuesAndAddToList(pictureList, null, entity.getImgsrc());
            }
        } else {
            setValuesAndAddToList(pictureList, null, newsSummary.getImgsrc());
        }

        newsPhotoDetail.setPictures(pictureList);
    }

    private void setValuesAndAddToList(List<NewsPhotoDetail.Picture> pictureList, String title, String imgsrc) {
        NewsPhotoDetail.Picture picture = new NewsPhotoDetail.Picture();
        if (title != null) {
            picture.setTitle(title);
        }
        picture.setImgSrc(imgsrc);

        pictureList.add(picture);
    }

    private void goToPhotoDetailActivity(NewsPhotoDetail newsPhotoDetail) {
        Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
        intent.putExtra("newsposturl", newsPhotoDetail);
        startActivity(intent);
    }

    private void goToNewsDetailActivity(View view, int position) {
        Intent intent = setIntent(position);
        startActivity(view, intent);
    }

    private Intent setIntent(int position) {
        List<NewsSummary> newsSummaryList = newsListAdapter.getData();

        Intent intent = new Intent(mActivity, NewsDetailActivity.class);
        intent.putExtra("newspostid", newsSummaryList.get(position).getPostid());
        intent.putExtra("newsposturl", newsSummaryList.get(position).getImgsrc());
        return intent;
    }

    private void startActivity(View view, Intent intent) {
        ImageView newsSummaryPhotoIv = (ImageView) view.findViewById(R.id.news_summary_photo_iv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mActivity, newsSummaryPhotoIv, getResources().getString(R.string.transition_photos));
            startActivity(intent, options.toBundle());
        } else {

            //让新的Activity从一个小的范围扩大到全屏
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        }
    }


    @Override
    protected void initData() {


    }

    @Override
    public void onLoadMoreRequested() {
        mStartPage += 20;
        newsPresentre.loadNews(mNewsType, mNewsId, mStartPage, true);
    }

    @Override
    public void setNewsList(List<NewsSummary> newsSummary) {
        newsListAdapter.setNewData(newsSummary);
        mSwipeRefreshLayout.setRefreshing(false);
        emptyView.setVisibility(View.GONE);
        newsListAdapter.removeAllFooterView();
    }

    @Override
    public void addList(List<NewsSummary> newsSummary) {
        newsListAdapter.addData(newsSummary);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String message) {
        if (NetUtil.isNetworkAvailable()) {
            SnackBarUtil.show(mRecyclerView, message);
        }
        if(mStartPage == 0 && newsListAdapter.getData().size() == 0){
            emptyView.setVisibility(View.VISIBLE);
        }
        mSwipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }


    @OnClick(R.id.empty_view)
    public void onClick() {
        emptyView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        newsPresentre.loadNews(mNewsType, mNewsId, mStartPage, false);
    }

    @Override
    public void onDestroy() {
        newsPresentre.unsubcrible();
        super.onDestroy();
    }
}
