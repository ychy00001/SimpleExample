package com.smalltown.rainsimpleexample.cache;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;
import com.smalltown.rainsimpleexample.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Diagrams on 2016/1/6 18:12
 */
public class DiskCache {
    public static final String TAG = "DiskCache";

    private Context mContext;
    private DiskLruCache mDiskLruCache = null;
    /** 用于DiskLruCache的同步锁 */
    private final Object mDiskCacheLock = new Object();
    private long maxCacheSize = 10 * 1024 * 1024;
    private boolean mDiskCacheStarting;//标记是否在读取缓存

    public DiskCache(Context context) {
        this.mContext = context;
        openCache();
    }

    /**
     * 打开缓存
     */
    public void openCache() {
        File cacheDir = getDiskCacheDir(mContext, "rain");
        try {
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            mDiskLruCache = DiskLruCache.open(cacheDir, getAppVersion(mContext), 1, maxCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加至缓存
     * 新建线程创建，多线程操作
     *
     * @param data json数据
     */
    public void addCache(final String data, final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                addCacheSyn(url, data);
            }
        }).start();
    }

    /**
     * 具体添加缓存方法 添加缓存为什么要上锁
     *
     * @param url  链接
     * @param data 数据
     */
    private void addCacheSyn(String url, String data) {
        OutputStream outputStream = null;
        synchronized (mDiskCacheLock) {
            try {
                String key = hashKeyForDisk(url);
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);//关键语句 获取editor
                if (editor != null) {
                    outputStream = editor.newOutputStream(0);//拿导editor的outputstream
                    outputStream.write(data.getBytes());//写入数据
                    editor.commit();//提交写入数据
                    //强制写入缓存 避免应用被用户强制关闭时,无法保存缓存
                    mDiskLruCache.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            mDiskCacheStarting = false;
            mDiskCacheLock.notifyAll();
        }
    }

    /**
     * 获取缓存的JsonCache
     *
     * @param url 链接
     *
     * @return json字符串
     */
    public String getJsonCache(String url) {
        synchronized (mDiskCacheLock) {
            // 当初始化未完成时,则持续等待
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "get data from disk - " + e);
                }
            }

            String key = hashKeyForDisk(url);
            InputStream is = null;
            try {
                if (mDiskLruCache != null) {
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    if (snapshot != null) {
                        is = snapshot.getInputStream(0);
                        if (is != null) {
                            return IOUtil.converStreamString(is);
                        }
                        return null;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * MD5加密转16进制
     *
     * @param key 外部传来的key
     *
     * @return 加密后的key
     */
    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = byteToHexString(mDigest.digest());
            return cacheKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 处理加密后的转换
     *
     * @param bytes 要加密字符数组
     *
     * @return 返回加密key
     */
    private String byteToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取缓存路径
     *
     * @param context    上下文
     * @param uniqueName 标识名称
     *
     * @return 返回缓存目录文件
     */
    @SuppressWarnings("ConstantConditions")
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用程序版本号
     *
     * @param context 上下文
     *
     * @return 程序版本号
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    /**
     * 检查指定的路径下剩余的可用空间
     *
     * @param path 要检查的路径
     *
     * @return 可用空间(byte)
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static long getUsableSpace(File path) {
        boolean isSupport = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
        if (isSupport) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }


    /** 判断当前DiskCache是否已经关闭 */
    public boolean isClosed() {
        return mDiskLruCache == null || mDiskLruCache.isClosed();
    }

    /**
     * 删除文件名对应的缓存,该方法本身不是异步的,注意不要阻塞主线程
     *
     * @param name 要删除的文件
     */
    public void deleteCache(String name) {
        synchronized (mDiskCacheLock) {
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                String key = hashKeyForDisk(name);
                try {
                    mDiskLruCache.remove(key);
                    Log.d(TAG, "Disk cache " + name + " is deleted");
                } catch (IOException e) {
                    Log.e(TAG, "delete cache " + name + " " + e);
                }
            }
        }
    }

    /** 清理所有缓存,该方法本身不是异步的,注意不要阻塞主线程 */
    public void clearAllCache() {
        synchronized (mDiskCacheLock) {
            mDiskCacheStarting = true;
            if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
                try {
                    mDiskLruCache.delete();
                    Log.d(TAG, "Disk cache cleared");
                } catch (IOException e) {
                    Log.e(TAG, "clearCache - " + e);
                }
                mDiskLruCache = null;
                openCache();
            }
        }
    }

    /**
     * 关闭缓存
     */
    public void close() {
        if (mDiskLruCache != null && !mDiskLruCache.isClosed())
            try {
                mDiskLruCache.close();
            } catch (IOException e) {
                Log.e(TAG, "close cache exception - " + mDiskLruCache.getDirectory());
            }
        mDiskLruCache = null;
    }
}
