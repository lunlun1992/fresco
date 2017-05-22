package com.facebook.samples.demo.kpg;

import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.decoder.ImageDecoder;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.image.ImmutableQualityInfo;
import com.facebook.imagepipeline.image.QualityInfo;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imagepipeline.memory.PoolFactory;
import com.facebook.imageutils.KpgUtil;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.Pools;
import android.util.Log;
import android.util.Pair;

/**
 * Created by shuailongcheng on 22/05/2017.
 */

public class KpgImageDecoder implements ImageDecoder {

    private final KpgPlatformDecoder mPlatformDecoder;

    public KpgImageDecoder(PoolFactory poolFactory) {
        mPlatformDecoder = buildPlatformDecoder(poolFactory);
    }

    public static KpgPlatformDecoder buildPlatformDecoder(
            PoolFactory poolFactory) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int maxNumThreads = poolFactory.getFlexByteArrayPoolMaxNumThreads();
            return new KpgArtDecoder(
                    poolFactory.getBitmapPool(),
                    maxNumThreads,
                    new Pools.SynchronizedPool<>(maxNumThreads));
        } else {
            return null;
            //TODO 23/05/2017 fix the sws_scale libc bug
            //return new KpgKitKatPurgeableDecoder(poolFactory.getFlexByteArrayPool());
        }
    }

    @Override
    public CloseableImage decode(EncodedImage encodedImage, int length, QualityInfo qualityInfo,
            ImageDecodeOptions options) {
        parseMetadata(encodedImage);
        // TODO: 22/05/2017 puth mPlatformDecoder related implemention here
        Log.d("KpgImageDecoder", "decode");
        CloseableReference<Bitmap> bitmapReference = null;
        if(null != mPlatformDecoder) {
            bitmapReference = mPlatformDecoder.decodeKpgFromEncodedImage(encodedImage, options.bitmapConfig);
        }
        try {
            return new CloseableStaticBitmap(
                    bitmapReference,
                    ImmutableQualityInfo.FULL_QUALITY,
                    encodedImage.getRotationAngle());
        } finally {
            bitmapReference.close();
        }
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
