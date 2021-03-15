package ando.gallery.engine;

import ando.gallery.listener.OnResultCallbackListener;

public interface PictureSelectorEngine {

    /**
     * Create ImageLoad Engine
     */
    ImageEngine createEngine();

    /**
     * Create Result Listener
     */
    OnResultCallbackListener getResultCallbackListener();
}