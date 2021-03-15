package ando.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.StyleRes;
import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
import java.util.List;

import ando.gallery.animators.AnimationType;
import ando.gallery.config.PictureConfig;
import ando.gallery.config.PictureMimeType;
import ando.gallery.config.PictureSelectionConfig;
import ando.gallery.config.UCropOptions;
import ando.gallery.engine.ImageEngine;
import ando.gallery.entity.LocalMedia;
import ando.gallery.listener.OnCustomCameraInterfaceListener;
import ando.gallery.listener.OnCustomImagePreviewCallback;
import ando.gallery.listener.OnResultCallbackListener;
import ando.gallery.listener.OnVideoSelectedPlayCallback;
import ando.gallery.style.PictureParameterStyle;
import ando.gallery.style.PictureSelectorUIStyle;
import ando.gallery.style.PictureWindowAnimationStyle;
import ando.gallery.tools.DoubleUtils;
import ando.gallery.tools.SdkVersionUtils;

import static android.os.Build.VERSION_CODES.KITKAT;

public class PictureSelectionModel {
    private final PictureSelectionConfig selectionConfig;
    private final PictureSelector selector;

    public PictureSelectionModel(PictureSelector selector, int chooseMode) {
        this.selector = selector;
        selectionConfig = PictureSelectionConfig.getCleanInstance();
        selectionConfig.chooseMode = chooseMode;
    }

    public PictureSelectionModel(PictureSelector selector, int chooseMode, boolean camera) {
        this.selector = selector;
        selectionConfig = PictureSelectionConfig.getCleanInstance();
        selectionConfig.camera = camera;
        selectionConfig.chooseMode = chooseMode;
    }

    /**
     * @param themeStyleId PictureSelector Theme style
     *                     PictureSelectionModel
     *                     Use { R.style# picture_default_style #picture_Sina_style#picture_white_style#picture_QQ_style#picture_WeChat_style}
     */
    public PictureSelectionModel theme(@StyleRes int themeStyleId) {
        selectionConfig.themeStyleId = themeStyleId;
        return this;
    }

    /**
     * Setting PictureSelector UI Style
     *
     * @param uiStyle <p>
     *                {@link PictureSelectorUIStyle}
     *                </p>
     */
    public PictureSelectionModel setPictureUIStyle(PictureSelectorUIStyle uiStyle) {
        if (uiStyle != null) {
            PictureSelectionConfig.uiStyle = uiStyle;
            if (!selectionConfig.isWeChatStyle) {
                selectionConfig.isWeChatStyle = PictureSelectionConfig.uiStyle.isNewSelectStyle;
            }
        }
        return this;
    }

    public PictureSelectionModel setLanguage(int language) {
        selectionConfig.language = language;
        return this;
    }

    /**
     * Change the desired orientation of this activity.  If the activity
     * is currently in the foreground or otherwise impacting the screen
     * orientation, the screen will immediately be changed (possibly causing
     * the activity to be restarted). Otherwise, this will be used the next
     * time the activity is visible.
     *
     * @param requestedOrientation An orientation constant as used in
     *                             { ActivityInfo# screenOrientation ActivityInfo.screenOrientation}.
     */
    public PictureSelectionModel setRequestedOrientation(int requestedOrientation) {
        selectionConfig.requestedOrientation = requestedOrientation;
        return this;
    }

    /**
     * @param engine Image Load the engine
     */
    public PictureSelectionModel imageEngine(ImageEngine engine) {
        if (PictureSelectionConfig.imageEngine != engine) {
            PictureSelectionConfig.imageEngine = engine;
        }
        return this;
    }

    /**
     * @param selectionMode PictureSelector Selection model and PictureConfig.MULTIPLE or PictureConfig.SINGLE
     */
    public PictureSelectionModel selectionMode(int selectionMode) {
        selectionConfig.selectionMode = selectionMode;
        return this;
    }

    /**
     * @param isWeChatStyle Select style with or without WeChat enabled
     */
    public PictureSelectionModel isWeChatStyle(boolean isWeChatStyle) {
        selectionConfig.isWeChatStyle = isWeChatStyle;
        return this;
    }

    /**
     * @param isUseCustomCamera Whether to use a custom camera
     */
    public PictureSelectionModel isUseCustomCamera(boolean isUseCustomCamera) {
        selectionConfig.isUseCustomCamera = Build.VERSION.SDK_INT > KITKAT && isUseCustomCamera;
        return this;
    }

    /**
     * @param callback Provide video playback control，Users are free to customize the video display interface
     */
    public PictureSelectionModel bindCustomPlayVideoCallback(OnVideoSelectedPlayCallback callback) {
        PictureSelectionConfig.customVideoPlayCallback = new WeakReference<>(callback).get();
        return this;
    }

    /**
     * @param callback Custom preview callback function
     */
    public PictureSelectionModel bindCustomPreviewCallback(OnCustomImagePreviewCallback callback) {
        PictureSelectionConfig.onCustomImagePreviewCallback = new WeakReference<>(callback).get();
        return this;
    }

    /**
     * # The developer provides an additional callback interface to the user where the user can perform some custom actions
     * {link 如果是自定义相机则必须使用.startActivityForResult(this,PictureConfig.REQUEST_CAMERA);方式启动否则PictureSelector处理不了相机后的回调}
     */
    public PictureSelectionModel bindCustomCameraInterfaceListener(OnCustomCameraInterfaceListener listener) {
        PictureSelectionConfig.onCustomCameraInterfaceListener = new WeakReference<>(listener).get();
        return this;
    }

    /**
     * @param buttonFeatures Set the record button function
     *                       # 具体参考 CustomCameraView.BUTTON_STATE_BOTH、BUTTON_STATE_ONLY_CAPTURE、BUTTON_STATE_ONLY_RECORDER
     */
    public PictureSelectionModel setButtonFeatures(int buttonFeatures) {
        selectionConfig.buttonFeatures = buttonFeatures;
        return this;
    }

    /**
     * @param enableCrop Do you want to start cutting ?
     */
    public PictureSelectionModel isEnableCrop(boolean enableCrop) {
        selectionConfig.enableCrop = enableCrop;
        return this;
    }

    /**
     * @param uCropOptions UCrop parameter configuration is provided
     */
    public PictureSelectionModel basicUCropConfig(UCropOptions uCropOptions) {
        selectionConfig.uCropOptions = uCropOptions;
        return this;
    }

    /**
     * @param isMultipleSkipCrop Whether multiple images can be skipped when cropping
     */
    public PictureSelectionModel isMultipleSkipCrop(boolean isMultipleSkipCrop) {
        selectionConfig.isMultipleSkipCrop = isMultipleSkipCrop;
        return this;
    }

    /**
     * @param freeStyleCropEnabled Crop frame is move ?
     */
    public PictureSelectionModel freeStyleCropEnabled(boolean freeStyleCropEnabled) {
        selectionConfig.freeStyleCropEnabled = freeStyleCropEnabled;
        return this;
    }

    /**
     * @param scaleEnabled Crop frame is zoom ?
     */
    public PictureSelectionModel scaleEnabled(boolean scaleEnabled) {
        selectionConfig.scaleEnabled = scaleEnabled;
        return this;
    }

    /**
     * @param rotateEnabled Crop frame is rotate ?
     */
    public PictureSelectionModel rotateEnabled(boolean rotateEnabled) {
        selectionConfig.rotateEnabled = rotateEnabled;
        return this;
    }

    /**
     * @param circleDimmedLayer Circular head cutting
     */
    public PictureSelectionModel circleDimmedLayer(boolean circleDimmedLayer) {
        selectionConfig.circleDimmedLayer = circleDimmedLayer;
        return this;
    }

    /**
     * @param dimmedColor
     */
    public PictureSelectionModel setCropDimmedColor(int dimmedColor) {
        selectionConfig.circleDimmedColor = dimmedColor;
        return this;
    }

    /**
     * @param circleDimmedBorderColor setCircleDimmedBorderColor
     */
    public PictureSelectionModel setCircleDimmedBorderColor(int circleDimmedBorderColor) {
        selectionConfig.circleDimmedBorderColor = circleDimmedBorderColor;
        return this;
    }

    /**
     * @param circleStrokeWidth setCircleStrokeWidth
     */
    public PictureSelectionModel setCircleStrokeWidth(int circleStrokeWidth) {
        selectionConfig.circleStrokeWidth = circleStrokeWidth;
        return this;
    }

    /**
     * @param showCropFrame Whether to show crop frame
     */
    public PictureSelectionModel showCropFrame(boolean showCropFrame) {
        selectionConfig.showCropFrame = showCropFrame;
        return this;
    }

    /**
     * @param showCropGrid Whether to show CropGrid
     */
    public PictureSelectionModel showCropGrid(boolean showCropGrid) {
        selectionConfig.showCropGrid = showCropGrid;
        return this;
    }

    /**
     * @param hideBottomControls Whether is Clipping function bar
     *                           单选有效
     */
    public PictureSelectionModel hideBottomControls(boolean hideBottomControls) {
        selectionConfig.hideBottomControls = hideBottomControls;
        return this;
    }

    /**
     * @param aspect_ratio_x Crop Proportion x
     * @param aspect_ratio_y Crop Proportion y
     */
    public PictureSelectionModel withAspectRatio(int aspect_ratio_x, int aspect_ratio_y) {
        selectionConfig.aspect_ratio_x = aspect_ratio_x;
        selectionConfig.aspect_ratio_y = aspect_ratio_y;
        return this;
    }

    /**
     * @param isWithVideoImage Whether the pictures and videos can be selected together
     */
    public PictureSelectionModel isWithVideoImage(boolean isWithVideoImage) {
        selectionConfig.isWithVideoImage =
                selectionConfig.selectionMode != PictureConfig.SINGLE
                        && selectionConfig.chooseMode == PictureMimeType.ofAll() && isWithVideoImage;
        return this;
    }

    /**
     * When the maximum number of choices is reached, does the list enable the mask effect
     */
    public PictureSelectionModel isMaxSelectEnabledMask(boolean isMaxSelectEnabledMask) {
        selectionConfig.isMaxSelectEnabledMask = isMaxSelectEnabledMask;
        return this;
    }

    /**
     * @param maxSelectNum PictureSelector max selection
     */
    public PictureSelectionModel maxSelectNum(int maxSelectNum) {
        selectionConfig.maxSelectNum = maxSelectNum;
        return this;
    }

    /**
     * @param minSelectNum PictureSelector min selection
     */
    public PictureSelectionModel minSelectNum(int minSelectNum) {
        selectionConfig.minSelectNum = minSelectNum;
        return this;
    }

    /**
     * @param maxVideoSelectNum PictureSelector video max selection
     */
    public PictureSelectionModel maxVideoSelectNum(int maxVideoSelectNum) {
        selectionConfig.maxVideoSelectNum = selectionConfig.chooseMode == PictureMimeType.ofVideo() ? 0 : maxVideoSelectNum;
        return this;
    }

    /**
     * @param minVideoSelectNum PictureSelector video min selection
     */
    public PictureSelectionModel minVideoSelectNum(int minVideoSelectNum) {
        selectionConfig.minVideoSelectNum = minVideoSelectNum;
        return this;
    }

    /**
     * Turn off Android Q to solve the problem that the width and height are reversed
     *
     * @param isChangeWH
     */
    public PictureSelectionModel closeAndroidQChangeWH(boolean isChangeWH) {
        selectionConfig.isAndroidQChangeWH = isChangeWH;
        return this;
    }

    /**
     * Turn off Android Q to solve the problem that the width and height are reversed
     *
     * @param isChangeVideoWH
     */
    public PictureSelectionModel closeAndroidQChangeVideoWH(boolean isChangeVideoWH) {
        selectionConfig.isAndroidQChangeVideoWH = isChangeVideoWH;
        return this;
    }

    /**
     * By clicking the title bar consecutively, RecyclerView automatically rolls back to the top
     *
     * @param isAutomaticTitleRecyclerTop
     */
    public PictureSelectionModel isAutomaticTitleRecyclerTop(boolean isAutomaticTitleRecyclerTop) {
        selectionConfig.isAutomaticTitleRecyclerTop = isAutomaticTitleRecyclerTop;
        return this;
    }


    /**
     * @param isSingleDirectReturn whether to return directly
     */
    public PictureSelectionModel isSingleDirectReturn(boolean isSingleDirectReturn) {
        selectionConfig.isSingleDirectReturn = selectionConfig.selectionMode
                == PictureConfig.SINGLE && isSingleDirectReturn;
        selectionConfig.isOriginalControl = (selectionConfig.selectionMode != PictureConfig.SINGLE || !isSingleDirectReturn) && selectionConfig.isOriginalControl;
        return this;
    }

    /**
     * Whether to turn on paging mode
     *
     * @param pageSize Maximum number of pages { pageSize is preferably no less than 20}
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy, int pageSize) {
        selectionConfig.isPageStrategy = isPageStrategy;
        selectionConfig.pageSize = pageSize < PictureConfig.MIN_PAGE_SIZE ? PictureConfig.MAX_PAGE_SIZE : pageSize;
        return this;
    }

    /**
     * Whether to turn on paging mode
     *
     * @param pageSize            Maximum number of pages {  pageSize is preferably no less than 20}
     * @param isFilterInvalidFile Whether to filter invalid files { Some of the query performance is consumed,Especially on the Q version}
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy, int pageSize, boolean isFilterInvalidFile) {
        selectionConfig.isPageStrategy = isPageStrategy;
        selectionConfig.pageSize = pageSize < PictureConfig.MIN_PAGE_SIZE ? PictureConfig.MAX_PAGE_SIZE : pageSize;
        selectionConfig.isFilterInvalidFile = isFilterInvalidFile;
        return this;
    }

    /**
     * Whether to turn on paging mode
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy) {
        selectionConfig.isPageStrategy = isPageStrategy;
        return this;
    }

    /**
     * Whether to turn on paging mode
     *
     * @param isPageStrategy
     * @param isFilterInvalidFile Whether to filter invalid files {Some of the query performance is consumed,Especially on the Q version}
     */
    public PictureSelectionModel isPageStrategy(boolean isPageStrategy, boolean isFilterInvalidFile) {
        selectionConfig.isPageStrategy = isPageStrategy;
        selectionConfig.isFilterInvalidFile = isFilterInvalidFile;
        return this;
    }

    /**
     * @param videoQuality video quality and 0 or 1
     */
    public PictureSelectionModel videoQuality(int videoQuality) {
        selectionConfig.videoQuality = videoQuality;
        return this;
    }

    /**
     * <p>
     * if Android SDK >=Q Please use the video/mp4 or video/jpeg ... PictureMimeType.MP4_Q or PictureMimeType.PNG_Q
     * else PictureMimeType.PNG or PictureMimeType.JPEG
     * </p>
     *
     * @param suffixType PictureSelector media format
     */
    public PictureSelectionModel imageFormat(String suffixType) {
        if (SdkVersionUtils.checkedAndroid_Q() || SdkVersionUtils.checkedAndroid_R()) {
            if (TextUtils.equals(suffixType, PictureMimeType.PNG)) {
                suffixType = PictureMimeType.PNG_Q;
            }
            if (TextUtils.equals(suffixType, PictureMimeType.JPEG)) {
                suffixType = PictureMimeType.JPEG_Q;
            }
            if (TextUtils.equals(suffixType, PictureMimeType.MP4)) {
                suffixType = PictureMimeType.MP4_Q;
            }
        }
        selectionConfig.suffixType = suffixType;
        return this;
    }

    /**
     * @param cropWidth  crop width
     * @param cropHeight crop height
     *                   this
     */
    public PictureSelectionModel cropImageWideHigh(int cropWidth, int cropHeight) {
        selectionConfig.cropWidth = cropWidth;
        selectionConfig.cropHeight = cropHeight;
        return this;
    }

    /**
     * @param videoMaxSecond selection video max second
     */
    public PictureSelectionModel videoMaxSecond(int videoMaxSecond) {
        selectionConfig.videoMaxSecond = (videoMaxSecond * 1000);
        return this;
    }

    /**
     * @param videoMinSecond selection video min second
     */
    public PictureSelectionModel videoMinSecond(int videoMinSecond) {
        selectionConfig.videoMinSecond = videoMinSecond * 1000;
        return this;
    }


    /**
     * @param recordVideoSecond video record second
     */
    public PictureSelectionModel recordVideoSecond(int recordVideoSecond) {
        selectionConfig.recordVideoSecond = recordVideoSecond;
        return this;
    }

    /**
     * @param recordVideoMinSecond video record second
     */
    public PictureSelectionModel recordVideoMinSecond(int recordVideoMinSecond) {
        selectionConfig.recordVideoMinSecond = recordVideoMinSecond;
        return this;
    }

    /**
     * @param imageSpanCount PictureSelector image span count
     */
    public PictureSelectionModel imageSpanCount(int imageSpanCount) {
        selectionConfig.imageSpanCount = imageSpanCount;
        return this;
    }

    /**
     * Less than how many KB images are not compressed
     */
    public PictureSelectionModel minimumCompressSize(int size) {
        selectionConfig.minimumCompressSize = size;
        return this;
    }

    /**
     * @param cutQuality crop compress quality default 90
     */
    public PictureSelectionModel cutOutQuality(int cutQuality) {
        selectionConfig.cropCompressQuality = cutQuality;
        return this;
    }

    /**
     * @param isCompress Whether to open compress
     */
    public PictureSelectionModel isCompress(boolean isCompress) {
        selectionConfig.isCompress = isCompress;
        return this;
    }

    /**
     * @param compressQuality Image compressed output quality
     */
    public PictureSelectionModel compressQuality(int compressQuality) {
        selectionConfig.compressQuality = compressQuality;
        return this;
    }

    /**
     * @param returnEmpty No data can be returned
     */
    public PictureSelectionModel isReturnEmpty(boolean returnEmpty) {
        selectionConfig.returnEmpty = returnEmpty;
        return this;
    }

    /**
     * @param synOrAsy Synchronous or asynchronous compression
     */
    public PictureSelectionModel synOrAsy(boolean synOrAsy) {
        selectionConfig.synOrAsy = synOrAsy;
        return this;
    }

    /**
     * @param focusAlpha After compression, the transparent channel is retained
     */
    public PictureSelectionModel compressFocusAlpha(boolean focusAlpha) {
        selectionConfig.focusAlpha = focusAlpha;
        return this;
    }

    /**
     * After recording with the system camera, does it support playing the video immediately using the system player
     */
    public PictureSelectionModel isQuickCapture(boolean isQuickCapture) {
        selectionConfig.isQuickCapture = isQuickCapture;
        return this;
    }

    /**
     * @param isOriginalControl Whether the original image is displayed
     */
    public PictureSelectionModel isOriginalImageControl(boolean isOriginalControl) {
        selectionConfig.isOriginalControl = !selectionConfig.camera
                && selectionConfig.chooseMode != PictureMimeType.ofVideo()
                && isOriginalControl;
        return this;
    }

    /**
     * @param path save path
     */
    public PictureSelectionModel compressSavePath(String path) {
        selectionConfig.compressSavePath = path;
        return this;
    }

    /**
     * Camera custom local file name
     * # Such as xxx.png
     *
     * @param fileName
     */
    public PictureSelectionModel cameraFileName(String fileName) {
        selectionConfig.cameraFileName = fileName;
        return this;
    }

    /**
     * crop custom local file name
     * # Such as xxx.png
     *
     * @param renameCropFileName
     */
    public PictureSelectionModel renameCropFileName(String renameCropFileName) {
        selectionConfig.renameCropFileName = renameCropFileName;
        return this;
    }

    /**
     * custom compress local file name
     * # Such as xxx.png
     *
     * @param renameFile
     */
    public PictureSelectionModel renameCompressFile(String renameFile) {
        selectionConfig.renameCompressFileName = renameFile;
        return this;
    }

    /**
     * @param zoomAnim Picture list zoom anim
     */
    public PictureSelectionModel isZoomAnim(boolean zoomAnim) {
        selectionConfig.zoomAnim = zoomAnim;
        return this;
    }

    /**
     * @param previewEggs preview eggs  It doesn't make much sense
     */
    public PictureSelectionModel isPreviewEggs(boolean previewEggs) {
        selectionConfig.previewEggs = previewEggs;
        return this;
    }

    /**
     * @param isCamera Whether to open camera button
     */
    public PictureSelectionModel isCamera(boolean isCamera) {
        selectionConfig.isCamera = isCamera;
        return this;
    }

    /**
     * Extra used with {#Environment.getExternalStorageDirectory() +  File.separator + "CustomCamera" + File.separator}  to indicate that
     *
     * @param outPutCameraPath Camera save path 只支持Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
     */
    public PictureSelectionModel setOutputCameraPath(String outPutCameraPath) {
        selectionConfig.outPutCameraPath = outPutCameraPath;
        return this;
    }


    /**
     * # file size The unit is M
     *
     * @param fileSize Filter file size
     */
    public PictureSelectionModel queryMaxFileSize(float fileSize) {
        selectionConfig.filterFileSize = fileSize;
        return this;
    }

    /**
     * @param isGif Whether to open gif
     */
    public PictureSelectionModel isGif(boolean isGif) {
        selectionConfig.isGif = isGif;
        return this;
    }

    /**
     * @param isWebp Whether to open .webp
     */
    public PictureSelectionModel isWebp(boolean isWebp) {
        selectionConfig.isWebp = isWebp;
        return this;
    }

    /**
     * @param isBmp Whether to open .isBmp
     */
    public PictureSelectionModel isBmp(boolean isBmp) {
        selectionConfig.isWebp = isBmp;
        return this;
    }

    /**
     * @param enablePreview Do you want to preview the picture?
     */
    public PictureSelectionModel isPreviewImage(boolean enablePreview) {
        selectionConfig.enablePreview = enablePreview;
        return this;
    }

    /**
     * @param enPreviewVideo Do you want to preview the video?
     */
    public PictureSelectionModel isPreviewVideo(boolean enPreviewVideo) {
        selectionConfig.enPreviewVideo = enPreviewVideo;
        return this;
    }

    /**
     * @param isNotPreviewDownload Previews do not show downloads
     */
    public PictureSelectionModel isNotPreviewDownload(boolean isNotPreviewDownload) {
        selectionConfig.isNotPreviewDownload = isNotPreviewDownload;
        return this;
    }

    /**
     * Specify get image format
     */
    public PictureSelectionModel querySpecifiedFormatSuffix(String specifiedFormat) {
        selectionConfig.specifiedFormat = specifiedFormat;
        return this;
    }

    /**
     * @param openClickSound Whether to open click voice
     */
    public PictureSelectionModel isOpenClickSound(boolean openClickSound) {
        selectionConfig.openClickSound = !selectionConfig.camera && openClickSound;
        return this;
    }

    /**
     * 是否可拖动裁剪框(setFreeStyleCropEnabled 为true 有效)
     */
    public PictureSelectionModel isDragFrame(boolean isDragFrame) {
        selectionConfig.isDragFrame = isDragFrame;
        return this;
    }

    /**
     * Whether the multi-graph clipping list is animated or not
     */
    public PictureSelectionModel isMultipleRecyclerAnimation(boolean isAnimation) {
        selectionConfig.isMultipleRecyclerAnimation = isAnimation;
        return this;
    }


    /**
     * 设置摄像头方向(前后 默认后置)
     */
    public PictureSelectionModel isCameraAroundState(boolean isCameraAroundState) {
        selectionConfig.isCameraAroundState = isCameraAroundState;
        return this;
    }

    /**
     * @param selectionData Select the selected picture set
     */
    public PictureSelectionModel selectionData(List<LocalMedia> selectionData) {
        if (selectionConfig.selectionMode == PictureConfig.SINGLE && selectionConfig.isSingleDirectReturn) {
            selectionConfig.selectionMedias = null;
        } else {
            selectionConfig.selectionMedias = selectionData;
        }
        return this;
    }

    /**
     * 动态设置相册主题样式
     *
     * @param style 主题
     *              <p>{@link PictureSelectorUIStyle}</>
     */
    public PictureSelectionModel setPictureStyle(PictureParameterStyle style) {
        if (style != null) {
            PictureSelectionConfig.style = style;
            if (!selectionConfig.isWeChatStyle) {
                selectionConfig.isWeChatStyle = style.isNewSelectStyle;
            }
        } else {
            PictureSelectionConfig.style = PictureParameterStyle.ofDefaultStyle();
        }
        return this;
    }

    /**
     * Dynamically set the album to start and exit the animation
     *
     * @param windowAnimationStyle Activity Launch exit animation theme
     */
    public PictureSelectionModel setPictureWindowAnimationStyle(PictureWindowAnimationStyle windowAnimationStyle) {
        if (windowAnimationStyle != null) {
            PictureSelectionConfig.windowAnimationStyle = windowAnimationStyle;
        } else {
            PictureSelectionConfig.windowAnimationStyle = PictureWindowAnimationStyle.ofDefaultWindowAnimationStyle();
        }
        return this;
    }

    /**
     * Photo album list animation {}
     * Use {@link AnimationType#ALPHA_IN_ANIMATION or SLIDE_IN_BOTTOM_ANIMATION} directly.
     *
     * @param animationMode
     */
    public PictureSelectionModel setRecyclerAnimationMode(int animationMode) {
        selectionConfig.animationMode = animationMode;
        return this;
    }

    /**
     * # If you want to handle the Android Q path, if not, just return the uri，
     * The getAndroidQToPath(); field will be empty
     *
     * @param isAndroidQTransform
     */
    public PictureSelectionModel isAndroidQTransform(boolean isAndroidQTransform) {
        selectionConfig.isAndroidQTransform = isAndroidQTransform;
        return this;
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     */
    public void forResult(int requestCode) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null || selectionConfig == null) {
                return;
            }
            Intent intent;
            if (selectionConfig.camera && selectionConfig.isUseCustomCamera) {
                intent = new Intent(activity, PictureCustomCameraActivity.class);
            } else {
                intent = new Intent(activity, selectionConfig.camera
                        ? PictureSelectorCameraEmptyActivity.class :
                        selectionConfig.isWeChatStyle ? PictureSelectorWeChatStyleActivity.class
                                : PictureSelectorActivity.class);
            }
            selectionConfig.isCallbackMode = false;
            Fragment fragment = selector.getFragment();
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
            PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.windowAnimationStyle;
            activity.overridePendingTransition(windowAnimationStyle.activityEnterAnimation, R.anim.picture_anim_fade_in);
        }
    }

    /**
     * Start to select media and wait for result.
     *
     * @param listener The resulting callback listens
     */
    public void forResult(OnResultCallbackListener listener) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity activity = selector.getActivity();
            if (activity == null || selectionConfig == null) {
                return;
            }
            // 绑定回调监听
            PictureSelectionConfig.listener = new WeakReference<>(listener).get();
            selectionConfig.isCallbackMode = true;
            Intent intent;
            if (selectionConfig.camera && selectionConfig.isUseCustomCamera) {
                intent = new Intent(activity, PictureCustomCameraActivity.class);
            } else {
                intent = new Intent(activity, selectionConfig.camera
                        ? PictureSelectorCameraEmptyActivity.class :
                        selectionConfig.isWeChatStyle ? PictureSelectorWeChatStyleActivity.class
                                : PictureSelectorActivity.class);
            }
            Fragment fragment = selector.getFragment();
            if (fragment != null) {
                fragment.startActivity(intent);
            } else {
                activity.startActivity(intent);
            }
            PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.windowAnimationStyle;
            activity.overridePendingTransition(
                    windowAnimationStyle.activityEnterAnimation, R.anim.picture_anim_fade_in);
        }
    }

    /**
     * Start to select media and wait for result.
     *
     * @param requestCode Identity of the request Activity or Fragment.
     * @param listener    The resulting callback listens
     */
    public void forResult(int requestCode, OnResultCallbackListener listener) {
        if (!DoubleUtils.isFastDoubleClick()) {
            Activity target = null;
            Activity activity = selector.getActivity();
            if (activity == null || selectionConfig == null) {
                return;
            }
            // 绑定回调监听
            PictureSelectionConfig.listener = new WeakReference<>(listener).get();
            selectionConfig.isCallbackMode = true;
            Intent intent;
            if (selectionConfig.camera && selectionConfig.isUseCustomCamera) {
                intent = new Intent(activity, PictureCustomCameraActivity.class);
            } else {
                intent = new Intent(activity, selectionConfig.camera
                        ? PictureSelectorCameraEmptyActivity.class :
                        selectionConfig.isWeChatStyle ? PictureSelectorWeChatStyleActivity.class
                                : PictureSelectorActivity.class);
            }
            Fragment fragment = selector.getFragment();
            if (fragment != null) {
                fragment.startActivityForResult(intent, requestCode);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
            PictureWindowAnimationStyle windowAnimationStyle = PictureSelectionConfig.windowAnimationStyle;
            activity.overridePendingTransition(windowAnimationStyle.activityEnterAnimation, R.anim.picture_anim_fade_in);
        }
    }

    /**
     * 提供外部预览图片方法
     */
    public void openExternalPreview(int position, List<LocalMedia> medias) {
        if (selector != null) {
            selector.externalPicturePreview(position, medias, PictureSelectionConfig.windowAnimationStyle.activityPreviewEnterAnimation);
        } else {
            throw new NullPointerException("This PictureSelector is Null");
        }
    }

    /**
     * set preview video
     */
    public void externalPictureVideo(String path) {
        if (selector != null) {
            selector.externalPictureVideo(path);
        } else {
            throw new NullPointerException("This PictureSelector is Null");
        }
    }
}
