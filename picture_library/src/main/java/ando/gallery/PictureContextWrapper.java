package ando.gallery;

import android.content.Context;
import android.content.ContextWrapper;

import ando.gallery.language.PictureLanguageUtils;

/**
 * @author：luck
 * 2019-12-15 19:34
 * ContextWrapper
 */
public class PictureContextWrapper extends ContextWrapper {

    public PictureContextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrap(Context context, int language) {
        PictureLanguageUtils.setAppLanguage(context, language);
        return new PictureContextWrapper(context);
    }
}
