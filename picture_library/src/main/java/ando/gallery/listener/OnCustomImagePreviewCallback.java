package ando.gallery.listener;

import android.content.Context;

import java.util.List;

/**
 * @author：luck 2020/5/31 6:42 PM
 * OnImagePreviewCallback
 */
public interface OnCustomImagePreviewCallback<T> {
    /**
     * Custom Preview Callback
     */
    void onCustomPreviewCallback(Context context, List<T> previewData, int currentPosition);
}