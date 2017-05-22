package com.facebook.imageutils;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.Pair;

import com.facebook.common.time.SystemClock;
import com.facebook.imagepipeline.image.EncodedImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.annotation.Nullable;

/**
 * Created by fengweilun on 02/05/2017.
 */

public class KpgUtil
{
    static{
//        System.loadLibrary("avutil");
//        System.loadLibrary("avcodec");
//        System.loadLibrary("swresample");
//        System.loadLibrary("swscale");
        System.loadLibrary("ffmpeg");
        System.loadLibrary("kpg");
    }

    private native static int[] nativedecode(byte[] bs, int len, int pic_width, int pic_height);

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static Bitmap decodePurgable(byte[] data, int offset, int length, BitmapFactory.Options opts)
    {
        int[] argb = nativedecode(data, length, opts.outWidth, opts.outHeight);
        return Bitmap.createBitmap(argb, opts.outWidth, opts.outHeight, Bitmap.Config.ARGB_8888);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts, int filesize)
    {
        Bitmap bitmap;
        try {
            byte[] stream = new byte[filesize];
            is.read(stream);
            int[] argb = nativedecode(stream, filesize, opts.outWidth, opts.outHeight);
            bitmap = opts.inBitmap;
            bitmap.setHeight(opts.outHeight);
            bitmap.setWidth(opts.outWidth);
            bitmap.setPixels(argb, 0, opts.outWidth, 0, 0, opts.outWidth, opts.outHeight);
        }catch(IOException io)
        {
            return null;
        }
        return bitmap;
    }

    public static BitmapFactory.Options getOptions(
            EncodedImage encodedImage,
            Bitmap.Config bitmapConfig,
            int[] filesize)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = encodedImage.getSampleSize();
        ArrayList<Integer> arr = getSizeandDim(encodedImage.getInputStream());
        filesize[0] = arr.get(0);
        options.outWidth = arr.get(1);
        options.outHeight = arr.get(2);
        if (options.outWidth == -1 || options.outHeight == -1) {
            throw new IllegalArgumentException();
        }
        options.inPreferredConfig = bitmapConfig;
        return options;
    }


    @Nullable
    public static ArrayList<Integer> getSizeandDim(InputStream is)
    {
        Pair<Integer, Integer> result = null;
        byte[] headerBuffer = new byte[4];
        try {
            //MUST be RIFF
            is.read(headerBuffer);
            if(!compare(headerBuffer, "RIFF"))
                return null;

            //File Size
            int filesize = getInt(is);

            // Next the KPGB header
            is.read(headerBuffer);
            if (!compare(headerBuffer, "KPGB")) {
                return null;
            }

            // Next the KWVC header
            is.read(headerBuffer);
            if (!compare(headerBuffer, "KWVC")) {
                return null;
            }

            //Chunk Size
            getInt(is);

            //width, height
            int width = getInt(is);
            int height = getInt(is);

            ArrayList<Integer> ret = new ArrayList<Integer>();
            ret.add(filesize);
            ret.add(width);
            ret.add(height);
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    @Nullable
    public static Pair<Integer, Integer> getSize(InputStream is)
    {
        Pair<Integer, Integer> result = null;
        byte[] headerBuffer = new byte[4];
        try {
            //MUST be RIFF
            is.read(headerBuffer);
            if(!compare(headerBuffer, "RIFF"))
                return null;

            //File Size
            getInt(is);

            // Next the KPGB header
            is.read(headerBuffer);
            if (!compare(headerBuffer, "KPGB")) {
                return null;
            }

            // Next the KWVC header
            is.read(headerBuffer);
            if (!compare(headerBuffer, "KWVC")) {
                return null;
            }

            //Chunk Size
            getInt(is);

            //width, height
            return new Pair<>(getInt(is),  getInt(is));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * Compares some bytes with the text we're expecting
     *
     * @param what The bytes to compare
     * @param with The string those bytes should contains
     * @return True if they match and false otherwise
     */
    private static boolean compare(byte[] what, String with) {
        if (what.length != with.length()) {
            return false;
        }
        for (int i = 0; i < what.length; i++) {
            if (with.charAt(i) != what[i]) {
                return false;
            }
        }
        return true;
    }

    private static String getHeader(byte[] header) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < header.length; i++) {
            str.append((char) header[i]);
        }
        return str.toString();
    }

    private static int getInt(InputStream is) throws IOException {
        byte byte1 = (byte) is.read();
        byte byte2 = (byte) is.read();
        byte byte3 = (byte) is.read();
        byte byte4 = (byte) is.read();
        return (byte4 << 24) & 0xFF000000 |
                (byte3 << 16) & 0xFF0000 |
                (byte2 << 8) & 0xFF00 |
                (byte1) & 0xFF;
    }

    public static int get2BytesAsInt(InputStream is) throws IOException {
        byte byte1 = (byte) is.read();
        byte byte2 = (byte) is.read();
        return (byte2 << 8 & 0xFF00) | (byte1 & 0xFF);
    }

    private static int read3Bytes(InputStream is) throws IOException {
        byte byte1 = getByte(is);
        byte byte2 = getByte(is);
        byte byte3 = getByte(is);
        return (((int) byte3) << 16 & 0xFF0000) |
                (((int) byte2) << 8 & 0xFF00) |
                (((int) byte1) & 0xFF);
    }

    private static short getShort(InputStream is) throws IOException {
        return (short) (is.read() & 0xFF);
    }

    private static byte getByte(InputStream is) throws IOException {
        return (byte) (is.read() & 0xFF);
    }

    private static boolean isBitOne(byte input, int bitIndex) {
        return ((input >> (bitIndex % 8)) & 1) == 1;
    }
}
