package ando.gallery.listener;

import java.util.List;

/**
 * @author：luck
 * 2020-03-26 10:34
 * OnPhotoSelectChangedListener
 */
public interface OnPhotoSelectChangedListener<T> {
    /**
     * Photo callback
     */
    void onTakePhoto();

    /**
     * Selected LocalMedia callback
     *
     * @param data
     */
    void onChange(List<T> data);

    /**
     * Image preview callback
     *
     * @param data
     * @param position
     */
    void onPictureClick(T data, int position);
}
