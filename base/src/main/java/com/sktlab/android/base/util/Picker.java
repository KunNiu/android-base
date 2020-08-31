package com.sktlab.android.base.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sktlab.android.base.glide.Glide4Engine;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.SelectionCreator;

import java.util.List;
import java.util.Objects;

public class Picker {
    public static final int PICKER_REQUEST = 10001;

    public enum MimeTypes {
        IMAGE, VIDEO, ALL
    }

    public static void pick(@NonNull Activity activity, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable) {
        pick(activity, mimeTypes, maxSelectable, PICKER_REQUEST);
    }

    public static void pick(@NonNull Activity activity, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, int requestCode) {
        pick(Matisse.from(activity)
                .choose(mimeTypes == MimeTypes.IMAGE ? MimeType.ofImage() : mimeTypes == MimeTypes.VIDEO ? MimeType.ofVideo() : MimeType.ofAll())
                .gridExpectedSize((int) Utils.dpToPx(Objects.requireNonNull(activity), 120)), maxSelectable, requestCode);
    }

    public static void pick(@NonNull Fragment fragment, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable) {
        pick(fragment, mimeTypes, maxSelectable, PICKER_REQUEST);
    }

    public static void pick(@NonNull Fragment fragment, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, int requestCode) {
        pick(Matisse.from(fragment)
                .choose(mimeTypes == MimeTypes.IMAGE ? MimeType.ofImage() : mimeTypes == MimeTypes.VIDEO ? MimeType.ofVideo() : MimeType.ofAll())
                .gridExpectedSize((int) Utils.dpToPx(Objects.requireNonNull(fragment.getContext()), 120)), maxSelectable, requestCode);
    }

    private static void pick(@NonNull SelectionCreator creator, @IntRange(from = 1, to = 9) int maxSelectable, int resultCode) {
        creator.countable(maxSelectable != 1)
                .capture(false)
                .maxSelectable(maxSelectable)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())    // for glide-V4
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .showSingleMediaType(true)
                .forResult(resultCode);
    }

    public static List<String> obtainPathResult(Intent data) {
        return Matisse.obtainPathResult(data);
    }

    public static List<Uri> obtainResult(Intent data) {
        return Matisse.obtainResult(data);
    }
}
