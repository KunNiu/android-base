package com.sktlab.android.base.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

import com.huantansheng.easyphotos.Builder.AlbumBuilder;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;
import com.sktlab.android.base.glide.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Picker {
    public static final int PICKER_REQUEST = 10001;

    public enum MimeTypes {
        IMAGE, VIDEO, BOTH
    }

//    public static void pick(@NonNull Activity activity, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, boolean capture) {
//        pick(activity, mimeTypes, maxSelectable, capture, -1);
//    }
//
//    public static void pick(@NonNull Activity activity, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, boolean capture, int requestCode) {
//        pick(Matisse.from(activity), mimeTypes, maxSelectable, activity.getPackageName() + ".fileProvider", capture, requestCode);
//    }
//
//    public static void pick(@NonNull Fragment fragment, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, boolean capture) {
//        pick(fragment, mimeTypes, maxSelectable, capture, -1);
//    }
//
//    public static void pick(@NonNull Fragment fragment, MimeTypes mimeTypes, @IntRange(from = 1, to = 9) int maxSelectable, boolean capture, int requestCode) {
//        pick(Matisse.from(fragment), mimeTypes, maxSelectable, fragment.requireActivity().getPackageName() + ".fileProvider", capture, requestCode);
//    }
//
//    private static void pick(Matisse matisse, MimeTypes mimeTypes, int maxSelectable, String authority, boolean capture, int requestCode) {
//        matisse.choose(mimeTypes == MimeTypes.IMAGE ? MimeType.ofImage() : mimeTypes == MimeTypes.VIDEO ? MimeType.ofVideo() : MimeType.ofAll())
//                .countable(maxSelectable != 1)
//                .showPreview(false)
//                .capture(capture)
//                .captureStrategy(new CaptureStrategy(true, authority))
//                .maxSelectable(maxSelectable)
//                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                .thumbnailScale(0.85f)
//                .imageEngine(new GlideEngine())
//                .originalEnable(false)
//                .maxOriginalSize(10)
//                .autoHideToolbarOnSingleTap(true)
//                .showSingleMediaType(true)
//                .forResult(requestCode > 0 ? requestCode : PICKER_REQUEST);
//
//    }

    public static void pick(Activity context, MimeTypes mimeTypes, int maxSelectable, boolean showCamera, int requestCode) {
        pick(EasyPhotos.createAlbum(context, showCamera, false, GlideEngine.getInstance()),
                context.getPackageName() + ".fileProvider", maxSelectable, mimeTypes, requestCode);
    }

    public static void pick(Fragment context, MimeTypes mimeTypes, int maxSelectable, boolean showCamera, int requestCode) {
        pick(EasyPhotos.createAlbum(context, showCamera, false, GlideEngine.getInstance()),
                context.requireActivity().getPackageName() + ".fileProvider", maxSelectable, mimeTypes, requestCode);
    }

    public static void pick(android.app.Fragment context, MimeTypes mimeTypes, int maxSelectable, boolean showCamera, int requestCode) {
        pick(EasyPhotos.createAlbum(context, showCamera, false, GlideEngine.getInstance()),
                context.getActivity().getPackageName() + ".fileProvider", maxSelectable, mimeTypes, requestCode);
    }

    private static void pick(AlbumBuilder builder, String authority, int maxSelectable, MimeTypes mimeTypes, int requestCode) {
        builder.setFileProviderAuthority(authority)
                .setCleanMenu(false)
                .setGif(false)
                .setPuzzleMenu(false)
//                .setOriginalMenu(false, false, "")
                .setCameraLocation(Setting.LIST_FIRST)
                .setCleanMenu(false)
//                .complexSelector(true, maxSelectable, maxSelectable)
                .setCount(maxSelectable);
        if (mimeTypes == MimeTypes.VIDEO) {
            builder.onlyVideo();
        } else {
            builder.setVideo(mimeTypes != MimeTypes.IMAGE);
        }
        builder.start(requestCode > 0 ? requestCode : PICKER_REQUEST);
    }

    public static String easyPath(Intent data) {
        if (data != null) {
            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
            if (resultPhotos != null && !resultPhotos.isEmpty()) {
                return resultPhotos.get(0).path;
            }
        }
        return null;
    }

    public static Uri easyUri(Intent data) {
        String path = easyPath(data);
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                return Uri.fromFile(file);
            }
        }
        return null;
    }

    public static List<String> easyPaths(Intent data) {
        if (data != null) {
            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
            if (resultPhotos != null && !resultPhotos.isEmpty()) {
                List<String> paths = new ArrayList<>();
                for (Photo photo : resultPhotos) {
                    paths.add(photo.path);
                }
                if (!paths.isEmpty()) {
                    return paths;
                }
            }
        }
        return null;
    }

    public static List<Uri> easyUris(Intent data) {
        List<String> paths = easyPaths(data);
        if (paths != null) {
            List<Uri> uris = new ArrayList<>();
            for (String path : paths) {
                File file = new File(path);
                if (file.exists()) {
                    uris.add(Uri.fromFile(file));
                }
            }
            if (!uris.isEmpty()) {
                return uris;
            }
        }
        return null;
    }

//    public static List<String> obtainPathResult(Intent data) {
//        return Matisse.obtainPathResult(data);
//    }
//
//    public static List<Uri> obtainResult(Intent data) {
//        return Matisse.obtainResult(data);
//    }
}
