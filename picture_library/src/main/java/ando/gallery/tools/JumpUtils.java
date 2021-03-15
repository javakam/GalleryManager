package ando.gallery.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import ando.gallery.PicturePreviewActivity;
import ando.gallery.PictureSelectorPreviewWeChatStyleActivity;
import ando.gallery.PictureVideoPlayActivity;

public class JumpUtils {
    /**
     * 启动视频播放页面
     */
    public static void startPictureVideoPlayActivity(Context context, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(context, PictureVideoPlayActivity.class);
            intent.putExtras(bundle);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }

    /**
     * 启动预览界面
     */
    public static void startPicturePreviewActivity(Context context, boolean isWeChatStyle, Bundle bundle, int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Intent intent = new Intent();
            intent.setClass(context, isWeChatStyle ? PictureSelectorPreviewWeChatStyleActivity.class : PicturePreviewActivity.class);
            intent.putExtras(bundle);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                ((Activity) context).startActivityForResult(intent, requestCode);
            }
        }
    }
}
