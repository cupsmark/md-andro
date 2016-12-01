LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := meetdesk-library
LOCAL_SRC_FILES := meetdesk-library.c

include $(BUILD_SHARED_LIBRARY)