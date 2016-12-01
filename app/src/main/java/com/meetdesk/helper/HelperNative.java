package com.meetdesk.helper;

/**
 * Created by ekobudiarto on 11/22/16.
 */
public class HelperNative {

    static{
        System.loadLibrary("meetdesk-library");
    }

    public static native String getURL(int code);
}
