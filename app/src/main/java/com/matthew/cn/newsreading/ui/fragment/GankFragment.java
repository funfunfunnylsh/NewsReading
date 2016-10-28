package com.matthew.cn.newsreading.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.PhotoGirl;
import com.matthew.cn.newsreading.mvp.gank.GankContract;
import com.matthew.cn.newsreading.mvp.gank.GnakPresenterImpl;
import com.matthew.cn.newsreading.ui.activity.GankDetailActivity;
import com.matthew.cn.newsreading.ui.adapter.GnakAdapter;
import com.matthew.cn.newsreading.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/10/27.
 */
public class GankFragment extends BaseFragment implements GankContract.GankView, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.news_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private GnakPresenterImpl mGnakPresenterImpl;
    private int size = 10;
    private int startpage = 1;
    private GnakAdapter gnakAdapter;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors));
        //        //实现首次自动显示加载提示
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        gnakAdapter = new GnakAdapter(new ArrayList<PhotoGirl>());
        gnakAdapter.openLoadMore(10);
        gnakAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mRecyclerView.setAdapter(gnakAdapter);
        gnakAdapter.setLoadingView(LayoutInflater.from(getActivity()).inflate(R.layout.load_loading_layout, mRecyclerView, false));
        gnakAdapter.setOnLoadMoreListener(this);
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                //TODO
                Intent intent = new Intent(getActivity(), GankDetailActivity.class);
                intent.putExtra("photourl", gnakAdapter.getData().get(i).getUrl());
                startActivity(view, intent);

            }
        });


    }

    private void startActivity(View view, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(mActivity, view, getResources().getString(R.string.transition_photos));
            startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(view, view.getWidth() / 2, view.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        }
    }

    @Override
    protected void initData() {
        mGnakPresenterImpl = new GnakPresenterImpl(this);
        mGnakPresenterImpl.loadGanks(size, startpage, false);
    }


    @Override
    public void onRefresh() {
        startpage = 1;
        mGnakPresenterImpl.loadGanks(size, startpage, false);
    }

    @Override
    public void onLoadMoreRequested() {
        startpage += 1;
        mGnakPresenterImpl.loadGanks(size, startpage, true);

    }

    @Override
    public void setGankList(List<PhotoGirl> photoGirls) {
        gnakAdapter.setNewData(photoGirls);
        mSwipeRefreshLayout.setRefreshing(false);
        gnakAdapter.removeAllFooterView();
    }

    @Override
    public void addList(List<PhotoGirl> photoGirls) {
        gnakAdapter.addData(photoGirls);
    }

    @Override
    public void showProgress() {
//        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
//        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showMsg(String message) {
        mSwipeRefreshLayout.setRefreshing(false);
//        progressBar.setVisibility(View.GONE);
        ToastUtils.showToast(getActivity(), message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
