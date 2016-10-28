package com.matthew.cn.newsreading.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.matthew.cn.newsreading.NewsApplication;
import com.matthew.cn.newsreading.R;
import com.matthew.cn.newsreading.entity.NewsSummary;
import com.matthew.cn.newsreading.util.DimenUtil;
import com.matthew.cn.newsreading.util.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
public class NewsListAdapter extends BaseQuickAdapter<NewsSummary>{


    private TextView title_tv;
    private LinearLayout linearLayout;
    private ImageView iv_left,iv_middle,iv_right;

    public NewsListAdapter(List<NewsSummary> data) {
        super(R.layout.item_news, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsSummary newsSummary) {
        ImageView photo_iv = baseViewHolder.getView(R.id.news_summary_photo_iv);
        title_tv = baseViewHolder.getView(R.id.news_summary_title_tv);
        TextView desc = baseViewHolder.getView(R.id.news_summary_digest_tv);
        TextView time = baseViewHolder.getView(R.id.news_summary_ptime_tv);

        linearLayout = baseViewHolder.getView(R.id.news_summary_photo_iv_group);
        iv_left = baseViewHolder.getView(R.id.news_summary_photo_iv_left);
        iv_middle = baseViewHolder.getView(R.id.news_summary_photo_iv_middle);
        iv_right = baseViewHolder.getView(R.id.news_summary_photo_iv_right);

        String title = newsSummary.getLtitle();
        if (title == null) {
            title = newsSummary.getTitle();
        }
        title_tv.setText(title);
        time.setText(newsSummary.getPtime());

        if(TextUtils.isEmpty(newsSummary.getDigest())){ //image
            linearLayout.setVisibility(View.VISIBLE);
            desc.setVisibility(View.GONE);
            photo_iv.setVisibility(View.GONE);

            setImageView(newsSummary);
        }else{ //normal
            linearLayout.setVisibility(View.GONE);
            desc.setVisibility(View.VISIBLE);
            photo_iv.setVisibility(View.VISIBLE);

            desc.setText(newsSummary.getDigest());
            ImageLoader.load(NewsApplication.getAppContext(),newsSummary.getImgsrc(),photo_iv);
        }
    }



    private void setImageView(NewsSummary newsSummary) {
        int PhotoThreeHeight = (int) DimenUtil.dp2px(90);
        int PhotoTwoHeight = (int) DimenUtil.dp2px(120);
        int PhotoOneHeight = (int) DimenUtil.dp2px(150);

        String imgSrcLeft = null;
        String imgSrcMiddle = null;
        String imgSrcRight = null;

        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();

        if (newsSummary.getAds() != null) {
            List<NewsSummary.AdsBean> adsBeanList = newsSummary.getAds();
            int size = adsBeanList.size();
            if (size >= 3) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();
                imgSrcRight = adsBeanList.get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;

                title_tv.setText(NewsApplication.getAppContext()
                        .getString(R.string.photo_collections, adsBeanList.get(0).getTitle()));
            } else if (size >= 2) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();
                imgSrcMiddle = adsBeanList.get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = adsBeanList.get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else if (newsSummary.getImgextra() != null) {
            int size = newsSummary.getImgextra().size();
            if (size >= 3) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();
                imgSrcRight = newsSummary.getImgextra().get(2).getImgsrc();

                layoutParams.height = PhotoThreeHeight;
            } else if (size >= 2) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();
                imgSrcMiddle = newsSummary.getImgextra().get(1).getImgsrc();

                layoutParams.height = PhotoTwoHeight;
            } else if (size >= 1) {
                imgSrcLeft = newsSummary.getImgextra().get(0).getImgsrc();

                layoutParams.height = PhotoOneHeight;
            }
        } else {
            imgSrcLeft = newsSummary.getImgsrc();

            layoutParams.height = PhotoOneHeight;
        }

        setPhotoImageView(imgSrcLeft, imgSrcMiddle, imgSrcRight);
        linearLayout.setLayoutParams(layoutParams);
    }

    private void setPhotoImageView(String imgSrcLeft, String imgSrcMiddle, String imgSrcRight) {
        if (imgSrcLeft != null) {
            showAndSetPhoto(iv_left, imgSrcLeft);
        } else {
            hidePhoto(iv_left);
        }

        if (imgSrcMiddle != null) {
            showAndSetPhoto(iv_middle, imgSrcMiddle);
        } else {
            hidePhoto(iv_middle);
        }

        if (imgSrcRight != null) {
            showAndSetPhoto(iv_right, imgSrcRight);
        } else {
            hidePhoto(iv_right);
        }
    }

    private void showAndSetPhoto(ImageView imageView, String imgSrc) {
        imageView.setVisibility(View.VISIBLE);
        ImageLoader.load(NewsApplication.getAppContext(), imgSrc, imageView);
    }

    private void hidePhoto(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }
}
