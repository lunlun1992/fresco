package com.facebook.samples.demo.kpg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.common.internal.Preconditions;
import com.facebook.common.internal.Throwables;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.references.CloseableReference;
import com.facebook.imagepipeline.common.TooManyBitmapsException;
import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.memory.BitmapCounter;
import com.facebook.imagepipeline.memory.BitmapCounterProvider;
import com.facebook.imagepipeline.memory.FlexByteArrayPool;
import com.facebook.imagepipeline.nativecode.Bitmaps;
import com.facebook.imageutils.JfifUtil;

/**
 * Created by fengweilun on 22/05/2017.
 */

public class KpgKitKatPurgeableDecoder implements KpgPlatformDecoder {

  private final FlexByteArrayPool mFlexByteArrayPool;
  private final BitmapCounter mUnpooledBitmapsCounter;

  public KpgKitKatPurgeableDecoder(FlexByteArrayPool flexByteArrayPool) {
    mFlexByteArrayPool = flexByteArrayPool;
    mUnpooledBitmapsCounter = BitmapCounterProvider.get();
  }

  protected static final byte[] EOI = new byte[] {
      (byte) JfifUtil.MARKER_FIRST_BYTE, (byte) JfifUtil.MARKER_EOI };

  protected static boolean endsWithEOI(CloseableReference<PooledByteBuffer> bytesRef, int length) {
    PooledByteBuffer buffer = bytesRef.get();
    return length >= 2 &&
        buffer.read(length - 2) == (byte) JfifUtil.MARKER_FIRST_BYTE &&
        buffer.read(length - 1) == (byte) JfifUtil.MARKER_EOI;
  }

  private static void putEOI(byte[] imageBytes, int offset) {
    // TODO 5884402: remove dependency on JfifUtil
    imageBytes[offset] = (byte) JfifUtil.MARKER_FIRST_BYTE;
    imageBytes[offset + 1] = (byte) JfifUtil.MARKER_EOI;
  }

  CloseableReference<Bitmap> decodeKPGAsPurgeable(CloseableReference<PooledByteBuffer> bytesRef, BitmapFactory.Options options, int file_size) {
    byte[] suffix = endsWithEOI(bytesRef, file_size) ? null : EOI;
    final PooledByteBuffer pooledByteBuffer = bytesRef.get();
    Preconditions.checkArgument(file_size <= pooledByteBuffer.size());
    // allocate bigger array in case EOI needs to be added
    final CloseableReference<byte[]> encodedBytesArrayRef = mFlexByteArrayPool.get(file_size + 2);
    try {
      byte[] encodedBytesArray = encodedBytesArrayRef.get();
      pooledByteBuffer.read(0, encodedBytesArray, 0, file_size);
      if (suffix != null) {
        putEOI(encodedBytesArray, file_size);
        file_size += 2;
      }
      Bitmap bitmap = KpgUtil.decodePurgable(encodedBytesArray, 0, file_size, options);
      return pinBitmap(bitmap);
    } finally {
      CloseableReference.closeSafely(encodedBytesArrayRef);
    }
  }

  @Override
  public CloseableReference<Bitmap> decodeKpgFromEncodedImage(
      EncodedImage encodedImage, Bitmap.Config bitmapConfig) {
    int[] file_size = new int[1];
    final BitmapFactory.Options options = com.facebook.imageutils.KpgUtil
        .getOptions(encodedImage, bitmapConfig, file_size);
    CloseableReference<PooledByteBuffer> bytesRef = encodedImage.getByteBufferRef();
    Preconditions.checkNotNull(bytesRef);
    try {
      return decodeKPGAsPurgeable(bytesRef, options, file_size[0]);
    } catch (RuntimeException re) {
      throw re;
    }
  }
  public CloseableReference<Bitmap> pinBitmap(Bitmap bitmap) {
    try {
      // Real decoding happens here - if the image was corrupted, this will throw an exception
      Bitmaps.pinBitmap(bitmap);
    } catch (Exception e) {
      bitmap.recycle();
      throw Throwables.propagate(e);
    }
    if (!mUnpooledBitmapsCounter.increase(bitmap)) {
      bitmap.recycle();
      throw new TooManyBitmapsException();
    }
    return CloseableReference.of(bitmap, mUnpooledBitmapsCounter.getReleaser());
  }

}
