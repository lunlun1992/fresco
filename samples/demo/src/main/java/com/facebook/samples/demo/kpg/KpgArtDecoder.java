package com.facebook.samples.demo.kpg;

import java.io.InputStream;
import java.nio.ByteBuffer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.util.Pools;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.VisibleForTesting;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapPool;
import com.facebook.imageutils.BitmapUtil;

/**
 * Created by fengweilun on 22/05/2017.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class KpgArtDecoder implements KpgPlatformDecoder
{
  private static final int DECODE_BUFFER_SIZE = 16 * 1024;
  private final BitmapPool mBitmapPool;
  /**
   * ArtPlatformImageDecoder decodes images from InputStream - to do so we need to provide
   * temporary buffer, otherwise framework will allocate one for us for each decode request
   */
  @VisibleForTesting final Pools.SynchronizedPool<ByteBuffer> mDecodeBuffers;

  public KpgArtDecoder(BitmapPool bitmapPool, int maxNumThreads, Pools.SynchronizedPool decodeBuffers)
  {
    mBitmapPool = bitmapPool;
    mDecodeBuffers = decodeBuffers;
    for (int i = 0; i < maxNumThreads; i++) {
      mDecodeBuffers.release(ByteBuffer.allocate(DECODE_BUFFER_SIZE));
    }
  }

  protected CloseableReference<Bitmap> decodeKpgFromStream(
      InputStream inputStream,
      BitmapFactory.Options options,
      int filesize)
  {
    Preconditions.checkNotNull(inputStream);
    int sizeInBytes = BitmapUtil.getSizeInByteForBitmap(
        options.outWidth,
        options.outHeight,
        options.inPreferredConfig);
    final Bitmap bitmapToReuse = mBitmapPool.get(sizeInBytes);
    if (bitmapToReuse == null) {
      throw new NullPointerException("BitmapPool.get returned null");
    }
    options.inBitmap = bitmapToReuse;

    Bitmap decodedBitmap;
    ByteBuffer byteBuffer = mDecodeBuffers.acquire();
    if (byteBuffer == null) {
      byteBuffer = ByteBuffer.allocate(DECODE_BUFFER_SIZE);
    }
    try {
      options.inTempStorage = byteBuffer.array();
      decodedBitmap = KpgUtil.decodeStream(inputStream, null, options, filesize);
    } catch (RuntimeException re) {
      mBitmapPool.release(bitmapToReuse);
      throw re;
    } finally {
      mDecodeBuffers.release(byteBuffer);
    }

    if (bitmapToReuse != decodedBitmap) {
      mBitmapPool.release(bitmapToReuse);
      decodedBitmap.recycle();
      throw new IllegalStateException();
    }

    return CloseableReference.of(decodedBitmap, mBitmapPool);
  }
  @Override
  public CloseableReference<Bitmap> decodeKpgFromEncodedImage(
      EncodedImage encodedImage, Bitmap.Config bitmapConfig) {
    int[] file_size = new int[1];
    final BitmapFactory.Options options = com.facebook.imageutils.KpgUtil
        .getOptions(encodedImage, bitmapConfig, file_size);
    boolean retryOnFail=options.inPreferredConfig != Bitmap.Config.ARGB_8888;
    try {
      return decodeKpgFromStream(encodedImage.getInputStream(), options, file_size[0]);
    } catch (RuntimeException re) {
      if (retryOnFail) {
        return decodeKpgFromEncodedImage(encodedImage, Bitmap.Config.ARGB_8888);
      }
      throw re;
    }
  }
}
