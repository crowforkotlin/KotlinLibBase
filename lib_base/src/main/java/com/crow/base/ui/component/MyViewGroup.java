package com.crow.base.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyViewGroup extends RelativeLayout {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("PasteMangaX","MyViewGroup1     .dispatchTouchEvent:"  + ev);
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("PasteMangaX","MyViewGroup1     .onInterceptTouchEvent:"  + ev);
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("PasteMangaX","MyViewGroup1     .onTouchEvent自行处理:"  + event);
        return super.onTouchEvent(event);
    }
}
