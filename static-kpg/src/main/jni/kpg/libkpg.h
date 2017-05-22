//
// Created by fengweilun on 03/05/2017.
//

#ifndef FRESCO_LIBKPG_H
#define FRESCO_LIBKPG_H

extern "C"
{
#include <jni.h>
#include <android/log.h>
#include "libavcodec/avcodec.h"
#include "libswscale/swscale.h"
#include "libavutil/avutil.h"
#include <stdlib.h>
#include <stdio.h>
#include <stdint.h>

#define LOGD_TAG(TAG, ...)  __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
#define LOGE_TAG(TAG, ...)  __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define LOG_TAG "streamer-jni"
#define LOGV(...)  __android_log_print(ANDROID_LOG_VERBOSE, LOG_TAG, __VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGW(...)  __android_log_print(ANDROID_LOG_WARN, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)


void sanitizein(char *line) {
    while (*line) {
        if (*line < 0x08 || (*line > 0x0D && *line < 0x20))
            *line = '?';
        line++;
    }
}

static void av_log_new_callback(void *ptr, int level, const char *fmt, va_list vl) {
    int print_prefix = 1;
    char line[1024];

    av_log_format_line(ptr, level, fmt, vl, line, sizeof(line), &print_prefix);

    sanitizein(line);
    if (level == AV_LOG_ERROR) {
        LOGE_TAG("av_log", "%s", line);
    } else {
        LOGD_TAG("av_log", "%s", line);
    }
}

void open_ffmpeg_log_in_debug_mode() {
    if (1) {
        LOGD("ffmpeg log opened");
        av_log_set_callback(av_log_new_callback);
        av_log_set_level(100);
    } else {
        LOGD("ffmpeg log not open");
    }
}
}

#endif //FRESCO_LIBKPG_H
