package com.crow.base.ui.component;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context,  AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("PasteMangaX","MyView1    .dispatchTouchEvent:"  + ev);
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("PasteMangaX","MyView1    .onTouchEvent:"  + event);
        return super.onTouchEvent(event);
    }
}
