package com.matthew.cn.newsreading.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.afa.tourism.greendao.gen.NewsChannelTable;
import com.matthew.cn.newsreading.NewsApplication;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.event.ChannelChangeEvent;
import com.matthew.cn.newsreading.global.Constants;
import com.matthew.cn.newsreading.global.NewsChannelTableManager;
import com.matthew.cn.newsreading.global.RxSchedulers;
import com.matthew.cn.newsreading.ui.adapter.NewsFragmentPagerAdapter;
import com.matthew.cn.newsreading.ui.fragment.GankFragment;
import com.matthew.cn.newsreading.ui.fragment.NewsListFragment;
import com.matthew.cn.newsreading.ui.fragment.VideoListFragment;
import com.matthew.cn.newsreading.util.RxBus;
import com.matthew.cn.newsreading.util.SnackBarUtil;
import com.matthew.cn.newsreading.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.add_channel_iv)
    ImageView addChannelIv;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.main_nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.main_fragment_container)
    FrameLayout mainFragmentContainer;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private boolean isBackPressed;
    private List<Fragment> mNewsFragmentList = new ArrayList<>();

    private String mCurrentViewPagerName;
    private List<String> mChannelNames;

    Fragment currentFragment;
    MenuItem currentMenuItem;

    private Subscription mSubscription;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSubscription = RxBus.getInstance().toObservable(ChannelChangeEvent.class)
                .subscribe(new Action1<ChannelChangeEvent>() {
                    @Override
                    public void call(ChannelChangeEvent channelChangeEvent) {
                        getChannels();
                    }
                });
    }

    @Override
    protected void initView() {
        mToolbar.setTitle("新闻");
        setSupportActionBar(mToolbar);

        initDrawer();
        initNavigationView();
        initViewPagers();
    }

    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        //设置左上角显示三道横线
        toggle.syncState();
        mToolbar.setTitle(R.string.app_name);
    }

    private void initNavigationView() {
        currentMenuItem = mNavView.getMenu().findItem(R.id.nav_news);

        mNavView.setCheckedItem(R.id.nav_news);//设置默认选中
        //设置NavigationView对应menu item的点击事情
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_news:
                        mToolbar.setTitle("新闻");
                        linearLayout.setVisibility(View.VISIBLE);
                        mViewPager.setVisibility(View.VISIBLE);
                        mainFragmentContainer.setVisibility(View.GONE);

                        break;
                    case R.id.nav_photo:
                        mToolbar.setTitle("妹纸");
                        linearLayout.setVisibility(View.GONE);
                        mViewPager.setVisibility(View.GONE);
                        mainFragmentContainer.setVisibility(View.VISIBLE);


                        break;
                    case R.id.nav_video:
                        mToolbar.setTitle("视频");
                        linearLayout.setVisibility(View.GONE);
                        mViewPager.setVisibility(View.GONE);
                        mainFragmentContainer.setVisibility(View.VISIBLE);
                        break;
                }

                if (currentMenuItem != item && currentMenuItem != null && item.getItemId() != R.id.nav_news) {
                    currentMenuItem.setChecked(false);
                    currentMenuItem = item;
                    currentMenuItem.setChecked(true);
                    switchFragment(getFragmentById(currentMenuItem.getItemId()));
                }

                //隐藏NavigationView
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private Fragment getFragmentById(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_photo:
                fragment=new GankFragment();
                break;
            case R.id.nav_video:
                fragment=new VideoListFragment();
                break;

        }
        return fragment;
    }

    private void switchFragment(Fragment fragment) {

        if (currentFragment == null || !currentFragment.getClass().getName().equals(fragment.getClass().getName()))
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, fragment)
                    .commit();
        currentFragment = fragment;

    }

    private void initViewPagers() {
        linearLayout.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.VISIBLE);
        mainFragmentContainer.setVisibility(View.GONE);

    }

    @OnClick(R.id.add_channel_iv)
    public void onClick(){
        Intent intent = new Intent(this, NewsChannelsActivity.class);
        startActivity(intent);

    }


    @Override
    protected void initData() {
        getChannels();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (isBackPressed) {
                if (JCVideoPlayer.backPress()) {
                    return;
                }
                super.onBackPressed();
                return;
            }

            isBackPressed = true;

            SnackBarUtil.show(mDrawerLayout, R.string.back_pressed_tip);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isBackPressed = false;
                }
            }, 2000);
        }
    }

    public void getChannels(){
        Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                NewsChannelTableManager.initDB();
                subscriber.onNext(NewsChannelTableManager.loadNewsChannelsMine());
                subscriber.onCompleted();
            }
        })
                .compose(RxSchedulers.<List<NewsChannelTable>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsChannelTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.showToast(MainActivity.this, NewsApplication.getAppContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {

                        final List<String> channelNames = new ArrayList<>();
                        if (newsChannelTables != null) {
                            setNewsList(newsChannelTables, channelNames);
                        }

                    }
                });
    }

    private void setNewsList(List<NewsChannelTable> newsChannels, List<String> channelNames) {
        mNewsFragmentList.clear();
        for (NewsChannelTable newsChannel : newsChannels) {
            NewsListFragment newsListFragment = createListFragments(newsChannel);
            mNewsFragmentList.add(newsListFragment);
            channelNames.add(newsChannel.getNewsChannelName());
        }
        setViewPager(channelNames);
    }

    private NewsListFragment createListFragments(NewsChannelTable newsChannel) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(Constants.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(Constants.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> channelNames) {
        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager(), channelNames, mNewsFragmentList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mNewsFragmentList.size()-1);
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabs.setupWithViewPager(mViewPager);

//        mTabs.setTabsFromPagerAdapter(adapter);
        setPageChangeListener();

        mChannelNames = channelNames;
        int currentViewPagerPosition = getCurrentViewPagerPosition();
        mViewPager.setCurrentItem(currentViewPagerPosition, false);
    }

    private int getCurrentViewPagerPosition() {
        int position = 0;
        if (mCurrentViewPagerName != null) {
            for (int i = 0; i < mChannelNames.size(); i++) {
                if (mCurrentViewPagerName.equals(mChannelNames.get(i))) {
                    position = i;
                }
            }
        }
        return position;
    }

    private void setPageChangeListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentViewPagerName = mChannelNames.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
