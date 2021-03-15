package ando.gallery.app;

import android.content.Context;

import ando.gallery.engine.PictureSelectorEngine;

/**
 * @author：luck
 * 2019-12-03 15:14
 * IApp
 */
public interface IApp {
    /**
     * Application
     *
     *
     */
    Context getAppContext();

    /**
     * PictureSelectorEngine
     *
     *
     */
    PictureSelectorEngine getPictureSelectorEngine();
}
