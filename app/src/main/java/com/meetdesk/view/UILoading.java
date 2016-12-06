package com.meetdesk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.meetdesk.R;


/**
 * Created by ekobudiarto on 7/11/15.
 */
public class UILoading extends ImageView{

    Context context;
    Animation rotation;

    public UILoading(Context context) {
        super(context);
        this.context = context;
        //setImageResource(R.drawable.v3loading);
    }

    public UILoading(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public UILoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    public void show()
    {
        rotation = AnimationUtils.loadAnimation(context, R.anim.viewloading);
        rotation.setRepeatMode(Animation.RESTART);
        rotation.setRepeatCount(Animation.INFINITE);
        this.startAnimation(rotation);
        this.setVisibility(View.VISIBLE);
        invalidate();
    }

    public void dismiss()
    {
        rotation.setRepeatMode(0);
        rotation.setRepeatCount(0);
        this.setVisibility(View.GONE);
    }

}
