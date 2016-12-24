package com.ericyl.utils.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String getStringFormat(@NonNull Context context, @StringRes int resId, Object... objects) {
        return String.format(context.getString(resId), objects);
    }

    public static String getString(@NonNull Context context, @StringRes int resId) {
        return context.getString(resId);
    }

    public static String[] getString(@NonNull Context context, @StringRes int... resIds) {
        String[] strs = new String[resIds.length];
        for (int i = 0; i < resIds.length; i++) {
            String str = getString(context, resIds[i]);
            strs[i] = str;
        }
        return strs;
    }

    public static String[] match(@NonNull String rule, @NonNull String source, int count) {
        Pattern pattern = Pattern.compile(rule);
        Matcher matcher = pattern.matcher(source);
        String[] result = null;
        while (matcher.find()) {
            result = new String[count];
            for (int i = 1; i <= count; i++) {
                result[i - 1] = matcher.group(i);
            }
        }
        return result;
    }


}
