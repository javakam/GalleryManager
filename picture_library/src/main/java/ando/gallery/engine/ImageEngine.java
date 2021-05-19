package ando.gallery.engine;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ando.gallery.listener.OnImageCompleteCallback;
import ando.gallery.widget.longimage.SubsamplingScaleImageView;

public interface ImageEngine {
    /**
     * Loading image
     */
    void loadImage(@Nullable String url, @NonNull ImageView imageView);

    /**
     * Loading image
     */
    void loadImage(@Nullable String url, @NonNull ImageView imageView, SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback);

    /**
     * Load album catalog pictures
     */
    void loadFolderImage(@Nullable String url, @NonNull ImageView imageView);

    /**
     * Load GIF image
     */
    void loadAsGifImage(@Nullable String url, @NonNull ImageView imageView);

    /**
     * Load picture list picture
     */
    void loadGridImage(@Nullable String url, @NonNull ImageView imageView);
}
