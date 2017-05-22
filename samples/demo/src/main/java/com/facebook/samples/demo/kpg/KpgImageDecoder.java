package com.facebook.samples.demo.kpg;

import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imageutils.KpgUtil;

import android.util.Pair;

/**
 * Created by shuailongcheng on 22/05/2017.
 */

public class KpgImageDecoder implements ImageDecoder {

    @Override
    public CloseableImage decode(EncodedImage encodedImage, int length, QualityInfo qualityInfo,
            ImageDecodeOptions options) {
        parseMetadata(encodedImage);
        return decodeKpgStaticImage(encodedImage, options);
    }


    public CloseableStaticBitmap decodeKpgStaticImage(
            final EncodedImage encodedImage,
            ImageDecodeOptions options) {
        // TODO: 22/05/2017 puth mPlatformDecoder related implemention here
//        CloseableReference<Bitmap> bitmapReference =
//                mPlatformDecoder.decodeKpgFromEncodedImage(encodedImage, options.bitmapConfig);
//        try {
//            return new CloseableStaticBitmap(
//                    bitmapReference,
//                    ImmutableQualityInfo.FULL_QUALITY,
//                    encodedImage.getRotationAngle());
//        } finally {
//            bitmapReference.close();
//        }
        return null;
    }

    private static void parseMetadata(EncodedImage encodedImage) {
        readKpgImageSize(encodedImage);
    }

    private static Pair<Integer, Integer> readKpgImageSize(EncodedImage encodedImage) {
        if (encodedImage == null) {
            return null;
        }
        final Pair<Integer, Integer> dimensions = KpgUtil.getSize(encodedImage.getInputStream());
        if (dimensions != null) {
            encodedImage.setWidth(dimensions.first);
            encodedImage.setHeight(dimensions.second);
        }
        return dimensions;
    }

}
