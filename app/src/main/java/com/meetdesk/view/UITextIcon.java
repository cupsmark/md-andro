package com.meetdesk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.meetdesk.R;

/**
 * Created by ekobudiarto on 9/17/16.
 */
public class UITextIcon extends LinearLayout {

    Context mContext;
    View mainView;
    ImageView viewThumb;
    UIText viewText;
    String text = "";
    int resID = 0;

    public UITextIcon(Context context) {
        super(context);
        this.mContext = context;
        inflateLayout();
    }

    public UITextIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inflateLayout();
    }

    public UITextIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inflateLayout();
    }

    private void inflateLayout()
    {
        mainView = inflate(mContext, R.layout.view_text_icon, this);
        viewThumb = (ImageView) mainView.findViewById(R.id.view_text_icon_thumb);
        viewText = (UIText) mainView.findViewById(R.id.view_text_icon_title);
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public void setImage(int resID)
    {
        this.resID = resID;
    }

    public void inflate()
    {
        viewThumb.setImageResource(resID);
        viewText.setText(text);
    }
}
