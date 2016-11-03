package com.taozi.gifts;

import android.app.Application;
import android.os.Environment;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.se7en.utils.DeviceUtils;

import java.io.File;

/**
 * Created by Tao Yimin on 2016/9/11.
 * 记得在清单文件注册!
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ImageLoader配置
        initImageLoader();
        DeviceUtils.setContext(this);
    }

    /**
     * 初始化ImageLoader配置
     */
    private void initImageLoader() {
        //TODO 设置缓存文件夹的目标路径
        String cacheTargetPath = "/Gifts/ImageCache";
        String storagePath = "";
        //判断外部储存卡是否挂载
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //如果外部储存卡处于挂载状态则使用外部储存卡
            storagePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            //如果外部储存卡处于非挂载状态则使用内部储存卡
            storagePath = Environment.getDataDirectory().getAbsolutePath();
        }
        //获得缓存目录的绝对路径
        String cacheAbsolutePath = storagePath + cacheTargetPath;
        //配置ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                //设置内存缓存的大小(当超过这个值会去回收bitmap),Runtime.getRuntime().maxMemory()获取当前应用运行时内存的大小
                .memoryCache(new LruMemoryCache((int) (Runtime.getRuntime().maxMemory() / 4)))
                //设置磁盘缓存的位置
                .diskCache(new UnlimitedDiskCache(new File(cacheAbsolutePath)))
                //每个缓存文件的最大长宽
                .memoryCacheExtraOptions(200, 200)
                //缓存的File数量
                .diskCacheFileCount(100)
                //设置加载图片的线程池数量
                .threadPoolSize(8)
                //线程优先级
                //.threadPriority(Thread.NORM_PRIORITY - 2)
                //硬盘缓存50MB
                //.diskCacheSize(50 * 1024 * 1024)
                //将保存的时候的URI名称用MD5
                //.diskCacheFileNameGenerator(new Md5FileNameGenerator())
                //将保存的时候的URI名称用HASHCODE加密
                //.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        //全局初始化此配置
        ImageLoader.getInstance().init(config);
    }
}