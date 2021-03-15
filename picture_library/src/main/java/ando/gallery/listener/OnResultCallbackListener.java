package ando.gallery.listener;

import android.app.Activity;

import java.util.List;

public interface OnResultCallbackListener {
    default void onStart(Activity activity) {
    }

    /**
     * return LocalMedia result
     */
    void onResult(List<?> result);

    /**
     * Cancel
     */
    void onCancel();
}