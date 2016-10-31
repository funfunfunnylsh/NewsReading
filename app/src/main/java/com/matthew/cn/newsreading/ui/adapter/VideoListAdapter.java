package com.matthew.cn.newsreading.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matthew.cn.newsreading.NewsApplication;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.VideoData;
import com.squareup.picasso.Picasso;

import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Administrator on 2016/10/31.
 */
public class VideoListAdapter extends BaseQuickAdapter<VideoData> {


    public VideoListAdapter(List<VideoData> data) {
        super(R.layout.item_video_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, VideoData videoData) {
        JCVideoPlayerStandard jcVideoPlayerStandard = baseViewHolder.getView(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(videoData.getMp4_url()
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, TextUtils.isEmpty(videoData.getDescription()) ? videoData.getTitle() + "" : videoData.getDescription());
//        Picasso.with(NewsApplication.getAppContext()).load(videoData.getCover()).into(jcVideoPlayerStandard.thumbImageView);
        Glide.with(NewsApplication.getAppContext()).load(videoData.getCover()).centerCrop().into(jcVideoPlayerStandard.thumbImageView);

        ImageView iv_log = baseViewHolder.getView(R.id.iv_logo);
        Picasso.with(NewsApplication.getAppContext()).load(videoData.getTopicImg()).into(iv_log);

        baseViewHolder.setText(R.id.tv_from, videoData.getTopicName());
        baseViewHolder.setText(R.id.tv_play_time, videoData.getPlayCount()+"次播放");

    }
}
