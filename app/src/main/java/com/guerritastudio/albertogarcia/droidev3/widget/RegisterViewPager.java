package com.guerritastudio.albertogarcia.droidev3.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class RegisterViewPager extends ViewPager {
    private static String TAG = RegisterViewPager.class.getSimpleName();

    private boolean enabled = true;

    public RegisterViewPager(Context context) {
        super(context);
    }

    public RegisterViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        //Log.e(TAG, "setPagingEnabled = " + enabled);
        this.enabled = enabled;
    }

    public boolean getPagingEnabled() {
        return enabled;
    }

}
