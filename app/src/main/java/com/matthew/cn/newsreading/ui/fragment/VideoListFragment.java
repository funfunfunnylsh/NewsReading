package com.matthew.cn.newsreading.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.VideoData;
import com.matthew.cn.newsreading.global.Constants;
import com.matthew.cn.newsreading.global.HostType;
import com.matthew.cn.newsreading.global.RetrofitManager;
import com.matthew.cn.newsreading.global.RxSchedulers;
import com.matthew.cn.newsreading.ui.adapter.VideoListAdapter;
import com.matthew.cn.newsreading.util.MyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by Administrator on 2016/10/31.
 */
public class VideoListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener{


    @BindView(R.id.news_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;


    private int startPage = 0;
    private String type = Constants.VIDEO_ENTERTAINMENT_ID;

    private VideoListAdapter mVideoListAdapter;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    protected void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getIntArray(R.array.gplus_colors));
        //实现首次自动显示加载提示
//        mSwipeRefreshLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefreshLayout.setRefreshing(true);
//            }
//        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mVideoListAdapter = new VideoListAdapter(new ArrayList<VideoData>());
        mVideoListAdapter.openLoadMore(5);
        mVideoListAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mRecyclerView.setAdapter(mVideoListAdapter);
        mVideoListAdapter.setLoadingView(LayoutInflater.from(getActivity()).inflate(R.layout.load_loading_layout, mRecyclerView, false));
        mVideoListAdapter.setOnLoadMoreListener(this);

        //视频监听
        mRecyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                if (JCVideoPlayerManager.getFirst() != null) {
                    JCVideoPlayer videoPlayer = (JCVideoPlayer) JCVideoPlayerManager.getFirst();
                    if (((ViewGroup) view).indexOfChild(videoPlayer) != -1 && videoPlayer.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
                        JCVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });


    }

    @Override
    protected void initData() {
        getData(startPage);

    }

    @Override
    public void onRefresh() {
        startPage = 0;
        getData(startPage);
    }

    @Override
    public void onLoadMoreRequested() {
        startPage += 1;
        getData(startPage);
    }



    public void getData(final int startPage) {
        RetrofitManager.getInstance(HostType.VIDEO).getVideoListObservable(type, startPage)
                .flatMap(new Func1<Map<String, List<VideoData>>, Observable<VideoData>>() {
                    @Override
                    public Observable<VideoData> call(Map<String, List<VideoData>> map) {
                        return Observable.from(map.get(type));
                    }
                })
                        //转化时间
                .map(new Func1<VideoData, VideoData>() {
                    @Override
                    public VideoData call(VideoData videoData) {
                        String ptime = MyUtil.formatDate(videoData.getPtime());
                        videoData.setPtime(ptime);
                        return videoData;
                    }
                })
                .distinct()//去重
                .toSortedList(new Func2<VideoData, VideoData, Integer>() {
                    @Override
                    public Integer call(VideoData videoData, VideoData videoData2) {
                        return videoData2.getPtime().compareTo(videoData.getPtime());
                    }
                })
                        //声明线程调度
                .compose(RxSchedulers.<List<VideoData>>defaultSchedulers())
                .subscribe(new Subscriber<List<VideoData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<VideoData> videoDatas) {
                        if(startPage != 0){
                            mVideoListAdapter.addData(videoDatas);
                        }else{
                            mVideoListAdapter.setNewData(videoDatas);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}
