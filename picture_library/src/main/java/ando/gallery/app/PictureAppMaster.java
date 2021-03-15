package ando.gallery.app;

import android.content.Context;

import ando.gallery.engine.PictureSelectorEngine;

/**
 * @author：luck
 * 2019-12-03 15:12
 * PictureAppMaster
 */
public class PictureAppMaster implements IApp {


    @Override
    public Context getAppContext() {
        if (app == null) {
            return null;
        }
        return app.getAppContext();
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        if (app == null) {
            return null;
        }
        return app.getPictureSelectorEngine();
    }

    private PictureAppMaster() {
    }

    private static PictureAppMaster mInstance;

    public static PictureAppMaster getInstance() {
        if (mInstance == null) {
            synchronized (PictureAppMaster.class) {
                if (mInstance == null) {
                    mInstance = new PictureAppMaster();
                }
            }
        }
        return mInstance;
    }

    private IApp app;

    public void setApp(IApp app) {
        this.app = app;
    }

    public IApp getApp() {
        return app;
    }
}
