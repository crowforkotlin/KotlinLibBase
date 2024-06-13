package com.crow.base.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MyViewGroup2 extends RelativeLayout {
    public MyViewGroup2(Context context) {
        super(context);
    }

    public MyViewGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("PasteMangaX","MyViewGroup2     .dispatchTouchEvent:"  + ev);
        return super.dispatchTouchEvent(ev);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i("PasteMangaX","MyViewGroup2     .onInterceptTouchEvent:"  + ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("PasteMangaX","MyViewGroup.onTouchEvent自行处理:"  + event);
        return super.onTouchEvent(event);
    }
}
