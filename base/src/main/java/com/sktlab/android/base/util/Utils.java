package com.sktlab.android.base.util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.StringRes;
import androidx.core.graphics.ColorUtils;
import androidx.core.util.Pair;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sktlab.android.base.R;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;

public class Utils {
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    public static MediaType guessMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        fileName = fileName.replace("#", "");
        String contentType = fileNameMap.getContentTypeFor(fileName);
        if (contentType == null) {
            return MediaType.parse("application/octet-stream");
        }
        return MediaType.parse(contentType);
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static byte[] file2Bytes(File file) {
        RandomAccessFile rf = null;
        byte[] data = null;
        try {
            rf = new RandomAccessFile(file, "r");
            data = new byte[(int) rf.length()];
            rf.readFully(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rf != null) {
                    rf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static boolean isPhoneNumberValid(String phoneNumber, String countryCode) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, countryCode);
            return phoneNumberUtil.isValidNumber(numberProto);
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static class TimeAgo {
        private int ago;
        @StringRes
        private int unitRes;

        TimeAgo(int ago, int unitRes) {
            this.ago = ago;
            this.unitRes = unitRes;
        }

        public int getAgo() {
            return ago;
        }

        public int getUnitRes() {
            return unitRes;
        }
    }

    public static TimeAgo howLongAgo(long timestamp) {
        long current = System.currentTimeMillis() / 1000;
        long ago = current - timestamp;
        if (ago < 60) {
            return new TimeAgo((int) ago, R.string.second);
        }
        if (ago < 60 * 60) {
            return new TimeAgo((int) ago / 60, R.string.minute);
        }
        if (ago < 60 * 60 * 24) {
            return new TimeAgo((int) ago / 60 / 60, R.string.hour);
        }
        if (ago < 60 * 60 * 24 * 7) {
            return new TimeAgo((int) ago / 60 / 60 / 24, R.string.day);
        }
        if (ago < 60 * 60 * 24 * 7 * 4) {
            return new TimeAgo((int) ago / 60 / 60 / 24 / 7, R.string.week);
        }
        if (ago < 60 * 60 * 24 * 7 * 4 * 12) {
            return new TimeAgo((int) ago / 60 / 60 / 24 / 7 / 4, R.string.month);
        }
        return new TimeAgo((int) ago / 60 / 60 / 24 / 7 / 4 / 12, R.string.year);
    }

    public static Pair<Integer, Integer> getMeasurementFromLocalPath(String path) {
        File file = new File(path);
        if (file.exists() && file.isFile()) {
            String type = guessMimeType(file.getName()).type();
            if (type.equals("image")) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                return Pair.create(options.outWidth, options.outHeight);
            } else if (type.equals("video")) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                String widthS = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
                String heightS = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
                retriever.release();
                return Pair.create(Integer.valueOf(widthS), Integer.valueOf(heightS));
            }
        }
        return Pair.create(null, null);
    }

}
