package com.meetdesk.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.meetdesk.R;

import java.util.Locale;

/**
 * Created by ekobudiarto on 9/15/16.
 */
public class UIText extends TextView {

    Context mContext;

    public UIText(Context context) {
        super(context);
        this.mContext = context;
        setRegular();
    }

    public UIText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        setDefault(attrs);
    }

    public UIText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setDefault(attrs);
    }

    private void setDefault(AttributeSet attrs)
    {
        final TypedArray arrFont = mContext.obtainStyledAttributes(attrs, R.styleable.MeetbroFontStyle);
        final String fontType = (arrFont.getString(R.styleable.MeetbroFontStyle_fontType) == null) ? "Regular" : arrFont.getString(R.styleable.MeetbroFontStyle_fontType);
        final String fontStyle = (arrFont.getString(R.styleable.MeetbroFontStyle_fontStyle) == null) ? "Normal" : arrFont.getString(R.styleable.MeetbroFontStyle_fontStyle);

        if(fontType.toLowerCase(Locale.US).equals("bold"))
        {
            setBold();
        }
        else if(fontType.toLowerCase(Locale.US).equals("light"))
        {
            if(fontStyle.toLowerCase(Locale.US).equals("italic"))
            {
                setLightItalic();
            }
            else
            {
                setLight();
            }
        }
        else if(fontType.toLowerCase(Locale.US).equals("semibold"))
        {
            if(fontStyle.toLowerCase(Locale.US).equals("italic"))
            {
                setSemiboldItalic();
            }
            else
            {
                setSemibold();
            }
        }
        else if(fontType.toLowerCase(Locale.US).equals("thin"))
        {
            setThin();
        }
        else if(fontType.toLowerCase(Locale.US).equals("medium"))
        {
            if(fontStyle.toLowerCase(Locale.US).equals("italic"))
            {
                setMediumItalic();
            }
            else {
                setMedium();
            }
        }
        else
        {
            if(fontStyle.toLowerCase(Locale.US).equals("italic"))
            {
                setItalic();
            }
            else {
                setRegular();
            }
        }
    }

    private void setRegular()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Regular.ttf");
        setTypeface(tf);
    }

    private void setBold()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        setTypeface(tf);
    }

    private void setItalic()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Italic.ttf");
        setTypeface(tf);
    }

    private void setLight()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Light.ttf");
        setTypeface(tf);
    }

    private void setLightItalic()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-LightItalic.ttf");
        setTypeface(tf);
    }

    private void setMedium()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Medium.ttf");
        setTypeface(tf);
    }

    private void setMediumItalic()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-MediumItalic.ttf");
        setTypeface(tf);
    }

    private void setSemibold()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Semibold.ttf");
        setTypeface(tf);
    }

    private void setSemiboldItalic()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-SemiboldItalic.ttf");
        setTypeface(tf);
    }

    private void setThin()
    {
        Typeface tf = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Thin.ttf");
        setTypeface(tf);
    }
}
