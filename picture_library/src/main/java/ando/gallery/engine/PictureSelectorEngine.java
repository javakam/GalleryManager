package ando.gallery.engine;

import ando.gallery.entity.LocalMedia;
import ando.gallery.listener.OnResultCallbackListener;

/**
 * @author：luck
 * 2020/4/22 11:36 AM
 * PictureSelectorEngine
 */
public interface PictureSelectorEngine {

    /**
     * Create ImageLoad Engine
     *
     *
     */
    ImageEngine createEngine();

    /**
     * Create Result Listener
     *
     *
     */
    OnResultCallbackListener<LocalMedia> getResultCallbackListener();
}
