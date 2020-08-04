package com.sktlab.android.base.util;

import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CipherUtil {
    private static final int DEFAULT_FLAG = Base64.URL_SAFE | Base64.NO_WRAP;
//    private static final int DEFAULT_FLAG = Base64.NO_WRAP;
//    private static final int DEFAULT_FLAG = Base64.URL_SAFE;
//    private static final int DEFAULT_FLAG = Base64.DEFAULT;

    private static final char[] DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String encodeBase64(byte[] input) {
        return encodeBase64(input, DEFAULT_FLAG);
    }

    public static String encodeBase64(byte[] input, int flag) {
        return Base64.encodeToString(input, flag);
    }

    public static byte[] decodeBase64(String str) {
        return decodeBase64(str, DEFAULT_FLAG);
    }

    public static byte[] decodeBase64(String str, int flag) {
        return Base64.decode(str, flag);
    }

    /**
     * URL 解码
     *
     * @return String
     */
    public static String decodeURL(String str) {
        String result = "";
        if (null == str) {
            return result;
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     *
     * @return String
     */
    public static String encodeURL(String str) {
        String result = "";
        if (null == str) {
            return result;
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String toHexString(@NonNull String data) {
        return toHexString(data.getBytes());
    }

    public static String toHexString(@NonNull byte[] data) {
        char[] out = new char[data.length << 1];
        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return new String(out);
    }

    public static byte[] getSHA256Hash(byte[] data) {
        try {
            MessageDigest sha256 = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256);
            sha256.update(data);
            return sha256.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static byte[] getSHA256Hash(String data) {
        return getSHA256Hash(data.getBytes());
    }
}
