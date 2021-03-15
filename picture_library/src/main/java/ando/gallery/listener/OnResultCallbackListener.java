package ando.gallery.listener;

import android.app.Activity;

import java.util.List;

import ando.gallery.entity.LocalMedia;

public interface OnResultCallbackListener {
    default void onStart(Activity activity) {
    }

    /**
     * return LocalMedia result
     */
    void onResult(List<LocalMedia> result);

    /**
     * Cancel
     */
    void onCancel();
}