package com.smalltown.rainsimpleexample.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader;
import com.smalltown.rainsimpleexample.util.MD5Util;
import com.smalltown.rainsimpleexample.util.ToastUtil;

import java.io.*;

/**
 * Created by Diagrams on 2016/1/5 11:42
 */
public class BitmapCache implements ImageLoader.ImageCache {

    public static BitmapCache bitmapCache;
    private LruCache<String, Bitmap> mLruCache;
    private Bitmap bitmap;
    private Context context;

    /**
     * 单例获取实例
     * @return BitmapCache
     */
    public static BitmapCache getInstance() {
        if (null == bitmapCache) {
            bitmapCache = new BitmapCache();
        }
        return bitmapCache;
    }

    /**
      * 初始化
      * @param context 上下文
      */
    public void initilize(Context context){
        //新建缓存对象
        this.context = context;
        int maxSize = (int) (Runtime.getRuntime().maxMemory()/8);
        mLruCache = new LruCache<String, Bitmap>(maxSize){
            /**
             * 重写sizeOf为了移除缓存时候计算大小
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //一行上对应的字节 * 行号
                return value.getRowBytes()*value.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        //从缓存中读取图像
        bitmap = mLruCache.get(url);
        if(bitmap != null){
            return bitmap;
        }
        //从本地读取图像
        bitmap = getImageByLocal(url);
        if(bitmap != null){
            return bitmap;
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        if(null == mLruCache.get(url)){
            saveImageToCache(url,bitmap);
        }
        if(null == getImageByLocal(url)){
            saveImageToLocal(url,bitmap);
        }
    }

    private Bitmap getImageByLocal(String url){
        try {
            //获取本地缓存目录
            File cacheDir = context.getCacheDir();
            //加密保护 保护路径
            String imageName = MD5Util.encode(url).substring(0,10);
            //获取图片
            InputStream is = new FileInputStream(new File(cacheDir,imageName));
            Bitmap decodeStream = BitmapFactory.decodeStream(is);
            if(decodeStream != null){
                //写入内存
                saveImageToCache(url,decodeStream);
                return decodeStream;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存图片至缓存
     * @param url 链接
     * @param bitmap 图片
     */
    private void saveImageToCache(String url,Bitmap bitmap) {
        mLruCache.put(url, bitmap);
    }

    /**
     * 保存图片至本地
     * @param url 链接
     * @param bitmap 图片
     */
    private void saveImageToLocal(String url,Bitmap bitmap){
        try {
            //获取本地缓存目录
            File cacheDir = context.getCacheDir();
            //加密保户
            String imageName = MD5Util.encode(url).substring(0,10);
            OutputStream os = new FileOutputStream(new File(cacheDir, imageName));
            //压缩至本地
            bitmap.compress(Bitmap.CompressFormat.PNG,100, os);
            ToastUtil.showToast("图片保存至本地");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
