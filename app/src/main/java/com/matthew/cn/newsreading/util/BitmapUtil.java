package com.matthew.cn.newsreading.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/10/25.
 */
public class BitmapUtil {
    private static final String TAG = "BtimapUtil";


    /**
     * 根据网址获得图片，优先从本地获取，本地没有则从网络下载
     *
     * @param url  图片网址
     * @param context 上下文
     * @return 图片
     */
    public static Bitmap getBitmap(String url,Context context){
        Log.e(TAG, "------url="+url);
        String imageName= url.substring(url.lastIndexOf("/")+1, url.length());
        File file = new File(getPath(context),imageName);
        if(file.exists()){
            Log.e(TAG, "getBitmap from Local");
            return BitmapFactory.decodeFile(file.getPath());
        }
        return getNetBitmap(url,file,context);
    }

    /**
     * 根据传入的list中保存的图片网址，获取相应的图片列表
     *
     * @param list  保存图片网址的列表
     * @param context 上下文
     * @return 图片列表
     */
    public static List<Bitmap> getBitmap(List<String> list,Context context){
        List<Bitmap> result = new ArrayList<Bitmap>();
        for(String strUrl : list){
            Bitmap bitmap = getBitmap(strUrl,context);
            if(bitmap!=null){
                result.add(bitmap);
            }
        }
        return result;
    }

    /**
     * 获取图片的存储目录，在有sd卡的情况下为 “/sdcard/apps_images/本应用包名/cach/images/”
     * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
     *
     * @param context 上下文
     * @return 本地图片存储目录
     */
    private static String getPath(Context context){
        String path = null;
        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName()+"/cach/images/";
        if(hasSDCard){
            path="/sdcard/apps_images/"+packageName;
        }else{
            path="/data/data/"+packageName;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if(!isExist){

            file.mkdirs();

        }
        return file.getPath();
    }

    /**
     * 网络可用状态下，下载图片并保存在本地
     *
     * @param strUrl 图片网址
     * @param file 本地保存的图片文件
     * @param context  上下文
     * @return 图片
     */
    private static Bitmap getNetBitmap(String strUrl,File file,Context context) {
        Log.e(TAG, "getBitmap from net");
        Bitmap bitmap = null;
        if(NetUtil.isNetworkAvailable()){
            try {
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoInput(true);
                con.connect();
                InputStream in = con.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                FileOutputStream out = new FileOutputStream(file.getPath());
                bitmap.compress(Bitmap.CompressFormat.PNG,100, out);
                out.flush();
                out.close();
                in.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{

            }
        }
        return bitmap;
    }



}
