package com.meetdesk.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.meetdesk.R;

/**
 * Created by ekobudiarto on 11/3/16.
 */
public class UIDialogConfirm extends Dialog {

    Context mContext;
    UIButton buttonYes, buttonNo;
    UIText textMessage, dialogTitle;
    String message, title;

    public UIDialogConfirm(Context context) {
        super(context);
        this.mContext = context;
    }

    public UIDialogConfirm(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    protected UIDialogConfirm(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.view_dialog_confirm);

        buttonYes = (UIButton) findViewById(R.id.view_dialog_confirm_yes);
        buttonNo = (UIButton) findViewById(R.id.view_dialog_confirm_no);
        textMessage = (UIText) findViewById(R.id.view_dialog_confirm_message);
        dialogTitle = (UIText) findViewById(R.id.view_dialog_confirm_title);

        textMessage.setText(message);
        dialogTitle.setText(title);
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setDialogTitle(String title)
    {
        this.title = title;
    }

    public UIButton getButtonYes()
    {
        return this.buttonYes;
    }

    public UIButton getButtonNo()
    {
        return this.buttonNo;
    }

}
