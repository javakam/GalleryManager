package ando.gallery.tools;

import android.os.Build;

/**
 * @author：luck
 * 2019-07-17 15:12
 * Android Sdk版本判断
 */
public class SdkVersionUtils {
    /**
     * 判断是否是Android Q版本
     *
     *
     */
    public static boolean checkedAndroid_Q() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }

    /**
     * 判断是否是Android R版本
     *
     *
     */
    public static boolean checkedAndroid_R() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }
}
