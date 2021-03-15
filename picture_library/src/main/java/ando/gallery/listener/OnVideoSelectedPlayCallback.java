package ando.gallery.listener;


/**
 * video playback callback
 */
public interface OnVideoSelectedPlayCallback<T> {
    /**
     * Play the video
     */
    void startPlayVideo(T data);
}
