package com.matthew.cn.newsreading.ui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matthew.cn.newsreading.NewsApplication;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.PhotoGirl;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */
public class GnakAdapter extends BaseQuickAdapter<PhotoGirl>{

    public GnakAdapter(List<PhotoGirl> data) {
        super(R.layout.item_gank, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, PhotoGirl photoGirl) {
        ImageView photo_iv = baseViewHolder.getView(R.id.photo_iv);

        Picasso.with(NewsApplication.getAppContext()).load(photoGirl.getUrl())
                .placeholder(R.color.image_place_holder)
                .into(photo_iv);

    }
}
