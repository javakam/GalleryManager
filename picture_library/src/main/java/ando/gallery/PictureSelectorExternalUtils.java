package ando.gallery;

import android.content.Context;
import android.net.Uri;

import androidx.exifinterface.media.ExifInterface;

import java.io.InputStream;

import ando.gallery.config.PictureMimeType;
import ando.gallery.tools.PictureFileUtils;
import ando.gallery.tools.SdkVersionUtils;

/**
 * @author：luck
 * 2020-04-12 13:13
 * PictureSelector对外提供的一些方法
 */
public class PictureSelectorExternalUtils {
    /**
     * 获取ExifInterface
     */
    public static ExifInterface getExifInterface(Context context, String url) {
        ExifInterface exifInterface = null;
        InputStream inputStream = null;
        try {
            if (SdkVersionUtils.checkedAndroid_Q() && PictureMimeType.isContent(url)) {
                inputStream = context.getContentResolver().openInputStream(Uri.parse(url));
                if (inputStream != null) {
                    exifInterface = new ExifInterface(inputStream);
                }
            } else {
                exifInterface = new ExifInterface(url);
            }
            return exifInterface;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PictureFileUtils.close(inputStream);
        }
        return null;
    }
}
