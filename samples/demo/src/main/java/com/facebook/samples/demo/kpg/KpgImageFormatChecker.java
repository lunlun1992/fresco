package com.facebook.samples.demo.kpg;

import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatCheckerUtils;

import android.support.annotation.Nullable;


/**
 * Created by shuailongcheng on 22/05/2017.
 */

public class KpgImageFormatChecker implements ImageFormat.FormatChecker {

    /**
     * KPG Start Code Determine
     */
    private static final int KPG_HEADER_LENGTH = 12;

    private static boolean matchBytePattern(
            final byte[] byteArray,
            final int offset,
            final byte[] pattern) {
        if (pattern == null || byteArray == null) {
            return false;
        }
        if (pattern.length + offset > byteArray.length) {
            return false;
        }

        for (int i = 0; i < pattern.length; ++i) {
            if (byteArray[i + offset] != pattern[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if first headerSize bytes of imageHeaderBytes constitute a valid header for a KPG
     * image.
     * Details on KPG header can be found <a href="http://wiki.corp.kuaishou.com/pages/viewpage.action?pageId=13357265">
     * </a>
     *
     * @return true if imageHeaderBytes is a valid header for a kpg image
     */
    private static boolean isKpgHeader(final byte[] imageHeaderBytes, final int headerSize) {
        return headerSize >= KPG_HEADER_LENGTH &&
                matchBytePattern(imageHeaderBytes, 0, ImageFormatCheckerUtils.asciiBytes("RIFF")) &&
                matchBytePattern(imageHeaderBytes, 8, ImageFormatCheckerUtils.asciiBytes("KPGB"));
    }

    @Override
    public int getHeaderSize() {
        return KPG_HEADER_LENGTH;
    }

    @Nullable
    @Override
    public ImageFormat determineFormat(byte[] headerBytes, int headerSize) {
        if (isKpgHeader(headerBytes, headerSize)) {
            return KpgImageFormat.KPG;
        } else {
            return null;
        }
    }
}
