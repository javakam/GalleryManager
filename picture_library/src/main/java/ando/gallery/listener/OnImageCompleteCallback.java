package ando.gallery.listener;

/**
 * @author：luck
 * 2020-01-03 16:43
 * Image load complete callback
 */
public interface OnImageCompleteCallback {
    /**
     * Start loading
     */
    void onShowLoading();

    /**
     * Stop loading
     */
    void onHideLoading();
}
