package com.meetdesk.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.meetdesk.R;
import com.meetdesk.helper.HelperGeneral;

/**
 * Created by ekobudiarto on 12/5/16.
 */
public class UIToast extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    Context mContext;
    public UIToast(Context context) {
        super(context);
        this.mContext = context;
    }

    public UIToast(Context context, String text)
    {
        super(context);
        this.mContext = context;
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        param.bottomMargin = 10;
        UIText viewText = new UIText(mContext);
        viewText.setLayoutParams(param);
        viewText.setTextSize(16);
        viewText.setText(text);
        viewText.setTextColor(HelperGeneral.getColor(mContext, R.color.base_white));
        viewText.setBackgroundColor(Color.parseColor("#80000000"));
        viewText.setPadding(20, 20, 20, 20);
        setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 30);
        setDuration(Toast.LENGTH_LONG);
        setDuration(5000);
        setView((View) viewText);
    }

}
