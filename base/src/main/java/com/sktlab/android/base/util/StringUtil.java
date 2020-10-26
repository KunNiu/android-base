package com.sktlab.android.base.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    //数字
    private static final String REG_NUMBER = ".*\\d+.*";
    private static final String REG_LETTER = ".*[a-zA-Z]+.*";
    private static final String REG_MARK = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";

    public static boolean validatePassword(@NotNull String password) {
//        Pattern number = Pattern.compile(REG_NUMBER);
//        Pattern letter = Pattern.compile(REG_LETTER);
//        Pattern mark = Pattern.compile(REG_MARK);
//        Matcher mNumber = number.matcher(password);
//        Matcher mLetter = letter.matcher(password);
//        Matcher mMark = mark.matcher(password);
        return /*mNumber.matches() && mLetter.matches() && mMark.matches() && */password.length() >= 6;
    }

    public static boolean isEmpty(final CharSequence cs) {
        return StringUtils.isEmpty(cs);
    }

    public static boolean validateEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public static String substringAfterLast(final String str, final String separator) {
        return StringUtils.substringAfterLast(str, separator);
    }

    public static String numberSimplify(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        if (number < 10000) {
            return String.format("%.1f", number / 1000f) + "K";
        }
        return String.format("%.1f", number / 10000f) + "W";
    }
}
