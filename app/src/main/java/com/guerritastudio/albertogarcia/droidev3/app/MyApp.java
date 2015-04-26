package com.guerritastudio.albertogarcia.droidev3.app;

import android.app.Application;

import com.guerritastudio.albertogarcia.droidev3.model.DroidEv3;

/**
 * Created by AlbertoGarcia on 23/4/15.
 */
public class MyApp extends Application {

    private DroidEv3 mDroidEv3;

    public void setDroidEv3(DroidEv3 droidEv3) {
        mDroidEv3 = droidEv3;
    }

    public DroidEv3 getDroidEv3() {
        return mDroidEv3;
    }

}
