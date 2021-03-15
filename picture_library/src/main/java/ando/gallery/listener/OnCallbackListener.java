package ando.gallery.listener;

/**
 * @author：luck
 * 2020/4/24 11:48 AM
 * OnCallbackListener
 */
public interface OnCallbackListener<T> {
    /**
     * @param data
     */
    void onCall(T data);
}
