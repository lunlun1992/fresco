package com.facebook.samples.demo.kpg;

import android.graphics.Bitmap;

import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;

/**
 * Created by fengweilun on 22/05/2017.
 */

public interface KpgPlatformDecoder {
  /**
   * Creates a bitmap from encoded KPG bytes.
   *
   * @param encodedImage the reference to the encoded image with the reference to the encoded bytes
   * @param bitmapConfig the {@link android.graphics.Bitmap.Config} used to create the decoded
   * Bitmap
   * @return the bitmap
   * @throws TooManyBitmapsException if the pool is full
   * @throws java.lang.OutOfMemoryError if the Bitmap cannot be allocated
   */
  CloseableReference<Bitmap> decodeKpgFromEncodedImage(
      EncodedImage encodedImage,
      Bitmap.Config bitmapConfig);
}
