package ando.gallery.engine;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import ando.gallery.listener.OnImageCompleteCallback;
import ando.gallery.widget.longimage.SubsamplingScaleImageView;

public interface ImageEngine {
    /**
     * Loading image
     */
    void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);

    /**
     * Loading image
     */
    void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback);

    /**
     * Load album catalog pictures
     */
    void loadFolderImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);

    /**
     * Load GIF image
     */
    void loadAsGifImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);

    /**
     * Load picture list picture
     */
    void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView);
}
