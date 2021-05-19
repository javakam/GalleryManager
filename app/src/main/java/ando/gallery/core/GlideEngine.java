package ando.gallery.core;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;

import ando.gallery.engine.ImageEngine;
import ando.gallery.listener.OnImageCompleteCallback;
import ando.gallery.tools.MediaUtils;
import ando.gallery.widget.longimage.ImageSource;
import ando.gallery.widget.longimage.ImageViewState;
import ando.gallery.widget.longimage.SubsamplingScaleImageView;

public class GlideEngine implements ImageEngine {

    /**
     * 加载图片
     */
    @Override
    public void loadImage(@Nullable String url, @NonNull ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    /**
     * 加载网络图片适配长图方案
     * # 注意：此方法只有加载网络图片才会回调
     *
     * @param callback 网络图片加载回调监听 {link after version 2.5.1 Please use the #OnImageCompleteCallback#}
     */
    @Override
    public void loadImage(@Nullable String url,
                          @NonNull ImageView imageView,
                          SubsamplingScaleImageView longImageView, OnImageCompleteCallback callback) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .into(new ImageViewTarget<Bitmap>(imageView) {
                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (callback != null) {
                            callback.onShowLoading();
                        }
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        if (callback != null) {
                            callback.onHideLoading();
                        }
                        if (resource != null) {
                            boolean eqLongImage = MediaUtils.isLongImg(resource.getWidth(),
                                    resource.getHeight());
                            longImageView.setVisibility(eqLongImage ? View.VISIBLE : View.GONE);
                            imageView.setVisibility(eqLongImage ? View.GONE : View.VISIBLE);
                            if (eqLongImage) {
                                // 加载长图
                                longImageView.setQuickScaleEnabled(true);
                                longImageView.setZoomEnabled(true);
                                longImageView.setDoubleTapZoomDuration(100);
                                longImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
                                longImageView.setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER);
                                longImageView.setImage(ImageSource.bitmap(resource),
                                        new ImageViewState(0, new PointF(0, 0), 0));
                            } else {
                                // 普通图片
                                imageView.setImageBitmap(resource);
                            }
                        }
                    }
                });
    }

    /**
     * 加载相册目录
     */
    @Override
    public void loadFolderImage(@Nullable String url, @NonNull ImageView imageView) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .override(180, 180)
                .centerCrop()
                .sizeMultiplier(0.5f)
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.
                                        create(imageView.getContext().getResources(), resource);
                        circularBitmapDrawable.setCornerRadius(8);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    /**
     * 加载gif
     */
    @Override
    public void loadAsGifImage(@Nullable String url,
                               @NonNull ImageView imageView) {
        Glide.with(imageView.getContext())
                .asGif()
                .load(url)
                .into(imageView);
    }

    /**
     * 加载图片列表图片
     */
    @Override
    public void loadGridImage(@Nullable String url, @NonNull ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(url)
                .override(200, 200)
                .centerCrop()
                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(imageView);
    }

    private GlideEngine() {
    }

    private static GlideEngine instance;

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }
}
