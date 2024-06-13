package com.crow.base.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView2 extends View {
    public MyView2(Context context) {
        super(context);
    }

    public MyView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("PasteMangaX","MyView2   .dispatchTouchEvent:"  + ev);
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("PasteMangaX","MyView2   .onTouchEvent:"  + event);
        return super.onTouchEvent(event);
    }

}
