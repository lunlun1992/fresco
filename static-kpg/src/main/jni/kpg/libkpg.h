//
// Created by fengweilun on 03/05/2017.
//

#ifndef FRESCO_LIBKPG_H
#define FRESCO_LIBKPG_H

extern "C"
{
#include <jni.h>
#include <android/log.h>
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)
#define TAG "Native"
}

#endif //FRESCO_LIBKPG_H
