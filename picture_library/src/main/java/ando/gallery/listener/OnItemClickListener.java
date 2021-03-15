package ando.gallery.listener;

import android.view.View;

/**
 * @author：luck
 * 2020-03-26 10:50
 * OnItemClickListener
 */
public interface OnItemClickListener {
    /**
     * Item click event
     *
     * @param v
     * @param position
     */
    void onItemClick(View v, int position);
}
