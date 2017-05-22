//
// Created by fengweilun on 03/05/2017.
//
extern "C"
{
#include "libkpg.h"


typedef struct CodecContext {
    AVCodec *pCodec;
    AVFrame *pFrame;
    AVCodecContext *pCodecCtx;
} CodecContext;

#define HEADER_LENGTH 28

JNIEXPORT
jintArray Java_com_facebook_imageutils_KpgUtil_nativedecode(JNIEnv *env, jclass clazz, jbyteArray bs, jint len, jint width, jint height) {
    //Header offset
    LOGI("Start Prepare");

    int ret, got_picture, i;
    AVPacket packet;
    CodecContext codec;
    jintArray outarray = env->NewIntArray(width * height);
    int *arrayelem = (int *)malloc(sizeof(int) * width * height);
    jbyte* bsbyte = env->GetByteArrayElements(bs, NULL);

    avcodec_register_all();
    open_ffmpeg_log_in_debug_mode();

    codec.pCodec = avcodec_find_decoder(AV_CODEC_ID_HEVC);
    if (!codec.pCodec) {
        LOGE("Codec not found\n");
        return NULL;
    }
    codec.pCodecCtx = avcodec_alloc_context3(codec.pCodec);
    if (!codec.pCodecCtx) {
        LOGE("Could not allocate video codec context\n");
        return NULL;
    }
    if (avcodec_open2(codec.pCodecCtx, codec.pCodec, NULL) < 0) {
        LOGE("Could not open codec\n");
        return NULL;
    }
    av_init_packet(&packet);
    codec.pFrame = av_frame_alloc();

    packet.data = (uint8_t *)bsbyte + HEADER_LENGTH;
    packet.size = len - HEADER_LENGTH;

    LOGI("data: %d, %d, %d, %d", packet.data[0], packet.data[1], packet.data[2], packet.data[3]);
    LOGI("Start Decode");
    ret = avcodec_decode_video2(codec.pCodecCtx, codec.pFrame, &got_picture, &packet);
    if (ret < 0) {
        LOGE("Decode Error.\n");
        return NULL;
    }
    LOGE("Check Ret is Fine");
    if (got_picture) {
        LOGI("\nCodec Full Name:%s\n", codec.pCodecCtx->codec->long_name);
        LOGI("width:%d\nheight:%d\n\n", codec.pCodecCtx->width, codec.pCodecCtx->height);

        LOGI("Succeed to decode 1 frame!\n");
    } else {
        packet.data = NULL;
        packet.size = 0;
        while (1) {
            ret = avcodec_decode_video2(codec.pCodecCtx, codec.pFrame, &got_picture, &packet);
            if (ret < 0) {
                LOGE("Decode Error.\n");
                return NULL;
            }
            if (!got_picture) {
                break;
            } else {
                LOGI("Flush Decoder: Succeed to decode 1 frame!\n");
                break;
            }
        }
    }

    {
        SwsContext *swsContext = NULL;
        const uint8_t *const srcslice[3] = {codec.pFrame->data[0], codec.pFrame->data[1], codec.pFrame->data[2]};
        uint8_t *const dstslice[3] = {(uint8_t *)arrayelem, NULL, NULL};
        const int srcStride[3] = {codec.pFrame->linesize[0], codec.pFrame->linesize[1], codec.pFrame->linesize[2]};
        int dstStride[3] = {width * 4, 0, 0};
        LOGI("Start change PIXFMT");
        swsContext = sws_getContext(width, height, AV_PIX_FMT_YUV420P, width, height, AV_PIX_FMT_BGRA, SWS_BICUBIC, NULL, NULL, NULL);
        LOGI("Start scale");
        sws_scale(swsContext, srcslice, srcStride, 0, height, dstslice, dstStride);
        sws_freeContext(swsContext);
        env->SetIntArrayRegion(outarray, 0, width * height, arrayelem);
    }

    LOGI("Start Free");
    avcodec_close(codec.pCodecCtx);
    av_free((void *) codec.pCodecCtx);
    av_frame_unref(codec.pFrame);
    av_frame_free(&codec.pFrame);
    av_packet_unref(&packet);
    free(arrayelem);
    return outarray;
}
}

