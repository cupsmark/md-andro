package com.meetdesk.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.meetdesk.R;


/**
 * Created by ekobudiarto on 7/11/15.
 */
public class UIDialogLoading extends Dialog {

    Context context;
    UILoading loading;

    public UIDialogLoading(Context context) {
        super(context);
        this.context = context;
    }

    public UIDialogLoading(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected UIDialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_dialog_loading);
        loading = (UILoading) findViewById(R.id.dialog_loading_view);
        loading.setImageResource(R.drawable.icon_loading);
        loading.show();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void dismiss() {
        //loading.dismiss();
        super.dismiss();
    }
}
