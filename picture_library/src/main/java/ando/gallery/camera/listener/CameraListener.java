package ando.gallery.camera.listener;

import androidx.annotation.NonNull;

import java.io.File;

/**
 * 相机回调监听
 */
public interface CameraListener {
    /**
     * 拍照成功返回
     */
    void onPictureSuccess(@NonNull File file);

    /**
     * 录像成功返回
     */
    void onRecordSuccess(@NonNull File file);

    /**
     * 使用相机出错
     */
    void onError(int videoCaptureError, String message, Throwable cause);
}