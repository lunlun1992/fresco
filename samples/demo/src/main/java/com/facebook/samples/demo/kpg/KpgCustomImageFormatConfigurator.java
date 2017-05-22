package com.facebook.samples.demo.kpg;

import com.facebook.imagepipeline.decoder.ImageDecoderConfig;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * 参考fresco demo showcase:CustomImageFormatConfigurator.java
 * Created by shuailongcheng on 22/05/2017.
 */

public class KpgCustomImageFormatConfigurator {

    @Nullable
    public static ImageDecoderConfig createImageDecoderConfig(Context context) {
        ImageDecoderConfig.Builder config = ImageDecoderConfig.newBuilder();
        config.addDecodingCapability(
                KpgImageFormat.KPG,
                new KpgImageFormatChecker(),
                new KpgImageDecoder());
        return config.build();
    }
}
