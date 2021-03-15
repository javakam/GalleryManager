package ando.gallery.listener;

import java.util.List;

/**
 * @author：luck
 * 2020-04-16 12:42
 * OnQueryMediaResultListener
 */
public interface OnQueryDataResultListener<T> {
    /**
     * Query to complete The callback listener
     *
     * @param data        The data source
     * @param currentPage The page number
     * @param isHasMore   Is there more
     */
    void onComplete(List<T> data, int currentPage, boolean isHasMore);
}
