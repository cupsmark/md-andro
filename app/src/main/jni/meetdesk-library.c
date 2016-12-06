#include <jni.h>
#include <android/log.h>


JNIEXPORT jstring JNICALL Java_com_meetdesk_helper_HelperNative_getURL(JNIEnv * env, jobject obj, jint code)
{
    if (code == 11171) {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/upload/");
    }
    else if (code == 11172) {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/get-region-list");
    }
    else if (code == 11173) {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/product-category-list");
    }
    else if (code == 11174) {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/auth-register");
    }
    else if (code == 11175) {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/auth-login");
    }
    else if (code == 11176)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/product-list");
    }
    else if (code == 11177)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/product-detail");
    }
    else if (code == 11178)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/product-hot-list");
    }
    else if (code == 11179)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/room-wish-list");
    }
    else if (code == 11180)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/room-wish-add");
    }
    else if (code == 11181)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/room-wish-delete");
    }
    else if (code == 11182)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/rent-out");
    }
    else if (code == 11183)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/account-detail");
    }
    else if (code == 11184)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/account-update");
    }
    else if (code == 11185)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/upload-file");
    }
    else if (code == 11186)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/auth-register-device");
    }
    else if (code == 11187)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/message-detail-list");
    }
    else if (code == 11188)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/message-list");
    }
    else if (code == 11189)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/message-add");
    }
    else if (code == 11190)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/message-detail-add");
    }
    else if (code == 11191)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/auth-login-social");
    }
    else if (code == 11192)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/booking-check");
    }
    else if (code == 11193)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/booking-save");
    }
    else if (code == 11194)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/booking-review");
    }
    else if (code == 11195)
    {
        return (*env)->NewStringUTF(env, "http://meetdesk.cupslice.com/index.php/api/v1/booking-list");
    }
    else {
        return (*env)->NewStringUTF(env, "no-data");
    }
}