package com.facebook.samples.demo.kpg;

import com.facebook.imageformat.ImageFormat;

/**
 * Created by shuailongcheng on 22/05/2017.
 */

public final class KpgImageFormat {
    public static final ImageFormat KPG = new ImageFormat("KPG", "kpg");

    public static boolean isKpgFormat(ImageFormat imageFormat) {
        return imageFormat == KPG;
    }
}

