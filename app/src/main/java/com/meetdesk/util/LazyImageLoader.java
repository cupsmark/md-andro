package com.meetdesk.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.meetdesk.external.uil.cache.disc.naming.Md5FileNameGenerator;
import com.meetdesk.external.uil.core.DisplayImageOptions;
import com.meetdesk.external.uil.core.ImageLoader;
import com.meetdesk.external.uil.core.ImageLoaderConfiguration;
import com.meetdesk.external.uil.core.assist.ImageScaleType;
import com.meetdesk.external.uil.core.assist.QueueProcessingType;
import com.meetdesk.external.uil.core.imageaware.ImageAware;
import com.meetdesk.external.uil.core.listener.ImageLoadingListener;

/**
 * Created by ekobudiarto on 11/6/16.
 */
public class LazyImageLoader {

    Context mContext;
    DisplayImageOptions opt;
    ImageLoader loader;

    public LazyImageLoader(Context context)
    {
        this.mContext = context;

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .threadPoolSize(5)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);


        opt = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        loader = ImageLoader.getInstance();
    }

    public void showImage(String path, ImageAware imageAware)
    {
        loader.displayImage(path, imageAware, opt);
    }

    public void showImage(String path, ImageAware imageAware, ImageLoadingListener listener)
    {
        loader.displayImage(path, imageAware, opt, listener);
    }

    public void showImage(String path, ImageView imageView)
    {
        loader.displayImage(path, imageView, opt);
    }

    public void showImage(String path, ImageView imageView, ImageLoadingListener listener)
    {
        loader.displayImage(path, imageView, opt, listener);
    }

}
