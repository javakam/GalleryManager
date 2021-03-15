package ando.gallery.listener;

import android.content.Context;

import ando.gallery.config.PictureSelectionConfig;

/**
 * @author：luck
 * 2020/4/27 3:24 PM
 * OnCustomCameraInterfaceListener
 */
public interface OnCustomCameraInterfaceListener {
    /**
     * Camera Menu
     *
     * @param context
     * @param config
     * @param type
     */
    void onCameraClick(Context context, PictureSelectionConfig config, int type);
}
