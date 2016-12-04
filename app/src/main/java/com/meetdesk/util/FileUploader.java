package com.meetdesk.util;

import android.content.Context;

/**
 * Created by ekobudiarto on 12/3/16.
 */
public class FileUploader {

    Context mContext;
    String token, sourceFile, msg, resultFilename;
    boolean success = false;

    public FileUploader(Context context)
    {
        this.mContext = context;
        token = "";
        sourceFile = "";
        msg = "";
        resultFilename = "";
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void setFilename(String filename)
    {
        this.sourceFile = filename;
    }

    public void start()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
