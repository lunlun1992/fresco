# Copyright 2004-present Facebook. All Rights Reserved.

LOCAL_PATH := $(call my-dir)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libavcodec
#LOCAL_SRC_FILES := libavcodec.so
#include $(PREBUILT_SHARED_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libavutil
#LOCAL_SRC_FILES := libavutil.so
#include $(PREBUILT_SHARED_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libswresample
#LOCAL_SRC_FILES := libswresample.so
#include $(PREBUILT_SHARED_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libswscale
#LOCAL_SRC_FILES := libswscale.so
#include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_ARM_MODE := arm
LOCAL_MODULE := libffmpeg
LOCAL_SRC_FILES := libffmpeg.so
include $(PREBUILT_SHARED_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libavutil
#LOCAL_SRC_FILES := libavutil.a
#include $(PREBUILT_STATIC_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libavcodec
#LOCAL_SRC_FILES := libavcodec.a
#include $(PREBUILT_STATIC_LIBRARY)

#include $(CLEAR_VARS)
#LOCAL_ARM_MODE := arm
#LOCAL_MODULE := libswscale
#LOCAL_SRC_FILES := libswscale.a
#include $(PREBUILT_STATIC_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := kpg
LOCAL_SRC_FILES := libkpg.cpp
LOCAL_CFLAGS += -fvisibility=hidden -D__STDC_CONSTANT_MACROS
LOCAL_C_INCLUDES := $(LOCAL_PATH)/include
LOCAL_LDLIBS := -llog -landroid -lm -lz

#LOCAL_SHARED_LIBRARIES := libavcodec
#LOCAL_SHARED_LIBRARIES += libavutil
#LOCAL_SHARED_LIBRARIES += libswscale
#LOCAL_SHARED_LIBRARIES += libswresample
LOCAL_SHARED_LIBRARIES += libffmpeg


include $(BUILD_SHARED_LIBRARY)
