package com.sktlab.android.base.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;
import java.util.Objects;

public class Picker {
    public static final int PICKER_REQUEST = 10001;

    public enum MimeTypes {
        IMAGE, VIDEO, ALL
    }

    public static void pick(@NonNull Activity activity, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, boolean capture) {
        pick(Matisse.from(activity), mimeTypes, maxSelectable, activity.getPackageName() + ".fileProvider", capture);
    }

    public static void pick(@NonNull Fragment fragment, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, boolean capture) {
        pick(Matisse.from(fragment), mimeTypes, maxSelectable, fragment.requireActivity().getPackageName() + ".fileProvider", capture);
    }

    private static void pick(Matisse matisse, MimeTypes mimeTypes, int maxSelectable, String authority, boolean capture) {
        matisse.choose(mimeTypes == MimeTypes.IMAGE ? MimeType.ofImage() : mimeTypes == MimeTypes.VIDEO ? MimeType.ofVideo() : MimeType.ofAll())
                .countable(maxSelectable != 1)
                .capture(capture)
                .captureStrategy(new CaptureStrategy(true, authority))
                .maxSelectable(maxSelectable)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())    // for glide-V4
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .showSingleMediaType(true)
                .forResult(PICKER_REQUEST);
    }

    public static List<String> obtainPathResult(Intent data) {
        return Matisse.obtainPathResult(data);
    }

    public static List<Uri> obtainResult(Intent data) {
        return Matisse.obtainResult(data);
    }
}
