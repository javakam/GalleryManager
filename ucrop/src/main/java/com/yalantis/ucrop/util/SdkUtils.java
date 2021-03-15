package com.yalantis.ucrop.util;

import android.os.Build;

/**
 * @author：luck
 * 2020-01-08 20:53
 * SdkUtils
 */
public class SdkUtils {
    /**
     * 判断是否是Android Q版本
     *
     *
     */
    public static boolean isQ() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
}
