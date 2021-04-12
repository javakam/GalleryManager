package ando.gallery.core;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import ando.gallery.app.IApp;
import ando.gallery.app.PictureAppMaster;
import ando.gallery.engine.PictureSelectorEngine;

public class App extends Application implements IApp, CameraXConfig.Provider {

    @Override
    public void onCreate() {
        super.onCreate();
        PictureAppMaster.getInstance().setApp(this);
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return Camera2Config.defaultConfig();
    }
}
