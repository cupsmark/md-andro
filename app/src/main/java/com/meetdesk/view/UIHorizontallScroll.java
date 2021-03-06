package com.meetdesk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * Created by ekobudiarto on 12/4/16.
 */
public class UIHorizontallScroll extends HorizontalScrollView {

    Context mContext;

    public UIHorizontallScroll(Context context) {
        super(context);
        this.mContext = context;
    }

    public UIHorizontallScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public UIHorizontallScroll(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        getParent().requestDisallowInterceptTouchEvent(true);
    }
}
