package ando.gallery.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import ando.gallery.R;
import ando.gallery.config.PictureMimeType;

import java.util.regex.Pattern;

public class StringUtils {

    public static void tempTextFont(TextView tv, int mimeType) {
        String text = tv.getText().toString().trim();
        String str = tv.getContext().getString(R.string.picture_empty_title);
        String sumText = str + text;
        Spannable placeSpan = new SpannableString(sumText);
        placeSpan.setSpan(new RelativeSizeSpan(0.8f), str.length(), sumText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(placeSpan);
    }

    /**
     * 匹配数值
     */
    public static int stringToInt(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches() ? Integer.valueOf(str) : 0;
    }

    /**
     * 根据类型获取相应的Toast文案
     */
    @SuppressLint("StringFormatMatches")
    public static String getMsg(Context context, String mimeType, int maxSelectNum) {
        if (PictureMimeType.isHasVideo(mimeType)) {
            return context.getString(R.string.picture_message_video_max_num, maxSelectNum);
        }else {
            return context.getString(R.string.picture_message_max_num, maxSelectNum);
        }
    }

    /**
     * 重命名相册拍照
     */
    public static String rename(String fileName) {
        String temp = fileName.substring(0, fileName.lastIndexOf("."));
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        return temp + "_" + DateUtils.getCreateFileName() + suffix;
    }

    /**
     * 重命名后缀
     */
    public static String renameSuffix(String fileName, String suffix) {
        String temp = fileName.substring(0, fileName.lastIndexOf("."));
        return temp + suffix;
    }

    /**
     * getEncryptionValue
     */
    public static String getEncryptionValue(String url, int width, int height) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url).append("_").append(width).append("x").append(height);
        return ValueOf.toString(Math.abs(hash(stringBuilder.hashCode())));
    }

    /**
     * hash
     */
    public static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
