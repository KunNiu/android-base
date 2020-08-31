package com.sktlab.android.base.glide;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

@GlideModule
public final class MGlideModule extends AppGlideModule {
//    //外部路径
//    private String sdRootPath = Environment.getExternalStorageDirectory().getPath();
//    private String appRootPath = null;
//
//    private String getStorageDirectory() {
//        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
//                sdRootPath : appRootPath;
//    }

    @Override
    public void applyOptions(@NonNull Context context, GlideBuilder builder) {
        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565));

//        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
//        //手机app路径
//        appRootPath = context.getCacheDir().getPath();
//        builder.setDiskCache(
//                new DiskLruCacheFactory(getStorageDirectory() + "/GlideDisk", diskCacheSizeBytes)
    }
}
