package com.meetdesk.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by ekobudiarto on 11/1/16.
 */
public class UIDialogList extends Dialog {

    Context mContext;
    ArrayList<String> dataID, dataTitle, dataThumb;
    String title;
    RecyclerView dialogRecycler;

    public UIDialogList(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public UIDialogList(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    protected UIDialogList(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
    }

    private void init()
    {
        dataID = new ArrayList<String>();
        dataTitle = new ArrayList<String>();
        dataThumb = new ArrayList<String>();
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void addDataID(String dataID)
    {
        this.dataID.add(dataID);
    }

    public void addDataTitle(String dataTitle)
    {
        this.dataTitle.add(dataTitle);
    }

}
