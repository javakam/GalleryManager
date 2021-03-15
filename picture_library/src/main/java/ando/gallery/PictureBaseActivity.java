package ando.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ando.gallery.app.PictureAppMaster;
import ando.gallery.compress.Luban;
import ando.gallery.compress.OnCompressListener;
import ando.gallery.config.PictureConfig;
import ando.gallery.config.PictureMimeType;
import ando.gallery.config.PictureSelectionConfig;
import ando.gallery.dialog.PictureCustomDialog;
import ando.gallery.dialog.PictureLoadingDialog;
import ando.gallery.engine.PictureSelectorEngine;
import ando.gallery.entity.LocalMedia;
import ando.gallery.entity.LocalMediaFolder;
import ando.gallery.immersive.ImmersiveManage;
import ando.gallery.immersive.NavBarUtils;
import ando.gallery.language.PictureLanguageUtils;
import ando.gallery.model.LocalMediaPageLoader;
import ando.gallery.thread.PictureThreadUtils;
import ando.gallery.tools.AndroidQTransformUtils;
import ando.gallery.tools.AttrsUtils;
import ando.gallery.tools.MediaUtils;
import ando.gallery.tools.PictureFileUtils;
import ando.gallery.tools.SdkVersionUtils;
import ando.gallery.tools.StringUtils;
import ando.gallery.tools.ToastUtils;
import ando.gallery.tools.VoiceUtils;

public abstract class PictureBaseActivity extends AppCompatActivity {
    protected PictureSelectionConfig config;
    protected boolean openWhiteStatusBar, numComplete;
    protected int colorPrimary, colorPrimaryDark;
    protected PictureLoadingDialog mLoadingDialog;
    protected List<LocalMedia> selectionMedias;
    protected Handler mHandler;
    protected View container;
    /**
     * if there more
     */
    protected boolean isHasMore = true;
    /**
     * page
     */
    protected int mPage = 1;
    /**
     * is onSaveInstanceState
     */
    protected boolean isOnSaveInstanceState;

    /**
     * Whether to use immersion, subclasses copy the method to determine whether to use immersion
     */
    @Override
    public boolean isImmersive() {
        return true;
    }

    /**
     * Whether to change the screen direction
     */
    public boolean isRequestedOrientation() {
        return true;
    }

    public void immersive() {
        ImmersiveManage.immersiveAboveAPI23(this
                , colorPrimaryDark
                , colorPrimary
                , openWhiteStatusBar);
    }

    /**
     * get Layout Resources Id
     */
    public abstract int getResourceId();

    /**
     * init Views
     */
    protected void initWidgets() {
    }

    /**
     * init PictureSelector Style
     */
    protected void initPictureSelectorStyle() {
    }

    /**
     * Set CompleteText
     */
    protected void initCompleteText(int startCount) {
    }

    /**
     * Set CompleteText
     */
    protected void initCompleteText(List<LocalMedia> list) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        config = PictureSelectionConfig.getInstance();
        PictureLanguageUtils.setAppLanguage(getContext(), config.language);
        if (!config.camera) {
            setTheme(config.themeStyleId == 0 ? R.style.picture_default_style : config.themeStyleId);
        }
        super.onCreate(savedInstanceState);
        newCreateEngine();
        newCreateResultCallbackListener();
        if (isRequestedOrientation()) {
            setNewRequestedOrientation();
        }
        mHandler = new Handler(Looper.getMainLooper());
        initConfig();
        if (isImmersive()) {
            immersive();
        }
        if (PictureSelectionConfig.uiStyle != null) {
            if (PictureSelectionConfig.uiStyle.picture_navBarColor != 0) {
                NavBarUtils.setNavBarColor(this, PictureSelectionConfig.uiStyle.picture_navBarColor);
            }
        } else if (PictureSelectionConfig.style != null) {
            if (PictureSelectionConfig.style.pictureNavBarColor != 0) {
                NavBarUtils.setNavBarColor(this, PictureSelectionConfig.style.pictureNavBarColor);
            }
        }
        int layoutResID = getResourceId();
        if (layoutResID != 0) {
            setContentView(layoutResID);
        }
        initWidgets();
        initPictureSelectorStyle();
        isOnSaveInstanceState = false;
        if (PictureSelectionConfig.listener != null) {
            PictureSelectionConfig.listener.onStart(this);
        }
    }

    /**
     * Get the image loading engine again, provided that the user implements the IApp interface in the Application
     */
    private void newCreateEngine() {
        if (PictureSelectionConfig.imageEngine == null) {
            PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
            if (baseEngine != null) {
                PictureSelectionConfig.imageEngine = baseEngine.createEngine();
            }
        }
    }

    /**
     * Retrieve the result callback listener, provided that the user implements the IApp interface in the Application
     */
    private void newCreateResultCallbackListener() {
        if (config.isCallbackMode) {
            if (PictureSelectionConfig.listener == null) {
                PictureSelectorEngine baseEngine = PictureAppMaster.getInstance().getPictureSelectorEngine();
                if (baseEngine != null) {
                    PictureSelectionConfig.listener = baseEngine.getResultCallbackListener();
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        if (config == null) {
            super.attachBaseContext(newBase);
        } else {
            super.attachBaseContext(PictureContextWrapper.wrap(newBase, config.language));
        }
    }

    protected void setNewRequestedOrientation() {
        if (config != null && !config.camera) {
            setRequestedOrientation(config.requestedOrientation);
        }
    }

    protected Context getContext() {
        return this;
    }

    private void initConfig() {
        selectionMedias = config.selectionMedias == null ? new ArrayList<>() : config.selectionMedias;
        if (PictureSelectionConfig.uiStyle != null) {
            openWhiteStatusBar = PictureSelectionConfig.uiStyle.picture_statusBarChangeTextColor;
            if (PictureSelectionConfig.uiStyle.picture_top_titleBarBackgroundColor != 0) {
                colorPrimary = PictureSelectionConfig.uiStyle.picture_top_titleBarBackgroundColor;
            }
            if (PictureSelectionConfig.uiStyle.picture_statusBarBackgroundColor != 0) {
                colorPrimaryDark = PictureSelectionConfig.uiStyle.picture_statusBarBackgroundColor;
            }
            numComplete = PictureSelectionConfig.uiStyle.picture_switchSelectTotalStyle;
            config.checkNumMode = PictureSelectionConfig.uiStyle.picture_switchSelectNumberStyle;
        } else if (PictureSelectionConfig.style != null) {
            openWhiteStatusBar = PictureSelectionConfig.style.isChangeStatusBarFontColor;
            if (PictureSelectionConfig.style.pictureTitleBarBackgroundColor != 0) {
                colorPrimary = PictureSelectionConfig.style.pictureTitleBarBackgroundColor;
            }
            if (PictureSelectionConfig.style.pictureStatusBarColor != 0) {
                colorPrimaryDark = PictureSelectionConfig.style.pictureStatusBarColor;
            }
            numComplete = PictureSelectionConfig.style.isOpenCompletedNumStyle;
            config.checkNumMode = PictureSelectionConfig.style.isOpenCheckNumStyle;
        } else {
            openWhiteStatusBar = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_statusFontColor);
            numComplete = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_style_numComplete);
            config.checkNumMode = AttrsUtils.getTypeValueBoolean(this, R.attr.picture_style_checkNumMode);
            colorPrimary = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimary);
            colorPrimaryDark = AttrsUtils.getTypeValueColor(this, R.attr.colorPrimaryDark);
        }
        if (config.openClickSound) {
            VoiceUtils.getInstance().init(getContext());
        }
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        isOnSaveInstanceState = true;
        outState.putParcelable(PictureConfig.EXTRA_CONFIG, config);
    }

    /**
     * loading dialog
     */
    protected void showPleaseDialog() {
        try {
            if (!isFinishing()) {
                if (mLoadingDialog == null) {
                    mLoadingDialog = new PictureLoadingDialog(getContext());
                }
                if (mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
                mLoadingDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dismiss dialog
     */
    protected void dismissDialog() {
        if (!isFinishing()) {
            try {
                if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
                    mLoadingDialog.dismiss();
                }
            } catch (Exception e) {
                mLoadingDialog = null;
                e.printStackTrace();
            }
        }
    }

    /**
     * compressImage
     */
    protected void compressImage(final List<LocalMedia> result) {
        showPleaseDialog();
        compressToLuban(result);
    }

    /**
     * compress
     */
    private void compressToLuban(List<LocalMedia> result) {
        if (config.synOrAsy) {
            PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<List<File>>() {
                @Override
                public List<File> doInBackground() throws Exception {
                    return Luban.with(getContext())
                            .loadMediaData(result)
                            .isCamera(config.camera)
                            .setTargetDir(config.compressSavePath)
                            .setCompressQuality(config.compressQuality)
                            .setFocusAlpha(config.focusAlpha)
                            .setNewCompressFileName(config.renameCompressFileName)
                            .ignoreBy(config.minimumCompressSize).get();
                }

                @Override
                public void onSuccess(List<File> files) {
                    if (files != null && files.size() > 0 && files.size() == result.size()) {
                        handleCompressCallBack(result, files);
                    } else {
                        onResult(result);
                    }
                }
            });
        } else {
            Luban.with(this)
                    .loadMediaData(result)
                    .ignoreBy(config.minimumCompressSize)
                    .isCamera(config.camera)
                    .setCompressQuality(config.compressQuality)
                    .setTargetDir(config.compressSavePath)
                    .setFocusAlpha(config.focusAlpha)
                    .setNewCompressFileName(config.renameCompressFileName)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(List<LocalMedia> list) {
                            onResult(list);
                        }

                        @Override
                        public void onError(Throwable e) {
                            onResult(result);
                        }
                    }).launch();
        }
    }

    /**
     * handleCompressCallBack
     */
    private void handleCompressCallBack(List<LocalMedia> images, List<File> files) {
        if (images == null || files == null) {
            exit();
            return;
        }
        boolean isAndroidQ = SdkVersionUtils.checkedAndroid_Q();
        int size = images.size();
        if (files.size() == size) {
            for (int i = 0, j = size; i < j; i++) {
                File file = files.get(i);
                if (file == null) {
                    continue;
                }
                String path = file.getAbsolutePath();
                LocalMedia image = images.get(i);
                boolean http = PictureMimeType.isHasHttp(path);
                boolean flag = !TextUtils.isEmpty(path) && http;
                boolean isHasVideo = PictureMimeType.isHasVideo(image.getMimeType());
                image.setCompressed(!isHasVideo && !flag);
                image.setCompressPath(isHasVideo || flag ? null : path);
                if (isAndroidQ) {
                    image.setAndroidQToPath(image.getCompressPath());
                }
            }
        }
        onResult(images);
    }

    /**
     * compress or callback
     */
    protected void handlerResult(List<LocalMedia> result) {
        if (config.isCompress
                && !config.isCheckOriginalImage) {
            compressImage(result);
        } else {
            onResult(result);
        }
    }


    /**
     * If you don't have any albums, first create a camera film folder to come out
     */
    protected void createNewFolder(List<LocalMediaFolder> folders) {
        if (folders.size() == 0) {
            // 没有相册 先创建一个最近相册出来
            LocalMediaFolder newFolder = new LocalMediaFolder();
            String folderName = getString(R.string.picture_camera_roll);
            newFolder.setName(folderName);
            newFolder.setFirstImagePath("");
            newFolder.setCameraFolder(true);
            newFolder.setBucketId(-1);
            newFolder.setChecked(true);
            folders.add(newFolder);
        }
    }

    /**
     * Insert the image into the camera folder
     */
    protected LocalMediaFolder getImageFolder(String path, String realPath, List<LocalMediaFolder> imageFolders) {
        File imageFile = new File(PictureMimeType.isContent(path) ? realPath : path);
        File folderFile = imageFile.getParentFile();
        for (LocalMediaFolder folder : imageFolders) {
            if (folderFile != null && folder.getName().equals(folderFile.getName())) {
                return folder;
            }
        }
        LocalMediaFolder newFolder = new LocalMediaFolder();
        newFolder.setName(folderFile != null ? folderFile.getName() : "");
        newFolder.setFirstImagePath(path);
        imageFolders.add(newFolder);
        return newFolder;
    }

    /**
     * return image result
     */
    protected void onResult(List<LocalMedia> images) {
        boolean isAndroidQ = SdkVersionUtils.checkedAndroid_Q();
        if (isAndroidQ && config.isAndroidQTransform) {
            showPleaseDialog();
            onResultToAndroidAsy(images);
        } else {
            dismissDialog();
            if (config.camera
                    && config.selectionMode == PictureConfig.MULTIPLE
                    && selectionMedias != null) {
                images.addAll(images.size() > 0 ? images.size() - 1 : 0, selectionMedias);
            }
            if (config.isCheckOriginalImage) {
                int size = images.size();
                for (int i = 0; i < size; i++) {
                    LocalMedia media = images.get(i);
                    media.setOriginal(true);
                    media.setOriginalPath(media.getPath());
                }
            }
            if (PictureSelectionConfig.listener != null) {
                PictureSelectionConfig.listener.onResult(images);
            } else {
                Intent intent = PictureSelector.putIntentResult(images);
                setResult(RESULT_OK, intent);
            }
            exit();
        }
    }

    /**
     * Android Q
     */
    private void onResultToAndroidAsy(List<LocalMedia> images) {
        PictureThreadUtils.executeByIo(new PictureThreadUtils.SimpleTask<List<LocalMedia>>() {
            @Override
            public List<LocalMedia> doInBackground() {
                int size = images.size();
                for (int i = 0; i < size; i++) {
                    LocalMedia media = images.get(i);
                    if (media == null || TextUtils.isEmpty(media.getPath())) {
                        continue;
                    }
                    boolean isCopyAndroidQToPath = !media.isCut()
                            && !media.isCompressed()
                            && TextUtils.isEmpty(media.getAndroidQToPath());
                    if (isCopyAndroidQToPath && PictureMimeType.isContent(media.getPath())) {
                        if (!PictureMimeType.isHasHttp(media.getPath())) {
                            String AndroidQToPath = AndroidQTransformUtils.copyPathToAndroidQ(getContext(),
                                    media.getPath(), media.getWidth(), media.getHeight(), media.getMimeType(), config.cameraFileName);
                            media.setAndroidQToPath(AndroidQToPath);
                        }
                    } else if (media.isCut() && media.isCompressed()) {
                        media.setAndroidQToPath(media.getCompressPath());
                    }
                    if (config.isCheckOriginalImage) {
                        media.setOriginal(true);
                        media.setOriginalPath(media.getAndroidQToPath());
                    }
                }
                return images;
            }

            @Override
            public void onSuccess(List<LocalMedia> images) {
                dismissDialog();
                if (images != null) {
                    if (config.camera
                            && config.selectionMode == PictureConfig.MULTIPLE
                            && selectionMedias != null) {
                        images.addAll(images.size() > 0 ? images.size() - 1 : 0, selectionMedias);
                    }
                    if (PictureSelectionConfig.listener != null) {
                        PictureSelectionConfig.listener.onResult(images);
                    } else {
                        Intent intent = PictureSelector.putIntentResult(images);
                        setResult(RESULT_OK, intent);
                    }
                    exit();
                }
            }
        });
    }

    /**
     * Close Activity
     */
    protected void exit() {
        finish();
        if (config.camera) {
            overridePendingTransition(0, R.anim.picture_anim_fade_out);
            if (getContext() instanceof PictureSelectorCameraEmptyActivity
                    || getContext() instanceof PictureCustomCameraActivity) {
                releaseResultListener();
            }
        } else {
            overridePendingTransition(0,
                    PictureSelectionConfig.windowAnimationStyle.activityExitAnimation);
            if (getContext() instanceof PictureSelectorActivity) {
                releaseResultListener();
                if (config.openClickSound) {
                    VoiceUtils.getInstance().releaseSoundPool();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
        super.onDestroy();
    }

    /**
     * start to camera、preview、crop
     */
    protected void startOpenCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            Uri imageUri;
            String cameraFileName = null;
            int chooseMode = config.chooseMode == PictureConfig.TYPE_ALL ? PictureConfig.TYPE_IMAGE : config.chooseMode;
            if (!TextUtils.isEmpty(config.cameraFileName)) {
                boolean isSuffixOfImage = PictureMimeType.isSuffixOfImage(config.cameraFileName);
                config.cameraFileName = !isSuffixOfImage ? StringUtils.renameSuffix(config.cameraFileName, PictureMimeType.JPEG) : config.cameraFileName;
                cameraFileName = config.camera ? config.cameraFileName : StringUtils.rename(config.cameraFileName);
            }
            if (SdkVersionUtils.checkedAndroid_Q()) {
                if (TextUtils.isEmpty(config.outPutCameraPath)) {
                    imageUri = MediaUtils.createImageUri(this, config.cameraFileName, config.suffixType);
                } else {
                    File cameraFile = PictureFileUtils.createCameraFile(this,
                            chooseMode, cameraFileName, config.suffixType, config.outPutCameraPath);
                    config.cameraPath = cameraFile.getAbsolutePath();
                    imageUri = PictureFileUtils.parUri(this, cameraFile);
                }
                if (imageUri != null) {
                    config.cameraPath = imageUri.toString();
                }
            } else {
                File cameraFile = PictureFileUtils.createCameraFile(this, chooseMode, cameraFileName, config.suffixType, config.outPutCameraPath);
                config.cameraPath = cameraFile.getAbsolutePath();
                imageUri = PictureFileUtils.parUri(this, cameraFile);
            }
            if (imageUri == null) {
                ToastUtils.s(getContext(), "open is camera error，the uri is empty ");
                if (config.camera) {
                    exit();
                }
                return;
            }
            config.cameraMimeType = PictureMimeType.ofImage();
            if (config.isCameraAroundState) {
                cameraIntent.putExtra(PictureConfig.CAMERA_FACING, PictureConfig.CAMERA_BEFORE);
            }
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }


    /**
     * start to camera、video
     */
    protected void startOpenCameraVideo() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            Uri videoUri;
            String cameraFileName = null;
            int chooseMode = config.chooseMode == PictureConfig.TYPE_ALL ? PictureConfig.TYPE_VIDEO : config.chooseMode;
            if (!TextUtils.isEmpty(config.cameraFileName)) {
                boolean isSuffixOfImage = PictureMimeType.isSuffixOfImage(config.cameraFileName);
                config.cameraFileName = isSuffixOfImage ? StringUtils.renameSuffix(config.cameraFileName, PictureMimeType.MP4) : config.cameraFileName;
                cameraFileName = config.camera ? config.cameraFileName : StringUtils.rename(config.cameraFileName);
            }
            if (SdkVersionUtils.checkedAndroid_Q()) {
                if (TextUtils.isEmpty(config.outPutCameraPath)) {
                    videoUri = MediaUtils.createVideoUri(this, config.cameraFileName, config.suffixType);
                } else {
                    File cameraFile = PictureFileUtils.createCameraFile(this, chooseMode, cameraFileName, config.suffixType, config.outPutCameraPath);
                    config.cameraPath = cameraFile.getAbsolutePath();
                    videoUri = PictureFileUtils.parUri(this, cameraFile);
                }
                if (videoUri != null) {
                    config.cameraPath = videoUri.toString();
                }
            } else {
                File cameraFile = PictureFileUtils.createCameraFile(this, chooseMode, cameraFileName, config.suffixType, config.outPutCameraPath);
                config.cameraPath = cameraFile.getAbsolutePath();
                videoUri = PictureFileUtils.parUri(this, cameraFile);
            }
            if (videoUri == null) {
                ToastUtils.s(getContext(), "open is camera error，the uri is empty ");
                if (config.camera) {
                    exit();
                }
                return;
            }
            config.cameraMimeType = PictureMimeType.ofVideo();
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
            if (config.isCameraAroundState) {
                cameraIntent.putExtra(PictureConfig.CAMERA_FACING, PictureConfig.CAMERA_BEFORE);
            }
            cameraIntent.putExtra(PictureConfig.EXTRA_QUICK_CAPTURE, config.isQuickCapture);
            cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, config.recordVideoSecond);
            cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, config.videoQuality);
            startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
        }
    }

    /**
     * Release listener
     */
    private void releaseResultListener() {
        if (config != null) {
            PictureSelectionConfig.destroy();
            LocalMediaPageLoader.setInstanceNull();
            PictureThreadUtils.cancel(PictureThreadUtils.getIoPool());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PictureConfig.APPLY_AUDIO_PERMISSIONS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, PictureConfig.REQUEST_CAMERA);
                }
            } else {
                ToastUtils.s(getContext(), getString(R.string.picture_audio));
            }
        }
    }

    /**
     * showPermissionsDialog
     *
     * @param isCamera
     * @param errorMsg
     */
    protected void showPermissionsDialog(boolean isCamera, String errorMsg) {

    }

    /**
     * Dialog
     *
     * @param content
     */
    protected void showPromptDialog(String content) {
        if (!isFinishing()) {
            PictureCustomDialog dialog = new PictureCustomDialog(getContext(), R.layout.picture_prompt_dialog);
            TextView btnOk = dialog.findViewById(R.id.btnOk);
            TextView tvContent = dialog.findViewById(R.id.tv_content);
            tvContent.setText(content);
            btnOk.setOnClickListener(v -> {
                if (!isFinishing()) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    /**
     * sort
     *
     * @param imageFolders
     */
    protected void sortFolder(List<LocalMediaFolder> imageFolders) {
        Collections.sort(imageFolders, (lhs, rhs) -> {
            if (lhs.getData() == null || rhs.getData() == null) {
                return 0;
            }
            int lSize = lhs.getImageNum();
            int rSize = rhs.getImageNum();
            return Integer.compare(rSize, lSize);
        });
    }
}
