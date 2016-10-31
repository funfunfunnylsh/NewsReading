package com.matthew.cn.newsreading.global;

/**
 * Created by Administrator on 2016/10/25.
 */
public class Constants {

    public static final String NEWS_BASEURL = "http://c.m.163.com/";

    /**
     * 图片
     */
    public static final String GANK_BASEURL = "http://gank.io/api/";

    /**
     *视频
     */
    public static final String VIDEO_BASEURL = "http://c.m.163.com/";


    // 头条TYPE
    public static final String HEADLINE_TYPE = "headline";
    // 房产TYPE
    public static final String HOUSE_TYPE = "house";
    // 其他TYPE
    public static final String OTHER_TYPE = "list";


    // 热点视频
    public static final String VIDEO_HOT_ID = "V9LG4B3A0";
    // 娱乐视频
    public static final String VIDEO_ENTERTAINMENT_ID = "V9LG4CHOR";
    // 搞笑视频
    public static final String VIDEO_FUN_ID = "V9LG4E6VR";
    // 精品视频
    public static final String VIDEO_CHOICE_ID = "00850FRB";


    /**
     * 新闻id获取类型
     *
     * @param id 新闻id
     * @return 新闻类型
     */
    public static String getType(String id) {
        switch (id) {
            case HEADLINE_ID:
                return HEADLINE_TYPE;
            case HOUSE_ID:
                return HOUSE_TYPE;
            default:
                break;
        }
        return OTHER_TYPE;
    }

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.NETEASE_NEWS_VIDEO:
                host = NEWS_BASEURL;
                break;
            case HostType.GANK_GIRL_PHOTO:
                host = GANK_BASEURL;
                break;
            case HostType.NEWS_DETAIL_HTML_PHOTO:
                host = "http://kaku.com/";
                break;
            case HostType.VIDEO:

                host = VIDEO_BASEURL;
                break;
            default:
                host = "";
                break;
        }
        return host;
    }



    // 头条id
    public static final String HEADLINE_ID = "T1348647909107";
    // 房产id
    public static final String HOUSE_ID = "5YyX5Lqs";




    public static final String NEWS_ID = "news_id";
    public static final String NEWS_TYPE = "news_type";

    public static final String SHARES_COLOURFUL_NEWS = "shares_colourful_news";
    public static final String NIGHT_THEME_MODE = "night_theme_mode";
    public static final String INIT_DB = "init_db";
    public static final String CHANNEL_POSITION = "channel_position";

    public static final int NEWS_CHANNEL_MINE = 0;
    public static final int NEWS_CHANNEL_MORE = 1;
}
